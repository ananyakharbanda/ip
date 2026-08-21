/**
 * Represents a basic todo task.
 */
public class Todo extends Task {
    /**
     * Creates an unfinished todo task.
     *
     * @param description the todo description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the todo display with its type and completion status.
     *
     * @return the todo type icon followed by the common task display
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
