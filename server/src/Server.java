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
    public Server(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            boolean clientAccepted = false;
            while (!clientAccepted) {
                try {
                    this.clientSocket = serverSocket.accept();
                    clientAccepted = true;
                    System.out.println("Socket accepted");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            new Thread(() -> {
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream())
                    );
                    String inputLine = "";
                    while (true) {
                        inputLine = in.readLine();
                        System.out.println(inputLine);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(7668);
        server.start();
    }
}
