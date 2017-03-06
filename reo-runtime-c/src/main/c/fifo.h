/**
 * @file fifo.h
 * @author Mathijs van de Nes
 */

#ifndef __REO_FIFO_H__
#define __REO_FIFO_H__

#include "container.h"
#include "porthandler.h"
#include "thread.h"

struct REOAutomaton;

/**
 * A FIFO buffer with two porthandles
 */
struct REOFifo
{
    /// Ringbuffer
    struct REOContainer** buffer;
    /// Index of the head
    unsigned head;
    /// Index of the tail
    unsigned tail;
    /// Capacity of the fifo + 1
    unsigned capacity;
    /// REOPortHandler for the input side of the FIFO
    struct REOPortHandler input_handler;
    /// REOPortHandler for the output side of the FIFO
    struct REOPortHandler output_handler;
    /// Reference to the automaton on the input side
    struct REOAutomaton *input_automaton;
    /// Reference to the automaton on the output side
    struct REOAutomaton *output_automaton;
    /// Port index at the input automaton
    unsigned input_index;
    /// Port index at the output automaton
    unsigned output_index;
    /// Mutex to lock the head and tail when read
    REOMutex mutex;
};

/**
 * Constructs a FIFO with the given capacity
 *
 * @memberof REOFifo
 * @param fifo Object pointer
 * @param capacity Requested capacity of the FIFO
 */
void REOFifo_construct(struct REOFifo *fifo, unsigned capacity);

/**
 * Cleans all contained data of a FIFO
 *
 * Usage of the FIFO after this is undefined
 *
 * @memberof REOFifo
 * @param fifo Object pointer
 */
void REOFifo_cleanup(struct REOFifo *fifo);

/**
 * Push a value to the FIFO
 *
 * This function may be used for initial values
 *
 * @memberof REOFifo
 * @param fifo Object pointer
 * @param value Data object to push
 */
void REOFifo_push(struct REOFifo *fifo, struct REOContainer* value);

/**
 * Bind a FIFO to automatons
 *
 * @memberof REOFifo
 * @param fifo Object pointer
 * @param input_automaton Reference to the input automaton
 * @param input_index Port index on the input automaton
 * @param output_automaton Reference to the output automaton
 * @param output_index Port index on the output automaton
 */
void REOFifo_bind(struct REOFifo *fifo, struct REOAutomaton *input_automaton, unsigned input_index, struct REOAutomaton *output_automaton, unsigned output_index);

/**
 * Finish construction of a FIFO
 *
 * This function will (de)register a fifo from the input/output automatons
 *
 * @memberof REOFifo
 * @param fifo Object pointer
 */
void REOFifo_finish(struct REOFifo *fifo);

#endif // __REO_FIFO_H__
