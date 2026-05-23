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

    public Server(int port) {
        this.clientsSocket = new CopyOnWriteArrayList<>();
        this.in = new CopyOnWriteArrayList<>();
        this.out = new CopyOnWriteArrayList<>();
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                while (true) {
                    Socket newClient = this.serverSocket.accept();
                    System.out.println("Someone connected!");
                    this.clientsSocket.add(newClient);
                    BufferedReader newIn = new BufferedReader(
                            new InputStreamReader(newClient.getInputStream())
                    );
                    this.in.add(newIn);
                    new Thread(() -> {
                        String message;
                        while (true) {
                            try {
                                message = newIn.readLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if (message != null) {
                                System.out.println(message);
                                broadcast(message);
                            }
                        }
                    }).start();
                    this.out.add(new PrintWriter(newClient.getOutputStream(), true));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void broadcast(String msg) {
        Iterator<PrintWriter> receivers = this.out.iterator();
        while (receivers.hasNext()) {
            PrintWriter receiver = receivers.next();
            receiver.println(msg);
        }
    }
}
