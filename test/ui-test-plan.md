# Duchess UI Test Plan

Run the tests from the repository root. The runner compiles the Java sources
into a temporary directory before starting each test case in a fresh process.

Run command: `java -cp {classes} Duchess`

## Test case 1: Mark a task as done

Aim: Verify that `mark N` marks the selected task as done and that `list`
displays `[X]` for that task while leaving other tasks unfinished.

Inputs:
```text
read book
return book
buy bread
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
added: read book
____________________________________________________________
____________________________________________________________
added: return book
____________________________________________________________
____________________________________________________________
added: buy bread
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [X] return book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[ ] read book
2.[X] return book
3.[ ] buy bread
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 2: Unmark a completed task

Aim: Verify that `unmark N` changes a completed task back to unfinished.

Inputs:
```text
read book
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
added: read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [X] read book
____________________________________________________________
____________________________________________________________
Okay, I've marked this task as not done yet:
  [ ] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
