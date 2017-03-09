#include "context.h"

#include <limits.h>
#include <stdlib.h>

#include "atomics.c"

typedef struct REOContext REOContext;

static unsigned PORTS_PER_INT = sizeof(nonatomic_uint) * CHAR_BIT;

static unsigned REOContext_get_index(unsigned port_idx);
static nonatomic_uint REOContext_get_mask(unsigned port_idx);

void REOContext_construct(REOContext *self, unsigned ports_len)
{
    unsigned integers_needed = (ports_len - 1) / PORTS_PER_INT + 1;
    self->integers = calloc(integers_needed, sizeof(atomic_uint));
}

void REOContext_cleanup(REOContext *self)
{
    free(self->integers);
}

void REOContext_add(REOContext *self, unsigned port_idx)
{
    unsigned index = REOContext_get_index(port_idx);
    nonatomic_uint mask = REOContext_get_mask(port_idx);

    ATOMIC_OR(((atomic_uint*)self->integers)[index], mask);
}

void REOContext_remove(REOContext *self, unsigned port_idx)
{
    unsigned index = REOContext_get_index(port_idx);
    nonatomic_uint mask = REOContext_get_mask(port_idx);

    ATOMIC_AND(((atomic_uint*)self->integers)[index], ~mask);
}

bool REOContext_contains(REOContext *self, unsigned port_idx)
{
    unsigned index = REOContext_get_index(port_idx);
    nonatomic_uint mask = REOContext_get_mask(port_idx);

    return (((atomic_uint*)self->integers)[index] & mask) == mask;
}

static unsigned REOContext_get_index(unsigned port_idx)
{
    return port_idx / PORTS_PER_INT;
}

static nonatomic_uint REOContext_get_mask(unsigned port_idx)
{
    return ((nonatomic_uint)1) << (port_idx % PORTS_PER_INT);
}
