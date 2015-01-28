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


The interruption system in deep
-------------------------------

# Interruptions module #

The processor has an `interruptions` component responsible for saving interruptions:
`module interruptions(rst, clk, pwm_out, button[2..0], handle_int: int_id[3..0])`

This module has a JK latch for each interruption. The J signal is a rising edge of the source of the
interruption (pwm_out for int 1, button[0] for int 2, and so on…).

A priority encoder returns in `int_id` the ID of the interruption with the higher priority/ID.

The handle_int bit, controlled by the sequencer, reset the JK latch corresponding to the higher interruption.
It also set the register psr.

# Sequencer #

At the beginning of each fetch state, the sequencer checks if the interruptions module has a waiting interruption
with a higher level that the current level. If so, it starts the interruption handle procedure.

Firstly, it pushes pc and psr (containing the actual flags) on the stack.
After that, it sets the handle_int bit to 1, so that the interruptions module reset the JK latch of the interruption, and update the register psr.
Finally, it reads the interruption table and sets pc to the position of the interruption handler.

The reti command just pops psr and pc from the stack, so that the flags before the interruption are restored.
