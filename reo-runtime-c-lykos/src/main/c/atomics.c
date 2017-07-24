/*
 * ATOMIC_AND and ATOMIC_OR will return the original value
 * ATOMIC_INC and ATOMIC_DEC will return the new value
 */

#ifndef __REO_ATOMICS_C__
#define __REO_ATOMICS_C__

#if defined _MSC_VER
#   include <Windows.h>
    typedef ULONG atomic_uint;
    typedef ULONG nonatomic_uint;
#   define ATOMIC_USING_INTERLOCKED
#elif defined HAVE_SYNC_AND_FETCH
    typedef unsigned int atomic_uint;
    typedef unsigned int nonatomic_uint;
#   define ATOMIC_USING_SYNC_AND_FETCH
#else
#   error "No atomic type available";
#endif

#if defined ATOMIC_USING_SYNC_AND_FETCH
#   define ATOMIC_AND(a, b) (__sync_fetch_and_and(&a, b))
#   define ATOMIC_OR(a, b) (__sync_fetch_and_or(&a, b))
#   define ATOMIC_INC(a) (__sync_add_and_fetch(&a, 1))
#   define ATOMIC_DEC(a) (__sync_sub_and_fetch(&a, 1))
#elif defined ATOMIC_USING_INTERLOCKED
#   define ATOMIC_AND(a, b) (InterlockedAnd(&(LONG)a, b))
#   define ATOMIC_OR(a, b) (InterlockedOr(&(LONG)a, b))
#   define ATOMIC_INC(a) (InterlockedIncrement(&(LONG)a))
#   define ATOMIC_DEC(a) (InterlockedDecrement(&(LONG)a))
#endif

#endif // __REO_ATOMICS_C__
