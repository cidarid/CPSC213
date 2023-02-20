#include <stdio.h>

int i, f;

void myMath() {
    f = 4012;
    i = ((((f + 1) + 4) << 5) & f) / 8;
    i = 256;
    i = i >> 8;
    printf("%d", i);
}

int main() { myMath(); }