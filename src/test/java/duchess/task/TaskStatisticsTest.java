package duchess.task;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests task-statistics calculations and boundary values. */
public class TaskStatisticsTest {
    /** Verifies incomplete counts and rounded completion percentages. */
    @Test
    public void statistics_partialCompletion_calculatesDerivedValues() {
        TaskStatistics statistics = new TaskStatistics(3, 2, 1);

        assertAll(
                () -> assertEquals(3, statistics.getTotalTasks()),
                () -> assertEquals(2, statistics.getCompletedTasks()),
                () -> assertEquals(1, statistics.getIncompleteTasks()),
                () -> assertEquals(1, statistics.getRecentlyCompletedTasks()),
                () -> assertEquals(67, statistics.getCompletionRatePercentage())
        );
    }

    /** Verifies that an empty task list reports a zero completion rate. */
    @Test
    public void statistics_emptyList_hasZeroCompletionRate() {
        TaskStatistics statistics = new TaskStatistics(0, 0, 0);

        assertEquals(0, statistics.getCompletionRatePercentage());
    }
}
