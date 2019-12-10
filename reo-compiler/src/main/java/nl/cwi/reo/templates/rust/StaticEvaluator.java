package nl.cwi.reo.templates.rust;

import nl.cwi.reo.semantics.predicates.*;
import java.util.List;

public class StaticEvaluator implements FormulaTraverser<Boolean, Boolean> {

    public Boolean visit(Constant x) {
        return null;
    }
    public Boolean visit(Function x, List<Boolean> args) {
        return null;
    }
    public Boolean visit(NonNullValue x) {
        return null;
    }
    public Boolean visit(NullValue x) {
        return null;
    }
    public Boolean visit(MemoryVariable x) { return null; }
    public Boolean visit(PortVariable x) { return null; }
    public Boolean visit(Conjunction x, List<Boolean> clauses) {
        // check for FALSE's
        for (Boolean c : clauses) {
            if (c != null && !c) return false;
        }
        // check for nulls
        for (Boolean c : clauses) {
            if (c == null) return null;
        }
        return true;
    }
    public Boolean visit(Disjunction x, List<Boolean> clauses) {

        // check for TRUE's
        for (Boolean c : clauses) {
            if (c != null && c) return true;
        }
        // check for nulls
        for (Boolean c : clauses) {
            if (c == null) return null;
        }
        return false;
    }
    public Boolean visit(Equality x, Boolean lhs, Boolean rhs) {
        if (lhs != null) {
            if (rhs != null) {
                return lhs == rhs;
            }
        }
        return null;
    }
    public Boolean visit(Negation x, Boolean arg) {
        if (arg != null) {
            return !arg;
        }
        return null;
    }
    public Boolean visit(Relation x, List<Boolean> args) { return null; }
    public Boolean visit(TruthValue x) {
        return x.getValue();
    }
}
