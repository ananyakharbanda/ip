package duchess;

import java.time.format.DateTimeParseException;

/** Converts user-entered text into validated task data and indexes. */
public final class Parser {
    /** Prevents creation of a stateless utility parser. */
    private Parser() {
    }

    /**
     * Creates the appropriate task subtype from a user command.
     *
     * <p>Commands without an explicit type are rejected because all supported
     * task commands have an explicit {@code todo}, {@code deadline}, or
     * {@code event} prefix.</p>
     *
     * @param command the complete task command
     * @throws DuchessException if the command is empty, malformed, or unknown
     * @return a task object whose runtime type matches the command
     */
    public static Task parseTask(String command) throws DuchessException {
        String lowerCaseCommand = command.toLowerCase();
        if (command.trim().isEmpty()) {
            throw new DuchessException("OOPS!!! A command cannot be empty. "
                    + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.");
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
            try {
                return new Deadline(details[0], details[1]);
            } catch (DateTimeParseException exception) {
                throw new DuchessException("OOPS!!! The deadline date is invalid. "
                        + "Use yyyy-MM-dd, for example: 2019-12-02.");
            }
        }
        if (lowerCaseCommand.equals("event") || lowerCaseCommand.startsWith("event ")) {
            String[] details = splitTaskDetails(command.substring("event".length()), "/at");
            validateTaskDetails("event", details, "/at");
            return new Event(details[0], details[1]);
        }

        throw new DuchessException("OOPS!!! I'm sorry, but I don't know what that means :-(\n"
                + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.");
    }

    /**
     * Converts the one-based task number in a command into a zero-based list index.
     *
     * @param command the complete command
     * @param prefix the command prefix, such as {@code "mark "}
     * @return the zero-based index, or {@code -1} for malformed input
     */
    public static int parseTaskIndex(String command, String prefix) {
        try {
            int oneBasedIndex = Integer.parseInt(command.substring(prefix.length()).trim());
            return oneBasedIndex - 1;
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            return -1;
        }
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
}
