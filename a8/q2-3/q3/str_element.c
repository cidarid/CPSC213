#include "str_element.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "refcount.h"

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

struct int_element {
  struct int_class *class;
  int value;
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
  if (is_str_element(a)) {
    if (is_str_element(b)) {
      char *a_val = ((struct str_element *)a)->value;
      char *b_val = ((struct str_element *)b)->value;
      return strcmp(a_val, b_val);
    }
    return 1;
  }
  if (is_str_element(b)) return -1;
  int a_val = ((struct int_element *)a)->value;
  int b_val = ((struct int_element *)b)->value;
  return a_val - b_val;
  // both are strings
}

void str_finalizer(void *p) {
  struct str_element *element = (struct str_element *)p;
  free(element->value);
}

struct str_element *str_element_new(char *value) {
  struct str_element *this = rc_malloc(sizeof(*this), str_finalizer);
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