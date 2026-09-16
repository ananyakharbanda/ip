# Duchess User Guide

Duchess is your squad task partner: a desktop chatbot that helps you organise
things to do, track deadlines and events, and see your progress.

![Duchess showing a task roster and completion statistics](Ui.png)

[Quick start](#quick-start) · [Features](#features) · [Saving and recovery](#saving-and-recovery)

## Quick start

1. Install **Java 25**.
2. Put `duchess.jar` in a folder where you want to keep your tasks. Open a
   terminal in that folder and run `java -jar duchess.jar`.
3. Type `todo prepare demo slides` in the command box and press **Enter** or
   click **Send**. Duchess confirms the addition.
4. Enter `list` to see your tasks, then `mark 1` to complete the first task.

If you are running from the source project instead, use `./gradlew run`
(macOS/Linux) or `gradlew.bat run` (Windows) from the project root.
For the text-only interface, replace `run` with `runCli`.

The screenshot shows sample tasks; a fresh installation starts with an empty list.

## Features

Replace words in `UPPER_CASE` with your own values. Type the command words and
markers such as `/by` exactly as shown; do not type the placeholder names.
Commands are case-insensitive, and extra whitespace between words is accepted.
Dates must be real dates in `yyyy-MM-dd` format, such as `2026-09-18`.

### Add a task

| Task type | Format | Example |
| --- | --- | --- |
| Todo: something to do | `todo DESCRIPTION` | `todo prepare demo slides` |
| Deadline: something due on a date | `deadline DESCRIPTION /by DATE` | `deadline submit proposal /by 2026-09-18` |
| Event: an activity across dates | `event DESCRIPTION /from START_DATE /to END_DATE` | `event project workshop /from 2026-09-19 /to 2026-09-20` |

Event end dates must be later than start dates. Keep `/from` before `/to`.
For a free-text event time, use `event team meeting /at Thursday 4pm` instead.
Do not combine `/at` with `/from` or `/to`.

Descriptions and date/time values cannot be empty. Duchess rejects duplicate
tasks with the same type and details, even if their completion status differs.

### View tasks: `list`

Shows all tasks in the order you added them. For example:

```text
Here are the tasks in your list:
1.[T][ ] prepare demo slides
2.[D][ ] submit proposal (by: Sep 18 2026)
3.[E][ ] team meeting (at: Thursday 4pm)
```

`[T]`, `[D]`, and `[E]` mean todo, deadline, and event. `[X]` means completed;
`[ ]` means incomplete. Dates display in a readable form such as `Sep 18 2026`.

### Search tasks: `find TEXT`

Example: `find proposal` finds tasks whose descriptions contain `proposal`.
Search is case-insensitive and matches part of a word. Multiple words are
searched as one phrase, so `find team meeting` looks for that phrase.
Dates and event times are not searched. If nothing matches, only the results
heading appears.

Search results retain the numbers from `list`. If a result is numbered `3`,
use `mark 3` to complete it, even if it is the only search result.

### Complete, reopen, or remove tasks

| Action | Format | Example |
| --- | --- | --- |
| Mark completed | `mark NUMBER` | `mark 1` |
| Mark incomplete again | `unmark NUMBER` | `unmark 1` |
| Remove from your list | `delete NUMBER` | `delete 2` |

Use a positive whole-number task index from `list` or `find`. Deleting a task
renumbers later tasks; run `list` again before your next update.
Deletion has no undo command, so check the task number first.

### Check progress: `stats`

Shows total, completed, and incomplete tasks, the number completed in the past
seven days, and your completion percentage. For example:

```text
Task statistics:
Total tasks: 5
Completed tasks: 1
Incomplete tasks: 4
Completed in the past 7 days: 1
Completion rate: 20%
```

These counts cover tasks still in your list. Older saved tasks without a
completion timestamp count toward completed tasks, but not the seven-day count.

### Get help or exit

Enter `help` to see the command playbook. Enter `bye` to close Duchess.
`list`, `stats`, `help`, and `bye` take no extra arguments.

### Use GUI shortcuts

- **Check roster** runs `list`.
- **New case** fills in `todo `; add a description and submit it.
- **Find clue** fills in `find `; add your search text and submit it.
- **Help** displays the command playbook in the conversation.

### Squad aliases

Prefer squad vocabulary? These command names work with the same arguments:

| Standard command | Alias | Standard command | Alias |
| --- | --- | --- | --- |
| `todo` | `case` | `deadline` | `timer` |
| `event` | `briefing` | `list` | `rollcall` |
| `find` | `intel` | `stats` | `report` |
| `mark` | `close` | `unmark` | `reopen` |
| `delete` | `archive` | `help` | `brief` |
| `bye` | `signoff` | | |

`archive` deletes a task just like `delete`; it does not keep an archive.

## Saving and recovery

Duchess automatically saves task changes to `data/duchess.txt` inside the
folder you launched it from. Start from the same folder each time to load your
saved tasks. No manual save command is needed.

- **Invalid command:** read the error, correct your input, and try again.
  Use `help` for the format or `list` for current task numbers.
- **Tasks missing after restarting:** check that you launched Duchess from the
  same folder. A missing data file starts a new, empty list.
- **Startup warning about loading:** saving is disabled to protect your file.
  Back up `data/duchess.txt`, repair the file or its permissions, then restart.
  Changes in this session will be lost when you exit.
- **Warning about saving:** the change is still in memory. Check that the data
  folder is writable and `data/duchess.txt` is a file. After fixing the problem,
  make another task change to retry saving the whole list. If saving was
  disabled at startup, repair the original file and restart first.

To move your tasks to another computer, close Duchess and copy
`data/duchess.txt` into the new launch folder's `data` folder.
