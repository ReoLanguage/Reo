#include "container.h"

#include <stdlib.h>
#include <string.h>

#include "atomics.c"

/**
 * Atomic reference counted container
 */
struct REOContainer
{
    /// Reference count
    atomic_uint refcount;
    /// Wrapped data pointer
    char *data;
    /// Length of the data
    unsigned int len;
};

struct REOContainer* REOContainer_wrap(char* data, unsigned int len)
{
    struct REOContainer *result = malloc(sizeof(struct REOContainer));
    result->refcount = 1;
    result->data = data;
    result->len = len;
    
    return result;
}

void* REOContainer_get(struct REOContainer* self)
{
    return self->data;
}

unsigned int REOContainer_getlen(struct REOContainer* self)
{
    return self->len;
}

void REOContainer_retain(struct REOContainer* self)
{
    ATOMIC_INC(self->refcount);
}

void REOContainer_release(struct REOContainer* self)
{
    if (self == NULL) {
        return;
    }

    if (ATOMIC_DEC(self->refcount) == 0) {
        free(self->data);
        free(self);
    }
}

int REOContainer_compare(struct REOContainer *a, struct REOContainer *b)
{
    if (a == b) {
        return 0;
    }

    unsigned int len = (a->len < b->len) ? a->len : b->len;
    unsigned int cmpres = memcmp(a->data, b->data, len);

    if (cmpres != 0 || a->len == b-> len) return cmpres;
    return (a->len < b->len) ? -1 : 1;
}
