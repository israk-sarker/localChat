import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        System.out.print("Server or client? : ");
        String input = read.nextLine();
        int port = 3894;
        if (input.equals("server")) {
            new Server(port);
        } else {
            new Client(port);
        }
    }
}