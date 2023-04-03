
#include <stdio.h>
int x[] = {1, 2, 3, -1, -2, 0, 184, 340057058};
int y[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

int k = 0;

void f(int a) {
  k = 0;
  int b = 0x80000000;
  while (a != 0) {
    if ((a & b) != 0) {
      k++;
    }
    a = a << 1;
  }
}

int main(int argc, char *argv[]) {
  for (int i = 7; i >= 0; i--) {
    f(x[i]);
    y[i] = k;
  }

  for (int i = 0; i <= 7; i++) {
    printf("%d\n", x[i]);
  }
  for (int i = 0; i <= 7; i++) {
    printf("%d\n", y[i]);
  }
  return 0;
}
