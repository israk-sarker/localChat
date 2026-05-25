import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
                BufferedReader newIn;
                PrintWriter newOut;
                try {
                    Socket newClient = this.serverSocket.accept();
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
                    newOut.println("Write your nickname: ");
                    String nickname;
                    try {
                        nickname = newIn.readLine();
                    } catch (IOException e) {
                        nickname = "unknown";
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
                            System.out.println(message);
                            broadcast(newOut, nickname, message);
                        }
                    }
                }).start();
            }
        }).start();

        new Thread(() -> {
            Scanner read = new Scanner(System.in);
            while (true) {
                String toSend = read.nextLine();
                if (toSend.charAt(0) != '/') {
                    broadcast(null, "Admin", toSend);
                    continue;
                }
                if (toSend.startsWith("/kick ")) {
                    kick(toSend.substring(6));
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
