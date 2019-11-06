package nl.cwi.reo.templates.rust;

import nl.cwi.reo.semantics.predicates.*;

import java.util.List;
import java.util.Vector;

public interface FormulaTraverser<F,T> extends TermTraverser<T> {
    F visit(Conjunction x, List<F> clauses);
    F visit(Disjunction x, List<F> clauses);
    F visit(Equality x, T lhs, T rhs);
    F visit(Negation x, F arg);
    F visit(Relation x, List<T> args);
    F visit(TruthValue x);

    default F traverse(Formula f) {
        if (f instanceof Conjunction) {
            Conjunction c = (Conjunction) f;
            List<F> clauses = new Vector<>();
            for (Formula x : c.getClauses()) {
                clauses.add(traverse(x));
            }
            return visit(c, clauses);
        } else if (f instanceof Disjunction) {
            Disjunction d = (Disjunction) f;
            List<F> clauses = new Vector<>();
            for (Formula c : d.getClauses()) {
                clauses.add(traverse(c));
            }
            return visit(d, clauses);
        } else if (f instanceof Equality) {
            Equality c = (Equality) f;
            return visit(c, traverse(c.getLHS()), traverse(c.getRHS()));
        } else if (f instanceof Negation) {
            Negation n = (Negation) f;
            return visit(n, traverse(n.getFormula()));
        } else if (f instanceof Relation) {
            Relation r = (Relation) f;
            List<T> clauses = new Vector<>();
            for (Term t : r.getArgs()) {
                clauses.add(traverse(t));
            }
            return visit(r, clauses);
        } else if (f instanceof TruthValue) {
            return visit((TruthValue) f);
        } else {
            throw new Error("OH NO ITS A (FORMULA) " + f.getClass());
        }
    }
}
