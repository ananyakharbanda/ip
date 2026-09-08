package duchess;

import java.util.ArrayList;
import java.util.Locale;

import duchess.parser.Parser;
import duchess.storage.Storage;
import duchess.task.Task;
import duchess.task.TaskList;
import duchess.ui.Ui;

/** Coordinates Duchess's task commands and provides responses to user interfaces. */
public class Duchess {
    private static final String COMMAND_TYPE_ADD = "add";
    private static final String COMMAND_TYPE_BYE = "bye";
    private static final String COMMAND_TYPE_DELETE = "delete";
    private static final String COMMAND_TYPE_ERROR = "error";
    private static final String COMMAND_TYPE_FIND = "find";
    private static final String COMMAND_TYPE_HELP = "help";
    private static final String COMMAND_TYPE_LIST = "list";
    private static final String COMMAND_TYPE_MARK = "mark";
    private static final String COMMAND_TYPE_OTHER = "other";
    private static final String COMMAND_TYPE_UNMARK = "unmark";
    private static final String SAVE_ERROR = "OOPS!!! I couldn't save your task list to disk.";
    private static final String HELP_RESPONSE = """
            Available commands:
            todo <description>              Add a todo task.
            deadline <description> /by <date>
                                             Add a deadline task using yyyy-MM-dd.
            event <description> /at <time>  Add an event task.
            list                              Show all tasks.
            find <keyword>                   Find tasks by keyword.
            mark <task number>               Mark a task as done.
            unmark <task number>             Mark a task as not done.
            delete <task number>             Delete a task.
            help                              Show this command guide.
            bye                               Exit Duchess.

            Commands are not case-sensitive.
            """;

    /** Persists Duchess's tasks. */
    private final Storage storage;

    /** Stores the tasks shared by the CLI and GUI interfaces. */
    private final TaskList tasks;

    /** Identifies the most recently processed command for GUI styling. */
    private String commandType;

    /** Records whether the user has issued the {@code bye} command. */
    private boolean exitRequested;

    /** Creates a Duchess application using the default storage location. */
    public Duchess() {
        this(new Storage(), null);
    }

    /** Creates a Duchess instance with collaborators supplied by a test. */
    Duchess(Storage storage, TaskList tasks) {
        assert storage != null : "Duchess requires a storage collaborator";
        this.storage = storage;
        this.tasks = tasks == null ? storage.loadTasks() : tasks;
        commandType = COMMAND_TYPE_OTHER;
    }

