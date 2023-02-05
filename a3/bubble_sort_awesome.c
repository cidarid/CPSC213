/*
TODO: Extend bubble_sort_dynamic.c. This program must not include square
brackets anywhere (including comments). All access to arrays must be made using
pointer arithmetic and the dereference operator *.
*/
#include <stdio.h>
#include <stdlib.h>

// Array of values to sort
int* val;

void sort(int n) {
  // Temp val,
  int t;
  // Loop from back to front of array using i as position
  for (int i = n - 1; i > 0; i--)
    // Loop through all numbers before *(val + i), if any numbers encountered
    // are greater than *(val + i), swap *(val + i) and *(val + j)
    for (int j = i - 1; j >= 0; j--)
      if (*(val + i) < *(val + j)) {
        t = *(val + i);
        *(val + i) = *(val + j);
        *(val + j) = t;
      }
}

int main(int argc, char** argv) {
  char* ep;
  // Length of array
  int n;
  n = argc - 1;
  val = malloc(n * sizeof(*val));
  for (int i = 0; i < n; i++) {
    *(val + i) = strtol(*(argv + i + 1), &ep, 10);
    if (*ep) {
      fprintf(stderr, "Argument %d is not a number\n", i);
      return -1;
    }
  }
  sort(n);
  // Prints sorted array
  for (int i = 0; i < n; i++) printf("%d\n", *(val + i));
}