/**
 * Represents a task in Duchess's in-memory task list.
 */
public class Task {
    /** The text entered by the user for this task. */
    protected String description;

    /** Whether this task has been marked as done. */
    protected boolean isDone;

    /** The category used when displaying this task. */
    protected TaskType type;

    /**
     * Creates a new, unfinished task.
     *
     * @param description the text entered by the user
     */
    public Task(String description) {
        this(description, TaskType.TODO);
    }

    /**
     * Creates a new task with a specified category.
     *
     * @param description the text entered by the user
     * @param type the task category
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns this task's category.
     *
     * @return the task category
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Returns the symbol used to display the task's completion status.
     *
     * @return {@code "X"} for a completed task, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the common task display, including its completion status.
     *
     * @return the status icon followed by the task description
     */
    @Override
    public String toString() {
        return "[" + type.getIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
