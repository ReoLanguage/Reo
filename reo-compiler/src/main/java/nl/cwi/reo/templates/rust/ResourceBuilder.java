package nl.cwi.reo.templates.rust;

import nl.cwi.reo.semantics.predicates.*;

import java.util.*;
import  nl.cwi.reo.templates.rust.ImperativeFormProto.Instruction;


/*
Given a Reo term, returns a NAME to the resource it represents.
After the traversal, it guarantees that orderedTermResources will describe the population of
resources such that your resouce will exist.
 */
public class ResourceBuilder  {

    private Map<Variable, Boolean> isNull;

    // TODO need a way to hash instances properly or we will never get cache hits
    private Map<RsTerm, Integer> createdResourceIndices = new HashMap<>(); // implicitiy -> name also
    private List<Instruction> ins = new ArrayList<>();

    ResourceBuilder(Map<Variable, Boolean> isNull) {
        this.isNull = isNull;
    }

    private String indexToResourceName(int index) {
        return Integer.toString(index);
    }

    String termToResource(Term t) {
        RsTerm rst = termToRsTerm(t);
        if (createdResourceIndices.containsKey(rst)) {
            return indexToResourceName(createdResourceIndices.get(rst));
        }
        if (rst instanceof RsTerm.RsNamed) {
            return ((RsTerm.RsNamed) rst).name;
        } else if (rst instanceof RsTerm.RsCall) {
            RsTerm.RsCall c = (RsTerm.RsCall) rst;
            String dest = indexToResourceName(ins.size());
            ins.add(new Instruction.CreateFromFunc(dest, c.callHandleName, c.args));
            return indexToResourceName(ins.size()-1);
        } else {
            throw new Error("Cannot handle");
        }
    }


    RsTerm formulaToRsTerm(Formula f) {
        if (f instanceof Conjunction) {
            Conjunction c = (Conjunction) f;
            List<RsTerm> clauses = new Vector<>();
            for (Formula x : c.getClauses()) {
                clauses.add(formulaToRsTerm(x));
            }
            return new RsTerm.RsAnd(clauses);
        } else if (f instanceof Disjunction) {
            Disjunction d = (Disjunction) f;
            List<RsTerm> clauses = new Vector<>();
            for (Formula c : d.getClauses()) {
                clauses.add(formulaToRsTerm(c));
            }
            return new RsTerm.RsOr(clauses);
        } else if (f instanceof Equality) {
            Equality c = (Equality) f;
            return new RsTerm.RsIsEq(termToRsTerm(c.getLHS()), termToRsTerm(c.getRHS()));
        } else if (f instanceof Negation) {
            Negation n = (Negation) f;
            return new RsTerm.RsNot(formulaToRsTerm(n.getFormula()));
        } else if (f instanceof Relation) {
            throw new Error("Unimplemented");
        } else if (f instanceof TruthValue) {
            TruthValue x = (TruthValue) f;
            if (x.getValue()) {
                return new RsTerm.RsTrue();
            } else {
                return new RsTerm.RsFalse();
            }
        } else {
            throw new Error("OH NO ITS AN UNKNOWN FORMULA SUBCLASS: " + f.getClass());
        }
    }
    private RsTerm termToRsTerm(Term t) {
        if (t instanceof PortVariable) {
            return new RsTerm.RsNamed(((PortVariable) t).getName());
        } else if (t instanceof MemoryVariable) {
            return new RsTerm.RsNamed(((MemoryVariable) t).getName());
        } else if (t instanceof Function) {
            Function f = (Function) t;
            String funcName = ("fn_" + f.getName()
                    .toLowerCase()
                    .replaceAll("\\s", "")
                    .replaceAll("[^a-z0-9_]", "_"))
                    .replaceAll("_+", "_")
                    .replaceFirst("_$", "");
            List<RsTerm> args = new ArrayList<>();
            for (Term arg : f.getArgs()) {
                args.add(termToRsTerm(arg));
            }
            return new RsTerm.RsCall(funcName, args);
        } else {
            throw new Error("Not sure");
        }
    }

    public List<Instruction> computeInstructionList() {
        return ins;
    }
}
