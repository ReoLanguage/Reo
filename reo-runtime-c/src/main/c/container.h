/**
 * @file container.h
 * @author Mathijs van de Nes
 */

#ifndef __REO_CONTAINER_H__
#define __REO_CONTAINER_H__

struct REOContainer;

/**
 * Wraps a value in a REOContainer
 *
 * @memberof REOContainer
 * @param data pointer to the data to wrap. This object will asume ownership
 * @param len the lengths in bytes of the data. Used for copying the data.
 */
struct REOContainer* REOContainer_wrap(char* data, unsigned int len);

/**
 * Returns a reference to the value contained
 *
 * @memberof REOContainer
 * @param container Object pointer
 */
void* REOContainer_get(struct REOContainer* container);

/**
* Returns the length of the data contained
*
* @memberof REOContainer
* @param container Object pointer
*/
unsigned int REOContainer_getlen(struct REOContainer* container);

/**
 * Increase the reference count
 *
 * This function must be called when creating an additional reference to the container
 *
 * @memberof REOContainer
 * @param container Object pointer
 */
void REOContainer_retain(struct REOContainer* container);

/**
 * Decrease the reference count
 *
 * Call this function when you stop using a reference
 *
 * @memberof REOContainer
 * @param container Object pointer
 */
void REOContainer_release(struct REOContainer* container);

/**
 * Compare function for two containers
 *
 * Returns 0 if the data in c1 == c2, <0 if c1 < c2 and >0 if c1 > c2
 *
 * @memberof REOContainer
 * @param c1 The first container to compare
 * @param c2 The second container to compare
 */
int REOContainer_compare(struct REOContainer *c1, struct REOContainer *c2);

#endif // __REO_CONTAINER_H__
