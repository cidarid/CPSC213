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
}