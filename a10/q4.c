#include "uthread.h"
#include "uthread_mutex_cond.h"
#include <stdio.h>
#include <stdlib.h>

uthread_t t0, t1, t2;
uthread_mutex_t mutex;
uthread_cond_t cond0, cond1;
int done0 = 0, done1 = 0;

void randomStall() {
  int i, r = random() >> 16;
  while (i++ < r)
    ;
}

void *p0(void *v) {
  randomStall();
  uthread_mutex_lock(mutex);
  printf("zero\n");
  done0 = 1;
  uthread_cond_signal(cond0);
  uthread_mutex_unlock(mutex);
  return NULL;
}

void *p1(void *v) {
  randomStall();
  uthread_mutex_lock(mutex);
  while (!done0)
    uthread_cond_wait(cond0);
  printf("one\n");
  done1 = 1;
  uthread_cond_signal(cond1);
  uthread_mutex_unlock(mutex);
  return NULL;
}

void *p2(void *v) {
  randomStall();
  uthread_mutex_lock(mutex);
  while (!done1)
    uthread_cond_wait(cond1);
  printf("two\n");
  uthread_mutex_unlock(mutex);
  return NULL;
}

int main(int arg, char **arv) {
  uthread_init(4);
  mutex = uthread_mutex_create();
  cond0 = uthread_cond_create(mutex);
  cond1 = uthread_cond_create(mutex);
  t0 = uthread_create(p0, NULL);
  t1 = uthread_create(p1, NULL);
  t2 = uthread_create(p2, NULL);
  randomStall();
  uthread_join(t0, NULL);
  uthread_join(t1, NULL);
  uthread_join(t2, NULL);
  printf("three\n");
  printf("------\n");
  uthread_mutex_destroy(mutex);
  uthread_cond_destroy(cond0);
  uthread_cond_destroy(cond1);
}
