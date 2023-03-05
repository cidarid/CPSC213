.pos 0x1000
code:
ld $s, r0             # &s into r0
ld (r0), r0           # s into r0
ld $0, r1             # i
ld $n, r2     
ld (r2), r2        
not r2
inc r2                # -n (offset)
ld $0, r5             # counter for both loops
list_avg:
mov r1, r5            # i => r5
add r2, r5            # i - n => r5
beq r5, end_list_avg  # if (i - n) == 0 (i.e. i >= n), branch
ld $0, r3             # j
ld $-4, r4            # j offset
ld $0, r6             # total for student
inca r0               # s[i].sid changes to s[i].grade[0]
inc r1                # i++
s_avg:
mov r3, r5            # move counter pos to r5
add r4, r5            # r5 - 4 => r5
beq r5, end_s_avg     # if (j - 4) == 0 (i.e. j >= 4), branch
ld (r0), r7           # grade[j] => r6
inca r0               # increase array pos by 1
add r7, r6            # r6 (total) += grade[j]
inc r3                # j++
br s_avg
end_s_avg:
shr $2, r6             # total /= 4
st r6, (r0)
inca r0
br list_avg          
end_list_avg:
j end
swap:
ld $base, r0
ld $24, r1
add r0, r1
ld $-6, r4
swap_loop:
beq r4, end_swap_loop
ld (r0), r2
ld (r1), r3
st r3, (r0)
st r2, (r1)
inca r0
inca r1
inc r4
br swap_loop
end_swap_loop:
j r6
end:
halt


.pos 0x2000
n:      .long 4       # Count of students
m:      .long 0       # put the answer here
s:      .long base    # address of the array
base:   .long 1234    # student 0 ID
        .long 10      # grade 0
        .long 20      # grade 1
        .long 30      # grade 2
        .long 40      # grade 3
        .long 0       # 0 avg
        .long 3456    # student 1 ID
        .long 50      # grade 0
        .long 60      # grade 1
        .long 70      # grade 2
        .long 80      # grade 3
        .long 0       # 1 avg
        .long 4269    # student 2 ID
        .long 00      # grade 0
        .long 00      # grade 1
        .long 00      # grade 2
        .long 00      # grade 3
        .long 0       # 2 avg
        .long 8008    # student 3 ID
        .long 100     # grade 0
        .long 100     # grade 1
        .long 100     # grade 2
        .long 100     # grade 3
        .long 0       # 3 avg
avg:    .long 0       # computed average     