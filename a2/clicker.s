ld $a, r0
ld $i, r1
ld (r1), r1
ld (r0, r1, 4) , r2
ld $2, r1
st r2, 8(r0)

.pos 0x1000
a:              .long 0x00000001    # array[0]
                .long 0x00000002    # array[1]
                .long 0x00000003    # array[2]
                .long 0x00000004    # array[3]
                .long 0x00000005    # array[4]
                .long 0x00000006    # array[5]
                .long 0x00000007    # array[6]
                .long 0x00000008    # array[7]
                .long 0x00000009    # array[8]

.pos 0x2000
i:              .long 0x00000001    # array[0]