.pos 0x100
                 ld $q, r0              # &q => r0
                 halt                   # halt

.pos 0x1000
a:               .long 0x0              # a
p:               .long 0x0              # *p 

.pos 0x2000
b:               .long 0x0              # b[0]
                 .long 0x0              # b[1]
                 .long 0x0              # b[2]
                 .long 0x0              # b[3]
                 .long 0x0              # b[4]
