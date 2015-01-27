STACK    = 0x200
SEGS7    = 0xA0000000 // adresse d'I/O des afficheurs 7-segments

	.org 0
	ba start
	// interrupt table
	.word null_handler // pwm
	.word btn1_handler // btn[1]
	.word btn2_handler // btn[2]
	.word btn3_handler // btn[3]

null_handler:
	reti

btn1_handler:
	set 2, %r2
	st %r2, [%r1]
	ba btn1_handler

btn2_handler:
	set 3, %r2
	st %r2, [%r1]
	ba btn2_handler

btn3_handler:
	set 4, %r2
	st %r2, [%r1]
	ba btn3_handler

start:
	set STACK, %sp
	set SEGS7, %r1

	// activation des afficheurs
	set 0b1111, %r2
	st %r2, [%r1+1]

loop:
	set 1, %r2
	st %r2, [%r1]
	ba loop
