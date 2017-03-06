#include "problem.h"

#include "container.h"
#include "porthandler.h"

#include <stdlib.h>
#include <stdint.h>
#include <string.h>

/**
 * Data constraint
 */
struct REOConstraint
{
    /// List of all variables in the problem
    struct REOVariable *variables;
    /// List of indices of variables that are used in this problem
    unsigned *participating_variables;
    /// Number of participating variables
    unsigned participating_variables_len;
    /// Type of the constraint
    REOConstraintType type;
};

/**
 * A variable to be used in a problem
 */
struct REOVariable
{
    /// Flag indicating mutability of the variable
    bool mut;
    /// If mutable, specifies wether the data is filled in
    bool set;
    /// Data that the variable contains
    struct REOContainer *data;
    /// PortHandler corresponding to this variable. May be NULL
    struct REOPortHandler *porthandler;
    /// Identifier for this variable.
    unsigned uid;
};

/**
 * Domain of the variables
 */
typedef struct REOConstraintDomain
{
    /// List of possible data values
    struct REOContainer **data;
    /// Lenght of the list
    unsigned len;
} REOConstraintDomain;

/* ==== REOVariable ==== */

static void REOVariable_construct(struct REOVariable *self, struct REOPortHandler *porthandler, unsigned uid)
{
    self->porthandler = porthandler;
    self->uid = uid;

    self->data = NULL;
    if (porthandler) {
        self->data = porthandler->peek(porthandler->object);
    }

    if (self->data == NULL && (!porthandler || porthandler->set)) {
        self->mut = true;
        self->set = false;
    }
    else {
        self->mut = false;
        self->set = true;
    }
    
}

static void REOVariable_cleanup(struct REOVariable *self)
{
    REOContainer_release(self->data);
    self->data = NULL;
    self->set = false;
}

static void REOVariable_set_to_port(struct REOVariable *self)
{
    if (self->porthandler == NULL) {
        return;
    }

    if (self->mut) {
        REOContainer_retain(self->data);
        self->porthandler->set(self->porthandler->object, self->data);
    }
}

static void REOVariable_reset(struct REOVariable *self)
{
    if (self->porthandler != NULL) {
        REOVariable_cleanup(self);
        REOVariable_construct(self, self->porthandler, self->uid);
    }
    else if (self->mut) {
        REOVariable_cleanup(self);
    }
}

/* ==== REOConstraintDomain ==== */

static void REOConstraintDomain_construct(struct REOConstraintDomain *self, struct REOProblem *problem)
{
    unsigned len = 0;
    struct REOContainer **data = NULL;

    for (unsigned i = 0; i < problem->variables_len; ++i)
    {
        if (problem->variables[i].mut) {
            continue;
        }

        bool found = false;
        for (unsigned j = 0; j < len && !found; ++j)
        {
            found = (REOContainer_compare(problem->variables[i].data, data[j]) == 0);
        }

        if (!found) {
            len += 1;
            data = realloc(data, len * sizeof(struct REOContainer*));
            data[len - 1] = problem->variables[i].data;
            REOContainer_retain(data[len - 1]);
        }
    }

    self->len = len;
    self->data = data;
}

static void REOConstraintDomain_cleanup(struct REOConstraintDomain *self)
{
    for (unsigned i = 0; i < self->len; ++i)
    {
        REOContainer_release(self->data[i]);
    }
    free(self->data);
}

/* ==== REOConstraint ==== */

static void REOConstraint_construct(struct REOConstraint *self, unsigned num_variables, REOConstraintType type, struct REOVariable *variables)
{
    self->participating_variables_len = num_variables;
    self->participating_variables = calloc(num_variables, sizeof(unsigned));

    self->type = type;
    self->variables = variables;
}

static void REOConstraint_cleanup(struct REOConstraint *self)
{
    free(self->participating_variables);
}

static bool REOConstraint_unsatisfiable_equality(struct REOConstraint *self)
{
    bool seen_data = false;
    struct REOContainer *data = NULL;

    for (unsigned i = 0; i < self->participating_variables_len; ++i)
    {
        unsigned idx = self->participating_variables[i];
        struct REOVariable *variable = &self->variables[idx];

        if (!variable->set) {
            continue;
        }

        if (!seen_data) {
            data = variable->data;
            seen_data = true;
        }
        else if (REOContainer_compare(data, variable->data) != 0) {
            return true;
        }
    }

    return false;
}

