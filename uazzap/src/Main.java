import java.net.BindException;
import java.util.Scanner;

public class Main {
    // Costanti ANSI per i colori e la formattazione
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BOLD = "\u001B[1m";

    public static void main(String[] args) {
        // Banner di benvenuto in ASCII Art
        System.out.println(CYAN + BOLD + "  _____ _           _       _               ");
        System.out.println(" / ____| |         | |     / \\              ");
        System.out.println("| |    | |__   __ _| |_   /  /\\  _ __  _ __ ");
        System.out.println("| |    | '_ \\ / _` | __| /  /  \\| '_ \\| '_ \\");
        System.out.println("| |____| | | | (_| | |_ /  / /\\ \\ |_) | |_) |");
        System.out.println(" \\_____|_| |_|\\__,_|\\__|  /____\\_\\ .__/| .__/");
        System.out.println("                                 | |   | |   ");
        System.out.println("                                 |_|   |_|   " + RESET);
        System.out.println(CYAN + BOLD + "--- Java Chat System v1.0 ---\n" + RESET);
        System.out.println(CYAN + "Commands list for server: /kick <nickname>" + RESET);
        System.out.println(CYAN + "Commands list for client: /list, /whisper <nickname> <message>, /quit" + RESET);
        System.out.println();

        Scanner read = new Scanner(System.in);
        boolean roleTaken = false;

        while (!roleTaken) {
            // Prompt di scelta colorato
            System.out.print(YELLOW + BOLD + "Seleziona modalità (server / client): " + RESET);
            String input = read.nextLine().toLowerCase().trim();
            if (!input.equals("server") && !input.equals("client")) {
                System.out.println(RED + "Input non valido" + RESET);
                continue;
            }

            System.out.print(CYAN + "Seleziona la porta [default: 6769]: " + RESET);
            int port;
            String portInput;
            try {
                portInput = read.nextLine();
                if (portInput == null || portInput.isEmpty()) {
                    port = 6769;
                } else {
                    port = Integer.parseInt(portInput);
                }
            } catch (Exception e) {
                System.out.println(RED + "Porta non valida" + RESET);
                continue;
            }

            try {
                if (input.equals("server")) {
                    System.out.println(CYAN + "Inizializzato Server sulla porta " + port + RESET);
                    new Server(port);
                    roleTaken = true;
                } else if (input.equals("client")) {
                    System.out.print(CYAN + "Inserisci l'indirizzo [default: localhost]: " + RESET);
                    String address = read.nextLine();
                    if (address == null || address.isEmpty()) {
                        address = "localhost";
                    }
                    System.out.println(CYAN + "Inizializzato Client" + RESET);
                    new Client(address, port);
                    roleTaken = true;
                } else {
                    // Messaggio di errore in rosso se l'input è sbagliato
                    System.out.println(RED + "Comando non valido. Inserisci 'server' oppure 'client'." + RESET);
                }
            } catch (RuntimeException e) {
                System.out.println("Porta gia utilizzata :/");
            }
        }
    }
}