#include <stdio.h>
#include <stdlib.h>

int main() {
  int* p;
  int a[10];
  p = a + 5;
  *p = 30;
}
