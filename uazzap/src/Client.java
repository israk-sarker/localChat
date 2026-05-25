import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int port;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String address, int port) {
        try {
            this.port = port;
            this.clientSocket = new Socket(address, port);
            this.in = new BufferedReader(
                    new InputStreamReader(this.clientSocket.getInputStream())
            );
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (Exception e) {
            this.port = -1;
            System.out.println("Connessione rifiutata! Sei sicuro ce un server qua???");
            return;
        }

        new Thread(() -> {
            String message;
            while (true) {
                try {
                    message = in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (message == null) {
                    return;
                }
                if (message.equals("/help")) {
                    System.out.println("Commands list for client: /list, /whisper <nickname> <message>, /rename <nickname>, /quit");
                } else if (message.equals("/quit")) {
                    System.out.println("You have left the chat");
                    this.out.println("/quit");
                    try {
                        this.clientSocket.close();
                        System.exit(0);
                    } catch (IOException ignored) {}
                } else {
                    System.out.println(message);
                }
            }
        }).start();

        new Thread(() -> {
            Scanner read = new Scanner(System.in);
            while (true) {
                String toSend = read.nextLine();
                this.out.println(toSend);
            }
        }).start();
    }
}
