#include "str_element.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* TODO: Implement all public str_element functions, including element interface
functions.

You may add your own private functions here too. */

struct str_class {
  void (*print)(struct element *);
  int (*compare)(struct element *, struct element *);
  struct str_element *(*str_element_new)(char *value);
  char *(*str_element_get_value)(struct str_element *);
  int (*is_str_element)(struct element *);
};

struct str_element {
  struct str_class *class;
  char *value;
};

struct str_class str_class_table;

/* Print this element (without any trailing newline) */
void str_print(struct element *element) {
  struct str_element *real = (struct str_element *)element;
  printf("%s", real->value);
}
/* Compare two elements. int_element should always compare LESS than
 * str_element. The compare function should return: a number < 0 if the first
 * element is less than the second element, 0 if the elements compare equal, a
 * number > 0 otherwise.
 */
int str_compare(struct element *a, struct element *b) {
  return 0;
  /*if (is_int_element(a)) {
    if (!is_int_element(b)) return -1;
    int a_val = ((struct int_element *)a)->value;
    int b_val = ((struct int_element *)b)->value;
    return a_val - b_val;
  }
  if (is_int_element(b)) return 1;
  // both are strings*/
}

struct str_element *str_element_new(char *value) {
  struct str_element *this = malloc(sizeof(*this));
  this->class = &str_class_table;
  char *copied_value = malloc(sizeof(value));
  this->value = strcpy(copied_value, value);
  return this;
}

char *str_element_get_value(struct str_element *str_element) {
  return str_element->value;
}

int is_str_element(struct element *element) {
  return element->class == (struct element_class *)&str_class_table ? 1 : 0;
}

struct str_class str_class_table = {
    str_print,      str_compare, str_element_new, str_element_get_value,
    is_str_element,
};