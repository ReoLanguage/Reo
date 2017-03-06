#include "fifo.h"

#include "automaton.h"

#include <stdbool.h>
#include <stdlib.h>

/* Utility functions */
static bool REOFifo_is_empty(struct REOFifo *self);
static bool REOFifo_is_full(struct REOFifo *self);
static void REOFifo_update_contexts(struct REOFifo *self);

/* Handler functions */
static struct REOContainer* REOFifo_peek(struct REOFifo *self);
static struct REOContainer* REOFifo_peek_null(void*);
static void REOFifo_pop(struct REOFifo *self);
static void REOFifo_noop(void*);
static void REOFifo_unblock_input(struct REOFifo *self);
static void REOFifo_unblock_output(struct REOFifo *self);

/*
 * This class uses a ringbuffer.
 * The head points to the first available data
 * The tail points to the first available empty space
 * We allocate one space more than needed. This way we can discern
 * the empty and the full case.
*/

void REOFifo_construct(struct REOFifo *self, unsigned capacity)
{
    REOMutex_init(&self->mutex);
    self->buffer = calloc(capacity + 1, sizeof(struct REOContainer*));
    self->capacity = capacity + 1;
    self->head = 0;
    self->tail = 0;
    
    self->input_handler.object = self;
    self->input_handler.set = (void(*)(void*, struct REOContainer*))REOFifo_push;
    self->input_handler.peek = REOFifo_peek_null;
    self->input_handler.resolve = (void(*)(void*))REOFifo_update_contexts;
    self->input_handler.wake = REOFifo_noop;

    self->output_handler.object = self;
    self->output_handler.set = NULL;
    self->output_handler.peek = (struct REOContainer* (*)(void*))REOFifo_peek;
    self->output_handler.resolve = (void(*)(void *))REOFifo_pop;
    self->output_handler.wake = REOFifo_noop;
}

void REOFifo_cleanup(struct REOFifo *self)
{
    REOMutex_destroy(&self->mutex);

    for (unsigned i = self->head; i != self->tail; i = (i + 1) % self->capacity)
    {
        REOContainer_release(self->buffer[i]);
    }

    free(self->buffer);
}

void REOFifo_push(struct REOFifo *self, struct REOContainer* value)
{
    REOMutex_lock(&self->mutex);

    if (REOFifo_is_full(self)) {
        REOMutex_unlock(&self->mutex);
        REOContainer_release(value);
        return;
    }

    self->buffer[self->tail] = value;
    self->tail = (self->tail + 1) % self->capacity;

    REOMutex_unlock(&self->mutex);

    REOFifo_update_contexts(self);

}

void REOFifo_bind(struct REOFifo *self, struct REOAutomaton *input_automaton, unsigned input_index, struct REOAutomaton *output_automaton, unsigned output_index)
{
    self->input_automaton = input_automaton;
    self->output_automaton = output_automaton;
    self->input_index = input_index;
    self->output_index = output_index;
}

void REOFifo_finish(struct REOFifo *self)
{
    REOFifo_update_contexts(self);
}

static void REOFifo_pop(struct REOFifo *self)
{
    REOMutex_lock(&self->mutex);

    if (REOFifo_is_empty(self)) {
        REOMutex_unlock(&self->mutex);
    }

    REOContainer_release(self->buffer[self->head]);
    self->head = (self->head + 1) % self->capacity;

    REOMutex_unlock(&self->mutex);

    REOFifo_update_contexts(self);
}

static bool REOFifo_is_empty(struct REOFifo *self)
{
    return self->tail == self->head;
}

static bool REOFifo_is_full(struct REOFifo *self)
{
    return ((self->tail + 1) % self->capacity == self->head);
}

static struct REOContainer* REOFifo_peek(struct REOFifo *self)
{
    REOMutex_lock(&self->mutex);

    if (REOFifo_is_empty(self)) {
        REOMutex_unlock(&self->mutex);
        return REOContainer_wrap(malloc(1), 1);
    }

    struct REOContainer* result = self->buffer[self->head];
    REOContainer_retain(result);

    REOMutex_unlock(&self->mutex);

    return result;
}

static struct REOContainer* REOFifo_peek_null(void* unused)
{
    (void)unused;
    return NULL;
}

static void REOFifo_noop(void* unused)
{
    (void)unused;
}

static void REOFifo_unblock_input(struct REOFifo *self)
{
    REOAutomaton_kick(self->input_automaton);
}

static void REOFifo_unblock_output(struct REOFifo *self)
{
    REOAutomaton_kick(self->output_automaton);
}

static void REOFifo_update_contexts(struct REOFifo *self)
{
    REOMutex_lock(&self->mutex);

    if (REOFifo_is_full(self)) {
        REOContext_remove(&self->input_automaton->context, self->input_index);
    }
    else {
        REOContext_add(&self->input_automaton->context, self->input_index);
        REOFifo_unblock_input(self);
    }

    if (REOFifo_is_empty(self)) {
        REOContext_remove(&self->output_automaton->context, self->output_index);
    }
    else {
        REOContext_add(&self->output_automaton->context, self->output_index);
        REOFifo_unblock_output(self);
    }

    REOMutex_unlock(&self->mutex);
}
