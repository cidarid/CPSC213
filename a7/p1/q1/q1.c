#include <stdio.h>
#include <stdlib.h>

// This is what's at 0x1000, and &arr is what's at 0x100
int* arr;

void helper() {
  arr[4] = 3;
  arr[2] = 1;
}

int main() {
  arr = malloc(sizeof(int) * 10);
  helper();
  for (int i = 0; i < 10; i++) printf("%d\n", arr[i]);
  /*int r3 = arr[4];
  r3 += 3;
  arr[4] = r3;
  r3 = arr[2];
  r3 += 1;
  arr[2] = r3;*/
}

/*
int sb;
int* sb_p;
int* _0x100 = 0x1000;


int main() {
  sb_p = &sb;
  sb_p++;
  three();
}

void two() {
  int r0 = *sb_p;
  int r1 = *(sb_p + 1);
  int r3 = *(_0x100 + r1);
  r0 += r3;
  r0 = *(_0x100 + r1);
}

void three() {
  sb_p -= 3;
  *sb_p = 1;
  *(sb_p + 1) = 2;
  sb_p -= 2;
  *sb_p = 3;
  *(sb_p + 1) = 4;
  two();
  sb_p += 2;
  sb_p =
}*/
