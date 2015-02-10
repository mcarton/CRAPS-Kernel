ba start

// interrupt table
.word null_handler
.word null_handler
.word null_handler
.word null_handler
.word null_handler
.word soft_handler

null_handler: reti
soft_handler:
    push %r1
    push %r2
    set 0xA0000000, %r1
    set 0x1234, %r2
    st %r2, [%r1]
    add %r1, 1, %r1
    setq 0b1111, %r2
    st %r2, [%r1]
    pop %r2
    pop %r1
    reti

start:
    set 0x1fff, %sp
    setq 1, %r1
    setq 2, %r2
    .word 0x60800000 // soft int
    setq 3, %r3
    setq 4, %r4
