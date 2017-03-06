#ifndef __REO_THREAD_H__
#define __REO_THREAD_H__

#include <stdbool.h>
#include <pthread.h>
#include <semaphore.h>

typedef pthread_mutex_t REOMutex;

void REOMutex_init(REOMutex *mutex);
void REOMutex_destroy(REOMutex *mutex);
void REOMutex_lock(REOMutex *mutex);
void REOMutex_unlock(REOMutex *mutex);

typedef sem_t REOSemaphore;

void REOSemaphore_init(REOSemaphore *semaphore, int count);
void REOSemaphore_destroy(REOSemaphore *semaphore);
void REOSemaphore_wait(REOSemaphore *semaphore);
bool REOSemaphore_timedwait(REOSemaphore *semaphore, unsigned long milliseconds);
void REOSemaphore_signal(REOSemaphore *semaphore);
void REOSemaphore_drain(REOSemaphore *semaphore);

typedef pthread_t REOThread;

void REOThread_create(REOThread *thread, void *(*start_routine) (void *), void *arg);
void REOThread_join(REOThread thread, void **retval);

#endif // __REO_THREAD_H__
