.pos 0x100
            ld $n, r0           # r0 = address of n
            ld $c, r2           # r2 = address of c
            ld $a, r3           # r3 = address of a[0]
            ld $b, r4           # r4 = address of b[0]

            ld (r0), r0         # r0 = value of n
            ld $0, r1           # r1 = temp_i = 0
            ld (r2), r2         # r2 = value of c
            mov r1, r5          # r5 = temp_i

loop:       mov r0, r7          # r7 = r0 (n)
            not r7              # twos comliment
            inc r7              # for negative n
            add r5, r7          # r5 = i + -n
            beq r7, end_loop    # if r5 == 0: goto end_loop

if:         ld (r3, r5, 4), r7  # r7 = a[i]
            ld (r4, r5, 4), r6  # r6 = b[i] 
            not r6              # twos compliment
            inc r6              # for negative b[i]
            inc r5              # temp_i++
            add r6, r7
            bgt r7, then        # if r7 > 0: goto then
            br loop             # else: goto loop start

then:       inc r2              # increment c'
            br loop             # goto loop start
            
end_loop:   ld $c, r0           # r0 = address of c
            st r2, (r0)         # r0 = r2 (store value of r2(c) to c in mem)
            st r5, 4(r0)       # 4(r0) = r5 (store value of r5(temp_i) to i in mem)
            halt

.pos 0x1000
c:               .long 0x00000000
i:               .long 0xffffffff         # i
n:               .long 0x00000005         # n
a:               .long 0x0000000a         # a[0]
                 .long 0x00000014         # a[1]
                 .long 0x0000001e         # a[2]
                 .long 0x00000028         # a[3]
                 .long 0x00000032         # a[4]
b:               .long 0x0000000b         # b[0]
                 .long 0x00000014         # b[1]
                 .long 0x0000001c         # b[2]
                 .long 0x0000002c         # b[3]
                 .long 0x00000030         # b[4]
