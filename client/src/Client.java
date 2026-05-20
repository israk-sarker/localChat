import com.sun.tools.jconsole.JConsoleContext;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String address;
    private int port;

    private Socket socket;
    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        try {
            this.socket = new Socket(address, port);
            System.out.println("Connected to the server!");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(this.socket.getInputStream())
            );
            //just repeats what you write
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();

            Scanner read = new Scanner(System.in);
            String userInput;
            while (true) {
                userInput = read.nextLine();
                out.println(userInput);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String address = "localhost";
        int port = 7668;
        Client cl = new Client(address, port);
        cl.start();
    }
}
