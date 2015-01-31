// test and set on 0

set 6, %r1
st %r0, [%r1]
.word 0x60000001
