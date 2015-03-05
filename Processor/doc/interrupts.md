Interrupts
==========

Update of the ALU (Arithmetic Logic Unit)
-----------------------------------------

Firstly, we have added a new command for the ALU: `and15`. Its opcode is
`100100`.
It returns `a & 0xf`, saving only the last four bits.

This operation is only meant to be used internally by the processor.

Overview of the interrupt system
--------------------------------

The interrupt system in our processor is really simple.
For now, we have 7 types of interrupts:

 ID |  Name  |           Description
----|--------|----------------------------------
  1 |    PWM | A rising edge of the PWM signal
  2 | btn[1] | The button 1 is pressed
  3 | btn[2] | The button 2 is pressed
  4 | btn[3] | The button 3 is pressed
  5 | RS-232 | A byte has been read
  6 | RS-232 | (The serial port is ready to send another byte)
  7 |   soft | A software interrupt

Note: button 0 is used for the reset.
Note: the sixth handler does not actually work

The identifier of the interrupt act as a priority level.

When an interrupt is launched, the processor checks if its priority level is
strictly higher than the current interrupt level (at the beginning, the
interrupt level is obviously 0). If not, the interrupt is saved and will
be launched again later. If so, the processor looks at the interrupt table,
which is located at the address 1. This table describes where is the handler
for each type of interrupt. For instance, the value at address 1 is the
position of the interrupt handler for the interrupt 1 (*PWM*), the value
at address 2 is the position of the handler for interrupt 2, and so on. The
processor also updates the register *psr* (`%r25`), which contains the value of
each flags and the current interrupt identifier/level.

The *psr* register bits are as follow:

| 0 .. 3 | 4 | 5 | 6 | 7 | 8  ...  31 |
|--------|---|---|---|---|------------|
| int ID | C | V | Z | N | `0xffffff` |

Because the interrupt system needs to push stuff on the stack (*pc* and
*psr*), you have to use a special `ret`, called `reti` at the end of the
handler.
Obviously, an interrupt handler should be transparent. If you want to use a
register, you should probably push its value on the stack first, and pop it
before the `reti`.

When an interrupt handler starts and takes some time, we can probably
receive the same interrupt. In this case, the interrupt handler will be
launched again after the end of the current one. If an interrupt with a
lower priority is received, the corresponding interrupt handler will be
launched after the current one.
If an interrupt with a higher priority is received, the handler will be
launched directly, and we will come back to the current handler after.

Software interrupt
------------------

A new instruction has been added to launch a software interrupt.
Their bits are as follow:

| 0 | 1 | 2 | 3 .. 8 | 9 ... 31 |
|:-:|:-:|:-:|:------:|:--------:|
| 0 | 1 | 1 | 000001 | *unused* |


The interrupt system in deep
----------------------------
# Interrupts module #

The processor has an `interruptions` component responsible for saving
interrupts:

```
module interruptions(rst, clk, pwm_out, button[2..0], rsDataAvailable, rsTBE, soft_int, handle_int: int_id[3..0])
```

This module has a *JK* latch for each interrupt. The *J* signal is a rising
edge of the source of the interrupt (`pwm_out` for interrupt 1,
`button[0]` for interrupt 2, and so on…).

A priority encoder returns in `int_id` the ID of the interrupt with the
higher priority/ID.

The `handle_int` bit, controlled by the sequencer, reset the *JK* latch
corresponding to the higher interrupt. It also sets the register *psr*.

# Sequencer #

At the beginning of each fetch state, the sequencer checks if the interrupts
module has a waiting interrupt with a higher level that the current level.
If so, it starts the interrupt handle procedure.

Firstly, it pushes *pc* and *psr* (containing the actual flags) on the stack.
After that, it sets the `handle_int` bit to 1, so that the interrupts module
reset the *JK* latch of the interrupt, and update the register *psr*.
Finally, it reads the interrupt table and sets *pc* to the position of the
interrupt handler.

The `reti` command just pops *psr* and *pc* from the stack, so that the flags
before the interrupt are restored.
