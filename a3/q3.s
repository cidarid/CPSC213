.pos 0x100
                ld $a, r0              # &a => r0
                ld $p, r1              # &p => r1
                ld $b, r2              # &b[0] => r2
                
                st r0, (r1)            # p = &a
                
                ld (r1), r3            # &a => r3
                ld (r3), r7            # a => r3
                dec r7                 # *p - 1 => r3 or a-- => r3

                st r2, (r1)            # p = &b[0]
                shl $2, r1             # p++ or (p = &b[1])

                st r0, (r4)            # a => r4
                ld (r2, r4, 4), r5     # b[a] => r5
                st r5, (r1, r4, 4)     # p[a] = b[a]

                ld (r2), r6            # b[0] => r6
                st r6, 12(r1)          # *(p+3) = b[0]

                halt                   # halt

.pos 0x1000
a:              .long 0x3              # a = 3
p:              .long 0x0              # *p 

.pos 0x2000
b:              .long 0x7               # b[0] = 7
                .long 0x13              # b[1] = 19
                .long 0x5f              # b[2] = 95
                .long 0x6               # b[3] = 6
                .long 0x13              # b[4] = 19
