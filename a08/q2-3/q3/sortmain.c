#include <stdio.h>
#include <stdlib.h>

#include "element.h"
#include "int_element.h"
#include "refcount.h"
#include "str_element.h"

/* If the string is numeric, return an int_element. Otherwise return a
 * str_element. */
struct element *parse_string(char *str) {
  char *endp;
  /* strtol returns a pointer to the first non-numeric character in endp.
     If it gets to the end of the string, that character will be the null
     terminator. */
  int value = strtol(str, &endp, 10);
  if (str[0] != '\0' && endp[0] == '\0') {
    /* String was non-empty and strtol conversion succeeded - integer */
    return (struct element *)int_element_new(value);
  } else {
    return (struct element *)str_element_new(str);
  }
}

int compare(const void *_a, const void *_b) {
  struct element **a = _a;
  struct element **b = _b;
  return (*a)->class->compare(*a, *b);
}

void finalizer(void *item) {
  struct element **elements = item;
  for (int i = 0; i < sizeof(elements) / sizeof(struct element *); i++) {
    rc_free_ref(elements[i]);
  }
}

int main(int argc, char **argv) {
  struct element **elements =
      rc_malloc(sizeof(struct element *) * (argc - 1), 0);
  for (int i = 0; i < argc - 1; i++) {
    elements[i] = parse_string(argv[i + 1]);
  }
  qsort(elements, argc - 1, sizeof(struct element *), compare);
  /* TODO: Sort elements with qsort */
  printf("Sorted: ");
  for (int i = 0; i < argc - 1; i++) {
    elements[i]->class->print(elements[i]);
    rc_free_ref(elements[i]);
    printf(" ");
  }
  printf("\n");
  rc_free_ref(elements);
}
