The shell is the most interesting task we have: it is able to communicate
via the serial port, and receive commands.
Each command can also send back a result.

The shell is a task that is always started when we start the board, so that
we can communicate with it and with the operating system.

List of commands
================

echo
----

Send back the parameters to the computer.

help
----

Print a help string with a list of commands.

quit
----

Send a string to the computer, telling it to exit.
It won't close the process or do anything on the board.

kill
----

Kill a running process.
It must take as a parameter the PID of a running process.

ps
--

List all running processes, with their PID and their names.

The name of the process is known because it is at the start of the stack for each process (also
with the optional arguments to the process).

exec
----

Load and execute a new task.

This command will first read a 32 bits integer containing the size of the code.
Then, it will read the code and put it in memory, and start the new process.

run
----

Start a built-in task.

The parameter must be the name of a task:
 - dummy (an infinite loop)
 - leds (put the led on/off according to the switches)
 - counter (a counter on the 7 segments)
