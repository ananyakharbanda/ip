package duchess.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests event-specific time storage and display behavior. */
public class EventTest {
    /** Verifies that an event stores and displays its supplied time. */
    @Test
    public void event_timeValue_isStoredAndDisplayed() {
        Event event = new Event("buy bread", "Saturday");

        assertEquals("Saturday", event.getAt());
        assertEquals("[E][ ] buy bread (at: Saturday)", event.toString());
    }
}
