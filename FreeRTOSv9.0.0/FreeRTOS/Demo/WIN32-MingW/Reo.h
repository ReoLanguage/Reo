#ifndef __REO_H__
#define __REO_H__

#define NULL ((void *) 0)

/**
 * Port for single producer single consumer
 */
typedef struct {
	volatile unsigned int get_request :1;
	volatile void *put_request;
	void (*signal_producer)();
	void (*signal_consumer)();
} port;

/**
 * Offers a datum on a given port and blocks until it is accepted.
 *
 * @memberof Port
 * @param port Reo port
 * @param datum Pointer to datum
 * @return 1 if datum is a null pointer, and 0 otherwise.
 */
int put(port *port, void *datum);

/**
 * Requests a datum from a given port and blocks until it is available.
 *
 * @memberof Port
 * @param port Reo port
 * @return Datum offered by the port.
 */
void* get(port *port);

#endif // __REO_H__
