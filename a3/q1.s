.pos 0x100
                 ld $q, r0              # &q => r0
                 ld 20(r0), r1          # value of q[5] => r1
                 ld $s, r2              # &s => r2
                 st r1, (r2)            # stores q[5] => s
                 ld (r0, r1, 4), r3     # loads q[q[5]], which is equivalent to q[s] => r3
                 st r3, (r2)            # stores q[s] => s
                 ld $y, r4              # &y => r4
                 ld $b, r5              # &b => r5
                 st r5, (r4)            # &b => y
                 ld $6, r6              # 6 => r6
                 st r6, (r5)            # 6 => *y (b)
                 ld 8(r0), r6           # q[2] => r6
                 add r0, r6             # q + q[2] (&q[q[2]]) => r6
                 st r6, (r4)            # &q[q[2]] => y
                 ld (r4), r6            # *y => r6
                 ld 12(r0), r7          # q[3] => r7
                 add r6, r7             # *y + q[3] => r7
                 st r7, r6              # *y + q[3] => *y

                 


.pos 0x1000
s:               .long 0x0
b:               .long 0x0
y:               .long 0x0

.pos 0x2000
q:               .long 0x0
                 .long 0x0
                 .long 0x0
                 .long 0x0
                 .long 0x0
                 .long 0x0
                 .long 0x0
                 .long 0x0
                 .long 0x0
                 .long 0x0