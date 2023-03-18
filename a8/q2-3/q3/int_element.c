#include "int_element.h"

#include <stdio.h>
#include <stdlib.h>

/* TODO: Implement all public int_element functions, including element interface
functions.

You may add your own private functions here too. */

struct int_element_class {
  struct element_class *super;
  void (*print)(void *);
  int (*compare)(void *, void *);
  int (*int_element_get_value)(struct int_element *);
  int (*is_int_element)(struct element *);
};

/* Forward reference to a int_element. You get to define the structure. */
struct int_element {
  struct int_element_class *class;
  int value;
};

void print(void *element) {
  printf("%d", *(int *)(element + 1));
}

int compare(void *a, void *b) {
  return 1;
}

/* Static constructor that creates new integer elements. */
struct int_element *int_element_new(int value) {
  struct int_element_class class = {NULL, &print, &compare,
                                    &int_element_get_value, &is_int_element};
  struct int_element *new_int = malloc(sizeof(struct int_element));
  new_int->class = &class;
  new_int->value = value;
  return new_int;
}

/* Static function that obtains the value held in an int_element. */
int int_element_get_value(struct int_element *element) {
  return element->value;
}

/* Static function that determines whether this is an int_element. */
int is_int_element(struct element *element) {
  return 1;
}