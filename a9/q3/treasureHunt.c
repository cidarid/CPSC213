#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/errno.h>

#include "disk.h"
#include "queue.h"
#include "uthread.h"

queue_t pending_read_queue;
unsigned int val = 0;
int still_running = 1;

void interrupt_service_routine() {
  void *val;
  void (*callback)(void *, void *);
  queue_dequeue(pending_read_queue, &val, NULL, &callback);
  callback(val, NULL);
}

void handleOtherReads(void *resultv, void *countv) {
  // TODO
}

void handleFirstRead(void *resultv, void *countv) {
  printf("yeah");
  val = *(int *)resultv;
  still_running = 0;
  printf("Your boolean variable is: %s\n", still_running ? "true" : "false");
  printf("%d", val);
  exit(0);
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

  // TODO
  while (still_running)
    ;  // infinite loop so that main doesn't return before hunt completes
  printf("%d", *starting_block);
  free(starting_block);
}
