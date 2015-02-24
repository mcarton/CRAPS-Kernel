STACK    = 0x2000
SEGS7    = 0xA0000000 // adresse d'I/O des afficheurs 7-segments

    ba start
    // interrupt table
    .word null_handler // pwm
    .word btn_handler // btn[1]
    .word btn_handler // btn[2]
    .word btn_handler // btn[3]
    .word null_handler
    .word null_handler
    .word null_handler

null_handler:
    reti

btn_handler:
    cmp %r0, %r20
    push %r2
    set 0x42, %r2
    st %r2, [%r1]
    pop %r2
    reti

start:
    set STACK, %sp
    set SEGS7, %r1

    // activation des afficheurs
    set 0b1111, %r2
    st %r2, [%r1+1]

    // boucle qui affiche 1
    set 1, %r2
loop:
    cmp %r20, %r20
    st %r2, [%r1]
    ba loop
