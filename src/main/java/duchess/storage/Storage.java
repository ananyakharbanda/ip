package duchess.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;

import duchess.task.Deadline;
import duchess.task.Event;
import duchess.task.Task;
import duchess.task.TaskList;
import duchess.task.Todo;

/**
 * Saves Duchess tasks in a file relative to the project root.
 *
 * <p>Text fields are Base64-encoded so descriptions and details may contain
 * the record separator without making the file ambiguous.</p>
 */
public class Storage {
    /** The relative location used for Duchess's saved task list. */
    private static final Path DATA_FILE = Path.of("data", "duchess.txt");

    /** Creates a storage service that uses Duchess's default data file. */
    public Storage() {
    }

    /**
     * Loads valid task records from disk.
     *
     * <p>A missing file means Duchess has no saved tasks yet. Invalid lines
     * are ignored individually, allowing valid records in a partially
     * corrupted file to remain usable.</p>
     *
     * @return the restored tasks, or an empty task list when the file is absent
     *         or cannot be read
     */
    public TaskList loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.isRegularFile(DATA_FILE)) {
            return new TaskList();
        }

        try {
            for (String line : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
                Task task = deserialize(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException exception) {
            // A damaged or inaccessible file should not prevent Duchess from starting.
            return new TaskList();
        }
        return new TaskList(tasks.toArray(new Task[0]));
    }

    /**
     * Writes the current task list to disk.
     *
     * <p>The parent directory is created when needed. Writing through a
     * temporary file prevents a failed write from leaving a half-written task
     * list.</p>
     *
     * @param tasks the task list to save
     * @throws IOException if the directory or file cannot be written
     */
    public void saveTasks(TaskList tasks) throws IOException {
        Path parent = DATA_FILE.getParent();
        Files.createDirectories(parent);
        Path temporaryFile = Files.createTempFile(parent, "duchess", ".tmp");

        try {
            ArrayList<String> lines = new ArrayList<>();
            for (int i = 0; i < tasks.size(); i++) {
                lines.add(serialize(tasks.get(i)));
            }
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, DATA_FILE, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, DATA_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    /**
     * Converts one task into a type, status, and encoded field record.
     *
     * @param task the task to convert
     * @return one line suitable for the data file
     */
    private String serialize(Task task) {
        String type = task.getType().name().substring(0, 1);
        String status = task.isDone() ? "1" : "0";
        String description = encode(task.getDescription());

        if (task instanceof Deadline deadline) {
            return type + "|" + status + "|" + description + "|" + encode(deadline.getBy().toString());
        }
        if (task instanceof Event event) {
            return type + "|" + status + "|" + description + "|" + encode(event.getAt());
        }
        return type + "|" + status + "|" + description;
    }

    /** Encodes a text field without introducing record separators. */
    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts one saved line back into a task, returning null for bad data.
     *
     * @param line one line from the data file
     * @return the restored task, or null when the record is malformed
     */
    private Task deserialize(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] fields = line.split("\\|", -1);
        if (fields.length < 3 || (!fields[0].equals("T") && !fields[0].equals("D")
                && !fields[0].equals("E")) || (!fields[1].equals("0") && !fields[1].equals("1"))) {
            return null;
        }

        boolean isDone = fields[1].equals("1");
        String description = decode(fields[2]);
        if (description == null || description.isBlank()) {
            return null;
        }

        Task task;
        if (fields[0].equals("T") && fields.length == 3) {
            task = new Todo(description);
        } else if (fields[0].equals("D") && fields.length == 4) {
            String by = decode(fields[3]);
            if (by == null || by.isBlank()) {
                return null;
            }
            try {
                task = new Deadline(description, by);
            } catch (RuntimeException exception) {
                return null;
            }
        } else if (fields[0].equals("E") && fields.length == 4) {
            String at = decode(fields[3]);
            if (at == null || at.isBlank()) {
                return null;
            }
            task = new Event(description, at);
        } else {
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /** Decodes a saved field, returning null when it is not valid Base64. */
    private String decode(String value) {
        try {
            return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
