#include <stdio.h>

struct Student {
  int sid;
  int grade[4];
  int average;  // this is computed by your program
};

int n;              // number of students
int m;              // you store the median student's id here
struct Student* s;  // a dynamic array of n students

void sort(int* a, int n) {
  for (int i = n - 1; i > 0; i--)
    for (int j = 1; j <= i; j++)
      if (a[j - 1] > a[j]) {
        int t = a[j];
        a[j] = a[j - 1];
        a[j - 1] = t;
      }
}

int main() {
  struct Student s;
  s.grade[0] = 0;
  s.grade[1] = 4;
  s.grade[2] = 0;
  s.grade[3] = 8;
  int total = 0;
  for (int i = 0; i < 4; i++) {
    int currentGrade = *(s.grade + i);
    total += currentGrade;
    printf("%d\n", currentGrade);
  }
  printf("average: %d\n", total >> 2);
}