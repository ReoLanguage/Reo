package nl.cwi.reo.templates.rust;

import nl.cwi.reo.semantics.predicates.*;

import java.util.*;

public class ConcreteGuardBuilder implements FormulaTraverser<Formula, Term> {
    Map<Variable, Boolean> isNull;
    ConcreteGuardBuilder(Map<Variable, Boolean> isNull) {
        this.isNull = isNull;
    }

    public Term visit(Constant x) {
        return x;
    }
    public Term visit(Function x, List<Term> args) {
        return new Function(x.getName(), args, x.getInfix(), x.getTypeTag());
    }
    public Term visit(NonNullValue x) {
        return x;
    }
    public Term visit(NullValue x) { return x; }
    public Term visit(MemoryVariable x) { return x; }
    public Term visit(PortVariable x) { return x; }
    public Formula visit(Conjunction x, List<Formula> clauses) { return new Conjunction(clauses); }
    public Formula visit(Disjunction x, List<Formula> clauses) { return new Disjunction(clauses); }
    public Formula visit(Equality x, Term lhs, Term rhs) {
        boolean leftIsNull, rightIsNull;
        if (lhs instanceof NullValue) {
            leftIsNull = true;
        } else if (lhs instanceof Variable) {
            leftIsNull = isNull.get(lhs);
        } else {
            // can't compare
            return new Equality(lhs, rhs);
        }
        if (rhs instanceof NullValue) {
            rightIsNull = true;
        } else if (rhs instanceof Variable) {
            rightIsNull = isNull.get(rhs);
        } else {
            // can't compare
            return new Equality(lhs, rhs);
        }
        return new TruthValue(leftIsNull == rightIsNull);
    }
    public Formula visit(Negation x, Formula arg) { return new Negation(arg); }
    public Formula visit(Relation x, List<Term> args) { return new Relation(x.getName(), args, x.isInfix()); }
    public Formula visit(TruthValue x) {return x;}
}
