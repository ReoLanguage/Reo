package nl.cwi.reo.templates.rust;

import java.util.List;
import java.util.Vector;

/*
Rusty analog of Reo's Term and Formula rolled into one. Analog of the Rust type Term in Reo-rs.
 */
public abstract class RsTerm {
    public String nonBoolTypeOf() {
        return null;
    };
    private static String newTempName(ImperativeFormProto proto, ImperativeFormProto.RuleDef rd) {
        String name = "temp_" + proto.nextTempPlease();
        proto.typeConstraints.add(new ImperativeFormProto.TypeConstraint.ExistsConstraint(name));
        return name;
    }

    abstract void generateCode(StringBuilder sb);
    /////////////////////////

    static class RsTrue extends RsTerm {
        @Override void generateCode(StringBuilder sb) {
            sb.append("True");
        }
    }
    static class RsFalse extends RsTerm {
        @Override void generateCode(StringBuilder sb) {
            sb.append("False");
        }
    }
    static class RsNot extends RsTerm {
        RsTerm inner;
        RsNot(RsTerm inner) {
            this.inner = inner;
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
        RsCall(String callHandleName, List<RsTerm> args) {
            this.callHandleName = callHandleName;
            this.args = args;
        }

        @Override void generateCode(StringBuilder sb) {
            sb.append("Named(");
            sb.append(myResource);
            sb.append(")");
        }
    }
    static class RsIsEq extends RsTerm {
        RsTerm lhs, rhs;
        RsIsEq(RsTerm lhs, RsTerm rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
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
        @Override void generateCode(StringBuilder sb) {
            sb.append("Named(\"");
            sb.append(name);
            sb.append("\")");
        }
    }
}
