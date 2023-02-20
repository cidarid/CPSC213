.pos 0x100
  ld $first, r0        # loads first into r0
  ld $array, r1        # loads array into r1
  ld $3, r2            # loads 3 into r2
  ld (r1, r2, 4), r3   # loads array[3] into r3
  st r3, (r0)          # loads array[3] into first
  ld $5, r6            # loads 5 into r6
  ld (r1, r6, 4), r5   # loads array[5] into r5
  st r5, (r1, r2, 4)   # stores array[5] into array[3]
  ld (r0), r7          # loads value of first into r7
  st r7, (r1, r6, 4)   # stores value of first into array[5]
  halt

.pos 0x1000
first:           .long 0x0000000a         # s
.pos 0x2000
array:           .long 0x000000a0         # array[0]
                 .long 0x000000a1         # array[1]
                 .long 0x000000a2         # array[2]
                 .long 0x000000a3         # array[3]
                 .long 0x000000a4         # array[4]
                 .long 0x000000a5         # array[5]
                 .long 0x000000a6         # array[6]
                 .long 0x000000a7         # array[7]
                 .long 0x000000a8         # array[8]