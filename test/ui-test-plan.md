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

## Test case 2: Unmark a completed task

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
