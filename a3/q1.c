#include <stdio.h>
#include <stdlib.h>

// YOU: Allocate these global variables, using these names
int s, b;
int* y;
int q[10] = {16, 32, 5, 4, 21, 1, 0, 0, 0, 0};

int main(int argv, char** argc) {
    // Ignore this block of code
    /*if (argv != 11) {
        fprintf(stderr, "usage: q[0] ... q[9]\n");
        exit(EXIT_FAILURE);
    }
    for (int j = 0; j < 10; j++) q[j] = atol(argc[1 + j]);*/

    // YOU: Implement this code
    s = q[5];
    s = q[s];
    y = &b;
    *y = 6;
    y = &q[q[2]];
    *y = *y + q[3];

    // Ignore this block of code
    printf("s=%d b=%d y=&q[%d] q={", s, b, y - q);
    for (int j = 0; j < 10; j++) printf("%d%s", q[j], j < 9 ? ", " : "}\n");
}
