package nl.cwi.reo.templates.rust;

import nl.cwi.reo.semantics.predicates.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Term / Formula visitor. Adds names of memory cells that occur in the given term / formula
 */
public class MemReadTraverser implements FormulaTraverser<Void, Void> {
    Set<String> sawReadMemory;

    MemReadTraverser(Term t, Set<String> sawReadMemory) {
        this.sawReadMemory = sawReadMemory;
        traverse(t);
    }
    MemReadTraverser(Formula f, Set<String> sawReadMemory) {
        this.sawReadMemory = sawReadMemory;
        traverse(f);
    }

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
    public Void visit(MemoryVariable x) {
        sawReadMemory.add(x.getName());
        return null;
    }
    public Void visit(PortVariable x) {
        return null;
    }

    public Void  visit(Conjunction x, List<Void> clauses) {
        return null;
    }
    public Void  visit(Disjunction x, List<Void> clauses) {
        return null;
    }
    public Void  visit(Equality x, Void lhs, Void rhs) {
        return null;
    }
    public Void  visit(Negation x, Void  arg) {
        return null;
    }
    public Void  visit(Relation x, List<Void> args) {
        return null;
    }
    public Void  visit(TruthValue x) {
        return null;
    }
}
