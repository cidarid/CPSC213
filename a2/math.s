x`.pos 0x100
                 ld $i, r0      # loads i into r0
                 ld $f, r1      # loads f into r1
                 ld (r1), r1    # loads the value of f into r1
                 ld $0x1, r2    # loads 1 into r2
                 add r1, r2     # f + 1, storing result in r2
                 ld $0x4, r3    # loads 4 into r3
                 add r3, r2     # r2 + 4, storing result in r2
                 shl $0x5, r2   # r2 << 5, storing result in r2
                 and r1, r2     # r2 & f, storing result in r2
                 shr $0x3, r2   # r2 >> 3, storing result in r2
                 st r2, (r0)    # stores r2 into i
                 halt
                 

.pos 0x1000
i:              .long 0xffffffff

.pos 0x2000
f:              .long 0xffffffff