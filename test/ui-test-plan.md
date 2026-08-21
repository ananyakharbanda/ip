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

## Test case 2: Delete a task and preserve list ordering

Aim: Verify that `delete N` removes the selected polymorphic task, reports the
new count, and reindexes the remaining tasks correctly.

Inputs:
```text
todo read book
deadline return book /by June 6th
event project meeting /at Aug 6th 2pm to 4pm
todo join sports club
list
delete 3
list
mark 3
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
added: [D][ ] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
added: [E][ ] project meeting (at: Aug 6th 2pm to 4pm)
____________________________________________________________
____________________________________________________________
added: [T][ ] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (at: Aug 6th 2pm to 4pm)
4.[T][ ] join sports club
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (at: Aug 6th 2pm to 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
3.[T][ ] join sports club
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
3.[T][X] join sports club
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 3: Reject invalid delete commands without changing state

Aim: Verify that invalid delete indexes and missing indexes do not remove the
existing task and that the session continues normally.

Inputs:
```text
todo read book
delete 0
delete 2
list
delete
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
OOPS!!! Please provide a valid task number between 1 and 1.
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a valid task number between 1 and 1.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
OOPS!!! Please use 'delete <task number>', for example: delete 1.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 4: Preserve state after invalid commands

Aim: Verify that invalid commands do not add or modify tasks, and that valid
commands still work afterward.

Inputs:
```text
todo read book
todo
blah
deadline report
mark 2
list
event meeting /at Monday
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
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! I'm sorry, but I don't know what that means :-(
Try todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline must include a non-empty /by value. Example: deadline task description /by time.
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a valid task number between 1 and 1.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
added: [E][ ] meeting (at: Monday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] meeting (at: Monday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 5: Accept case-insensitive typed commands

Aim: Verify that task types and control commands work regardless of letter
case and still produce the correct polymorphic state.

Inputs:
```text
TODO   read book
DEADLINE report /BY Friday
EVENT meeting /AT Monday
MARK 2
LIST
BYE
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
added: [D][ ] report (by: Friday)
____________________________________________________________
____________________________________________________________
added: [E][ ] meeting (at: Monday)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] report (by: Friday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] report (by: Friday)
3.[E][ ] meeting (at: Monday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 6: Reject malformed details and task numbers

Aim: Verify that malformed deadline/event commands and invalid mark commands
leave the task list empty and the session usable.

Inputs:
```text
deadline project
event meeting
mark 1
unmark 0
mark
unmark
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
OOPS!!! A deadline must include a non-empty /by value. Example: deadline task description /by time.
____________________________________________________________
____________________________________________________________
OOPS!!! An event must include a non-empty /at value. Example: event task description /at time.
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a valid task number between 1 and 0.
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a valid task number between 1 and 0.
____________________________________________________________
____________________________________________________________
OOPS!!! Please use 'mark <task number>', for example: mark 1.
____________________________________________________________
____________________________________________________________
OOPS!!! Please use 'unmark <task number>', for example: unmark 1.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 7: Mark and unmark a task

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

## Test case 8: Recover from blank input and whitespace-only tasks

Aim: Verify that blank input and whitespace-only descriptions are rejected,
then a valid task can still be added and listed.

Inputs:
```text

todo   
todo write code
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
OOPS!!! A command cannot be empty. Try todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
added: [T][ ] write code
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] write code
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
