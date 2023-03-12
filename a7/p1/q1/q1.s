.pos 0x0
    ld   $sb, r5         # Address of sb into r5
    inca r5              # Increment address at r5 by 4 bytes
    gpc  $6, r6          # Gets the address of the halt command two lines from now
    j    0x300           #
    halt                 #

.pos 0x100
    .long 0x00001000     #

.pos 0x200
    ld   (r5), r0        # 3 => r0
    ld   4(r5), r1       # 4 => r1
    ld   $0x100, r2      # 
    ld   (r2), r2        # Get value at 0x100 (0x1000), store into r2
    ld   (r2, r1, 4), r3 # Get value of address 0x1000 + 16 (0x1010), store into r3 
    add  r3, r0          # r0 += 0
    st   r0, (r2, r1, 4) # r0 => 0x1000 + 16 (1010)
    j    (r6)            # jump to 0x33a (ld $0x8, r0 instruction 18 lines after this)

.pos 0x300
    ld   $-12, r0        # -12 => r0
    add  r0, r5          # Decrement address at r5 by 12 bytes (now 8 bytes behind &sb)
    st   r6, 8(r5)       # Store jump point in sb
    ld   $1, r0          # Load 1 into r0
    st   r0, (r5)        # Store 1 into the memory address 8 bytes behind &sb
    ld   $2, r0          # 
    st   r0, 4(r5)       # Store 2 into the memory address 4 bytes behind &sb
    ld   $-8, r0         #
    add  r0, r5          # Address at r5 is now 16 bytes behind &sb
    ld   $3, r0          # 
    st   r0, (r5)        # Store 3 16 bytes behind &sb
    ld   $4, r0          #
    st   r0, 4(r5)       # Store 4 12 bytes behind sb
    gpc  $6, r6          # Get address of instruction 2 lines from now, store into r6
    j    0x200           #
    ld   $8, r0          # 
    add  r0, r5          # r5 += 8
    ld   (r5), r1        # value of r5 into r1
    ld   4(r5), r2       # value of (r5+4) into r1
    ld   $-8, r0         # 
    add  r0, r5          # r5 -= 8
    st   r1, (r5)        # store r1 into r5
    st   r2, 4(r5)       # store r2 into *(r5 + 4)
    gpc  $6, r6          # get address of ld $8, r0 instruction
    j    0x200           #
    ld   $8, r0          #
    add  r0, r5          # r5 += 8 (should be address of sb)
    ld   8(r5), r6       # 8(r5) into r6 (out of program scope)
    ld   $12, r0         #
    add  r0, r5          # r5 += 12, 12 bytes ahead of sb
    j    (r6)            #

.pos 0x1000
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0

.pos 0x8000
    # These are here so you can see (some of) the stack contents
    # while running the code on the simulator.
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
sb: .long 0
