.pos 0x100
start:
    ld   $sb, r5         # load the stack pointer
    inca r5              # point the stack pointer to the bottom of the stack
    gpc  $6, r6          # r6 = current address of the code
    j    main            # main()
    halt

f:
    deca r5              # sp -= 4 (allocate 4 bytes on the stack)
    ld   $0, r0          # r0 = 0 (k = 0)
    ld   4(r5), r1       # r1 = sp + 4 (load the function parameters into r1 (a))
    ld   $0x80000000, r2 # 0x80000000 = r2 (b)
f_loop:
    beq  r1, f_end       # if a == 0: goto f_end
    mov  r1, r3          # r3 = a 
    and  r2, r3          # r3 = b & r3
    beq  r3, f_if1       # if b & a == 0: goto f_if1
    inc  r0              # k++
f_if1:
    shl  $1, r1          # a  = a << 1
    br   f_loop          # goto f_loop
f_end:
    inca r5              # sp += 4 (deallocate 4 bytes on the stack)
    j    (r6)            # goto return address

main:
    deca r5              # sp -= 4 (allocate 4 bytes on the stack for the return address)
    deca r5              # sp -= 4 (allocate 4 bytes on the stack)
    st   r6, 4(r5)       # save the return address to the bottom of the stack
    ld   $8, r4          # r4 = 8 => i
main_loop:
    beq  r4, main_end    # if r4 == 0: goto main_end
    dec  r4              # r4-- (i--)
    ld   $x, r0          # r0 = &x
    ld   (r0, r4, 4), r0 # r0 = r0[r4] (r0 = x[i])
    deca r5              # sp -= 4 (allocate 4 bytes on the stack )
    st   r0, (r5)        # write x[i] to the top of the stack as a parameter
    gpc  $6, r6          # r6 = current address of the code for returning
    j    f               # f()
    inca r5              # sp += 4 (deallocate 4 bytes on the stack)
    ld   $y, r1          # r1 = &y
    st   r0, (r1, r4, 4) # y[i] = r0 (k)
    br   main_loop       # goto main_loop
main_end:
    ld   4(r5), r6       # load top of the stack into r6
    inca r5              # sp += 4 (deallocate 4 bytes on the stack)
    inca r5              # sp += 4 (deallocate 4 bytes on the stack)
    j    (r6)            # goto the return address (r6)

.pos 0x2000
x:
    .long 1
    .long 2
    .long 3
    .long -1
    .long -2
    .long 0
    .long 184
    .long 340057058

y:
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0
    .long 0

.pos 0x8000
# These are here so you can see (some of) the stack contents.
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
