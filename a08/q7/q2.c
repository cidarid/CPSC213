int q2(int a, int b, int c) {

  if (a < 10 || a > 18)
    return 0;

  switch (a - 10) {
  case 0:
    c += b;
    break;
  case 1:
    c = 0;
    break;
  case 2:
    c = b - c;
    break;
  case 3:
    c = 0;
    break;
  case 4:
    if (b > c)
      c = 1;
    else
      c = 0;
    break;
  case 5:
    c = 0;
    break;
  case 6:
    if (c > b)
      c = 1;
    else
      c = 0;
    break;
  case 7:
    c = 0;
    break;
  case 8:
    if (c == b)
      c = 1;
    else
      c = 0;
    break;
  }

  return c;
}
