#include "Reo.h"
#include <stdio.h>
#include <pthread.h>

static void* red(void *args);
static void call_red();

static void* green(void *args);
static void call_green();

static void* blue(void *args);
static void call_blue();

static void* protocol(void *args);
static void call_protocol();

static port a = { 0, NULL, &call_red, &call_protocol };
static port b = { 0, NULL, &call_green, &call_protocol };
static port c = { 0, NULL, &call_protocol, &call_blue };

pthread_t type_id[4];

int main() {

	int Tred = pthread_create(&(type_id[0]), NULL, &red, NULL);
	int Tgreen = pthread_create(&(type_id[1]), NULL, &green, NULL);
	int Tblue = pthread_create(&(type_id[2]), NULL, &blue, NULL);
	int Tprotocol = pthread_create(&(type_id[3]), NULL, &protocol, NULL);

//	if (thread) {
//		// Optionally do stuff, such as wait on the thread.
//	}

//	xTaskCreate(red, "red", configMINIMAL_STACK_SIZE, NULL, 1, NULL);
//	xTaskCreate(green, "green", configMINIMAL_STACK_SIZE, NULL, 1, NULL);
//	xTaskCreate(blue, "blue", configMINIMAL_STACK_SIZE, NULL, 1, NULL);
//	xTaskCreate(protocol, "protocol", configMINIMAL_STACK_SIZE, NULL, 1, NULL);
//
//	vTaskStartScheduler();

	for (;;)
		;

	return 0;
}

static void* red(void *args) {
	for (;;) {
		for (int i = 0; i < 50; i++)
			;
		printf("put a!\n");
		fflush( stdout);
		put(&a, (void *) 0x121UL);
	}
	return 0;
}

static void call_red() {
	printf("signal red\n");
	fflush( stdout);
}

static void* green(void *args) {
	for (;;) {
		for (int i = 0; i < 50; i++)
			;
		printf("put b!\n");
		fflush( stdout);
		put(&b, (void *) 0x121UL);
	}
	return 0;
}

static void call_green() {
	printf("signal green\n");
	fflush( stdout);
}

static void* blue(void *args) {
	for (;;) {
		for (int i = 0; i < 40; i++)
			;

		printf("get c\n");
		fflush( stdout);
		get(&c);
		printf("Received!\r\n");
		fflush( stdout);
	}
	return 0;
}

static void call_blue() {
	printf("signal blue\n");
	fflush( stdout);
}

static void* protocol(void *args) {
	void *m;

	for (;;) {
		if (m == NULL && a.put_request != NULL && b.put_request != NULL
				&& c.get_request != 0) {
			put(&c, get(&a));
			m = get(&b);
		}
		if (m != NULL) {
			put(&c, m);
			m = NULL;
		}
	}
	return 0;
}

static void call_protocol() {
//		printf("a:");
//		printf(a.put_request);
//		printf("\r\n");
//		printf("b:");
//		printf(b.put_request);
//		printf("\r\n");
//		printf("c:");
//		printf(c.get_request);
//		printf("\r\n");
		fflush(stdout);
}
