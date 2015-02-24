STACK    = 0x2000
SEGS7    = 0xA0000000 // adresse d'I/O des afficheurs 7-segments

    ba start
    // interrupt table
    .word null_handler // pwm
    .word btn1_handler // btn[1]
    .word btn2_handler // btn[2]
    .word btn3_handler // btn[3]
    .word null_handler
    .word null_handler
    .word null_handler

null_handler:
    reti

btn1_handler:
    push %r2
    push %r3
    push %r4
    set 2, %r2

    // compte jusqu'à 3200000
    set 0, %r3
    set 3200000, %r4 // max
loop_btn1:
    st %r2, [%r1]
    add %r20, %r3, %r3
    cmp %r3, %r4
    bleu loop_btn1

    pop %r4
    pop %r3
    pop %r2
    reti

btn2_handler:
    push %r2
    push %r3
    push %r4
    set 3, %r2

    // compte jusqu'à 3200000
    set 0, %r3
    set 3200000, %r4 // max
loop_btn2:
    st %r2, [%r1]
    add %r20, %r3, %r3
    cmp %r3, %r4
    bleu loop_btn2

    pop %r4
    pop %r3
    pop %r2
    reti

btn3_handler:
    push %r2
    push %r3
    push %r4
    set 4, %r2

    // compte jusqu'à 3200000
    set 0, %r3
    set 3200000, %r4 // max
loop_btn3:
    st %r2, [%r1]
    add %r20, %r3, %r3
    cmp %r3, %r4
    bleu loop_btn3

    pop %r4
    pop %r3
    pop %r2
    reti

start:
    set STACK, %sp
    set SEGS7, %r1

    // activation des afficheurs
    set 0b1111, %r2
    st %r2, [%r1+1]
    
    // boucle qui affiche 1
loop:
    set 1, %r2
    st %r2, [%r1]
    ba loop
