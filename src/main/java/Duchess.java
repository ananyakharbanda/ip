import java.util.Scanner;

/**
 * The main entry point for the Duchess chatbot.
 */
public class Duchess {
    private static final int MAX_TASKS = 100;

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
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("bye")) {
                System.out.println(separator);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            System.out.println(separator);

            if (command.equalsIgnoreCase("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ".[" + tasks[i].getStatusIcon() + "] "
                            + tasks[i].getDescription());
                }
            } else if (command.toLowerCase().startsWith("mark ")) {
                markTask(command, tasks, taskCount);
            } else if (command.toLowerCase().startsWith("unmark ")) {
                unmarkTask(command, tasks, taskCount);
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("added: " + command);
            } else {
                System.out.println("Sorry, I cannot store more than " + MAX_TASKS + " tasks.");
            }

            System.out.println(separator);
        }
    }

    /**
     * Marks the task identified by a one-based index as done.
     *
     * @param command the complete mark command entered by the user
     * @param tasks the stored tasks
     * @param taskCount the number of stored tasks
     */
    private static void markTask(String command, Task[] tasks, int taskCount) {
        int taskIndex = parseTaskIndex(command, "mark ");
        if (taskIndex < 0 || taskIndex >= taskCount) {
            printInvalidTaskNumber(taskCount);
            return;
        }

        tasks[taskIndex].markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  [" + tasks[taskIndex].getStatusIcon() + "] "
                + tasks[taskIndex].getDescription());
    }

    /**
     * Marks the task identified by a one-based index as not done.
     *
     * @param command the complete unmark command entered by the user
     * @param tasks the stored tasks
     * @param taskCount the number of stored tasks
     */
    private static void unmarkTask(String command, Task[] tasks, int taskCount) {
        int taskIndex = parseTaskIndex(command, "unmark ");
        if (taskIndex < 0 || taskIndex >= taskCount) {
            printInvalidTaskNumber(taskCount);
            return;
        }

        tasks[taskIndex].markAsNotDone();
        System.out.println("Okay, I've marked this task as not done yet:");
        System.out.println("  [" + tasks[taskIndex].getStatusIcon() + "] "
                + tasks[taskIndex].getDescription());
    }

    /**
     * Converts the one-based task number in a command into a zero-based array index.
     *
     * @param command the complete command
     * @param prefix the command prefix, such as {@code "mark "}
     * @return the zero-based index, or {@code -1} for malformed input
     */
    private static int parseTaskIndex(String command, String prefix) {
        try {
            int oneBasedIndex = Integer.parseInt(command.substring(prefix.length()).trim());
            return oneBasedIndex - 1;
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            return -1;
        }
    }

    /**
     * Displays a consistent message when a command refers to no stored task.
     *
     * @param taskCount the number of stored tasks
     */
    private static void printInvalidTaskNumber(int taskCount) {
        System.out.println("Please provide a task number between 1 and " + taskCount + ".");
    }
}
