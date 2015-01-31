Test And Set
============

This document presents what has been done to support a test-and-set instruction
in the machine.

Presentation
------------

A new instruction kind has been added as none of the existent ones really
fitted this instruction. Their bits are as follow:

| 0 | 1 | 2 | 3..8 | 9 ... 25 | 26 ... 31 |
|:-:|:-:|:-:|:----:|:--------:|:---------:|
| 0 | 1 | 1 | *op* | *unused* |   *reg*   |

*op* represents the operation to be done. The only operation supported at the
moment is `0000000` which is the test-and-set operation but other similar
instructions could be added such as test-and-reset, test-and-complement or
compare-and-swap.

Effect
------

The instruction takes a register as a parameter. This register holds the
address of a word to be set to 1.

Additionally if the word value already was 1 the *Z* flag is sets and if the
word value was 0 the *Z* flag is unset.

Modifications to the `reti` instruction
---------------------------------------

The `reti` instruction had to be modified. The new format is as follow:

| 0  | 1  |    2   | 3 ... 31 |
|:--:|:--:|:------:|:--------:|
| 0¹ | 1¹ | **0**² | *unused* |

1. Unchanged.
2. Previously not checked and could be 1.
