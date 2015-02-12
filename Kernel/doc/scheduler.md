Scheduler
=========

The scheduler is a critical part of the operating system that will, based on a list of processes, run each process in turn.

Use of the interrupt system
---------------------------

To use the scheduler, we need to have a mean of interrupting the running code at a fixed frequency.

To do that we will use the PWM feature of the processor to generate a signal at a frequency we choose. This will trigger
a hardware interrupt, so we can create our interrupt handler to switch the process.

To generate that PWM, we have to set 2 parameters at `0xC0000000` and `0xC0000001` that will allow us to choose the frequency
of it.

Data structure
--------------

The process structure is quite simple: We keep the number of process, the ID of the current process and, for each process,
the address of the top of its stack. That's all.

For all the process that are not currently running, the top of the stack also contains the current address in the code of that
process, and the registers (`%r1` to `%r19`, the flags register, the register with the return address, the frame pointer…).

Initialization
--------------

To initialize the data structure we have to, for all processes except the first (that will start running), push the registers
on the stack so that the interrupt handler will pop them when it will switch to that process.
That simply consist of pushing a lot of zeros to the stack, one for each register.

At the bottom of the stack we will also have to put the address at which to start each process.

Then, we just jump to the start address of the first process.

Interrupt handler
-----------------

The handler is quite simple: For each interrupt, we increment a counter. Once it has reached a certain value,
we will assume it is time to do a context switch (change the current process).

To do the context switch, we just have to push the registers on top of the current stack, change the stack pointer to the stack
of the next process, and pop the registers from the stack.

One nice thing is that we don't have to manage the return address: Because we switch the stack, the `reti` instruction will
return to the address that is stored on top of the stack (and was stored during the last interrupt of that process).
