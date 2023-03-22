.pos 0x0
    ld   $0x1028, r5            # stack pointer to the botom of the stack
    ld   $0xfffffff4, r0        # r0 = -12
    add  r0, r5                 # allocate 3 bytes to the stack
    ld   $0x200, r0             # r0 = &a[0]
    ld   0x0(r0), r0            # r0 = a[0]
    st   r0, 0x0(r5)            # save a[0] to the stack
    ld   $0x204, r0             # r0 = &a[1]
    ld   0x0(r0), r0            # r0 = a[1]
    st   r0, 0x4(r5)            # save a[1] to the stack
    ld   $0x208, r0             # r0 = &a[2]
    ld   0x0(r0), r0            # r0 = a[2]
    st   r0, 0x8(r5)            # save a[2] to the stack
    gpc  $6, r6                 # set return address
    j    0x300                  # goto 0x300
    ld   $0x20c, r1             # r1 = &a[3]
    st   r0, 0x0(r1)            # r1 = a[3]
    halt
.pos 0x200
    .long 0x00000000            # a[0]
    .long 0x00000000            # a[1]
    .long 0x00000000            # a[2]
    .long 0x00000000            # a[3]
.pos 0x300
    ld   0x0(r5), r0            # r0 = top of the stack (a)
    ld   0x4(r5), r1            # r1 = next value on the stack (b)
    ld   0x8(r5), r2            # r2 = next value on the stack (c)
    ld   $0xfffffff6, r3        # r3 = -10
    add  r3, r0                 # r0 = a - 10
    mov  r0, r3                 # r3 = a - 10
    not  r3                     # r3 = ~(a - 10) - twos complement
    inc  r3                     # r3 = ~(a - 10) + 1 (for -r3)
    bgt  r3, L6                 # if r3 > 0: goto L6
    mov  r0, r3                 # r3 = a - 10
    ld   $0xfffffff8, r4        # r4 = -8
    add  r4, r3                 # r3 = a - 18
    bgt  r3, L6                 # if r3 > 0: goto L6
    ld   $0x400, r3             # r3 = address of jump table
    ld   (r3, r0, 4), r3        # r3 = jump_table[a-10]
    j    (r3)                   # goto 0x400[a - 10]
.pos 0x330
    add  r1, r2                 # r2 = b + c
    br   L7                     # goto L7
    not  r2                     # ~(b+c)  - twos complement
    inc  r2                     # ~(b+c) + 1 (for -r2)
    add  r1, r2                 # r2 = b - c
    br   L7                     # goto L7
    not  r2                     # ~(b-c)
    inc  r2                     # ~(b-c) + 1
    add  r1, r2                 # r2 = r2 + b
    bgt  r2, L0                 # if r2 > 0 goto L0
    ld   $0x0, r2               # r2 = 0
    br   L1                     # goto L1
L0:
    ld   $0x1, r2               # r2 = 1
L1:
    br   L7                     # goto l7
    not  r1                     # ~(b))
    inc  r1                     # ~b + 1
    add  r2, r1                 # r1 = r2 + r1
    bgt  r1, L2                 # if r1 > 0: goto L2
    ld   $0x0, r2               # r2 = 0
    br   L3                     # goto L3
L2:
    ld   $0x1, r2               # r2 = 1
L3:
    br   L7                     # goto L7
    not  r2                     # ~c
    inc  r2                     # ~c + 1
    add  r1, r2                 # r2 = r1 + r2
    beq  r2, L4                 # if r2 == 0: goto L4
    ld   $0x0, r2               # r2 = 0
    br   L5                     # goto L5
L4:
    ld   $0x1, r2               # r2 = 1
L5:
    br   L7                     # goto L7
L6:
    ld   $0x0, r2               # r2 = 0
    br   L7                     # goto L7
L7:
    mov  r2, r0                 # r0 = r2 (a = c)
    j    0x0(r6)                # goto return address

.pos 0x400                      # jump table
    .long 0x00000330            # L0
    .long 0x00000384            # L2
    .long 0x00000334            # L1
    .long 0x00000384            # L2
    .long 0x0000033c            # L4
    .long 0x00000384            # L2
    .long 0x00000354            # L3
    .long 0x00000384            # L2
    .long 0x0000036c            # L5
.pos 0x1000                     # stack
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
    .long 0x00000000
