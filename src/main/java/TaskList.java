import java.util.ArrayList;

/** Owns Duchess's collection of tasks and the operations performed on it. */
public class TaskList {
    /** The tasks currently stored by Duchess. */
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * <p>The collection is copied so callers cannot bypass this class's
     * operations by retaining a reference to the original list.</p>
     *
     * @param tasks the initial tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at a zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns the task at a zero-based index. */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /** Marks the task at a zero-based index as done. */
    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    /** Marks the task at a zero-based index as not done. */
    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }
}
