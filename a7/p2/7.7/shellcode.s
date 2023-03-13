start:
  gpc $10, r0
  ld $8, r1
  sys $2
  halt


str:
  .long 0x2f62696e # /bin
  .long 0x2f736800 # /sh\n
