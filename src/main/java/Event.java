/**
 * Represents a task associated with a specified event time.
 */
public class Event extends Task {
    /** The event time supplied by the user. */
    protected String at;

    /**
     * Creates an unfinished event task.
     *
     * @param description the event description
     * @param at the event time
     */
    public Event(String description, String at) {
        super(description);
        this.at = at;
    }

    /**
     * Returns the event display with its type, status, and event time.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (at: " + at + ")";
    }
}
