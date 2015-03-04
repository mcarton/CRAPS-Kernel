Registers
=========

Normal registers
----------------

Register `%r1` to `%r19` can be used as normal registers.

Constant registers
------------------

Two registers are constant. When read their value is always the same (see
bellow). When written to, the value is discarded.

 Name | Value
------|------------------------
`%r0` | `0`
`%r20`| `1`

Temporary registers
-------------------

Registers `%r21` and `%r22` are used by the ALU and should not be used by user.

%r24
----

%r24 is used by the compiler to store the position of static variables (such as
global variables and strings).

PSR (`%r25`)
------------

The %psr register bits are as follow:

| 0 .. 3 | 4 | 5 | 6 | 7 | 8  ...  31 |
|--------|---|---|---|---|------------|
| int ID | C | V | Z | N | `0xffffff` |

BRK (`%r26`)
------------

%r26 is the breakpoint position.

FP (`%r27`)
-----------

%r27 is the current frame's base pointer.

RET (`%r28`)
------------

%r28 contains the next instruction to execute at the end of the current function.

SP (`%r29`)
-----------

%r29 contains the position of the top of the stack.

PC (`%r30`)
-----------

%r30 contains the current program counter, ie. the next instruction to execute.

IR (`%r31`)
-----------

%r31 contains the current instruction.

Other registers
---------------

Register `%r23` is not used.

