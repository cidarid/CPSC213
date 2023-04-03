#include <assert.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/errno.h>

#include "disk.h"
#include "queue.h"
#include "uthread.h"

queue_t pending_read_queue;
volatile int result;
unsigned int sum = 0;
int* vals;
uthread_t* threads;

void interrupt_service_routine() {
  void* thread;
  void (*callback)(void*, void*);
  queue_dequeue(pending_read_queue, &thread, NULL, &callback);
  callback(thread, NULL);
}

void* read_block(void* blocknov) {
  uthread_t waiting_thread = uthread_self();
  int blockno = *(int*)blocknov;
  disk_schedule_read(&vals[blockno], blockno);
  uthread_block();
  sum += vals[blockno];
  return NULL;
}

void unblock(void* _thread, void* none) {
  uthread_t* thread = _thread;
  uthread_unblock(*thread);
}

int main(int argc, char** argv) {
  // Command Line Arguments
  static char* usage = "usage: tRead num_blocks";
  int num_blocks;
  char* endptr;
  if (argc == 2) num_blocks = strtol(argv[1], &endptr, 10);
  if (argc != 2 || *endptr != 0) {
    printf("argument error - %s \n", usage);
    return EXIT_FAILURE;
  }

  // Initialize
  uthread_init(1);
  disk_start(interrupt_service_routine);
  pending_read_queue = queue_create();
  threads = malloc(sizeof(uthread_t) * num_blocks);
  vals = malloc(sizeof(int) * num_blocks);

  int val = 0;
  int val_2 = val + 1;
  for (int i = 0; i < num_blocks; i++) {
    vals[i] = i;
    threads[i] = uthread_create(read_block, &vals[i]);
    queue_enqueue(pending_read_queue, &threads[i], NULL, unblock);
  }
  for (int i = 0; i < num_blocks; i++) {
    uthread_join(threads[i], NULL);
  }
  printf("%d\n", sum);
  free(threads);
  free(vals);
}
