#include "uthread.h"
#include "uthread_mutex_cond.h"
#include <stdio.h>
#include <stdlib.h>

#define NUM_THREADS 3
uthread_t threads[NUM_THREADS];
uthread_mutex_t mutex;
uthread_cond_t barrier_cond;
int count = 0;

void randomStall() {
  int i, r = random() >> 16;
  while (i++ < r)
    ;
}

void waitForAllOtherThreads() {
  uthread_mutex_lock(mutex);
  count++;
  if (count == NUM_THREADS) {
    count = 0;
    uthread_cond_broadcast(barrier_cond);
  } else {
    uthread_cond_wait(barrier_cond);
  }
  uthread_mutex_unlock(mutex);
}

void *p(void *v) {
  randomStall();
  printf("a\n");
  waitForAllOtherThreads();
  printf("b\n");
  return NULL;
}

int main(int arg, char **arv) {
  mutex = uthread_mutex_create();
  barrier_cond = uthread_cond_create(mutex);
  uthread_init(4);
  for (int i = 0; i < NUM_THREADS; i++)
    threads[i] = uthread_create(p, NULL);
  for (int i = 0; i < NUM_THREADS; i++)
    uthread_join(threads[i], NULL);
  printf("------\n");
  uthread_cond_destroy(barrier_cond);
  uthread_mutex_destroy(mutex);
}
