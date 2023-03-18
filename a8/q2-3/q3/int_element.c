#include "int_element.h"

#include <stdio.h>
#include <stdlib.h>

#include "refcount.h"

/* TODO: Implement all public int_element functions, including element interface
functions.

You may add your own private functions here too. */

struct int_class {
  void (*print)(struct element *);
  int (*compare)(struct element *, struct element *);
  struct int_element (*int_element_new)(int value);
  int (*int_element_get_value)(struct int_element *);
  int (*is_int_element)(struct element *);
};

/* Print this element (without any trailing newline) */
void (*print)(struct element *);
/* Compare two elements. int_element should always compare LESS than
 * str_element. The compare function should return: a number < 0 if the first
 * element is less than the second element, 0 if the elements compare equal, a
 * number > 0 otherwise.
 */
int (*compare)(struct element *, struct element *);

struct int_element {
  struct int_class *class;
  int value;
};

/* Static constructor that creates new integer elements. */
struct int_element *int_element_new(int value);
/* Static function that obtains the value held in an int_element. */
int int_element_get_value(struct int_element *);
/* Static function that determines whether this is an int_element. */
int is_int_element(struct element *);