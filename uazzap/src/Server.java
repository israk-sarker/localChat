import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private CopyOnWriteArrayList<Socket> clientsSocket;
    private CopyOnWriteArrayList<BufferedReader> in;
    private CopyOnWriteArrayList<PrintWriter> out;
    private CopyOnWriteArrayList<String> nicknames;
    
    public Server(int port) {
        this.clientsSocket = new CopyOnWriteArrayList<>();
        this.in = new CopyOnWriteArrayList<>();
        this.out = new CopyOnWriteArrayList<>();
        this.nicknames = new CopyOnWriteArrayList<>();
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            this.port = -1;
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            while (true) {
                Socket newClient;
                BufferedReader newIn;
                PrintWriter newOut;
                try {
                    newClient = this.serverSocket.accept();
                    System.out.println("Someone connected!");
                    this.clientsSocket.add(newClient);
                    newIn = new BufferedReader(
                            new InputStreamReader(newClient.getInputStream())
                    );
                    this.in.add(newIn);
                    newOut = new PrintWriter(newClient.getOutputStream(), true);
                    this.out.add(newOut);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                new Thread(() -> {
                    newOut.println("Scegli il tuo nickname [default: unknown]:");
                    String nickname;
                    try {
                        nickname = newIn.readLine();
                        if (nickname == null || nickname.isEmpty()) {
                            nickname = "unknown" + Math.round(Math.random() * 10000);
                            while (this.nicknames.contains(nickname)) {
                                nickname = "unknown" + Math.round(Math.random() * 10000);
                            }
                            newOut.println("You have now entered the chat");
                        } else {
                            newOut.println("Hi " + nickname + ", welcome in the chat!");
                        }
                    } catch (IOException ignored) {
                        nickname = "unknown" + Math.round(Math.random() * 10000);
                        while (this.nicknames.contains(nickname)) {
                            nickname = "unknown" + Math.round(Math.random() * 10000);
                        }
                        newOut.println("You have now entered the chat");
                    }
                    this.nicknames.add(nickname);
                    String message;
                    while (true) {
                        try {
                            message = newIn.readLine();
                        } catch (IOException e) {
                            break;
                        }
                        if (message != null) {
                            if (message.startsWith("/whisper ")) {
                                // format: /whisper <nickname> <message>
                                String[] parts = message.split(" ", 3);
                                if (parts.length < 3) {
                                    newOut.println("Usage: /whisper <nickname> <message>");
                                } else {
                                    String targetNick = parts[1];
                                    String whisperMsg = parts[2];
                                    boolean found = false;
                                    for (int i = 0; i < this.nicknames.size(); i++) {
                                        if (this.nicknames.get(i).equals(targetNick)) {
                                            this.out.get(i).println("[whisper from " + nickname + "]: " + whisperMsg);
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        newOut.println("User not found: " + targetNick);
                                    }
                                }
                            } else if (message.equals("/list")) {
                                newOut.println(Arrays.toString(this.nicknames.toArray()));
                            } else if (message.equals("/quit")) {
                                int index = nicknames.indexOf(nickname);
                                this.nicknames.remove(index);
                                this.clientsSocket.remove(index);
                                this.in.remove(index);
                                this.out.remove(index);
                                broadcast(null, "Server", nickname + " has left the chat");
                                try { newClient.close(); } catch (IOException ignored) {}
                                break;
                            } else {
                                System.out.println(message);
                                broadcast(newOut, nickname, message);
                            }
                        }
                    }
                }).start();
            }
        }).start();

        new Thread(() -> {
            Scanner read = new Scanner(System.in);
            while (true) {
                String toSend = read.nextLine();
                if (toSend.startsWith("/kick ")) {
                    if (toSend.split(" ").length != 2) {
                        System.out.println("Usage: /kick <nickname>");
                        continue;
                    }
                    kick(toSend.substring(6));
                } else {
                    broadcast(null, "Admin", toSend);
                }
            }
        }).start();
    }

    private void broadcast(PrintWriter sender, String senderNickname, String msg) {
        if (this.port == -1) {
            return;
        }
        Iterator<PrintWriter> receivers = this.out.iterator();
        while (receivers.hasNext()) {
            PrintWriter receiver = receivers.next();
            if (receiver != sender) {
                receiver.println("[" + senderNickname + "]: " + msg);
            }
        }
    }

    private void kick(String toKick) {
        for (int i = 0; i < nicknames.size(); i++) {
            if (nicknames.get(i).equals(toKick)) {
                try {
                    clientsSocket.get(i).close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
