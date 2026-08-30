import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests the task-index parsing behavior used by Duchess commands. */
public class ParserTest {
    /** Verifies that a valid one-based index becomes the expected zero-based index. */
    @Test
    public void parseTaskIndex_validIndex_returnsZeroBasedIndex() {
        assertAll(
                () -> assertEquals(0, Parser.parseTaskIndex("mark 1", "mark ")),
                () -> assertEquals(11, Parser.parseTaskIndex("delete 12", "delete "))
        );
    }

    /** Verifies that whitespace around a valid index is ignored. */
    @Test
    public void parseTaskIndex_whitespacePaddedIndex_returnsZeroBasedIndex() {
        assertEquals(0, Parser.parseTaskIndex("delete   1  ", "delete "));
    }

    /** Verifies that zero is rejected as an invalid one-based index. */
    @Test
    public void parseTaskIndex_zeroIndex_returnsNegativeValue() {
        assertTrue(Parser.parseTaskIndex("mark 0", "mark ") < 0);
    }

    /** Verifies that a negative task number is rejected as an invalid index. */
    @Test
    public void parseTaskIndex_negativeIndex_returnsNegativeValue() {
        assertTrue(Parser.parseTaskIndex("delete -1", "delete ") < 0);
    }

    /** Verifies that alphabetic input is rejected. */
    @Test
    public void parseTaskIndex_alphabeticIndex_returnsMinusOne() {
        assertEquals(-1, Parser.parseTaskIndex("mark abc", "mark "));
    }

    /** Verifies that decimal input is rejected. */
    @Test
    public void parseTaskIndex_decimalIndex_returnsMinusOne() {
        assertEquals(-1, Parser.parseTaskIndex("delete 1.5", "delete "));
    }

    /** Verifies that a missing index is rejected. */
    @Test
    public void parseTaskIndex_missingIndex_returnsMinusOne() {
        assertEquals(-1, Parser.parseTaskIndex("mark", "mark "));
    }

    /** Verifies that whitespace-only input is rejected. */
    @Test
    public void parseTaskIndex_blankIndex_returnsMinusOne() {
        assertEquals(-1, Parser.parseTaskIndex("unmark   ", "unmark "));
    }

    /** Verifies that extra tokens after an index are rejected. */
    @Test
    public void parseTaskIndex_extraTokens_returnsMinusOne() {
        assertEquals(-1, Parser.parseTaskIndex("delete 1 extra", "delete "));
    }

    /** Verifies that an integer outside the supported range is rejected. */
    @Test
    public void parseTaskIndex_overflowingIndex_returnsMinusOne() {
        assertEquals(-1, Parser.parseTaskIndex("mark 2147483648", "mark "));
    }
}
