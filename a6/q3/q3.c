#include <stdio.h>
#include <stdlib.h>

struct Student {
  int sid;
  int grade[4];
  int average;  // this is computed by your program
};

int n = 4;          // number of students
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

void printStudent(struct Student s) {
  printf("Student #%d, grades of %d, %d, %d, %d, average of %d.\n", s.sid,
         s.grade[0], s.grade[1], s.grade[2], s.grade[3], s.average);
}

void printAllStudents() {
  for (int i = 0; i < 4; i++) {
    printStudent(s[i]);
  }
}

int main() {
  s = malloc(sizeof(struct Student) * n);
  int* grades =
      (int[]){10, 20, 30, 40, 50, 60, 70, 80, 0, 0, 0, 0, 100, 100, 100, 100};
  // Populating grades of students
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < 4; j++) {
      s[i].grade[j] = grades[(i * 4) + j];
    }
  }
  printAllStudents();
  // list_avg
  for (int i = 0; i < n; i++) {
    int total = 0;
    int* temp = s + i;
    temp++;
    for (int j = 0; j < 4; j++) {
      total += *temp;
      temp++;
    }
    printf("Total: %d\n", total);
    s[i].average = total >> 2;
  }
  printAllStudents();
}