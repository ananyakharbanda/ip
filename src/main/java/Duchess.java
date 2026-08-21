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

            try {
                if (command.equalsIgnoreCase("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                } else if (command.toLowerCase().startsWith("mark ")) {
                    markTask(command, tasks, taskCount);
                } else if (command.toLowerCase().startsWith("unmark ")) {
                    unmarkTask(command, tasks, taskCount);
                } else if (command.equalsIgnoreCase("mark")) {
                    throw new DuchessException("OOPS!!! Please use 'mark <task number>', "
                            + "for example: mark 1.");
                } else if (command.equalsIgnoreCase("unmark")) {
                    throw new DuchessException("OOPS!!! Please use 'unmark <task number>', "
                            + "for example: unmark 1.");
                } else if (taskCount < MAX_TASKS) {
                    tasks[taskCount] = createTask(command);
                    taskCount++;
                    System.out.println("added: " + tasks[taskCount - 1]);
                } else {
                    throw new DuchessException("OOPS!!! Your task list is full. "
                            + "You cannot store more than " + MAX_TASKS + " tasks.");
                }
            } catch (DuchessException exception) {
                System.out.println(exception.getMessage());
            }

            System.out.println(separator);
        }
    }

    /**
     * Creates the appropriate task subtype from a user command.
     *
     * <p>Commands without an explicit type are treated as todo tasks so that
     * existing plain task input remains supported.</p>
     *
     * @param command the complete task command
     * @throws DuchessException if the command is empty, malformed, or unknown
     * @return a task object whose runtime type matches the command
     */
    private static Task createTask(String command) throws DuchessException {
        String lowerCaseCommand = command.toLowerCase();
        if (command.trim().isEmpty()) {
            throw new DuchessException("OOPS!!! A command cannot be empty. "
                    + "Try todo, deadline, event, list, mark, unmark, or bye.");
        }
        if (lowerCaseCommand.equals("todo") || lowerCaseCommand.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new DuchessException("OOPS!!! The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }
        if (lowerCaseCommand.equals("deadline") || lowerCaseCommand.startsWith("deadline ")) {
            String[] details = splitTaskDetails(command.substring("deadline".length()), "/by");
            validateTaskDetails("deadline", details, "/by");
            return new Deadline(details[0], details[1]);
        }
        if (lowerCaseCommand.equals("event") || lowerCaseCommand.startsWith("event ")) {
            String[] details = splitTaskDetails(command.substring("event".length()), "/at");
            validateTaskDetails("event", details, "/at");
            return new Event(details[0], details[1]);
        }

        throw new DuchessException("OOPS!!! I'm sorry, but I don't know what that means :-(\n"
                + "Try todo, deadline, event, list, mark, unmark, or bye.");
    }

    /**
     * Splits a typed task command into its description and detail value.
     *
     * @param taskDetails the text after the task type
     * @param marker the detail marker, such as {@code /by} or {@code /at}
     * @return a two-element array containing description and detail
     */
    private static String[] splitTaskDetails(String taskDetails, String marker) {
        int markerIndex = taskDetails.toLowerCase().indexOf(marker.toLowerCase());
        if (markerIndex < 0) {
            return new String[]{taskDetails.trim(), ""};
        }

        String description = taskDetails.substring(0, markerIndex).trim();
        String detail = taskDetails.substring(markerIndex + marker.length()).trim();
        return new String[]{description, detail};
    }

    /**
     * Checks that a deadline or event has both required pieces of information.
     *
     * @param taskType the task type being validated
     * @param details the parsed description and detail
     * @param marker the required detail marker
     * @throws DuchessException if the description or detail is missing
     */
    private static void validateTaskDetails(String taskType, String[] details, String marker)
            throws DuchessException {
        if (details[0].isEmpty()) {
            throw new DuchessException("OOPS!!! The description of a " + taskType
                    + " cannot be empty.");
        }
        if (details[1].isEmpty()) {
            String article = taskType.equals("event") ? "An" : "A";
            throw new DuchessException("OOPS!!! " + article + " " + taskType
                    + " must include a non-empty "
                    + marker + " value. Example: " + taskType + " task description "
                    + marker + " time.");
        }
    }

    /**
     * Marks the task identified by a one-based index as done.
     *
     * @param command the complete mark command entered by the user
     * @param tasks the stored tasks
     * @param taskCount the number of stored tasks
     */
    private static void markTask(String command, Task[] tasks, int taskCount)
            throws DuchessException {
        int taskIndex = parseTaskIndex(command, "mark ");
        validateTaskIndex(taskIndex, taskCount);

        tasks[taskIndex].markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks[taskIndex]);
    }

    /**
     * Marks the task identified by a one-based index as not done.
     *
     * @param command the complete unmark command entered by the user
     * @param tasks the stored tasks
     * @param taskCount the number of stored tasks
     */
    private static void unmarkTask(String command, Task[] tasks, int taskCount)
            throws DuchessException {
        int taskIndex = parseTaskIndex(command, "unmark ");
        validateTaskIndex(taskIndex, taskCount);

        tasks[taskIndex].markAsNotDone();
        System.out.println("Okay, I've marked this task as not done yet:");
        System.out.println("  " + tasks[taskIndex]);
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
    private static void validateTaskIndex(int taskIndex, int taskCount) throws DuchessException {
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new DuchessException("OOPS!!! Please provide a valid task number between 1 and "
                    + taskCount + ".");
        }
    }
}
