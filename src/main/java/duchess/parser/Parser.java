package duchess.parser;

import java.time.format.DateTimeParseException;
import java.util.Locale;

import duchess.DuchessException;
import duchess.task.Deadline;
import duchess.task.Event;
import duchess.task.Task;
import duchess.task.Todo;

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
     * @return a task object whose runtime type matches the command
     * @throws DuchessException if the command is empty, malformed, or unknown
     */
    public static Task parseTask(String command) throws DuchessException {
        assert command != null : "The parser requires a command string";
        String normalizedCommand = normalizeTaskAlias(command);
        String lowerCaseCommand = normalizedCommand.toLowerCase(Locale.ROOT);
        if (normalizedCommand.trim().isEmpty()) {
            throw new DuchessException("OOPS!!! A command cannot be empty. "
                    + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.");
        }
        if (lowerCaseCommand.equals("todo") || lowerCaseCommand.startsWith("todo ")) {
            String description = normalizedCommand.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new DuchessException("OOPS!!! The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }
        if (lowerCaseCommand.equals("deadline") || lowerCaseCommand.startsWith("deadline ")) {
            String[] details = splitTaskDetails(normalizedCommand.substring("deadline".length()), "/by");
            validateTaskDetails("deadline", details, "/by");
            try {
                return new Deadline(details[0], details[1]);
            } catch (DateTimeParseException exception) {
                throw new DuchessException("OOPS!!! The deadline date is invalid. "
                        + "Use yyyy-MM-dd, for example: 2019-12-02.");
            }
        }
        if (lowerCaseCommand.equals("event") || lowerCaseCommand.startsWith("event ")) {
            String eventDetails = normalizedCommand.substring("event".length());
            if (eventDetails.toLowerCase(Locale.ROOT).contains("/from")
                    || eventDetails.toLowerCase(Locale.ROOT).contains("/to")) {
                return parseEventRange(eventDetails);
            }
            String[] details = splitTaskDetails(eventDetails, "/at");
            validateTaskDetails("event", details, "/at");
            return new Event(details[0], details[1]);
        }

        throw new DuchessException("OOPS!!! I'm sorry, but I don't know what that means :-(\n"
                + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.");
    }

    /** Parses an event's required description and ISO start/end dates. */
    private static Event parseEventRange(String eventDetails) throws DuchessException {
        String[] startDetails = splitTaskDetails(eventDetails, "/from");
        String[] endDetails = splitTaskDetails(startDetails[1], "/to");
        if (startDetails[0].isBlank() || endDetails[0].isBlank() || endDetails[1].isBlank()) {
            throw new DuchessException("OOPS!!! Use event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }
        try {
            return new Event(startDetails[0], endDetails[0], endDetails[1]);
        } catch (DateTimeParseException exception) {
            throw new DuchessException("OOPS!!! The event dates are invalid. Use yyyy-MM-dd.");
        } catch (IllegalArgumentException exception) {
            throw new DuchessException("OOPS!!! The event end date cannot be before its start date.");
        }
    }

    /** Converts squad-room task aliases into their original command names. */
    private static String normalizeTaskAlias(String command) {
        String lowerCaseCommand = command.toLowerCase(Locale.ROOT);
        if (lowerCaseCommand.equals("case") || lowerCaseCommand.startsWith("case ")) {
            return "todo" + command.substring("case".length());
        }
        if (lowerCaseCommand.equals("timer") || lowerCaseCommand.startsWith("timer ")) {
            return "deadline" + command.substring("timer".length());
        }
        if (lowerCaseCommand.equals("briefing") || lowerCaseCommand.startsWith("briefing ")) {
            return "event" + command.substring("briefing".length());
        }
        return command;
    }

    /**
     * Converts the one-based task number in a command into a zero-based list index.
     *
     * @param command the complete command
     * @param prefix the command prefix, such as {@code "mark "}
     * @return the zero-based index, or {@code -1} for malformed input
     */
    public static int parseTaskIndex(String command, String prefix) {
        assert command != null && prefix != null : "Task-index parsing requires command and prefix text";
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
        int markerIndex = taskDetails.toLowerCase(Locale.ROOT).indexOf(marker.toLowerCase(Locale.ROOT));
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
        assert details != null && details.length == 2
                : "Task details must contain exactly a description and a detail value";
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
