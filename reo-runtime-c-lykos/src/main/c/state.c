#include "state.h"

#include "porthandler.h"
#include "transition.h"

#include <stdlib.h>

typedef struct REOState REOState;

void REOState_construct(struct REOState *self, unsigned num_transitions, unsigned num_ports, struct REOPortHandler* ports[])
{
    self->transitions = calloc(num_transitions, sizeof(struct REOTransition));
    self->transitions_len = num_transitions;

    self->ports = calloc(num_ports, sizeof(struct REOPortHandler*));
    self->last_transition_of_port = calloc(num_ports, sizeof(unsigned));
    self->transitions_for = calloc(num_ports, sizeof(struct REOTransition**));
    self->transitions_for_x_len = calloc(num_ports, sizeof(unsigned));
    self->ports_len = num_ports;

    for (unsigned i = 0; i < num_ports; ++i)
    {
        self->ports[i] = ports[i];
        self->transitions_for[i] = NULL;
        self->last_transition_of_port[i] = 0;
    }
}

void REOState_cleanup(REOState *self)
{
    for (unsigned i = 0; i < self->ports_len; ++i)
    {
        free(self->transitions_for[i]);
    }
    for (unsigned i = 0; i < self->transitions_len; ++i)
    {
        REOTransition_cleanup(&self->transitions[i]);
    }
    free(self->transitions);
    free(self->transitions_for);
    free(self->transitions_for_x_len);
    free(self->last_transition_of_port);
    free(self->ports);
}

void REOState_unblock_ports(REOState *self)
{
    for (unsigned i = 0; i < self->ports_len; ++i)
    {   
        self->ports[i]->wake(self->ports[i]->object);
    }
}

void REOState_bind_transitions_to_ports(struct REOState *self)
{
    // Reset all counts
    for (unsigned i = 0; i < self->ports_len; ++i)
    {
        self->transitions_for_x_len[i] = 0;
    }

    // Gather the number of transitions for each port
    for (unsigned i = 0; i < self->transitions_len; ++i)
    {
        struct REOTransition *transition = &self->transitions[i];
        for (unsigned j = 0; j < transition->ports_len; ++j)
        {
            self->transitions_for_x_len[transition->port_indices[j]] += 1;
        }
    }

    // Initialize the transitions_for array to the correct amount of NULLs
    for (unsigned i = 0; i < self->ports_len; ++i)
    {
        self->transitions_for[i] = calloc(self->transitions_for_x_len[i], sizeof(struct REOTransition*));
        
        for (unsigned j = 0; j < self->transitions_for_x_len[i]; ++j)
        {
            self->transitions_for[i][j] = NULL;
        }
    }

    // Create the references
    for (unsigned i = 0; i < self->transitions_len; ++i)
    {
        struct REOTransition *transition = &self->transitions[i];
        for (unsigned j = 0; j < transition->ports_len; ++j)
        {
            struct REOTransition **pointer = self->transitions_for[transition->port_indices[j]];
            for (;; pointer++)
            {
                if (*pointer == NULL) {
                    *pointer = transition;
                    break;
                }
            }
        }
    }
}