    /**
     * Starts Duchess in its text-based interface.
     *
     * @param args command-line arguments, which Duchess does not currently use
     */
    public static void main(String[] args) {
        Duchess duchess = new Duchess();
        Ui ui = new Ui();
        ui.showWelcome();

        while (ui.hasNextLine()) {
            String command = ui.readCommand();

            if (command.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            ui.showSeparator();
            ui.showMessage(duchess.getResponse(command));
            ui.showSeparator();
        }
    }

    /**
     * Processes one command and returns the response for a user interface.
     *
     * @param command the complete command entered by the user
     * @return the response that should be displayed to the user
     */
    public String getResponse(String command) {
        String safeCommand = command == null ? "" : command;
        String lowerCaseCommand = safeCommand.toLowerCase(Locale.ROOT);

        if (safeCommand.equalsIgnoreCase("bye")) {
            commandType = COMMAND_TYPE_BYE;
            exitRequested = true;
            return "Bye. Hope to see you again soon!";
        }

        try {
            if (safeCommand.equalsIgnoreCase("help")) {
                commandType = COMMAND_TYPE_HELP;
                return HELP_RESPONSE;
            } else if (safeCommand.equalsIgnoreCase("list")) {
                commandType = COMMAND_TYPE_LIST;
                return formatTasks(tasks, "Here are the tasks in your list:");
            } else if (lowerCaseCommand.startsWith("find ")) {
                commandType = COMMAND_TYPE_FIND;
                return findTasks(safeCommand);
            } else if (lowerCaseCommand.startsWith("mark ")) {
                commandType = COMMAND_TYPE_MARK;
                return markTask(safeCommand);
            } else if (lowerCaseCommand.startsWith("unmark ")) {
                commandType = COMMAND_TYPE_UNMARK;
                return unmarkTask(safeCommand);
            } else if (lowerCaseCommand.startsWith("delete ")) {
                commandType = COMMAND_TYPE_DELETE;
                return deleteTask(safeCommand);
            } else if (safeCommand.equalsIgnoreCase("mark")) {
                throw new DuchessException("OOPS!!! Please use 'mark <task number>', "
                        + "for example: mark 1.");
            } else if (safeCommand.equalsIgnoreCase("unmark")) {
                throw new DuchessException("OOPS!!! Please use 'unmark <task number>', "
                        + "for example: unmark 1.");
            } else if (safeCommand.equalsIgnoreCase("delete")) {
                throw new DuchessException("OOPS!!! Please use 'delete <task number>', "
                        + "for example: delete 1.");
            } else if (safeCommand.equalsIgnoreCase("find")) {
                throw new DuchessException("OOPS!!! Please use 'find <keyword>', "
                        + "for example: find book.");
            }

            commandType = COMMAND_TYPE_ADD;
            Task task = Parser.parseTask(safeCommand);
            assert task != null : "The parser must return a task for a valid add command";
            tasks.add(task);
            return withSaveWarning("added: " + task);
        } catch (DuchessException exception) {
            commandType = COMMAND_TYPE_ERROR;
            return exception.getMessage();
        }
    }

    /**
     * Returns the category of the most recently processed command.
     *
     * @return the command category used by the GUI to style responses
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * Returns whether the user has requested to leave the conversation.
     *
     * @return {@code true} after the {@code bye} command has been processed
     */
    public boolean isExitRequested() {
        return exitRequested;
    }

    /** Returns the response to a find command. */
    private String findTasks(String command) throws DuchessException {
        String keyword = command.substring("find ".length()).trim();
        if (keyword.isEmpty()) {
            throw new DuchessException("OOPS!!! Please use 'find <keyword>', "
                    + "for example: find book.");
        }

        return formatTasks(tasks.find(keyword), "Here are the matching tasks in your list:");
    }

    /** Returns the response to a mark command. */
    private String markTask(String command) throws DuchessException {
        int taskIndex = Parser.parseTaskIndex(command, "mark ");
        validateTaskIndex(taskIndex);

        tasks.markAsDone(taskIndex);
        return withSaveWarning("Nice! I've marked this task as done:\n  " + tasks.get(taskIndex));
    }

    /** Returns the response to an unmark command. */
    private String unmarkTask(String command) throws DuchessException {
        int taskIndex = Parser.parseTaskIndex(command, "unmark ");
        validateTaskIndex(taskIndex);

        tasks.markAsNotDone(taskIndex);
        return withSaveWarning("Okay, I've marked this task as not done yet:\n  "
                + tasks.get(taskIndex));
    }

    /** Returns the response to a delete command. */
    private String deleteTask(String command) throws DuchessException {
        int taskIndex = Parser.parseTaskIndex(command, "delete ");
        validateTaskIndex(taskIndex);

        Task deletedTask = tasks.delete(taskIndex);
        return withSaveWarning("Noted. I've removed this task:\n  " + deletedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    /** Returns a response after reporting a recoverable storage failure. */
    private String withSaveWarning(String response) {
        try {
            storage.saveTasks(tasks);
            return response;
        } catch (java.io.IOException exception) {
            return SAVE_ERROR + "\n" + response;
        }
    }

    /** Formats a task collection using the same numbering as the CLI. */
    private String formatTasks(TaskList taskList, String heading) {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            taskArray.add(taskList.get(i));
        }
        return formatTasks(taskArray, heading);
    }

    /** Formats a list of tasks using one-based indexes. */
    private String formatTasks(ArrayList<Task> taskArray, String heading) {
        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < taskArray.size(); i++) {
            response.append('\n').append(i + 1).append('.').append(taskArray.get(i));
        }
        return response.toString();
    }

    /** Validates a zero-based task index. */
    private void validateTaskIndex(int taskIndex) throws DuchessException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DuchessException("OOPS!!! Please provide a valid task number between 1 and "
                    + tasks.size() + ".");
        }
        assert taskIndex >= 0 && taskIndex < tasks.size()
                : "A validated task index must refer to an existing task";
    }
}
