.pos 0x100
                 ld $0, r0
                 ld $0x1000, r1
                 st r0, (r1)
                 ld (r1), r2
                 ld $0x2000, r3
                 st r2, (r3, r2, 4)

.pos 0x1000
f:              .long 0xffffffff    # first

.pos 0x2000
a:              .long 0xffffffff    # array[0]
                .long 0xffffffff    # array[1]
                .long 0xffffffff    # array[2]
                .long 0xffffff00    # array[3]
                .long 0xffffffff    # array[4]
                .long 0xffffffff    # array[5]
                .long 0xffffffff    # array[6]
                .long 0xffffffff    # array[7]
                .long 0xffffffff    # array[8]
                .long 0xffffffff    # array[9]