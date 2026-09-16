package duchess.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
    /** Verifies actual date storage and a human-readable range, including same-day events. */
    @Test
    public void event_dateRange_storesAndFormatsDates() {
        Event event = new Event("orientation", "2026-09-15", "2026-09-17");
        assertEquals(LocalDate.of(2026, 9, 15), event.getFrom());
        assertEquals(LocalDate.of(2026, 9, 17), event.getTo());
        assertEquals("[E][ ] orientation (from: Sep 15 2026 to: Sep 17 2026)", event.toString());
        Event sameDay = new Event("meeting", "2026-09-15", "2026-09-15");
        assertEquals(sameDay.getFrom(), sameDay.getTo());
    }

    /** Verifies that impossible dates and reversed ranges are rejected. */
    @Test
    public void event_invalidRange_throwsException() {
        assertThrows(DateTimeParseException.class,
                () -> new Event("meeting", "2026-02-30", "2026-03-01"));
        assertThrows(IllegalArgumentException.class,
                () -> new Event("meeting", "2026-09-17", "2026-09-15"));
    }
}
