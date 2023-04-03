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
  queue_dequeue(pending_read_queue, &thread, NULL, NULL);
  uthread_unblock(*(uthread_t*)thread);
}

void* read_block(void* blocknov) {
  int blockno = *(int*)blocknov;
  disk_schedule_read(&vals[blockno], blockno);
  queue_enqueue(pending_read_queue, uthread_self(), NULL, NULL);
  uthread_block();
  sum += vals[blockno];
  return NULL;
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

  for (int i = 0; i < num_blocks; i++) {
    vals[i] = i;
    threads[i] = uthread_create(read_block, &vals[i]);
  }
  for (int i = 0; i < num_blocks; i++) {
    uthread_join(threads[i], NULL);
  }
  printf("%d\n", sum);
  free(threads);
  free(vals);
}
