#include "automaton.h"

#include "state.h"
#include "transition.h"
#include "porthandler.h"

#include <stdlib.h>

typedef struct REOAutomaton REOAutomaton;

static void* REOAutomaton_loop(void* userdata);

void REOAutomaton_construct(REOAutomaton *self, unsigned ports_len, unsigned states, unsigned initial_state)
{
    REOMutex_init(&self->mutex);
    REOSemaphore_init(&self->semaphore, 0);
    REOContext_construct(&self->context, ports_len);

    self->states = calloc(states, sizeof(struct REOState));
    self->states_len = states;

    self->current_state = &self->states[initial_state];

    self->ports_len = ports_len;
    self->ports = calloc(ports_len, sizeof(struct REOPortHandler*));

    self->active = false;
}

void REOAutomaton_attach_ports(REOAutomaton *self, struct REOPortHandler* ports[]) {
    for (unsigned i = 0; i < self->ports_len; ++i) {
        self->ports[i] = ports[i];
    }
}

void REOAutomaton_cleanup(REOAutomaton *self)
{
    for (unsigned i = 0; i < self->states_len; ++i)
    {
        REOState_cleanup(&self->states[i]);
    }
    free(self->states);
    free(self->ports);

    REOContext_cleanup(&self->context);
    REOSemaphore_destroy(&self->semaphore);
    REOMutex_destroy(&self->mutex);
}

void REOAutomaton_start(REOAutomaton *self)
{
    self->active = true;
    REOThread_create(&self->thread, REOAutomaton_loop, (void*)self);
}

void REOAutomaton_stop(REOAutomaton *self)
{
    self->active = false;
    REOSemaphore_signal(&self->semaphore);
    REOThread_join(self->thread, NULL);
}

void REOAutomaton_kick(struct REOAutomaton *self)
{
    REOSemaphore_signal(&self->semaphore);
    REOState_unblock_ports(self->current_state);
}

static void* REOAutomaton_loop(void *userdata)
{
    REOAutomaton *self = (REOAutomaton*)userdata;

    while (self->active == true)
    {
        REOMutex_lock(&self->mutex);

        REOSemaphore_drain(&self->semaphore);
        struct REOState *target = NULL;
        for (unsigned idx = 0; idx < self->current_state->transitions_len; ++idx)
        {
            target = REOTransition_fire(&self->current_state->transitions[idx]);
            if (target != NULL) {
                /* Signal ourself such that the next wait will succeed */
                REOSemaphore_signal(&self->semaphore);
                break;
            }
        }

        REOMutex_unlock(&self->mutex);

        /* Check before and after waiting if we are still active */
        if (self->active == false) break;

        REOSemaphore_wait(&self->semaphore);
    }

    return NULL;
}
