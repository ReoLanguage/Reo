#ifndef __REO_STATE_H__
#define __REO_STATE_H__

struct REOTransition;
struct REOPortHandler;

/**
 * A state which an automaton can be in
 */
struct REOState
{
    /// List of porthandlers connected to this state
    struct REOPortHandler **ports;
    /// Amount of ports connected
    unsigned ports_len;
    /// List of indexes specifying the last transition a port has fired
    unsigned *last_transition_of_port;
    /// List of transitions
    struct REOTransition* transitions;
    /// Number of transitions
    unsigned transitions_len;
    /// transitions_for[i][j] gives the j'th transition for port i
    struct REOTransition*** transitions_for;
    /// Specifies the length of transitions_for[i]
    unsigned* transitions_for_x_len;
};

/**
 * Constructs a state
 *
 * @memberof REOState
 * @param state Object pointer
 * @param num_transitions Number of transitions to allocate space for
 * @param num_ports Number of ports connected to this transition
 * @param ports List of porthandlers connected
 */
void REOState_construct(struct REOState *state, unsigned num_transitions, unsigned num_ports, struct REOPortHandler* ports[]);

/**
 * Cleans the data of a state
 *
 * @memberof REOState
 * @param state Object pointer
 */
void REOState_cleanup(struct REOState *state);

/**
 * Constructs the transitions_for and transitions_for_x_len variables
 *
 * @memberof REOState
 * @param state Object pointer
 */
void REOState_bind_transitions_to_ports(struct REOState *state);

/**
 * Unblocks all ports connected to this state
 *
 * @param state Object pointer
 */
void REOState_unblock_ports(struct REOState *state);

#endif // __REO_STATE_H__
