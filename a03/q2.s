.pos 0x100
            ld $0, r0           # 0 => r0
            ld $tos, r1         # &tos => r1  
            ld $tmp, r2         # &tmp => r2
            ld $a, r3           # &a => r3
            ld $s, r4           # &s => r4

            st r0, (r1)         # 0 => tos
            st r0, (r2)         # 0 => tmp

            ld (r1), r1         # tos => r1

            ld (r3), r5         # a[0] => r5
            st r5, (r4, r1, 4)  # s[tos] = a[0]

            inc r1              # tos++

            ld 4(r3), r5        # a[1] => r5
            st r5, (r4, r1, 4)  # s[tos] = a[1]

            inc r1              # tos++

            ld 8(r3), r5        # a[2] => r5
            st r5, (r4, r1, 4)  # s[tos] = a[2]

            inc r1              # tos++
            dec r1              # tos--

            ld (r4, r1, 4), r5  # s[tos] => r5
            st r5, (r2)         # tmp = s[tos]

            dec r1              # tos--

            ld (r4, r1, 4), r5  # s[tos] => r5
            ld (r2), r3         # tmp => r3
            add r5, r3          # tmp + s[tos] => r5
            st r3, (r2)         # tmp = s[tos]

            dec r1              # tos--

            ld (r4, r1, 4), r5  # s[tos] => r5
            ld (r2), r3         # tmp => r3
            add r5, r3          # tmp + s[tos] => r5
            st r3, (r2)         # tmp = s[tos]

            halt

.pos 0x1000
tos:        .long 0x0 # tos
tmp:        .long 0x0 # tmp

.pos 0x2000
a:          .long 0xc # a[0] = 12
            .long 0x1f # a[1] = 31 
            .long 0x2 # a[2] = 2

.pos 0x3000
s:          .long 0x0 # s[0]
            .long 0x0 # s[1]
            .long 0x0 # s[2]
            .long 0x0 # s[3]
            .long 0x0 # s[4]
            .long 0x0 # s[5]
