#include "thread.h"

#include <time.h>

#define NSEC_PER_SEC 1000000000l
#define NSEC_PER_MSEC 1000000l

#if defined _MSC_VER
#   include <sys/types.h>
#   include <sys/timeb.h>
#endif

static struct timespec reltime_to_abstime(unsigned long milliseconds);

void REOMutex_init(REOMutex *mutex)
{
    pthread_mutex_init(mutex, NULL);
}

void REOMutex_destroy(REOMutex *mutex)
{
    pthread_mutex_destroy(mutex);
}

void REOMutex_lock(REOMutex *mutex)
{
    pthread_mutex_lock(mutex);
}

void REOMutex_unlock(REOMutex *mutex)
{
    pthread_mutex_unlock(mutex);
}

void REOSemaphore_init(REOSemaphore *semaphore, int count)
{
    sem_init(semaphore, 0, count);
}

void REOSemaphore_destroy(REOSemaphore *semaphore)
{
    sem_destroy(semaphore);
}

void REOSemaphore_wait(REOSemaphore *semaphore)
{
    sem_wait(semaphore);
}

bool REOSemaphore_timedwait(REOSemaphore *semaphore, unsigned long milliseconds)
{
    struct timespec tm = reltime_to_abstime(milliseconds);
    return sem_timedwait(semaphore, &tm) == 0;
}

void REOSemaphore_signal(REOSemaphore *semaphore)
{
    sem_post(semaphore);
}

void REOSemaphore_drain(REOSemaphore *semaphore)
{
    while (sem_trywait(semaphore) == 0);
}

void REOThread_create(REOThread *thread, void *(*start_routine) (void *), void *arg)
{
    pthread_create(thread, NULL, start_routine, arg);
}

void REOThread_join(REOThread thread, void **retval)
{
    pthread_join(thread, retval);
}

static struct timespec reltime_to_abstime(unsigned long milliseconds)
{
    struct timespec result;

#if defined _MSC_VER
    struct __timeb64 ftime;
    _ftime64_s(&ftime);
    
    result.tv_sec = ftime.time;
    result.tv_nsec = ftime.millitm * NSEC_PER_MSEC;
#else
    clock_gettime(CLOCK_REALTIME, &result);
#endif

    result.tv_sec += milliseconds / 1000;
    long nanoseconds = (milliseconds % 1000) * NSEC_PER_MSEC;
    if (result.tv_nsec >= NSEC_PER_SEC - nanoseconds) {
        result.tv_sec += 1;
        result.tv_nsec += nanoseconds - NSEC_PER_SEC;
    }
    else {
        result.tv_nsec += nanoseconds;
    }

    return result;
}
