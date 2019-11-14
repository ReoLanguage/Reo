package nl.cwi.reo.templates.rust;

import nl.cwi.reo.interpret.values.*;
import nl.cwi.reo.semantics.predicates.*;

import java.util.List;

/*
Visitor object that recustively builds a RsTerm. It can be accessed after construction
from the `root' field. Subtrees that represent RESOURCES will not be added to the term,
but rather added to the list of INSTRUCTIONS, linked together by the resulting resource.
 */
public class RsTermBuilder implements FormulaTraverser<RsTerm, RsTerm> {
    ImperativeFormProto proto;
    ImperativeFormProto.Predicate pred;
    RsTerm root;

    RsTermBuilder(ImperativeFormProto proto, ImperativeFormProto.Predicate pred, Term t) {
        this.proto = proto;
        this.pred = pred;
        this.root = traverse(t);
    }
    RsTermBuilder(ImperativeFormProto proto, ImperativeFormProto.Predicate pred, Formula f) {
        this.proto = proto;
        this.pred = pred;
        this.root = traverse(f);
    }

    public RsTerm visit(Constant x) {
        Value v = x.getValue();
        String name;
        if (v instanceof BooleanValue) {
            BooleanValue b = (BooleanValue) v;
            if (b.getValue()) {
                return new RsTerm.RsTrue();
            } else {
                return new RsTerm.RsFalse();
            }
        } else if (v instanceof DecimalValue) {
            name = String.valueOf(((DecimalValue) v).getValue());
        } else if (v instanceof IntegerValue) {
            name = String.valueOf(((IntegerValue) v).getValue());
        } else if (v instanceof StringValue) {
            name = ((StringValue) v).getValue();
        } else {
            throw new Error("Can't understand constant of class : " + v.getClass().getName());
        }
        name = "const_" + name;
        proto.nameDefs.constants.add(name);
        return new RsTerm.RsNamed(name);
    }
    public RsTerm visit(Function x, List<RsTerm> args) {
        String funcName = ("fn_" + x.getName()
                .toLowerCase()
                .replaceAll("\\s", "")
                .replaceAll("[^a-z0-9_]", "_"))
            .replaceAll("_+", "_")
            .replaceFirst("_$", "");
        return new RsTerm.RsCall(funcName, args);
    }

    public RsTerm visit(Conjunction x, List<RsTerm> clauses) {
        return new RsTerm.RsAnd(clauses);
    }
    public RsTerm visit(Disjunction x, List<RsTerm> clauses) {
        return new RsTerm.RsOr(clauses);
    }
    public RsTerm visit(Equality x, RsTerm lhs, RsTerm rhs) {
        if (x.getRHS() instanceof NullValue) {
            if (x.getLHS() instanceof MemoryVariable) {
                MemoryVariable m = (MemoryVariable) x.getLHS();
                String n = m.getName();
                if (pred.fullMem.contains(n)) {
                    return new RsTerm.RsFalse();
                } else if (pred.emptyMem.contains(n)) {
                    return new RsTerm.RsTrue();
                }
            } else {
                return new RsTerm.RsFalse();
            }
        }
        return new RsTerm.RsIsEq(lhs, rhs);
    }
    public RsTerm visit(Negation x, RsTerm arg) {
        return new RsTerm.RsNot(arg);
    }
    public RsTerm visit(Relation x, List<RsTerm> args) {
        throw new Error("TODO RELATION");
    }
    public RsTerm visit(TruthValue x) {
        if (x.getValue()) {
            return new RsTerm.RsTrue();
        } else {
            return new RsTerm.RsFalse();
        }
    }
    public RsTerm visit(NonNullValue x) {
        throw new Error("WASNT EXPECTING NON-NULL-VALUE");
    }
    public RsTerm visit(NullValue x) {
        return null; //NOTE, layer above deals with the removal of this null
    }
    public RsTerm visit(MemoryVariable x) {
        return new RsTerm.RsNamed(x.getName());
    }
    public RsTerm visit(PortVariable x) {
        return new RsTerm.RsNamed(x.getName());
    }
}
