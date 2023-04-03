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

void swap(struct Student* s1) {
  struct Student* s0 = s1 - 1;
  int* s1_int = (int*)s1;
  int* s0_int = (int*)s0;
  for (int i = 0; i < 6; i++) {
    int temp = *s1_int;
    *s1_int = *s0_int;
    *s0_int = temp;
    s1_int++;
    s0_int++;
  }
}

int main() {
  s = malloc(sizeof(struct Student) * n);
  int* grades =
      (int[]){10, 20, 30, 40, 50, 60, 70, 80, 0, 0, 0, 0, 100, 100, 100, 100};
  int* ids = (int[]){1234, 3456, 4269, 8008};
  // Populating grades of students
  for (int i = 0; i < n; i++) {
    s[i].sid = ids[i];
    for (int j = 0; j < 4; j++) {
      s[i].grade[j] = grades[(i * 4) + j];
    }
  }
  printAllStudents();
  // list_avg
  for (int i = 0; i < n; i++) {
    int total = 0;
    int* temp = (int*)(s + i);
    temp++;
    for (int j = 0; j < 4; j++) {
      total += *temp;
      temp++;
    }
    printf("Total: %d\n", total);
    s[i].average = total >> 2;
  }
  printAllStudents();
  swap(s + 1);
  printf("\n\n");
  printAllStudents();
  // Loop thru all sublists
  for (int i = n - 1; i > 0; i--) {
    // Loop through specific sublist
    for (int j = 1; j <= i; j++) {
      int* prev = s + j - 1;
      int prevAvg = *(prev + 5);
      int* curr = s + j;
      int currAvg = *(curr + 5);
      if (prevAvg > currAvg) {
        swap(curr);
      }
    }
  }
  printf("\n\n");
  printAllStudents();
}