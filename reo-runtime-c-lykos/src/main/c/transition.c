#include "transition.h"

#include "automaton.h"
#include "state.h"
#include "porthandler.h"

#include <stdbool.h>
#include <stdlib.h>

static bool REOTransition_check_ports(struct REOTransition *transition);
static bool REOTransition_solve_problem(struct REOTransition *transition);

void REOTransition_construct(struct REOTransition *self, struct REOAutomaton *automaton, unsigned target_state_index, unsigned ports_len, unsigned port_indices[])
{
    self->automaton = automaton;
    self->target = &automaton->states[target_state_index];
    
    self->port_indices = calloc(ports_len, sizeof(unsigned));
    self->ports_len = ports_len;

    for (unsigned i = 0; i < ports_len; ++i)
    {
        self->port_indices[i] = port_indices[i];
    }
}

struct REOState* REOTransition_fire(struct REOTransition* self)
{
    if (REOTransition_check_ports(self) == false || REOTransition_solve_problem(self) == false) {
        return NULL;
    }

    REOProblem_set_from_variables(&self->problem);
    for (unsigned i = 0; i < self->ports_len; ++i) {
        struct REOPortHandler *handler_to_resolve = self->automaton->ports[self->port_indices[i]];
        handler_to_resolve->resolve(handler_to_resolve->object);
    }
    self->automaton->current_state = self->target;
    REOAutomaton_kick(self->automaton);

    return self->target;
}

void REOTransition_cleanup(struct REOTransition *self)
{
    free(self->port_indices);
    REOProblem_cleanup(&self->problem);
}

bool REOTransition_check_ports(struct REOTransition *self)
{
    for (unsigned i = 0; i < self->ports_len; ++i)
    {
        if (REOContext_contains(&self->automaton->context, self->port_indices[i]) == false) {
            return false;
        }
    }
    return true;
}

bool REOTransition_solve_problem(struct REOTransition *self)
{
    REOProblem_set_initial_assignment(&self->problem);
    return REOProblem_solve(&self->problem);
}
