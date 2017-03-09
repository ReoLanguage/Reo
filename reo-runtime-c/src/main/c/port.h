/**
 * @file port.h
 * @author Mathijs van de Nes
 */

#ifndef __REO_PORT_H__
#define __REO_PORT_H__

#include "container.h"
#include "porthandler.h"
#include "thread.h"

#include <stdbool.h>

struct REOAutomaton;

/**
 * Enum indicating the status of a performed action
 */
typedef enum REOPortStatus_enum
{
    /// Action was successful
    RPS_Success,
    /// Action is pending
    RPS_Pending,
    /// Action has failed
    RPS_Failed,
} REOPortStatus;

/**
 * Port for performing I/O on an automaton
 */
struct REOPort
{
    /// Flag to specify wether this port can be used for put operations
    bool can_put;
    /// Flag to specify wether this port can be used for get operations
    bool can_get;
    /// Timeout for port actions in milliseconds. 0 means indefinitely
    long timeout_ms;
    /// Semaphore to signal the port thread for new action
    REOSemaphore semaphore;
    /// Reference to the parent automaton
    struct REOAutomaton *automaton;
    /// Index of this port in the automaton
    unsigned index;
    /// Status of the port, used when performing an operation
    REOPortStatus status;
    /// Container for data to be communicated
    struct REOContainer *data;
    /// REOPortHandler for this port
    struct REOPortHandler handler;
};

/**
 * Construct a port
 *
 * @memberof REOPort
 * @param port Object pointer
 * @param automaton Reference to the parent automaton
 * @param index Index of the port at the parent automaton
 */
void REOPort_construct(struct REOPort *port, struct REOAutomaton *automaton, unsigned index);

/**
 * Cleans all the data contained in the port
 *
 * @memberof REOPort
 * @param port Object pointer
 */
void REOPort_cleanup(struct REOPort *port);

/**
 * Perform a get action
 *
 * This is a blocking call
 *
 * @memberof REOPort
 * @param port Object pointer
 * @return Result of the performed action
 */
REOPortStatus REOPort_get(struct REOPort *port);

/**
 * Perform a put action
 *
 * This is a blocking call. The data to be put must be set beforehand
 *
 * @param port Object pointer
 * @return Result of the performed action
 */
REOPortStatus REOPort_put(struct REOPort *port);

/**
 * Wake the port
 *
 * This may be used to signal that there could be a transition available for firing
 *
 * @memberof REOPort
 * @param port Object pointer
 */
void REOPort_unblock(struct REOPort *port);

/**
 * Remove the port from the context
 *
 * @memberof REOPort
 * @param port Object pointer
 */
void REOPort_remove_from_context(struct REOPort *port);

/**
 * Return the data contained in this port
 *
 * @memberof REOPort
 * @param port Object pointer
 * @return The contained data
 */
struct REOContainer* REOPort_get_data(struct REOPort *port);

/**
 * Set the data contained in this port
 *
 * @memberof REOPort
 * @param port Object pointer
 * @param data The data to set
 */
void REOPort_set_data(struct REOPort *port, struct REOContainer *data);

/**
 * Sets the timeout of the port in milliseconds. 0 means none.
 *
 * @memberof REOPort
 * @param port Object pointer
 * @param timeout_ms Timeout length
 */
void REOPort_set_timeout(struct REOPort *port, unsigned long timeout_ms);

void REOPort_cancel(struct REOPort *port);

#endif // __REO_PORT_H__
