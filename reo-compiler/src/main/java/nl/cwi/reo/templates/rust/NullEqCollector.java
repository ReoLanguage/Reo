package nl.cwi.reo.templates.rust;

import nl.cwi.reo.semantics.predicates.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NullEqCollector implements FormulaTraverser<Void, Void> {
    Set<Variable> comparedToNull = new HashSet<>();

    public Void visit(Constant x) {
        return null;
    }
    public Void visit(Function x, List<Void> args) {
        return null;
    }
    public Void visit(NonNullValue x) {
        return null;
    }
    public Void visit(NullValue x) {
        return null;
    }
    public Void visit(MemoryVariable x) { return null; }
    public Void visit(PortVariable x) { return null; }
    public Void  visit(Conjunction x, List<Void> clauses) { return null; }
    public Void  visit(Disjunction x, List<Void> clauses) { return null; }
    public Void  visit(Equality x, Void lhs, Void rhs) {
        boolean leftNull = x.getLHS() instanceof NullValue;
        boolean rightNull = x.getRHS() instanceof NullValue;
        if (leftNull && !rightNull) {
            if (x.getRHS() instanceof Variable) {
                comparedToNull.add((Variable) x.getRHS());
            }
        } else if (!leftNull && rightNull) {
            if (x.getLHS() instanceof Variable) {
                comparedToNull.add((Variable) x.getLHS());
            }
        }
        return null;
    }
    public Void  visit(Negation x, Void  arg) { return null; }
    public Void  visit(Relation x, List<Void> args) { return null; }
    public Void  visit(TruthValue x) {return null;}

}
