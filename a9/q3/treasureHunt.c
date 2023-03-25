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
int still_running = 1;

void interrupt_service_routine() {
  void *val;
  void (*callback)(void *, void *);
  queue_dequeue(pending_read_queue, &val, NULL, &callback);
  callback(val, NULL);
}

void handleOtherReads(void *resultv, void *countv) {
  int second_block_val = *(int *)resultv;
  printf("Second block: %d\n", second_block_val);
  still_running = 0;
}

void handleFirstRead(void *resultv, void *countv) {
  first_block_val = *(int *)resultv;
  waiting_for_first_block = 0;
  printf("Waiting: %s\n", waiting_for_first_block ? "true" : "false");
  printf("First block: %d\n", first_block_val);
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
  int *second_block = malloc(sizeof(int));
  queue_enqueue(pending_read_queue, second_block, NULL, handleOtherReads);
  disk_schedule_read(second_block, first_block_val);
  // TODO
  while (still_running)
    ;  // infinite loop so that main doesn't return
       // before hunt completes
  printf("%d", *starting_block);
  free(starting_block);
}
