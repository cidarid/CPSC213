#include <assert.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

#include "uthread.h"
#include "uthread_mutex_cond.h"

#define NUM_ITERATIONS 10000

#ifdef VERBOSE
#define VERBOSE_PRINT(S, ...) printf(S, ##__VA_ARGS__)
#else
#define VERBOSE_PRINT(S, ...) ((void)0)  // do nothing
#endif

struct Agent {
  uthread_mutex_t mutex;
  uthread_cond_t match;
  uthread_cond_t paper;
  uthread_cond_t tobacco;
  uthread_cond_t smoke;
};

struct Agent* createAgent() {
  struct Agent* agent = malloc(sizeof(struct Agent));
  agent->mutex = uthread_mutex_create();
  agent->paper = uthread_cond_create(agent->mutex);
  agent->match = uthread_cond_create(agent->mutex);
  agent->tobacco = uthread_cond_create(agent->mutex);
  agent->smoke = uthread_cond_create(agent->mutex);
  return agent;
}

/**
 * You might find these declarations helpful.
 *   Note that Resource enum had values 1, 2 and 4 so you can combine resources;
 *   e.g., having a MATCH and PAPER is the value MATCH | PAPER == 1 | 2 == 3
 */
enum Resource { MATCH = 1, PAPER = 2, TOBACCO = 4 };
char* resource_name[] = {"", "match", "paper", "", "tobacco"};

// # of threads waiting for a signal. Used to ensure that the agent
// only signals once all other threads are ready.
int num_active_threads = 0;

int signal_count[5];  // # of times resource signalled
int smoke_count[5];   // # of times smoker with resource smoked

int materials;
uthread_cond_t paper_matches, paper_tobacco, tobacco_matches;

void wake_smoker(int available_materials) {
  VERBOSE_PRINT("Attempting to wake up smoker with materials of %d\n",
                materials);
  switch (available_materials) {
    case PAPER + MATCH:
      VERBOSE_PRINT("Waking up tobacco smoker\n");
      uthread_cond_signal(paper_matches);
      materials = 0;
      break;
    case PAPER + TOBACCO:
      VERBOSE_PRINT("Waking up matches smoker\n");
      uthread_cond_signal(paper_tobacco);
      materials = 0;
      break;
    case TOBACCO + MATCH:
      VERBOSE_PRINT("Waking up paper smoker\n");
      uthread_cond_signal(tobacco_matches);
      materials = 0;
      break;
    default:
      VERBOSE_PRINT("Invalid materials\n");
      break;
  }
}

/*
Handlers
These handle updating the materials when a material becomes available
*/

void* tobacco_handler(void* v) {
  struct Agent* a = v;
  uthread_mutex_lock(a->mutex);
  while (1) {
    uthread_cond_wait(a->tobacco);
    materials += TOBACCO;
    wake_smoker(materials);
  }
  uthread_mutex_unlock(a->mutex);
}

void* matches_handler(void* v) {
  struct Agent* a = v;
  uthread_mutex_lock(a->mutex);
  while (1) {
    uthread_cond_wait(a->match);
    materials += MATCH;
    wake_smoker(materials);
  }
  uthread_mutex_unlock(a->mutex);
}

void* paper_handler(void* v) {
  struct Agent* a = v;
  uthread_mutex_lock(a->mutex);
  while (1) {
    uthread_cond_wait(a->paper);
    materials += PAPER;
    wake_smoker(materials);
  }
  uthread_mutex_unlock(a->mutex);
}

/*
Smokers
These actually trigger the smoking of the smokers, and are only triggered when 2
materials are available.
*/

void* tobacco_smoker(void* v) {
  struct Agent* a = v;
  uthread_mutex_lock(a->mutex);
  while (1) {
    uthread_cond_wait(paper_matches);
    VERBOSE_PRINT("Tobacco smoker is smoking\n");
    uthread_cond_signal(a->smoke);
    smoke_count[TOBACCO]++;
  }
  uthread_mutex_unlock(a->mutex);
}

void* match_smoker(void* v) {
  struct Agent* a = v;
  uthread_mutex_lock(a->mutex);
  while (1) {
    uthread_cond_wait(paper_tobacco);
    VERBOSE_PRINT("Matches smoker is smoking\n");
    uthread_cond_signal(a->smoke);
    smoke_count[MATCH]++;
  }
  uthread_mutex_unlock(a->mutex);
}

void* paper_smoker(void* v) {
  struct Agent* a = v;
  uthread_mutex_lock(a->mutex);
  while (1) {
    uthread_cond_wait(tobacco_matches);
    VERBOSE_PRINT("Paper smoker is smoking\n");
    uthread_cond_signal(a->smoke);
    smoke_count[PAPER]++;
  }
  uthread_mutex_unlock(a->mutex);
}

