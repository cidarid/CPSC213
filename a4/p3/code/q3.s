.pos 0x1000
code:
ld $s, r0             # &s => r0
ld $i, r1             # &i => r1
ld (r1), r1           # i => r1
ld (r0, r1, 4), r2    # s.x[i] => r2
ld $v0, r3            # &v0 => r3
st r2, (r3)           # s.x[i] => v0
ld 8(r0), r2          # s.y => r2
ld (r2, r1, 4), r3    # s.y[i] => r3
ld $v1, r4            # &v1 => r4
st r3, (r4)           # s.y[i] => v1
ld 12(r0), r2         # s.z => r2
ld (r2, r1, 4), r3    # s.z->x[i] => r3
ld $v2, r4            # &v2 => r4
st r3, (r4)           # s.z->x[i] => v2
ld 12(r2), r2         # s.z->z => r2
ld 8(r2), r2          # s.z->z->y => r2
ld (r2, r1, 4), r3    # s.z->z->y[i] => r3
ld $v3, r4            # &v3 => r4
st r3, (r4)           # s.z->z->y[i] => v3
halt

.pos 0x2000
static:
i:      .long 1       # int
v0:     .long 0       # int
v1:     .long 0       # int
v2:     .long 0       # int
v3:     .long 0       # int
# struct S
s:      .long 0       # int x[0]
        .long 15      # int x[1]
        .long s_y     # int *y
        .long s_z     # struct S *z

.pos 0x3000
heap:
s_y:    .long 0       # s.y[0]
        .long 20      # s.y[1]
s_z:    .long 0       # s.z->x[0]
        .long 25      # s.z->x[1]
        .long 0       # s.z->y
        .long s_z_z   # s.z->z
s_z_z:  .long 0       # s.z->z->x[0]
        .long 0       # s.z->z->x[1]
        .long s_z_z_y # s.z->z->y
        .long 0       # s.z->z->z
s_z_z_y:.long 0       # s.z->z->y[0]
        .long 30       # s.z.z.->[1]