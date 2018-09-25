/**
 * @file porthandler.h
 * @author Mathijs van de Nes
 */

#ifndef __REO_PORTHANDLER_H__
#define __REO_PORTHANDLER_H__

#include "container.h"

#include <stdbool.h>

/**
 * Handler for ports
 *
 * This struct represents a handler for any port type.
 * The object may be a simple Port or FIFO
 */
struct REOPortHandler
{
    /// Object pointer to be passed as the first argument
    void* object;
    /// Get a reference to the data-value of the port
    struct REOContainer*(*peek)(void*);
    /// Perform a put action on the port
    void(*set)(void*, struct REOContainer*);
    /// Resolve the transaction on the port
    void(*resolve)(void*);
    /// Wake the port
    void(*wake)(void*);
};

#endif // __REO_PORTHANDLER_H__
