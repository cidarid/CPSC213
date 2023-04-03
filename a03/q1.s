.pos 0x100
                 ld $q, r0              # &q => r0
                 ld 20(r0), r1          # q[5] => r1
                 ld $s, r2              # &s => r2
                 st r1, (r2)            # q[5] => s
                 ld (r0, r1, 4), r3     # q[q[5]], which equals q[s] => r3
                 st r3, (r2)            # stores q[s] => s
                 ld $y, r4              # &y => r4
                 ld $b, r5              # &b => r5
                 st r5, (r4)            # &b => y
                 ld $6, r6              # 6 => r6
                 st r6, (r5)            # 6 => b, which equals *y
                 ld 8(r0), r6           # q[2] => r6
                 shl $2, r6             # q[2]*4 => r6, to account for 4 byte size in next step
                 add r0, r6             # q + q[2], which equals &q[q[2]] => r6
                 st r6, (r4)            # &q[q[2]] => y
                 ld (r6), r1            # *y => r1
                 ld 12(r0), r7          # q[3] => r7
                 add r1, r7             # *y + q[3] => r7
                 st r7, (r6)            # *y + q[3] => *y
                 halt                   # halt

.pos 0x1000
s:               .long 0x0              # s
b:               .long 0x0              # b
y:               .long 0x0              # y

.pos 0x2000
q:               .long 0x10             # q[0]
                 .long 0x20             # q[1]
                 .long 0x5              # q[2]
                 .long 0x4              # q[3]
                 .long 0x15             # q[4]
                 .long 0x1              # q[5]
                 .long 0x0              # q[6]
                 .long 0x0              # q[7]
                 .long 0x0              # q[8]
                 .long 0x0              # q[9]