/**
 * This is the agent procedure.  It is complete and you shouldn't change it in
 * any material way.  You can modify it if you like, but be sure that all it
 * does is choose 2 random resources, signal their condition variables, and then
 * wait wait for a smoker to smoke.
 */
void* agent(void* av) {
  struct Agent* a = av;
  static const int choices[] = {MATCH | PAPER, MATCH | TOBACCO,
                                PAPER | TOBACCO};
  static const int matching_smoker[] = {TOBACCO, PAPER, MATCH};

  srandom(time(NULL));

  uthread_mutex_lock(a->mutex);
  // Wait until all other threads are waiting for a signal
  while (num_active_threads < 3) uthread_cond_wait(a->smoke);

  // Do resource looping NUM_ITERATIONS times
  for (int i = 0; i < NUM_ITERATIONS; i++) {
    int r = random() % 6;
    switch (r) {
      // Match, paper
      case 0:
        signal_count[TOBACCO]++;
        VERBOSE_PRINT("match available\n");
        uthread_cond_signal(a->match);
        VERBOSE_PRINT("paper available\n");
        uthread_cond_signal(a->paper);
        break;
      // Match, tobacco
      case 1:
        signal_count[PAPER]++;
        VERBOSE_PRINT("match available\n");
        uthread_cond_signal(a->match);
        VERBOSE_PRINT("tobacco available\n");
        uthread_cond_signal(a->tobacco);
        break;
      // Paper, tobacco
      case 2:
        signal_count[MATCH]++;
        VERBOSE_PRINT("paper available\n");
        uthread_cond_signal(a->paper);
        VERBOSE_PRINT("tobacco available\n");
        uthread_cond_signal(a->tobacco);
        break;
      // Paper, match
      case 3:
        signal_count[TOBACCO]++;
        VERBOSE_PRINT("paper available\n");
        uthread_cond_signal(a->paper);
        VERBOSE_PRINT("match available\n");
        uthread_cond_signal(a->match);
        break;
      // Tobacco, match
      case 4:
        signal_count[PAPER]++;
        VERBOSE_PRINT("tobacco available\n");
        uthread_cond_signal(a->tobacco);
        VERBOSE_PRINT("match available\n");
        uthread_cond_signal(a->match);
        break;
      // Tobacco, paper
      case 5:
        signal_count[MATCH]++;
        VERBOSE_PRINT("tobacco available\n");
        uthread_cond_signal(a->tobacco);
        VERBOSE_PRINT("paper available\n");
        uthread_cond_signal(a->paper);
        break;
    }
    VERBOSE_PRINT("agent is waiting for smoker to smoke\n");
    uthread_cond_wait(a->smoke);
  }

  uthread_mutex_unlock(a->mutex);
  return NULL;
}

int main(int argc, char** argv) {
  struct Agent* a = createAgent();
  uthread_t agent_thread;

  uthread_init(5);

  paper_matches = uthread_cond_create(a->mutex);
  paper_tobacco = uthread_cond_create(a->mutex);
  tobacco_matches = uthread_cond_create(a->mutex);

  uthread_create(tobacco_handler, a);
  uthread_create(matches_handler, a);
  uthread_create(paper_handler, a);
  uthread_create(tobacco_smoker, a);
  uthread_create(match_smoker, a);
  uthread_create(paper_smoker, a);

  agent_thread = uthread_create(agent, a);
  num_active_threads += 3;
  uthread_join(agent_thread, NULL);

  VERBOSE_PRINT("Match: %d signals, %d smokes\n", signal_count[MATCH],
                smoke_count[MATCH]);
  VERBOSE_PRINT("Paper: %d signals, %d smokes\n", signal_count[PAPER],
                smoke_count[PAPER]);
  VERBOSE_PRINT("Tobacco: %d signals, %d smokes\n", signal_count[TOBACCO],
                smoke_count[TOBACCO]);
  assert(signal_count[MATCH] == smoke_count[MATCH]);
  assert(signal_count[PAPER] == smoke_count[PAPER]);
  assert(signal_count[TOBACCO] == smoke_count[TOBACCO]);
  assert(smoke_count[MATCH] + smoke_count[PAPER] + smoke_count[TOBACCO] ==
         NUM_ITERATIONS);

  printf("Smoke counts: %d matches, %d paper, %d tobacco\n", smoke_count[MATCH],
         smoke_count[PAPER], smoke_count[TOBACCO]);

  return 0;
}
