/*
TODO: Change bubble_sort_static.c to use dynamic arrays. Specifically, turn val
into a dynamic array and allow the program to sort arbitrary lists of integers
provided on the command line (e.g. more than 4). Recall that to allocate
dynamic arrays, you use the procedure malloc. For example, to allocate an array
of ten shorts, you say: short *s = malloc (10 * sizeof(*s));
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
    // Loop through all numbers before val[i], if any numbers encountered are
    // greater than val[i], swap val[i] and val[j]
    for (int j = i - 1; j >= 0; j--)
      if (val[i] < val[j]) {
        t = val[i];
        val[i] = val[j];
        val[j] = t;
      }
}

int main(int argc, char** argv) {
  char* ep;
  // Length of array
  int n;
  n = argc - 1;
  val = malloc(n * sizeof(*val));
  for (int i = 0; i < n; i++) {
    val[i] = strtol(argv[i + 1], &ep, 10);
    if (*ep) {
      fprintf(stderr, "Argument %d is not a number\n", i);
      return -1;
    }
  }
  sort(n);
  // Prints sorted array
  for (int i = 0; i < n; i++) printf("%d\n", val[i]);
}