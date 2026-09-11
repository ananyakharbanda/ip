package duchess;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import duchess.storage.Storage;
import duchess.task.Task;
import duchess.task.TaskList;
import duchess.task.Todo;

/** Tests the command-response boundary shared by the CLI and JavaFX interfaces. */
public class DuchessTest {
    @TempDir
    private Path temporaryDirectory;

    /** Verifies that the GUI-facing response includes the current task list. */
    @Test
    public void getResponse_listCommand_returnsTaskListResponse() {
        Duchess duchess = createDuchess(new TaskList());

        String response = duchess.getResponse("list");

        assertAll(
                () -> assertEquals("Here are the tasks in your list:", response),
                () -> assertEquals("list", duchess.getCommandType()),
                () -> assertFalse(duchess.isExitRequested())
        );
    }

    /** Verifies that the help command lists every supported command and input format. */
    @Test
    public void getResponse_helpCommand_listsAvailableCommands() {
        Duchess duchess = createDuchess(new TaskList());

        String response = duchess.getResponse("help");

        assertAll(
                () -> assertTrue(response.contains("todo <description>")),
                () -> assertTrue(response.contains("deadline <description> /by <date>")),
                () -> assertTrue(response.contains("event <description> /at <time>")),
                () -> assertTrue(response.contains("find <keyword>")),
                () -> assertTrue(response.contains("mark <task number>")),
                () -> assertTrue(response.contains("unmark <task number>")),
                () -> assertTrue(response.contains("delete <task number>")),
                () -> assertTrue(response.contains("help")),
                () -> assertTrue(response.contains("bye")),
                () -> assertEquals("help", duchess.getCommandType())
        );
    }

    /** Verifies that the GUI can process the command used to end a conversation. */
    @Test
    public void getResponse_byeCommand_requestsExit() {
        Duchess duchess = createDuchess(new TaskList());

        String response = duchess.getResponse("bye");

        assertAll(
                () -> assertEquals("Bye. Hope to see you again soon!", response),
                () -> assertEquals("bye", duchess.getCommandType()),
                () -> assertTrue(duchess.isExitRequested())
        );
    }

    /** Verifies that statistics use the injected current time and exact response format. */
    @Test
    public void getResponse_statsCommand_returnsStatistics() {
        Instant now = Instant.parse("2026-09-11T00:00:00Z");
        Task completedTask = new Todo("completed task");
        TaskList tasks = new TaskList(completedTask, new Todo("pending task"));
        Duchess duchess = new Duchess(new Storage(dataFile()), tasks, Clock.fixed(now, ZoneOffset.UTC));

        duchess.getResponse("mark 1");
        String response = duchess.getResponse("STATS");

        assertAll(
                () -> assertEquals("Task statistics:\n"
                        + "Total tasks: 2\n"
                        + "Completed tasks: 1\n"
                        + "Incomplete tasks: 1\n"
                        + "Completed in the past 7 days: 1\n"
                        + "Completion rate: 50%", response),
                () -> assertEquals("stats", duchess.getCommandType())
        );
    }

    /** Verifies that the stats command rejects arguments instead of silently ignoring them. */
    @Test
    public void getResponse_statsCommandWithArguments_returnsHelpfulError() {
        Duchess duchess = createDuchess(new TaskList());

        String response = duchess.getResponse("stats today");

        assertEquals("OOPS!!! Please use 'stats' without arguments.", response);
    }

    /** Verifies that adding and finding commands share the same task state. */
    @Test
    public void getResponse_addAndFindCommands_shareTaskState() {
        Duchess duchess = createDuchess(new TaskList());

        duchess.getResponse("todo read book");
        duchess.getResponse("deadline return book /by 2019-12-02");
        String response = duchess.getResponse("find BOOK");

        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[T][ ] read book\n"
                + "2.[D][ ] return book (by: Dec 02 2019)", response);
    }

    /** Verifies that mark, unmark, and delete commands update task state in order. */
    @Test
    public void getResponse_markUnmarkDeleteCommands_updateTaskState() {
        TaskList tasks = new TaskList(new Todo("first task"), new Todo("second task"));
        Duchess duchess = createDuchess(tasks);

        duchess.getResponse("mark 1");
        duchess.getResponse("unmark 1");
        String response = duchess.getResponse("delete 2");

        assertAll(
                () -> assertEquals("Noted. I've removed this task:\n  [T][ ] second task\n"
                        + "Now you have 1 tasks in the list.", response),
                () -> assertFalse(tasks.get(0).isDone()),
                () -> assertEquals(1, tasks.size())
        );
    }

    /** Verifies that invalid commands set the GUI-facing error category. */
    @Test
    public void getResponse_invalidCommand_setsErrorCommandType() {
        Duchess duchess = createDuchess(new TaskList());

        String response = duchess.getResponse("not a command");

        assertAll(
                () -> assertTrue(response.startsWith("OOPS!!!")),
                () -> assertEquals("error", duchess.getCommandType()),
                () -> assertFalse(duchess.isExitRequested())
        );
    }

    /** Returns a Duchess instance whose persistence is isolated to this test. */
    private Duchess createDuchess(TaskList tasks) {
        return new Duchess(new Storage(dataFile()), tasks);
    }

    /** Returns the temporary data file used by this test class. */
    private Path dataFile() {
        return temporaryDirectory.resolve("duchess.txt");
    }
}
