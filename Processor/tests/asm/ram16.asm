RAM16_BEGIN = 0xE0000000
RAM16_END = 0xE03FFFFF
SEGS7 = 0xA0000000

    ba start
    // interrupt table
    .word null_handler
    .word null_handler
    .word null_handler
    .word null_handler
    .word null_handler
    .word null_handler
    .word null_handler

null_handler: reti
start:
    set 0x2000, %sp

    // display 0
    set SEGS7, %r1
    set 0b1111, %r2
    st %r2, [%r1+1]
    set 0, %r2
    st %r2, [%r1]

    // begin
    set RAM16_BEGIN, %r2
    set RAM16_END, %r9
    set 0xffff, %r8
loop:
    st %r2, [%r2]
    ld [%r2], %r3
    and %r2, %r8, %r4 // r4 = r2 & 0xffff
    cmp %r4, %r3
    bne error
    add %r2, %r20, %r2 // %r2++
    cmp %r2, %r9
    bne loop
    ba success

    // display 9999
error:
    set 0x9999, %r2
    st %r2, [%r1]
    ba error

    // display 1111
success:
    set 0x1111, %r2
    st %r2, [%r1]
    ba success
