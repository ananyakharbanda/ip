package duchess.storage;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import duchess.task.Deadline;
import duchess.task.Event;
import duchess.task.Task;
import duchess.task.TaskList;
import duchess.task.Todo;

/** Tests persistence, legacy compatibility, and malformed-record handling. */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    /** Verifies that saving and loading preserves every supported task subtype. */
    @Test
    public void storage_saveAndLoad_preservesTaskTypesAndCompletion() throws Exception {
        Path dataFile = temporaryDirectory.resolve("nested").resolve("duchess.txt");
        Storage storage = new Storage(dataFile);
        Task completedTodo = new Todo("read book");
        completedTodo.markAsDone(Instant.parse("2026-09-01T00:00:00Z"));
        TaskList tasks = new TaskList(
                completedTodo,
                new Deadline("return book", "2019-12-02"),
                new Event("buy bread", "Saturday"));

        storage.saveTasks(tasks);
        TaskList restored = storage.loadTasks();

        assertAll(
                () -> assertEquals(3, restored.size()),
                () -> assertTrue(restored.get(0) instanceof Todo),
                () -> assertTrue(restored.get(0).isDone()),
                () -> assertEquals("read book", restored.get(0).getDescription()),
                () -> assertEquals(Instant.parse("2026-09-01T00:00:00Z"), restored.get(0).getCompletedAt()),
                () -> assertEquals(LocalDate.of(2019, 12, 2), ((Deadline) restored.get(1)).getBy()),
                () -> assertEquals("Saturday", ((Event) restored.get(2)).getAt())
        );
    }

    /** Verifies that malformed lines are ignored without discarding valid records. */
    @Test
    public void storage_malformedRecord_isIgnored() throws Exception {
        Path dataFile = temporaryDirectory.resolve("duchess.txt");
        Storage storage = new Storage(dataFile);
        storage.saveTasks(new TaskList(new Todo("valid task")));
        Files.writeString(dataFile, Files.readString(dataFile) + "\nnot a task record\n", StandardCharsets.UTF_8);

        TaskList restored = storage.loadTasks();

        assertEquals(1, restored.size());
        assertEquals("valid task", restored.get(0).getDescription());
    }

    /** Verifies that completed records from the legacy format remain readable. */
    @Test
    public void storage_legacyCompletedRecord_loadsWithoutTimestamp() throws Exception {
        Path dataFile = temporaryDirectory.resolve("duchess.txt");
        String encodedDescription = Base64.getEncoder().encodeToString(
                "legacy task".getBytes(StandardCharsets.UTF_8));
        Files.writeString(dataFile, "T|1|" + encodedDescription + "\n", StandardCharsets.UTF_8);

        Task restored = new Storage(dataFile).loadTasks().get(0);

        assertAll(
                () -> assertTrue(restored.isDone()),
                () -> assertNull(restored.getCompletedAt()),
                () -> assertFalse(restored.getDescription().isBlank())
        );
    }

    /** Verifies that a missing data file produces an empty task list. */
    @Test
    public void storage_missingFile_returnsEmptyTaskList() {
        TaskList restored = new Storage(temporaryDirectory.resolve("missing.txt")).loadTasks();

        assertEquals(0, restored.size());
    }

}
