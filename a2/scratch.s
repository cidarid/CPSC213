.pos 0x100
                 ld $i, r0
                 ld (r0), r0
                 ld $b, r1
                 ld $3, r2
                 ld 68(r2), r2
                 shr $68, r2
                 halt



.pos 0x1000
b:               .long 0x00000004 # b[0]
                 .long 0x00000002 # b[1]
                 .long 0x00000003 # b[2]
                 .long 0x00000004 # b[3]


.pos 0x2000
i:               .long 0x1