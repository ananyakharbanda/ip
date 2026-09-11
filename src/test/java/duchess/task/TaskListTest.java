package duchess.task;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/** Tests the core task collection operations used by Duchess. */
public class TaskListTest {
    /** Verifies that a new task list starts empty. */
    @Test
    public void taskList_newList_hasZeroTasks() {
        TaskList tasks = new TaskList();

        assertEquals(0, tasks.size());
    }

    /** Verifies that adding tasks preserves insertion order and updates the count. */
    @Test
    public void taskList_addTasks_preservesOrderAndSize() {
        Task todo = new Todo("read book");
        Task deadline = new Deadline("return book", "2019-12-02");
        TaskList tasks = new TaskList();

        tasks.add(todo);
        tasks.add(deadline);

        assertAll(
                () -> assertEquals(2, tasks.size()),
                () -> assertSame(todo, tasks.get(0)),
                () -> assertSame(deadline, tasks.get(1))
        );
    }

    /** Verifies that the varargs constructor protects the task list from array changes. */
    @Test
    public void taskList_constructorCopiesInitialTasks_keepsIndependentState() {
        Task todo = new Todo("read book");
        Task[] initialTasks = {todo};
        TaskList tasks = new TaskList(initialTasks);
        initialTasks[0] = new Todo("changed task");

        assertAll(
                () -> assertEquals(1, tasks.size()),
                () -> assertSame(todo, tasks.get(0))
        );
    }

    /** Verifies that deleting a task returns it and reindexes the remaining tasks. */
    @Test
    public void taskList_deleteTask_returnsTaskAndReindexesList() {
        Task first = new Todo("first task");
        Task second = new Event("second task", "Monday");
        TaskList tasks = new TaskList();
        tasks.add(first);
        tasks.add(second);

        Task deletedTask = tasks.delete(0);

        assertAll(
                () -> assertSame(first, deletedTask),
                () -> assertEquals(1, tasks.size()),
                () -> assertSame(second, tasks.get(0))
        );
    }

    /** Verifies that marking a task done updates its state and display icon. */
    @Test
    public void taskList_markAsDone_unfinishedTaskBecomesDone() {
        Task task = new Todo("read book");
        TaskList tasks = new TaskList();
        tasks.add(task);

        tasks.markAsDone(0);

        assertAll(
                () -> assertTrue(task.isDone()),
                () -> assertEquals("X", task.getStatusIcon()),
                () -> assertTrue(task.getCompletedAt() != null)
        );
    }

    /** Verifies that statistics count completed tasks only within the rolling seven-day window. */
    @Test
    public void taskList_getStatistics_countsCompletedAndRecentTasks() {
        Instant now = Instant.parse("2026-09-11T00:00:00Z");
        Task recentTask = new Todo("recent task");
        recentTask.markAsDone(Instant.parse("2026-09-08T00:00:00Z"));
        Task oldTask = new Todo("old task");
        oldTask.markAsDone(Instant.parse("2026-09-03T23:59:59Z"));
        Task incompleteTask = new Todo("incomplete task");
        TaskList tasks = new TaskList(recentTask, oldTask, incompleteTask);

        TaskStatistics statistics = tasks.getStatistics(now);

        assertAll(
                () -> assertEquals(3, statistics.getTotalTasks()),
                () -> assertEquals(2, statistics.getCompletedTasks()),
                () -> assertEquals(1, statistics.getIncompleteTasks()),
                () -> assertEquals(1, statistics.getRecentlyCompletedTasks()),
                () -> assertEquals(67, statistics.getCompletionRatePercentage())
        );
    }

    /** Verifies that an unknown completion time is excluded from recent statistics. */
    @Test
    public void taskList_getStatistics_excludesCompletedTasksWithoutTimestamp() {
        Instant now = Instant.parse("2026-09-11T00:00:00Z");
        Task legacyTask = new Todo("legacy task");
        legacyTask.markAsDone(null);
        TaskList tasks = new TaskList(legacyTask);

        TaskStatistics statistics = tasks.getStatistics(now);

        assertAll(
                () -> assertEquals(1, statistics.getCompletedTasks()),
                () -> assertEquals(0, statistics.getRecentlyCompletedTasks()),
                () -> assertEquals(100, statistics.getCompletionRatePercentage())
        );
    }

    /** Verifies that marking an already completed task does not refresh its known timestamp. */
    @Test
    public void taskList_markAsDone_completedTaskKeepsOriginalTimestamp() {
        Instant firstCompletion = Instant.parse("2026-09-01T00:00:00Z");
        Instant secondCompletion = Instant.parse("2026-09-11T00:00:00Z");
        Task task = new Todo("repeat task");
        TaskList tasks = new TaskList(task);

        tasks.markAsDone(0, firstCompletion);
        tasks.markAsDone(0, secondCompletion);

        assertEquals(firstCompletion, task.getCompletedAt());
    }

    /** Verifies that marking a task not done clears its completed state. */
    @Test
    public void taskList_markAsNotDone_doneTaskBecomesUnfinished() {
        Task task = new Todo("read book");
        task.markAsDone();
        TaskList tasks = new TaskList();
        tasks.add(task);

        tasks.markAsNotDone(0);

        assertAll(
                () -> assertFalse(task.isDone()),
                () -> assertEquals(" ", task.getStatusIcon()),
                () -> assertNull(task.getCompletedAt())
        );
    }

    /** Verifies that finding tasks ignores case while preserving list order. */
    @Test
    public void taskList_findKeyword_returnsCaseInsensitiveMatchesInOrder() {
        Task first = new Todo("read book");
        Task second = new Deadline("return book", "2019-12-02");
        Task third = new Todo("write code");
        TaskList tasks = new TaskList(first, second, third);

        ArrayList<Task> matchingTasks = tasks.find("BOOK");

        assertAll(
                () -> assertEquals(2, matchingTasks.size()),
                () -> assertSame(first, matchingTasks.get(0)),
                () -> assertSame(second, matchingTasks.get(1))
        );
    }
}
