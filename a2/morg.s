.pos 0x100
  ld $s, r0            # r0 = address of s
  ld $array, r1        # r1 = address of array
  ld $5, r2            # r2 = 5
  ld (r1, r2, 4), r3   # r3 = array[5]
  st r3, (r0)          # Stores value of array[5] into s

  ld $1, r2            # r2 = 1
  ld (r1, r2, 4), r5   # r5 = array[1]
  mov r5, r3          # r3 = value of r5, array[5] = array[1]
  st r0, (r5)          # r5 = value of r0, array[1] = s
  halt

.pos 0x1000
s:               .long 0x0000000a         # s
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