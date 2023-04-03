#include <stdio.h>
#include <stdlib.h>

struct S {
  int x[2];
  int *y;
  struct S *z;
};

int i;
int v0, v1, v2, v3;
struct S s;

int main() {
  s.y = malloc(2 * sizeof(int));
  s.z = malloc(sizeof(struct S));
  s.z->z = malloc(sizeof(struct S));
  s.z->z->y = malloc(2 * sizeof(int));
  i = 0;
  s.x[i] = 0x15;
  s.y[i] = 0x20;
  s.z->x[i] = 0x25;
  s.z->z->y[i] = 0x30;
  v0 = s.x[i];
  v1 = s.y[i];
  v2 = s.z->x[i];
  v3 = s.z->z->y[i];
  printf("v0:%x, v1:%x, v2:%x, v3:%x\n", v0, v1, v2, v3);
  return 0;
}