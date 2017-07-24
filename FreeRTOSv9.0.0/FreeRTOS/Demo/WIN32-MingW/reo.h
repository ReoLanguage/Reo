#ifndef __REO_H__
#define __REO_H__

/**
 * Port for single producer single consumer
 */
typedef struct {
	unsigned int get_request : 1;
	void *put_request;
	void (*signal_producer)();
	void (*signal_consumer)();
} Port;

/**
 * Perform a put operation
 *
 * This is a blocking call
 *
 * @memberof Port
 * @param port Pointer to port
 * @param datum Pointer to datum
 * @return 1 if datum is a null pointer, and 0 otherwise.
 */
int Port_put(Port *port, void *datum);

/**
 * Perform a get operation
 *
 * This is a blocking call
 *
 * @memberof Port
 * @param port Pointer to port
 * @return Datum offered by the port.
 */
void* Port_get(Port *port);

#endif // __REO_H__
