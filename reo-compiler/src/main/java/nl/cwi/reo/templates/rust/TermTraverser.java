package nl.cwi.reo.templates.rust;

import java.util.*;
import nl.cwi.reo.semantics.predicates.*;


/*
generic interface for visiting a Reo-internal Term
 */
public interface TermTraverser<T> {
    T visit(Constant x);
    T visit(Function x, List<T> args);
    T visit(NonNullValue x);
    T visit(NullValue x);
    T visit(MemoryVariable x);
    T visit(PortVariable x);

    default T traverse(Term t) {
        if (t instanceof Constant) {
            return visit((Constant) t);
        } else if (t instanceof Function) {
            Function f = (Function) t;
            List<T> args = new Vector<>();
            for (Term a : f.getArgs()) {
                args.add(traverse(a));
            }
            return visit(f, args);
        } else if (t instanceof NonNullValue) {
            return visit((NonNullValue) t);
        } else if (t instanceof NullValue) {
            return visit((NullValue) t);
        } else if (t instanceof MemoryVariable) {
            return visit((MemoryVariable) t);
        } else if (t instanceof PortVariable) {
            return visit((PortVariable) t);
        } else {
            throw new Error("OH NO ITS A (TERM)" + t.getClass());
        }
    }
}
