/**
 * The main entry point for the Duchess chatbot.
 */
public class Duchess {
    /** Creates a Duchess application entry point. */
    public Duchess() {
    }

    /**
     * Starts Duchess, loads saved tasks, and processes commands until the user exits.
     *
     * @param args command-line arguments, which Duchess does not currently use
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();
        TaskList tasks = storage.loadTasks();
        ui.showWelcome();

        while (ui.hasNextLine()) {
            String command = ui.readCommand();

            if (command.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            ui.showSeparator();

            try {
                if (command.equalsIgnoreCase("list")) {
                    ui.showTasks(tasks);
                } else if (command.toLowerCase().startsWith("mark ")) {
                    markTask(command, tasks, storage, ui);
                } else if (command.toLowerCase().startsWith("unmark ")) {
                    unmarkTask(command, tasks, storage, ui);
                } else if (command.toLowerCase().startsWith("delete ")) {
                    deleteTask(command, tasks, storage, ui);
                } else if (command.equalsIgnoreCase("mark")) {
                    throw new DuchessException("OOPS!!! Please use 'mark <task number>', "
                            + "for example: mark 1.");
                } else if (command.equalsIgnoreCase("unmark")) {
                    throw new DuchessException("OOPS!!! Please use 'unmark <task number>', "
                            + "for example: unmark 1.");
                } else if (command.equalsIgnoreCase("delete")) {
                    throw new DuchessException("OOPS!!! Please use 'delete <task number>', "
                            + "for example: delete 1.");
                } else {
                    tasks.add(Parser.parseTask(command));
                    saveTasks(storage, tasks, ui);
                    ui.showTaskAdded(tasks.get(tasks.size() - 1));
                }
            } catch (DuchessException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showSeparator();
        }
    }

    /** Saves the current state and reports a recoverable disk error. */
    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.saveTasks(tasks);
        } catch (java.io.IOException exception) {
            ui.showSaveError();
        }
    }

    /**
     * Marks the task identified by a one-based index as done.
     *
     * @param command the complete mark command entered by the user
     * @param tasks the stored tasks
     */
    private static void markTask(String command, TaskList tasks, Storage storage, Ui ui)
            throws DuchessException {
        int taskIndex = Parser.parseTaskIndex(command, "mark ");
        validateTaskIndex(taskIndex, tasks.size());

        tasks.markAsDone(taskIndex);
        saveTasks(storage, tasks, ui);
        ui.showTaskMarked(tasks.get(taskIndex));
    }

    /**
     * Marks the task identified by a one-based index as not done.
     *
     * @param command the complete unmark command entered by the user
     * @param tasks the stored tasks
     */
    private static void unmarkTask(String command, TaskList tasks, Storage storage, Ui ui)
            throws DuchessException {
        int taskIndex = Parser.parseTaskIndex(command, "unmark ");
        validateTaskIndex(taskIndex, tasks.size());

        tasks.markAsNotDone(taskIndex);
        saveTasks(storage, tasks, ui);
        ui.showTaskUnmarked(tasks.get(taskIndex));
    }

    /**
     * Deletes the task identified by a one-based index.
     *
     * @param command the complete delete command entered by the user
     * @param tasks the stored tasks
     * @throws DuchessException if the task number is invalid
     */
    private static void deleteTask(String command, TaskList tasks, Storage storage, Ui ui)
            throws DuchessException {
        int taskIndex = Parser.parseTaskIndex(command, "delete ");
        validateTaskIndex(taskIndex, tasks.size());

        Task deletedTask = tasks.delete(taskIndex);
        saveTasks(storage, tasks, ui);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /**
     * Checks that a command refers to a stored task.
     *
     * @param taskIndex the zero-based task index
     * @param taskCount the number of stored tasks
     * @throws DuchessException if the task index is outside the list
     */
    private static void validateTaskIndex(int taskIndex, int taskCount) throws DuchessException {
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new DuchessException("OOPS!!! Please provide a valid task number between 1 and "
                    + taskCount + ".");
        }
    }
}
