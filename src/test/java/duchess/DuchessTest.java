package duchess;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import duchess.storage.Storage;
import duchess.task.TaskList;

/** Tests the command-response boundary shared by the CLI and JavaFX interfaces. */
public class DuchessTest {
    /** Verifies that the GUI-facing response includes the current task list. */
    @Test
    public void getResponse_listCommand_returnsTaskListResponse() {
        Duchess duchess = new Duchess(new Storage(), new TaskList());

        String response = duchess.getResponse("list");

        assertAll(
                () -> assertEquals("Here are the tasks in your list:", response),
                () -> assertEquals("list", duchess.getCommandType()),
                () -> assertFalse(duchess.isExitRequested())
        );
    }

    /** Verifies that the GUI can process the command used to end a conversation. */
    @Test
    public void getResponse_byeCommand_requestsExit() {
        Duchess duchess = new Duchess(new Storage(), new TaskList());

        String response = duchess.getResponse("bye");

        assertAll(
                () -> assertEquals("Bye. Hope to see you again soon!", response),
                () -> assertEquals("bye", duchess.getCommandType()),
                () -> assertTrue(duchess.isExitRequested())
        );
    }
}
