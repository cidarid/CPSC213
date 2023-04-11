#include "uthread.h"
#include "uthread_mutex_cond.h"
#include <stdio.h>
#include <stdlib.h>

const int N = 100000;
int cnt = 0;

uthread_t t0, t1, t2;
uthread_mutex_t mutex;
uthread_cond_t count_up;
uthread_cond_t count_down;

void countUp() {
  for (int i = 0; i < N; i++) {
    uthread_mutex_lock(mutex);
    cnt += 1;
    uthread_mutex_unlock(mutex);
  }
}

void countDown() {
  for (int i = 0; i < N; i++) {
    uthread_mutex_lock(mutex);
    cnt -= 1;
    uthread_mutex_unlock(mutex);
  }
}

void *count(void *v) {
  countUp();
  countDown();
  return NULL;
}

int main(int arg, char **arv) {
  uthread_init(4);
  mutex = uthread_mutex_create();
  t0 = uthread_create(count, NULL);
  t1 = uthread_create(count, NULL);
  t2 = uthread_create(count, NULL);
  uthread_join(t0, NULL);
  uthread_join(t1, NULL);
  uthread_join(t2, NULL);
  uthread_mutex_destroy(mutex);
  printf("cnt = %d %s\n", cnt, cnt == 0 ? "" : "XXX");
}
