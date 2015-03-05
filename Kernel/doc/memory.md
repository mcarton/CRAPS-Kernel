The memory management is made of three functions (that are system calls):
 - malloc
 - free
 - realloc

These functions allow any program to dynamically allocate and free memory.

Malloc can allocate memory, free can deallocate previously allocated memory, and realloc
is used to increase the size of an allocated memory block (in case there is not enough room,
the block will be copied, and the returned value is always the new address of the allocated
memory).

Internals
---------

Internally, before each allocated memory block we store a header. This header contains three things :
 - The last bit is used to tell if the block is in use
 - The 7 bits before are used to store the ID of the process which allocated the memory.
   This allows us to automatically free all the memory allocated by a process when it is killed.
 - All the bits before (so, all but the first byte) contains the size of the block.

Our current implementation doesn't support the merge or split of block. To maximize block reutilization,
we only allocate block with a size that is a power of two.

To say, if we call « malloc(10) » a block of size 16 will be allocated. If we free it, the last bit of its header
will be set to zero.
If we then call « malloc(16) », the same block will be reused (as it is free and have the good size).
We would have called « malloc(5) » instead, it would not have been reused (we would have searched for a block of size 8).

A small optimization is that when a block is freed, if it is the last block of the list, it is completely erased and can be replaced
by a block of a different size.

Let's take a little example from the code :

    c = c + (*c >> 8) + 1;

If c is the pointer to the header of a block, this line will have it point to the pointer of the next block :
« (*c >> 8) » is the size of the block, so if we increase c by that size, add one (because of the size of the current header),
we will point to the next block header.
