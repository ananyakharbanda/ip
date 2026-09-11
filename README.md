# Duchess ✦ Royal Task Companion

Duchess is a Java 25 task manager with both a command-line interface and a
JavaFX graphical interface. It supports todos, deadlines, events, searching,
completion tracking, and completion statistics.

## Features

- Add `todo`, `deadline`, and `event` tasks.
- Mark, unmark, delete, list, and search tasks.
- View completion statistics, including tasks completed in the past seven days.
- Save tasks automatically to `data/duchess.txt`.
- Use the same command-processing logic from the CLI and JavaFX GUI.
- Use FXML for the stable JavaFX layout and JavaFX controllers for dynamic
  conversation content.

## Quick start

### Prerequisites

- Java Development Kit 25
- IntelliJ IDEA, if you want to work on the project in an IDE
- Python 3, only if you want to run the console UI regression plan

### Start the graphical interface

On macOS or Linux:

```bash
./scripts/run-gui.sh
```

On Windows:

```bat
scripts\run-gui.bat
```

You can also invoke the Gradle wrapper directly with `./gradlew run` or
`gradlew.bat run`.

### Start the command-line interface

On macOS or Linux:

```bash
./scripts/run-cli.sh
```

On Windows:

```bat
scripts\run-cli.bat
```

## Commands

| Command | Example | Purpose |
| --- | --- | --- |
| `todo <description>` | `todo read book` | Add a todo |
| `deadline <description> /by <date>` | `deadline submit report /by 2026-09-30` | Add a deadline |
| `event <description> /at <time>` | `event team meeting /at Friday 3pm` | Add an event |
| `list` | `list` | Display all tasks |
| `find <keyword>` | `find report` | Search task descriptions |
| `mark <number>` | `mark 2` | Mark a task as done |
| `unmark <number>` | `unmark 2` | Mark a task as not done |
| `delete <number>` | `delete 2` | Delete a task |
| `stats` | `stats` | Display completion statistics |
| `help` | `help` | Display the command guide |
| `bye` | `bye` | Exit Duchess |

Task numbers start from 1. Commands are case-insensitive.

## Build and test

Run compilation, the JUnit suite, and Checkstyle with:

```bash
./gradlew check
```

The equivalent project scripts are:

```bash
./scripts/test.sh       # macOS/Linux
scripts\test.bat       # Windows
```

Run the complete console UI regression plan with:

```bash
./scripts/test-ui.sh    # macOS/Linux
scripts\test-ui.bat    # Windows
```

The test plan compares complete user sessions against their expected output.
See [docs/testing.md](docs/testing.md) for the test layers and rationale.

## Creating an executable JAR

Build a self-contained JAR with:

```bash
./gradlew clean shadowJar
```

On Windows, use `gradlew.bat clean shadowJar`. The generated artifact is:

```text
build/libs/duchess.jar
```

Run it with:

```bash
java -jar build/libs/duchess.jar
```

The `build/` directory is ignored by Git and should not be committed.

## Project structure

```text
src/main/java/duchess/
├── Duchess.java              Shared command-processing boundary
├── parser/                   Command parsing and validation
├── task/                     Task domain classes and statistics
├── storage/                  File persistence and legacy-record handling
├── ui/                       Command-line presentation
└── gui/                      JavaFX application and controllers

src/main/resources/
├── view/                     FXML layouts and reusable controls
└── css/                      JavaFX stylesheets
```

The main FXML view composes reusable header, quick-command, conversation, and
composer components. Dynamic dialog content is created by JavaFX code because
it depends on the current command response.

For a fuller explanation, see [docs/architecture.md](docs/architecture.md)
and the [Duchess User Guide](docs/README.md).

## IntelliJ setup

1. Open this repository as an IntelliJ project.
2. Configure the project SDK and language level to Java 25.
3. Refresh the Gradle project.
4. Run the `run` Gradle task for the JavaFX interface, or the `runCli` task for
   the command-line interface.

The Gradle wrapper downloads and uses the project’s configured Gradle version,
so a global Gradle installation is not required.
