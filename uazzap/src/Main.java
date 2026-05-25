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
        System.out.println(CYAN + "--- Java Chat System v1.0 ---\n" + RESET);

        Scanner read = new Scanner(System.in);
        boolean roleTaken = false;

        while (!roleTaken) {
            // Prompt di scelta colorato
            System.out.print(YELLOW + BOLD + "Seleziona modalità (server / client): " + RESET);
            String input = read.nextLine().toLowerCase().trim();

            System.out.print(CYAN + "Seleziona la porta: " + RESET);
            int port = -1;
            try {
                port = Integer.parseInt(read.nextLine());
            } catch (Exception e) {
                System.out.println(RED + "Porta non valida" + RESET);
                continue;
            }

            try {
                if (input.equals("server")) {
                    System.out.println(CYAN + "Inizializzazione Server sulla porta " + port + "..." + RESET);
                    new Server(port);
                    roleTaken = true;
                } else if (input.equals("client")) {
                    System.out.println(CYAN + "Inizializzazione Client..." + RESET);
                    new Client(port);
                    roleTaken = true;
                } else {
                    // Messaggio di errore in rosso se l'input è sbagliato
                    System.out.println(RED + "Comando non valido. Inserisci 'server' oppure 'client'." + RESET);
                }
            } catch (RuntimeException e) {
                System.out.println("The port is already in use");
            }
        }
    }
}