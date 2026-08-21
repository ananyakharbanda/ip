# Pros and Cons of Using the `Task` Class

The `Task` class represents one task in Duchess. It stores the task description,
tracks whether the task is done, and provides methods such as `markAsDone()` and
`getStatusIcon()`.

## Pros

- **Keeps related data together:** The task description and completion status are
  stored in one object.
- **Improves readability:** Code can say `task.markAsDone()` instead of directly
  changing a separate completion array.
- **Reduces synchronization errors:** `Duchess` no longer needs to keep a task
  description array and a separate status array aligned by index.
- **Makes the program easier to extend:** Features such as deadlines, priorities,
  or tags can be added to `Task` without changing how tasks are stored.
- **Centralizes task behavior:** Status-related logic, such as returning `X` for
  completed tasks, belongs in the class that represents the task.

## Cons

- **Adds another file:** The project now has `Task.java` in addition to
  `Duchess.java`.
- **Introduces more code:** A constructor, fields, and methods are needed even
  for a simple task description.
- **Adds a small layer of indirection:** To access a description or status, the
  program calls methods on a `Task` object instead of using a string directly.
- **Requires understanding classes:** Beginners must understand object creation,
  fields, and methods before modifying task behavior.
- **Does not solve every design issue:** The current program still uses a fixed-
  size array and keeps all tasks in memory only while the program runs.

## Conclusion

For Duchess, using a `Task` class is worthwhile because task data and behavior
belong together. The small amount of extra code makes the program clearer and
provides a better foundation for future task features.
