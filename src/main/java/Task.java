/**
 * Represents a task entered by the user.
 *
 * <p>The task is kept in memory for the duration of the program and stores
 * the text exactly as it was entered.</p>
 */
public class Task {
    private final String description;

    /**
     * Creates a task with the given description.
     *
     * @param description the text entered by the user
     */
    public Task(String description) {
        this.description = description;
    }

    /**
     * Returns the task description.
     *
     * @return the text entered by the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the task description when the task is printed.
     *
     * @return the task description
     */
    @Override
    public String toString() {
        return description;
    }
}
