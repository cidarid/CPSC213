.pos 0x1000
code:
ld $s, r0             # load array into r0
ld $8, r1             # load 8 into r1
add r0, r1            # array + 8 (grade 0) into r1
ld $0, r2             # r2 will store the counter pos
ld $-4, r3            # 4 is the offset
ld $0, r4             # initialize total to 0
loop:
mov r2, r5            # move counter pos to r5
add r3, r5            # r5 - 4 => r5
beq r5, end_loop      # if this equals 0 the loop has gone 4 times so branch
ld (r1, r2, 4), r6    # grade[i] => r6
add r6, r4            # total += grade[i]
inc r2                # i++
br loop
end_loop:
shr $2, r4             # total /= 4
ld $0x18, r5
add r0, r5
st r4, (r5)           



.pos 0x2000
n:      .long 1       # just one student
m:      .long 0       # put the answer here
s:      .long base    # address of the array
base:   .long 1234    # student ID
        .long 80      # grade 0
        .long 60      # grade 1
        .long 78      # grade 2
        .long 90      # grade 3
avg:    .long 0       # computed average     