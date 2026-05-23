import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int port;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(int port) {
        try {
            this.port = port;
            this.clientSocket = new Socket("127.0.0.1", port);
            this.in = new BufferedReader(
                    new InputStreamReader(this.clientSocket.getInputStream())
            );
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            String message;
            while (true) {
                try {
                    message = in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (message != null) {
                    System.out.println("Someone: " + message);
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
