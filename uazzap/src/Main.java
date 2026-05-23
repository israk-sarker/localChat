import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        boolean roleTaken = false;
        int port = 3894;
        while (!roleTaken) {
            System.out.print("Server or client? : ");
            String input = read.nextLine();
            if (input.equals("server")) {
                new Server(port);
                roleTaken = true;
            }
            if (input.equals("client")){
                new Client(port);
                roleTaken = true;
            }
            if (!roleTaken) {
                System.out.println("Try again");
            }
        }
    }
}