#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/errno.h>

#include "disk.h"
#include "queue.h"
#include "uthread.h"

queue_t pending_read_queue;
unsigned int first_block_val = 0;
int waiting_for_first_block = 1;
int waiting_for_prev_block = 1;
volatile int pending_reads;

void interrupt_service_routine() {
  void *val;
  void (*callback)(void *, void *);
  queue_dequeue(pending_read_queue, &val, NULL, &callback);
  callback(val, NULL);
}

void handleOtherReads(void *resultv, void *countv) {
  int block_val = *(int *)resultv;
  pending_reads--;
  waiting_for_prev_block = 0;
}

void handleFirstRead(void *resultv, void *countv) {
  first_block_val = *(int *)resultv;
  waiting_for_first_block = 0;
}

int main(int argc, char **argv) {
  // Command Line Arguments
  static char *usage = "usage: treasureHunt starting_block_number";
  int starting_block_number;
  char *endptr;
  if (argc == 2) starting_block_number = strtol(argv[1], &endptr, 10);
  if (argc != 2 || *endptr != 0) {
    printf("argument error - %s \n", usage);
    return EXIT_FAILURE;
  }

  // Initialize
  uthread_init(1);
  disk_start(interrupt_service_routine);
  pending_read_queue = queue_create();

  // Start the Hunt

  int *starting_block = malloc(sizeof(int));
  queue_enqueue(pending_read_queue, starting_block, NULL, handleFirstRead);
  disk_schedule_read(starting_block, starting_block_number);

  while (waiting_for_first_block)
    ;
  int *blocks = malloc(sizeof(int) * first_block_val);
  queue_enqueue(pending_read_queue, &blocks[0], NULL, handleOtherReads);
  disk_schedule_read(&blocks[0], first_block_val);
  while (waiting_for_prev_block)
    ;
  pending_reads = first_block_val - 1;
  for (int i = 1; i < first_block_val; i++) {
    waiting_for_prev_block = 1;
    queue_enqueue(pending_read_queue, &blocks[i], NULL, handleOtherReads);
    disk_schedule_read(&blocks[i], blocks[i - 1]);
    while (waiting_for_prev_block)
      ;
  }

  while (pending_reads > 0)
    ;  // infinite loop so that main doesn't return
       // before hunt completes
  printf("%d", blocks[first_block_val - 1]);
  free(starting_block);
  free(blocks);
}
