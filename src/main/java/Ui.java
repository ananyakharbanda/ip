import java.util.Scanner;

/** Handles all console input and output for Duchess. */
public class Ui {
    /** The separator printed between Duchess messages. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** The banner displayed when Duchess starts. */
    private static final String BANNER = """
            +------------------------+
            |        Duchess         |
            +------------------------+
            """;

    /** Reads commands entered through the console. */
    private final Scanner scanner;

    /** Creates a user interface that reads from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Duchess's welcome message. */
    public void showWelcome() {
        showSeparator();
        System.out.print(BANNER);
        System.out.println("Hello! I'm Duchess.");
        System.out.println("What can I do for you?");
        showSeparator();
    }

    /** Displays Duchess's goodbye message. */
    public void showGoodbye() {
        showSeparator();
        System.out.println("Bye. Hope to see you again soon!");
        showSeparator();
    }

    /**
     * Returns whether another command is available from the user.
     *
     * @return {@code true} when another input line is available
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the next command line
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the separator between messages. */
    public void showSeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays every task with its one-based position.
     *
     * @param tasks the task list to display
     */
    public void showTasks(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays the task that was added.
     *
     * @param task the newly added task
     */
    public void showTaskAdded(Task task) {
        System.out.println("added: " + task);
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task the completed task
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task the reopened task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("Okay, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was deleted and reports the new count.
     *
     * @param task the deleted task
     * @param remainingTaskCount the number of tasks left after deletion
     */
    public void showTaskDeleted(Task task, int remainingTaskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + remainingTaskCount + " tasks in the list.");
    }

    /**
     * Displays a user-correctable error.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Displays the error used when saving tasks fails. */
    public void showSaveError() {
        System.out.println("OOPS!!! I couldn't save your task list to disk.");
    }
}
