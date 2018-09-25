/**
 * @file automaton.h
 * @author Mathijs van de Nes
 */

#ifndef __REO_AUTOMATON_H__
#define __REO_AUTOMATON_H__

#include "context.h"
#include "thread.h"

#include <stdbool.h>

struct REOState;

/**
 * Constraint Automaton
 *
 * An automaton is a container of all things related to a synchronous region
 */
struct REOAutomaton
{
    /// Context for all ports connected
    struct REOContext context;
    /// List of states
    struct REOState *states;
    /// Number of states
    unsigned states_len;
    /// Current state
    struct REOState *current_state;
    /// Mutex for locking the context and registered port data
    REOMutex mutex;
    /// Flag indicating wether the automaton is running on its own
    bool active;
    /// Semaphore used to signal an active automaton
    REOSemaphore semaphore;
    /// Thread variable the automaton runs on
    REOThread thread;
    /// The number of ports connected to this automaton
    unsigned ports_len;
    /// The ports (fifos and external) connected to this automaton
    struct REOPortHandler** ports;
};

/**
 * Constructor of the automaton
 *
 * @memberof REOAutomaton
 * @param automaton     Object pointer
 * @param ports_len     Number of ports to construct
 * @param states        Number of states to reserve
 * @param initial_state Index of the initial state
 */
void REOAutomaton_construct(struct REOAutomaton *automaton, unsigned ports_len, unsigned states, unsigned initial_state);

/**
* Attaches the specified porthandlers to the automaton
*
* @memberof REOAutomaton
* @param automaton     Object pointer
* @param ports         The ports to attach. Must be an array of length automaton->ports_len
*/
void REOAutomaton_attach_ports(struct REOAutomaton *automaton, struct REOPortHandler* ports[]);

/**
 * Destructor of an automaton
 *
 * This ensures all the contained variables are cleaned up
 *
 * @memberof REOAutomaton
 * @param automaton     Object pointer
 */
void REOAutomaton_cleanup(struct REOAutomaton *automaton);

/**
 * Activates the provided automaton
 *
 * @memberof REOAutomaton
 * @param automaton Object pointer
 */
void REOAutomaton_start(struct REOAutomaton *automaton);

/**
 * Stops the provided automaton
 *
 * @memberof REOAutomaton
 * @param automaton Object pointer
 */
void REOAutomaton_stop(struct REOAutomaton *automaton);

/**
 * Awakes the automaton and connected ports
 *
 * @memberof REOAutomaton
 * @param automaton Object pointer
 */
void REOAutomaton_kick(struct REOAutomaton *automaton);

#endif // __REO_AUTOMATON_H__
