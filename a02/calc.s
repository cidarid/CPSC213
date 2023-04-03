.pos 0x100
                 ld $c, r0
                 ld $m, r1
                 ld $y, r2
                 ld (r1), r3        # loads m's value into r3
                 ld $1, r4          # puts 1 into r4
                 add r3, r4         # puts m + 1 into r4
                 ld (r2, r3, 4), r5 # puts y[m] into r5
                 ld (r2, r4, 4), r6 # puts y[m+1] into r6
                 add r5, r6         # puts y[m] + y[m+1] into r6
                 st r6, (r0)        # stores y[m] + y[m+1] into c
                 ld (r0), r7        # gets c from memory
                 ld $0xff, r0       # loads 0xff into r0
                 and r0, r7         # puts c & 0xff into r7
                 ld $r, r0          # loads r into r0
                 st r7, (r0)        # stores c & 0xff into r
                 halt


.pos 0x1000
m:              .long 0x00000004
r:              .long 0xffffffff
c:              .long 0xffffffff

.pos 0x2000
y:              .long 0x00000001    # y[0]
                .long 0x00000002    # y[1]
                .long 0x00000003    # y[2]
                .long 0x00000004    # y[3]
                .long 0x00000121    # y[4]
                .long 0x00000005    # y[5]
                .long 0x00000007    # y[6]
                .long 0x00000008    # y[7]