/**
 * @file transition.h
 * @author Mathijs van de Nes
 */

#ifndef __REO_TRANSITION_H__
#define __REO_TRANSITION_H__

#include "problem.h"

struct REOAutomaton;
struct REOState;

/**
 * Transition from one state to another
 */
struct REOTransition
{
    /// Automaton containing this transition
    struct REOAutomaton *automaton;
    /// Target state
    struct REOState *target;
    /// Indices of ports of the state that must be pending
    unsigned *port_indices;
    /// Number of ports that must be pending
    unsigned ports_len;
    /// Data constraint problem
    struct REOProblem problem;
};

/**
 * Constructs a transition
 *
 * @memberof REOTransition
 * @param transition Object pointer
 * @param automaton Parent automaton
 * @param target_state_index Index of the target state
 * @param ports_len Number of ports connected
 * @param port_indices List of port indices connected
 */
void REOTransition_construct(struct REOTransition *transition, struct REOAutomaton *automaton, unsigned target_state_index, unsigned ports_len, unsigned port_indices[]);

/**
 * Cleans the transition
 *
 * @memberof REOTransition
 * @param transition Object pointer
 */
void REOTransition_cleanup(struct REOTransition *transition);

/**
 * Tries to fire a transition
 *
 * A transition can fire if:
 * * All of its ports are pending
 * * The data constraint can be forfilled
 *
 * @memberof REOTransition
 * @param transition Object pointer
 * @return `target` when the transition has fired
 * @return `NULL` otherwise
 */
struct REOState* REOTransition_fire(struct REOTransition* transition);

#endif // __REO_TRANSITION_H__
