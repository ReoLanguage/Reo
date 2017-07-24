#ifndef __REO_PROBLEM_H__
#define __REO_PROBLEM_H__

#include <stdbool.h>

// External types
struct REOPortHandler;

// Internal types
struct REOConstraint;
struct REOVariable;

/**
 * A data constraint problem instance
 */
struct REOProblem
{
    /// List of constraints
    struct REOConstraint *constraints;
    /// Number of constraints
    unsigned constraints_len;
    /// List of variables
    struct REOVariable *variables;
    /// Number of variables
    unsigned variables_len;
};

/**
 * Type of a constraint
 */
typedef enum REOConstraintType
{
    /// Always satisfied
    RCT_True,
    /// Never satisfied
    RCT_False,
    /// Satisfied if the data of all variables are equal
    RCT_Equality,
} REOConstraintType;

/**
 * Constructs a problem
 *
 * @memberof REOProblem
 * @param problem Object pointer
 * @param num_variables Number of variables to allocate for
 * @param num_constraints Number of constraints to allocate for
 */
void REOProblem_construct(struct REOProblem *problem, unsigned num_variables, unsigned num_constraints);

/**
 * Cleans a problem up
 *
 * @memberof REOProblem
 * @param problem Object pointer
 */
void REOProblem_cleanup(struct REOProblem *problem);

/**
 * Attach a port to a specific variable
 *
 * @memberof REOProblem
 * @param problem Object pointer
 * @param index Variable index to attach to
 * @param porthandler Handler to attache to the variable
 */
void REOProblem_set_variable(struct REOProblem *self, unsigned index, struct REOPortHandler *porthandler, unsigned uid);

/**
 * Fills in a specific constraint
 *
 * @memberof REOProblem
 * @param problem Object pointer
 * @param constraint_index The index of the constraint to fill in
 * @param type The type of the constraint
 * @param number_of_variables The number of variables to attach to this constraint
 * @param variable_indices The indices of the variables attached
 */
void REOProblem_set_constraint(struct REOProblem *problem, unsigned constraint_index, REOConstraintType type, unsigned number_of_variables, unsigned variable_indices[]);

void REOProblem_set_initial_assignment(struct REOProblem *problem);

/**
 * Tries to solve a problem
 *
 * @memberof REOProblem
 * @param problem Object pointer
 * @return `true` when the problem was be solved
 * @return `false` otherwise
 */
bool REOProblem_solve(struct REOProblem *problem);

/**
 * Updates the data of all ports according to the current assignment of variables
 *
 * @memberof REOProblem
 * @param problem Object pointer
 */
void REOProblem_set_from_variables(struct REOProblem *problem);

#endif // __REO_PROBLEM_H__
