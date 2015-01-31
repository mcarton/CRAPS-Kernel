// test and set on 1

set 6, %r1
st %r20, [%r1]
.word 0x60000001
