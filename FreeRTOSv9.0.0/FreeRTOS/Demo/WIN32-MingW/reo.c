#include "reo.h"

int Port_put(Port *port, void *datum) {
	if (datum == 0)
		return 1;
	while (port->put_request != 0) {
	}
	port->put_request = datum;
	port->signal_consumer();
	while (!port->get_request && port->put_request != 0) {
	}
	return 0;
}

void *Port_get(Port *port) {
	void *datum;
	while ((datum = port->put_request) == 0) {
	}
	port->put_request = 0;
	port->get_request = 0;
	port->signal_producer();
	return datum;
}
