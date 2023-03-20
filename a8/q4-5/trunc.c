#include "list.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void print_string(element_t ev) {
  char *e = (char *)ev;
  printf("%s\n", e);
}
void print_string_no_breaks(element_t ev) {
  char *e = (char *)ev;
  printf("%s ", e);
}
void string_to_int(element_t *rv, element_t av) {
  char *a = (char *)av;
  intptr_t *r = (intptr_t *)rv;
  char *pEnd;
  *r = strtol(a, &pEnd, 10);
  if (pEnd == a) {
    *r = -1;
  }
}

void set_null(element_t *rv, element_t av, element_t bv) {
  char **r = (char **)rv;
  char *a = (char *)av;
  intptr_t b = (intptr_t)bv;

  if (b < 0)
    *r = a;
  else
    *r = NULL;
}

int pos(element_t av) {
  intptr_t a = (intptr_t)av;
  return a >= 0;
}

int not_null(element_t av) {
  char *a = (char *)av;
  return a != NULL;
}

void truncate(element_t *rv, element_t av, element_t bv) {
  char **r = (char **)rv;
  char *a = (char *)av;
  intptr_t b = (intptr_t)bv;

  if (strlen(a) > b)
    a[(int)b] = 0;

  *r = a;
}

void max(element_t *rv, element_t av, element_t bv) {

  intptr_t *r = (intptr_t *)rv;
  intptr_t a = (intptr_t)av;
  intptr_t b = (intptr_t)bv;

  if (a >= b)
    *r = a;
  else
    *r = b;
}
int main(int argc, char *argv[]) {
  struct list *arg_list = list_create();
  // i = 1 to ignore first '.' argument
  for (int i = 1; i < argc; i++) {
    list_append(arg_list, argv[i]);
  }

  struct list *int_list = list_create();
  list_map1(string_to_int, int_list, arg_list);

  struct list *null_list = list_create();
  list_map2(set_null, null_list, arg_list, int_list);

  struct list *pos_list = list_create();
  list_filter(pos, pos_list, int_list);

  struct list *not_null_list = list_create();
  list_filter(not_null, not_null_list, null_list);

  struct list *trunc_list = list_create();
  list_map2(truncate, trunc_list, not_null_list, pos_list);

  list_foreach(print_string, trunc_list);
  list_foreach(print_string_no_breaks, trunc_list);

  element_t max_value = 0;
  list_foldl(max, &max_value, int_list);

  printf("\n%d\n", max_value);

  list_destroy(arg_list);
  list_destroy(int_list);
  list_destroy(null_list);
  list_destroy(pos_list);
  list_destroy(not_null_list);
  list_destroy(trunc_list);
}
