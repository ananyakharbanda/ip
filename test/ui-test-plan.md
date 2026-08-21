# Duchess UI Test Plan

Run the tests from the repository root. The runner compiles the Java sources
into a temporary directory before starting each test case in a fresh process.

Run command: `java -cp {classes} Duchess`

## Test case 1: Create and mark multiple task types

Aim: Verify that `todo`, `deadline`, and `event` commands create different
subtypes stored together, and that `mark N` works polymorphically.

Inputs:
```text
todo read book
deadline return book /by Sunday
event buy bread /at Saturday
mark 2
list
bye
```

Expected output:
```text
____________________________________________________________
+------------------------+
|        Duchess         |
+------------------------+
Hello! I'm Duchess.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: [T][ ] read book
____________________________________________________________
____________________________________________________________
added: [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
added: [E][ ] buy bread (at: Saturday)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Sunday)
3.[E][ ] buy bread (at: Saturday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 2: Handle empty and unknown commands

Aim: Verify that an empty todo description and an unknown command produce
specific error messages without terminating the session.

Inputs:
```text
todo
blah
bye
```

Expected output:
```text
____________________________________________________________
+------------------------+
|        Duchess         |
+------------------------+
Hello! I'm Duchess.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! I'm sorry, but I don't know what that means :-(
Try todo, deadline, event, list, mark, unmark, or bye.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 3: Handle malformed details and invalid task numbers

Aim: Verify that deadline and event details, as well as task numbers, produce
specific correction messages when they are invalid.

Inputs:
```text
deadline project
event meeting
mark 1
bye
```

Expected output:
```text
____________________________________________________________
+------------------------+
|        Duchess         |
+------------------------+
Hello! I'm Duchess.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline must include a non-empty /by value. Example: deadline task description /by time.
____________________________________________________________
____________________________________________________________
OOPS!!! An event must include a non-empty /at value. Example: event task description /at time.
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a valid task number between 1 and 0.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 4: Unmark a completed task

Aim: Verify that `unmark N` changes a completed task back to unfinished.

Inputs:
```text
todo read book
mark 1
unmark 1
list
bye
```

Expected output:
```text
____________________________________________________________
+------------------------+
|        Duchess         |
+------------------------+
Hello! I'm Duchess.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: [T][ ] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Okay, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
