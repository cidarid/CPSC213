#include <stdio.h>
int i = -1;
int n = 5;
int a[] = (int[]){10, 20, 30, 40, 50};
int b[] = (int[]){11, 20, 28, 44, 48};
int c = 0;

int main(int argc, char *argv[]) {
  for (i = 0; i < n; i++)
    if (a[i] > b[i])
      c += 1;
  printf("c: %d\n i: %d", c, i);
}
