.pos 0x100
                ld $a, r0              # &a => r0
                ld $p, r1              # &p => r1
                ld $b, r2              # &b[0] => r2
                ld $3, r3              # 3 => r3

                st r3, (r0)            # a = 3
                
                st r0, (r1)            # p = &a
      
                ld (r1), r3            # p* => r3 (value of r1, which is the address of a)
                
                ld (r3), r4            # *p => r4 (value of a to r4)
                dec r4                 # *p - 1 => r4 or a-- => r4
                st r4, (r3)            # r4 => *p  (value of a = r3)

                st r2, (r1)            # p = &b[0]
                ld (r1), r3            # p => r3
                ld $4, r7              # 12 => r7
                add r7, r3             # p* + 1
                st r3, (r1)            # p = &b[1]

                ld (r0), r3            # a => r3
                ld (r2, r3, 4), r4     # b[a] => r4
                ld (r1), r5            # p => r5
                st r4, (r5, r3, 4)     # r4 => p[a]

                ld (r1), r3            # p => r3
                ld $12, r7             # 12 => r7
                add r7, r3             # *(p + 3)
                ld (r2), r5            # b[0] => r5
                st r5, (r3)            # r5 => *(p + 3)

                halt                   # halt

.pos 0x1000
a:              .long 0x3              # a = 3
p:              .long 0x0              # *p 

.pos 0x2000
b:              .long 0x7               # b[0] = 7
                .long 0x10              # b[1] = 16
                .long 0x5f              # b[2] = 95
                .long 0x6               # b[3] = 6
                .long 0x13              # b[4] = 19
