#include "int_element.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "refcount.h"

/* TODO: Implement all public int_element functions, including element interface
functions.

You may add your own private functions here too. */

struct int_class {
  void (*print)(struct element *);
  int (*compare)(struct element *, struct element *);
  struct int_element *(*int_element_new)(int value);
  int (*int_element_get_value)(struct int_element *);
  int (*is_int_element)(struct element *);
};

struct str_element {
  struct str_class *class;
  char *value;
};

struct int_element {
  struct int_class *class;
  int value;
};

struct int_class int_class_table;

void int_finalizer(void *p) {
  return;
}

/* Static constructor that creates new integer elements. */
struct int_element *int_element_new(int value) {
  struct int_element *this = rc_malloc(sizeof(*this), int_finalizer);
  this->class = &int_class_table;
  this->value = value;
  return this;
}

/* Print this element (without any trailing newline) */
void int_print(struct element *element) {
  struct int_element *real = (struct int_element *)element;
  printf("%d", real->value);
}
/* Compare two elements. int_element should always compare LESS than
 * str_element. The compare function should return: a number < 0 if the first
 * element is less than the second element, 0 if the elements compare equal, a
 * number > 0 otherwise.
 */
int int_compare(struct element *a, struct element *b) {
  if (is_int_element(a)) {
    if (!is_int_element(b)) return -1;
    int a_val = ((struct int_element *)a)->value;
    int b_val = ((struct int_element *)b)->value;
    return a_val - b_val;
  }
  if (is_int_element(b)) return 1;
  // both are strings
  char *a_val = ((struct str_element *)a)->value;
  char *b_val = ((struct str_element *)b)->value;
  return strcmp(a_val, b_val);
}
/* Static function that obtains the value held in an int_element. */
int int_element_get_value(struct int_element *int_element) {
  return int_element->value;
}
/* Static function that determines whether this is an int_element. */
int is_int_element(struct element *element) {
  return element->class == (struct element_class *)&int_class_table ? 1 : 0;
}

struct int_class int_class_table = {
    int_print,      int_compare, int_element_new, int_element_get_value,
    is_int_element,
};