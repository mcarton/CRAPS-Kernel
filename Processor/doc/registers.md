Registers
=========

Normal registers
----------------

Register `%r1` to `%r9` can be used as normal registers.

Constant registers
------------------

Three registers are constant. When read their value is always the same (see
bellow). When written to, the value is discarded.

 Name | Value
------|------------------------
`%r0` | `0`
`%r19`| `0xffff` (all bits set)
`%r20`| `1`

Temporary registers
-------------------

Registers `%r21` and `%r22` are used by the ALU and should not be used by user.

PSR (`%r25`)
------------

BRK (`r%26`)
------------

FP (`r%27`)
-----------

RET (`r%28`)
------------

SP (`r%29`)
-----------

PC (`r%30`)
-----------

IR (`r%31`)
-----------

Other registers
---------------

Registers `%r10` to `%r18` and `%r22` to `%r24` are not used.

