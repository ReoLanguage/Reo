#include <Reo.h>

int put(port *port, void *datum) {
	if (datum == NULL)
		return 1;
	while (port->put_request != NULL)
		;
	port->put_request = datum;
	port->signal_consumer();
	while (port->get_request == 0 && port->put_request != NULL) {
		;
	}
	return 0;
}

void *get(port *port) {
	void *datum;
	while ((datum = port->put_request) == NULL)
		;
	port->put_request = NULL;
	port->get_request = 0;
	port->signal_producer();
	return datum;
}
