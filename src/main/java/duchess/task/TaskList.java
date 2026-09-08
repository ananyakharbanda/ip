package duchess.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/** Owns Duchess's collection of tasks and the operations performed on it. */
public class TaskList {
    /** The tasks currently stored by Duchess. */
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list containing the supplied tasks.
     *
     * <p>The collection is copied so callers cannot bypass this class's
     * operations by retaining a reference to the original list.</p>
     *
     * @param initialTasks the initial tasks, which may be omitted
     */
    public TaskList(Task... initialTasks) {
        assert initialTasks != null : "A task list requires a non-null initial task array";
        tasks = new ArrayList<>(Arrays.asList(initialTasks));
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        assert task != null : "A task list must not contain null tasks";
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index the zero-based task index
     * @return the task at the specified index
     */
    public Task get(int index) {
        assertValidIndex(index);
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index the zero-based task index
     * @return the removed task
     */
    public Task delete(int index) {
        assertValidIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks the task at a zero-based index as done.
     *
     * @param index the zero-based task index
     */
    public void markAsDone(int index) {
        assertValidIndex(index);
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at a zero-based index as not done.
     *
     * @param index the zero-based task index
     */
    public void markAsNotDone(int index) {
        assertValidIndex(index);
        tasks.get(index).markAsNotDone();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the current number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns tasks whose descriptions contain a keyword, ignoring letter case.
     *
     * @param keyword the text to search for
     * @return matching tasks in their original list order
     */
    public ArrayList<Task> find(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase(Locale.ROOT);
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase(Locale.ROOT)
                        .contains(lowerCaseKeyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /** Documents the index precondition shared by operations on one task. */
    private void assertValidIndex(int index) {
        assert index >= 0 && index < tasks.size()
                : "A task index must refer to an existing task";
    }
}
