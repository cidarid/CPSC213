.pos 0x100
                 ld $a, r0          # loads array address into r0
                 ld $0x3, r1        # loads 3 into r1
                 ld (r0, r1, 4), r2 # loads value of a[3] into r2
                 ld $f, r3          # loads f address into r3
                 st r2, (r3)        # stores the value of a[3] into f
                 ld $0x5, r4        # loads 5 into r4
                 ld (r0, r4, 4), r5 # loads value of a[5] into r4
                 st r5, (r0, r1, 4) # stores value of a[5] into a[3]
                 ld (r3), r3        # stores value of f into r3
                 st r3, (r0, r4, 4) # stores value of f into a[5]
                 halt


.pos 0x1000
f:              .long 0xffffffff    # first

.pos 0x2000
a:              .long 0x00000001    # array[0]
                .long 0x00000002    # array[1]
                .long 0x00000003    # array[2]
                .long 0x00000004    # array[3]
                .long 0x00000005    # array[4]
                .long 0x00000006    # array[5]
                .long 0x00000007    # array[6]
                .long 0x00000008    # array[7]
                .long 0x00000009    # array[8]