# Duchess testing guide

## Automated checks

Run the full build checks with the Gradle wrapper:

```text
./gradlew check
```

This compiles the project, runs the JUnit suite, and runs Checkstyle. The
equivalent project-owned wrappers are:

```text
scripts/test.sh       # POSIX systems
scripts\\test.bat     # Windows
```

The project targets Java 25. The wrapper scripts use the Java installation
selected in the environment, so configure Java 25 before running them.

## Test layers

- `ParserTest` checks deterministic command parsing and task-index validation.
- `TaskTest`, `DeadlineTest`, and `EventTest` check domain state and display.
- `TaskListTest` and `TaskStatisticsTest` check mutable state and calculations.
- `StorageTest` checks round trips, malformed records, legacy records, and
  missing files using temporary paths.
- `DuchessTest` checks the response boundary shared by the CLI and GUI.
- `test/ui-test-plan.md` checks complete user sessions and exact console output.

Run the UI plan with:

```text
python3 .codex/skills/test-ui/scripts/run_ui_tests.py
```

or use `scripts/test-ui.sh` / `scripts\\test-ui.bat`.

## Test design decisions

Tests prioritize state transitions, persistence boundaries, invalid input, and
derived statistics because failures in these areas affect multiple user-facing
commands. The UI plan remains an end-to-end safety net for exact output and
startup data loading.
