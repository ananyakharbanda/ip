import java.util.Scanner;

/**
 * The main entry point for the Duchess chatbot.
 */
public class Duchess {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = """
                +------------------------+
                |        Duchess         |
                +------------------------+
                """;

        System.out.println(separator);
        System.out.print(banner);
        System.out.println("Hello! I'm Duchess.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("bye")) {
                System.out.println(separator);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            System.out.println(separator);
            System.out.println(command);
            System.out.println(separator);
        }
    }
}
