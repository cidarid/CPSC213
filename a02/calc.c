#include <stdio.h>

int y[8] = {0, 1, 2, 3, 289, 5, 6, 7};
int m = 4;
int r, c;

void calc() {
    c = y[m] + y[m + 1];
    r = c & 0xff;
    printf("m: %d\nr: %d\nc: %d\n", m, r, c);
}

int main() { calc(); }