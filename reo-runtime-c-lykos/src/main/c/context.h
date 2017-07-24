/**
 * @file context.h
 * @author Mathijs van de Nes
 */

#ifndef __REO_CONTEXT_H__
#define __REO_CONTEXT_H__

#include <stdbool.h>

/**
 * Holds flags specifying which ports are available for I/O
 */
struct REOContext
{
    /// List of atomic integers
    void *integers;
};

/**
 * Constructs a context with for the requested number of ports
 *
 * @memberof REOContext
 * @param context Object pointer
 * @param ports_len Amount of ports
 */
void REOContext_construct(struct REOContext *context, unsigned ports_len);

/**
 * Cleans the context
 *
 * @memberof REOContext
 * @param context Object pointer
 */
void REOContext_cleanup(struct REOContext *context);

/**
 * Add the specified port to the context
 *
 * @memberof REOContext
 * @param context Object pointer
 * @param port_idx Index of the port to add
 */
void REOContext_add(struct REOContext *context, unsigned port_idx);

/**
 * Removes the specified port from the context
 *
 * @memberof REOContext
 * @param context Object pointer
 * @param port_idx Index of the port to remove
 */
void REOContext_remove(struct REOContext *context, unsigned port_idx);

/**
 * Returns wether the context contains a specific index
 *
 * @memberof REOContext
 * @param context Object pointer
 * @param port_idx Index of the port to check
 */
bool REOContext_contains(struct REOContext *context, unsigned port_idx);

#endif // __REO_CONTEXT_H__
