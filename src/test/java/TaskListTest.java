import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /** Verifies that the constructor protects the task list from later source-list changes. */
    @Test
    public void taskList_constructorCopiesInputList_keepsIndependentState() {
        Task todo = new Todo("read book");
        ArrayList<Task> initialTasks = new ArrayList<>();
        initialTasks.add(todo);

        TaskList tasks = new TaskList(initialTasks);
        initialTasks.clear();

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
                () -> assertEquals("X", task.getStatusIcon())
        );
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
                () -> assertEquals(" ", task.getStatusIcon())
        );
    }
}
