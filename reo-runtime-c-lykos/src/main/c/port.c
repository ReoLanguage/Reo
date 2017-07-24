#include "port.h"

#include "automaton.h"
#include "state.h"
#include "transition.h"

#include <stdbool.h>
#include <stdlib.h>

typedef struct REOPort REOPort;

static REOPortStatus REOPort_action(REOPort *port);
static void REOPort_register(REOPort *port);
static void REOPort_unregister(REOPort *port);
static bool REOPort_call(REOPort *port);
static void* REOPort_handler_peek(REOPort *port);
static void REOPort_handler_resolve(REOPort *port);

void REOPort_construct(REOPort *self, struct REOAutomaton *automaton, unsigned index)
{
    REOSemaphore_init(&self->semaphore, 0);
    self->automaton = automaton;
    self->index = index;
    self->status = RPS_Success;
    self->can_get = false;
    self->can_put = false;
    self->timeout_ms = 0;
    self->data = NULL;

    self->handler.object = self;
    self->handler.peek = (struct REOContainer*(*)(void *))REOPort_handler_peek;
    self->handler.set = (void(*)(void*, struct REOContainer *))REOPort_set_data;
    self->handler.resolve = (void(*)(void*))REOPort_handler_resolve;
    self->handler.wake = (void(*)(void*))REOPort_unblock;
}

void REOPort_cleanup(REOPort *self)
{
    REOContainer_release(self->data);
    REOSemaphore_destroy(&self->semaphore);
}

REOPortStatus REOPort_get(REOPort *self)
{
    if (self->can_get == false) return RPS_Failed;

    REOContainer_release(self->data);
    self->data = NULL;

    return REOPort_action(self);
}

REOPortStatus REOPort_put(REOPort *self)
{
    if (self->can_put == false) return RPS_Failed;
    return REOPort_action(self);
}

void REOPort_unblock(REOPort *self)
{
    REOSemaphore_signal(&self->semaphore);
}

void REOPort_remove_from_context(REOPort *self)
{
    REOContext_remove(&self->automaton->context, self->index);
}

struct REOContainer* REOPort_get_data(struct REOPort *self)
{
    REOContainer_retain(self->data);
    return self->data;
}

void REOPort_set_data(struct REOPort *self, struct REOContainer *data)
{
    REOContainer_release(self->data);
    self->data = data;
}

void REOPort_set_timeout(struct REOPort *self, unsigned long timeout_ms)
{
    self->timeout_ms = timeout_ms;
}

void REOPort_cancel(struct REOPort *self)
{
    REOPort_unregister(self);
    REOSemaphore_signal(&self->semaphore);
}

REOPortStatus REOPort_action(REOPort *self)
{
    self->status = RPS_Pending;
    REOPort_register(self);

    while (self->status == RPS_Pending && REOPort_call(self) == false)
    {
        if (self->timeout_ms == 0) {
            REOSemaphore_wait(&self->semaphore);
        }
        else {
            if (!REOSemaphore_timedwait(&self->semaphore, self->timeout_ms)) {
                REOPort_unregister(self);
                break;
            }
        }
    }

    return self->status;
}

void REOPort_register(REOPort *self)
{
    REOContext_add(&self->automaton->context, self->index);
}

void REOPort_unregister(REOPort *self)
{
    REOMutex_lock(&self->automaton->mutex);
    
    if (self->status == RPS_Success) {
        return;
    }

    REOContext_remove(&self->automaton->context, self->index);
    self->status = RPS_Failed;

    REOMutex_unlock(&self->automaton->mutex);
}

bool REOPort_call(REOPort *self)
{
    REOMutex_lock(&self->automaton->mutex);

    if (REOContext_contains(&self->automaton->context, self->index) == false) {
        REOMutex_unlock(&self->automaton->mutex);
        return true;
    }

    REOSemaphore_drain(&self->semaphore);

    struct REOState *current = self->automaton->current_state;
    unsigned my_t_len = current->transitions_for_x_len[self->index];
    for (unsigned i = 0; i < my_t_len; ++i)
    {
        unsigned idx = (current->last_transition_of_port[self->index] + i + 1) % my_t_len;
        struct REOState *target = REOTransition_fire(current->transitions_for[self->index][idx]);
        if (target != NULL) {
            current->last_transition_of_port[self->index] = idx;
            REOMutex_unlock(&self->automaton->mutex);
            return true;
        }
    }

    REOMutex_unlock(&self->automaton->mutex);
    return false;
}

static void* REOPort_handler_peek(REOPort *self)
{
    if (self->data) {
        REOContainer_retain(self->data);
    }
    return self->data;
}

static void REOPort_handler_resolve(REOPort *self)
{
    self->status = RPS_Success;
    REOPort_remove_from_context(self);
    REOPort_unblock(self);
}

