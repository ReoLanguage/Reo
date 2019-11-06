package nl.cwi.reo.templates.rust;

import java.util.List;
import java.util.Vector;

/*
Rusty analog of Reo's Term and Formula rolled into one. Analog of the Rust type Term in Reo-rs.
 */
public abstract class RsTerm {
    public String nonBoolTypeOf() {
        // null means BOOLEAN type for sure
        return null;
    };
    protected abstract Boolean knownBool();
    public abstract RsTerm simplify();
    public abstract String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd);
    private static String newTempName(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
        String name = "temp_" + proto.nextTempPlease();
        proto.typeConstraints.add(new ImperativeFormProto.TypeConstraint.ExistsConstraint(name));
        return name;
    }

    abstract void generateCode(StringBuilder sb);
    /////////////////////////

    static class RsTrue extends RsTerm {
        protected Boolean knownBool() {
            return true;
        }
        public RsTerm simplify() {
            return this;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            proto.nameDefs.constTrueUsed = true;
            return "CONST_BOOL_TRUE";
        }
        @Override void generateCode(StringBuilder sb) {
            sb.append("True");
        }
    }
    static class RsFalse extends RsTerm {
        protected Boolean knownBool() {
            return false;
        }
        public RsTerm simplify() {
            return this;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            proto.nameDefs.constFalseUsed = true;
            return "CONST_BOOL_FALSE";
        }
        @Override void generateCode(StringBuilder sb) {
            sb.append("False");
        }
    }
    static class RsNot extends RsTerm {
        RsTerm inner;
        RsNot(RsTerm inner) {
            this.inner = inner;
        }
        protected Boolean knownBool() {
            Boolean x = inner.knownBool();
            if (x == null) return null;
            return !x;
        }
        public RsTerm simplify() {
            Boolean b = knownBool();
            if (b != null) {
                if (b) return new RsTrue();
                return new RsFalse();
            }
            if (inner instanceof RsNot) {
                return ((RsNot) inner).inner;
            }
            return this;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            String name = newTempName(proto, rd);
            rd.ins.add(new ImperativeFormProto.Instruction.CreateFromFormula(name, this));
            return name;
        }
        @Override void generateCode(StringBuilder sb) {
            sb.append("Not(Box::new(");
            inner.generateCode(sb);
            sb.append("))");
        }
    }
    static class RsAnd extends RsTerm {
        List<RsTerm> clauses;
        RsAnd(List<RsTerm> clauses) {
            this.clauses = clauses;
        }
        protected Boolean knownBool() {
            boolean sawNull = false;
            for (RsTerm x : clauses) {
                Boolean b = x.knownBool();
                if (b == null) sawNull = true;
                if (!b) return false;
            }
            if (sawNull) return null;
            return true;
        }
        public RsTerm simplify() {
            List<RsTerm> clauses2 = new Vector<>();
            for (RsTerm t : clauses) {
                RsTerm t2 = t.simplify();
                Boolean b = t2.knownBool();
                if (b == null || !b) {
                    clauses2.add(t2);
                }
            }
            if (clauses2.isEmpty()) return new RsTerm.RsTrue();
            if (clauses2.size() == 1) return clauses2.get(0);
            clauses = clauses2;
            return this;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            String name = newTempName(proto, rd);
            rd.ins.add(new ImperativeFormProto.Instruction.CreateFromFormula(name, this));
            return name;
        }
        @Override void generateCode(StringBuilder sb) {
            sb.append("And(vec![");
            for (RsTerm c: clauses) {
                c.generateCode(sb);
                sb.append(", ");
            }
            sb.append("])");
        }
    }
    static class RsOr extends RsTerm {
        List<RsTerm> clauses;
        RsOr(List<RsTerm> clauses) {
            this.clauses = clauses;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            String name = newTempName(proto, rd);
            rd.ins.add(new ImperativeFormProto.Instruction.CreateFromFormula(name, this));
            return name;
        }
        protected Boolean knownBool() {
            boolean sawNull = false;
            for (RsTerm x : clauses) {
                Boolean b = x.knownBool();
                if (b == null) sawNull = true;
                else if (b) return true;
            }
            if (sawNull) return null;
            return clauses.isEmpty();
        }
        public RsTerm simplify() {
            List<RsTerm> clauses2 = new Vector<>();
            for (RsTerm t : clauses) {
                RsTerm t2 = t.simplify();
                Boolean b = t2.knownBool();
                if (b == null || !b) {
                    clauses2.add(t2);
                } else {
                    return t2;
                }
            }
            if (clauses2.isEmpty()) return new RsTerm.RsTrue();
            if (clauses2.size() == 1) return clauses2.get(0);
            clauses = clauses2;
            return this;
        }
        @Override void generateCode(StringBuilder sb) {
            sb.append("Or(vec![");
            for (RsTerm c: clauses) {
                c.generateCode(sb);
                sb.append(", ");
            }
            sb.append("])");
        }
    }
    static class RsCall extends RsTerm {
        String callHandleName, myResource;
        List<RsTerm> args;
        @Override public String nonBoolTypeOf() {
            return callHandleName;
        }
        RsCall(String callHandleName, List<RsTerm> args) {
            this.callHandleName = callHandleName;
            this.args = args;
        }
        protected Boolean knownBool() {
            return null;
        }
        public RsTerm simplify() {
            return this;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            List<String> argResources = new Vector<>();
            for (RsTerm arg: args) {
                String a = arg.newResource(proto, rd);
                argResources.add(a);
                // System.out.printf("ARG: %s%n", a);
            }
            String dest = newTempName(proto, rd);
            rd.ins.add(new ImperativeFormProto.Instruction.CreateFromFunc(dest, callHandleName, argResources));
            myResource = dest; // TODO HORRIBLE STATEFUL HACK


            List<String> argTypeOf = new Vector<>();
            for (RsTerm r: args) {
                String t = r.nonBoolTypeOf();
                // TODO t is NULL iff type MUST be null
                argTypeOf.add(t);
            }
            proto.nameDefs.funcTypeSigs.computeIfAbsent(callHandleName, x -> new ImperativeFormProto.FuncTypeSig(dest, argTypeOf));
            ImperativeFormProto.FuncTypeSig f = proto.nameDefs.funcTypeSigs.get(callHandleName);
            if (argTypeOf.size() != f.argTypeOf.size()) {
                throw new Error(String.format("Func used with different arity!. %d != %d", argTypeOf.size(), f.argTypeOf.size()));
            }

            String tl = dest;
            String tr = f.retTypeOf;
            if (tl != null && tr != null) {
                if (!tl.equals(tr)) {
                    proto.typeConstraints.add(new ImperativeFormProto.TypeConstraint.EqConstraint(tl, tr));
                }
            }
            for (int i = 0; i < argTypeOf.size(); i++) {
                tl = argTypeOf.get(i);
                tr = f.argTypeOf.get(i);
                if (tl != null && tr != null) {
                    if (!tl.equals(tr)) {
                        proto.typeConstraints.add(new ImperativeFormProto.TypeConstraint.EqConstraint(tl, tr));
                    }
                }
            }
            return dest;
        }

        @Override void generateCode(StringBuilder sb) {
            sb.append("Named(");
            sb.append(myResource);
            sb.append(")");
        }
    }
    static class RsIsEq extends RsTerm {
        RsTerm lhs, rhs;
        protected Boolean knownBool() {
            Boolean l = lhs.knownBool();
            Boolean r = rhs.knownBool();
            if (l != null) {
                if (l == r) return true;
                if (r != null) return false;
            }
            return null;
        }
        RsIsEq(RsTerm lhs, RsTerm rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }
        public RsTerm simplify() {
            Boolean bl = lhs.knownBool();
            Boolean br = rhs.knownBool();
            if (bl != null && bl == br) return new RsTrue();
            if (bl != null && br != null && bl.booleanValue() != br.booleanValue()) return new RsFalse();
            return this;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            String name = newTempName(proto, rd);
            rd.ins.add(new ImperativeFormProto.Instruction.CreateFromFormula(name, this));
            return name;
        }
        @Override void generateCode(StringBuilder sb) {
            sb.append("IsEq(");
            lhs.generateCode(sb);
            sb.append(", ");
            rhs.generateCode(sb);
            sb.append(")");
        }
    }
    static class RsNamed extends RsTerm {
        String name;
        RsNamed(String name) {
            this.name = name;
        }
        @Override public String nonBoolTypeOf() {
            return name;
        }
        public String newResource(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
            return name;
        }
        @Override void generateCode(StringBuilder sb) {
            sb.append("Named(\"");
            sb.append(name);
            sb.append("\")");
        }
        protected Boolean knownBool() {
            return null;
        }
        public RsTerm simplify() {
            return this;
        }
    }
}
