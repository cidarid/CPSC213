.pos 0x1000
code:
ld $s, r0             # &s into r0
ld (r0), r0           # s into r0

# Initializing external for loop values (i = 0; i < n; i++)
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

# Initializing internal for loop values (j = 0; j < 4; j++)
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
j bubble

swap:
ld $-24, r6
add r4, r6
ld $-6, r5
swap_loop:
beq r5, end_swap_loop
ld (r4), r7
ld (r6), r0
st r0, (r4)
st r7, (r6)
inca r4
inca r6
inc r5
br swap_loop
end_swap_loop:
j return_pos
end_sublists:
j end

bubble:
# Initializing for loop (i = n - 1; i > 0; i--)
ld $s, r0
ld (r0), r0
ld $n, r1
ld (r1), r1
dec r1                # i = n - 1
all_sublists:
beq r1, end_sublists  # if i has decremented to 0
ld $1, r2             # j => r2
mov r1, r3
not r3                # -i + 1 => r3
ld $0, r4             # counter for internal loop
sublist:
mov r3, r5            # -i => r5
add r2, r5            # j - i => r5
beq r5, end_sublist   # if (j - i == 0) (i.e. j == i)
mov r2, r5            # j => r5
mov r2, r6            # j => r6
shl $3, r5            # j *= 8
shl $4, r6            # j *= 16
add r6, r5            # 8*j + 16*j => r5 (equivalent to j * 24)
ld $s, r6             # s => r6
ld (r6), r6
add r5, r6            # s + j => r6
mov r6, r7            # s + j => r7
mov r7, r4            # using r4 to store a copy of s + j for later
ld $-24, r5
add r5, r6            # s + j - 1 => r6
ld $20, r5
add r5, r6            # (s[j - 1] + 20) => r6
ld (r6), r6
add r5, r7            # (s[j] + 20) => r7
ld (r7), r7
not r7
inc r7                # -(s[j].avg)
add r6, r7            # s[j - 1].avg - s[j].avg => r7
bgt r7, swap
return_pos:
inc r2                # j++
j sublist
end_sublist:
dec r1
j all_sublists
end: 
ld $n, r0
ld (r0), r0
shr $1, r0
mov r0, r1
shl $3, r0
shl $4, r1
add r0, r1
ld $s, r0
ld (r0), r0
add r0, r1
ld (r1), r1
ld $m, r0
st r1, (r0)
halt
halt


.pos 0x2000
n:      .long 3       # Count of students
m:      .long 0       # put the answer here
s:      .long base    # address of the array
base:   .long 1234    # student 0 ID
        .long 800     # grade 0
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
        .long 10      # grade 0
        .long 10      # grade 1
        .long 10      # grade 2
        .long 10      # grade 3
        .long 0       # 2 avg
        .long 8008    # student 3 ID
        .long 100     # grade 0
        .long 100     # grade 1
        .long 100     # grade 2
        .long 100     # grade 3
        .long 0       # 3 avg
avg:    .long 0       # computed average     