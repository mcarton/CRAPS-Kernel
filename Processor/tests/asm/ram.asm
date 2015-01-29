RAM_BEGIN = 100
RAM_END = 8192
SEGS7 = 0xA0000000

	// display 0
	set SEGS7, %r1
	set 0b1111, %r2
	st %r2, [%r1+1]
	set 0, %r2
	st %r2, [%r1]

	// begin
	set RAM_BEGIN, %r2
	set RAM_END, %r9
loop:
	st %r2, [%r2]
	ld [%r2], %r3
	cmp %r2, %r3
	bne error
	add %r2, %r20, %r2
	cmp %r2, %r9
	bne loop
	ba success

	// display 9999
error:
	set 0x9999, %r3
	st %r3, [%r1]
	ba error

	// display 1111
success:
	set 0x1111, %r3
	st %r3, [%r1]
	ba success
