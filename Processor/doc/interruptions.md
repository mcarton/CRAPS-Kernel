Interruptions
=============

Update of the ALU (Arithmetic Logic Unit)
-----------------------------------------

Firstly, we have added a new command for the ALU: and15, code 100100 (36).
It returns `a & 0xf`, saving only the last four bits.

This operation is only used internally by the processor.


Overview of the interruption system
-----------------------------------

The interruption system in our processor is really simple.
For now, we have only 4 types of interruptions :

 ID |  Name  |           Description
----|--------|----------------------------------
  1 |    PWM | A rising edge of the PWM signal
  2 | btn[1] |         The button 2 is pressed
  3 | btn[2] |         The button 3 is pressed
  4 | btn[3] |         The button 4 is pressed

Note: button 1 is used for the reset

The identifier of the interruption act as a priority level.

When an interruption is launched, the processor check if its priority level is strictly higher than
the current interruption level (at the beginning, the interruption level is obviously 0).
If not, the interruption is saved and will launched again later.
If so, the processor looks at the interruption table, which is located at the address 1.
This table describes where is the handler for each type of interruption. For instance,
the value at address 1 is the position of the interruption handler for the interruption 1 (PWM),
the value at address 2 is the position of the handler for interruption 2, and so on.
The processor also updates the register psr (%r25), which contains the value of each flags and the current
interruption identifier/level.

psr (%r25) bits:

| 0 .. 3 | 4 5 6 7 | 8           31 |
|--------|---------|----------------|
| int ID | C V Z N | 0xff 0xff 0xff |

Because the interruption system need to push stuff on the stack (pc and psr), you have to use a special ret,
called `reti` at the end of the handler.
Obviously, an interruption handler should be transparent. If you want to use a register, you should probably
push its value on the stack first, and pop it before the reti.

When an interruption handler starts and takes some time, we can probably receive the same interruption.
In this case, the interruption handler will be launched again after the end of the current one.
If an interruption with a lower priority is received, the corresponding interruption handler will be launched
after the current one.
If an interruption with a higher priority is received, the handler will be launched directly, and we will
come back to the current handler after.
