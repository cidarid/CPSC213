#include <stdio.h>
#include <stdlib.h>

int first;
int array[9] = {1, 2, 3, 4, 5, 6, 7, 8, 9};

void swap() {
    first = array[3];
    array[3] = array[5];
    array[5] = first;
}

int *c;

int main() {
    c = malloc(10 * sizeof(int));
    printf("c orig: %p\n", c);
    c = &c[3];
    printf("c after &: %p\n", c);
    *c = *&c[3];
    printf("c after *: %p\n", c);
    c = &c[3];
    printf("c[3] after *: %p\n", c);
    swap();
    return 0;
}