static bool REOConstraint_unsatisfiable(struct REOConstraint *self)
{
    switch (self->type)
    {
    case RCT_False:
    default:
        return true;
    case RCT_True:
        return false;
    case RCT_Equality:
        return REOConstraint_unsatisfiable_equality(self);
    }
}

/* ==== REOProblem ==== */

static bool REOProblem_check_assignment(struct REOProblem *self)
{
    for (unsigned i = 0; i < self->constraints_len; ++i)
    {
        if (REOConstraint_unsatisfiable(&self->constraints[i])) {
            return false;
        }
    }
    return true;
}

static bool REOProblem_issatisfiable(struct REOProblem *self, REOConstraintDomain *domain, unsigned index)
{
    if (!REOProblem_check_assignment(self)) {
        return false;
    }

    if (index >= self->variables_len) {
        return true;
    }

    struct REOVariable *current = &self->variables[index];
    
    if (!current->mut) {
        return REOProblem_issatisfiable(self, domain, index + 1);
    }

    current->set = true;
    for (unsigned i = 0; i < domain->len; ++i)
    {
        REOContainer_release(current->data);
        current->data = domain->data[i];
        REOContainer_retain(current->data);
        if (REOProblem_issatisfiable(self, domain, index + 1)) {
            return true;
        }
    }
    current->set = false;

    return false;
}

static void REOProblem_reset_variables(struct REOProblem *self)
{
    for (unsigned i = 0; i < self->variables_len; ++i)
    {
        REOVariable_reset(&self->variables[i]);
    }
}

void REOProblem_construct(struct REOProblem *self, unsigned num_variables, unsigned num_constraints)
{
    self->constraints_len = num_constraints;
    self->constraints = calloc(num_constraints, sizeof(struct REOConstraint));

    self->variables_len = num_variables;
    self->variables = calloc(num_variables, sizeof(struct REOVariable));
    for (unsigned i = 0; i < num_variables; ++i)
    {
        REOVariable_construct(&self->variables[i], NULL, 0);
    }
}

void REOProblem_cleanup(struct REOProblem *self)
{
    for (unsigned i = 0; i < self->variables_len; ++i)
    {
        REOVariable_cleanup(&self->variables[i]);
    }
    free(self->variables);
    for (unsigned i = 0; i < self->constraints_len; ++i)
    {
        REOConstraint_cleanup(&self->constraints[i]);
    }
    free(self->constraints);
}

void REOProblem_set_variable(struct REOProblem *self, unsigned index, struct REOPortHandler *porthandler, unsigned uid)
{
    REOVariable_construct(&self->variables[index], porthandler, uid);
}

void REOProblem_set_initial_assignment(struct REOProblem *self)
{
    REOProblem_reset_variables(self);
}

bool REOProblem_solve(struct REOProblem *self)
{
    REOConstraintDomain domain;
    REOConstraintDomain_construct(&domain, self);

    bool result = REOProblem_issatisfiable(self, &domain, 0);

    REOConstraintDomain_cleanup(&domain);
    return result;
}

void REOProblem_set_from_variables(struct REOProblem *self)
{
    for (unsigned i = 0; i < self->variables_len; ++i)
    {
        REOVariable_set_to_port(&self->variables[i]);
    }
}

void REOProblem_set_constraint(struct REOProblem *self, unsigned constraint_index, REOConstraintType type, unsigned number_of_variables, unsigned variable_indices[])
{
    struct REOConstraint *constraint = &self->constraints[constraint_index];

    REOConstraint_construct(constraint, number_of_variables, type, self->variables);
    
    for (unsigned i = 0; i < number_of_variables; ++i)
    {
        constraint->participating_variables[i] = variable_indices[i];
    }
}

void REOProblem_set_variable_uid(struct REOProblem *self, unsigned variable_index, unsigned uid)
{
    self->variables[variable_index].uid = uid;
}
