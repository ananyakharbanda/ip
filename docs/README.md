# Duchess User Guide

Duchess is a task manager that supports todos, deadlines, events, searching,
completion tracking, and completion statistics. The same command behavior is
available from the command-line interface and the JavaFX graphical interface.

## Running Duchess

Use Java 25 and run one of the following Gradle tasks from the project root:

```text
./gradlew run       # JavaFX graphical interface
./gradlew runCli    # command-line interface
```

Tasks are saved in `data/duchess.txt` and loaded again the next time Duchess
starts. Records that cannot be parsed are skipped so that one malformed record
does not prevent the remaining tasks from loading.

## Commands

### Add tasks

Add a general task with:

```text
todo <description>
```

Add a task with a date using:

```text
deadline <description> /by <yyyy-MM-dd>
```

For example, `deadline submit report /by 2026-09-30` creates a deadline task.

Add an event with:

```text
event <description> /at <time>
```

For example, `event team meeting /at Friday 3pm` creates an event task.

### View and search tasks

Use `list` to display every task in insertion order:

```text
list
```

Use `find <keyword>` to display tasks whose descriptions contain the keyword,
without changing the underlying task list:

```text
find report
```

### Update tasks

Tasks are numbered starting from 1. Mark, unmark, or delete a task with:

```text
mark <task number>
unmark <task number>
delete <task number>
```

For example, `mark 2` marks the second task as completed.

### View statistics and help

Use `stats` to view a summary of the tasks currently managed by Duchess:

```text
Task statistics:
Total tasks: 2
Completed tasks: 1
Incomplete tasks: 1
Completed in the past 7 days: 1
Completion rate: 50%
```

The seven-day count includes completed tasks with a known completion time in
the previous seven days. Legacy completed records without a timestamp remain
readable and count toward the overall completed total.

Use `help` to display the supported commands and `bye` to exit Duchess.

## JavaFX interface

The graphical interface uses JavaFX controls for the application window,
conversation area, command buttons, and composer. Its stable page structure is
declared in FXML, while dynamic task responses and dialogs are built in JavaFX
code so that they can react to the current application state.
