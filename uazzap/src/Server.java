import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public Server(int port) {
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
            this.clientSocket = this.serverSocket.accept();
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
            while (true) {
                Scanner read = new Scanner(System.in);
                String toSend = read.nextLine();
                this.out.println(toSend);
            }
        }).start();
    }
}
