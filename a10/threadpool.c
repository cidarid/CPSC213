#include "threadpool.h"

#include <stdio.h>
#include <stdlib.h>

#include "uthread.h"
#include "uthread_mutex_cond.h"

struct tpool {
  int num_threads;
  uthread_t *threads;
};

/**
 * Base procedure of every worker thread.  Calls available tasks
 * or blocks until a task becomes available.
 */
void *worker_thread(void *pool_v) {
  tpool_t pool = pool_v;

  // TODO
  return NULL;  // TODO: replace this placehold return value
}

/**
 * Create a new thread pool with max_threads thread-count limit.
 */
tpool_t tpool_create(unsigned int max_threads) {
  uthread_t *threads = malloc(sizeof(uthread_t) * max_threads);
  tpool_t pool = malloc(sizeof(tpool_t));
  (*pool).num_threads = max_threads;
  (*pool).threads = threads;
  return pool;
}

/**
 * Sechedule task f(arg) to be executed.
 */
void tpool_schedule_task(tpool_t pool, void (*f)(tpool_t, void *), void *arg) {
  // TODO
}

/**
 * Wait (by blocking) until all tasks have completed and thread pool is thus
 * idle
 */
void tpool_join(tpool_t pool) {
  // TODO
}

int main() {
  tpool_create(30);
}