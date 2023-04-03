#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

char* str1 = "Welcome! Please enter a name: ";
char* str2 = "Good luck, ";
char* str3 = "The secret phrase is \"squeamish ossifrage\"\n";

void print(char* buffer, int size) {
  write(1, buffer, size);
}

void proof() {
  print(str3, 43);
}

int main() {
  char* name = malloc(sizeof(char) * 32);
  print(str1, 30);
  ssize_t name_size = read(0, name, 256);
  print(str2, 11);
  print(name, name_size);
}
