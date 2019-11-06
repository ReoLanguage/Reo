package nl.cwi.reo.templates.rust;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.semantics.predicates.*;
import nl.cwi.reo.templates.*;
import java.util.*;

/*
Main workhorse of the Rust code generator
Translates the given Reo Protocol object into a Rusty Imperative Form representation in constructor
Subsequent calls of "generateCode" will write the Rust function for building that type to a given
stringbuffer.

Contains several inner classes for managing state both temporarily and for persistently storing the
finished imperative form.
 */
public class ImperativeFormProto {
    static abstract class TypeConstraint {
        abstract void applyTo(Map<String, TypeClass> classes);

        static class ExistsConstraint extends TypeConstraint {
            String name;

            ExistsConstraint(String name) {
                this.name = name;
            }

            void applyTo(Map<String, TypeClass> classes) {
                classes.computeIfAbsent(name, k -> new TypeClass(name));
            }

            @Override
            public String toString() {
                return String.format("ExistsConstraint { %s }", name);
            }
        }

        static class TraitConstraint extends TypeConstraint {
            String type, trait;

            void applyTo(Map<String, TypeClass> classes) {
                classes.computeIfAbsent(type, k -> new TypeClass(type));
                classes.get(type).rawConstraints.add(trait);
            }

            TraitConstraint(String type, String trait) {
                this.type = type;
                this.trait = trait;
            }

            @Override
            public String toString() {
                return String.format("TraitConstraint { type: %s, trait: %s }",
                        type, trait);
            }
        }

        static class FromConstraint extends TypeConstraint {
            String type, fromType, arg;

            void applyTo(Map<String, TypeClass> classes) {
                classes.computeIfAbsent(type, k -> new TypeClass(type));
                classes.get(type).rawConstraints.add(String.format("From<%s>", fromType));
            }

            FromConstraint(String type, String fromType, String arg) {
                this.type = type;
                this.fromType = fromType;
                this.arg = arg;
            }

            @Override
            public String toString() {
                return String.format("FromConstraint { type: %s, fromType: %s, arg: %s }",
                        type, fromType, arg);
            }
        }

        static class EqConstraint extends TypeConstraint {
            String lhs, rhs;
            void applyTo(Map<String, TypeClass> classes) {
                classes.computeIfAbsent(lhs, k -> new TypeClass(lhs));
                classes.computeIfAbsent(rhs, k -> new TypeClass(rhs));
                TypeClass tLhs = classes.get(lhs);
                TypeClass tRhs = classes.get(rhs);
                tLhs.members.addAll(tRhs.members);
                tLhs.members.addAll(tRhs.rawConstraints);
                tLhs.isBoolean |= tRhs.isBoolean;
                classes.put(rhs, tLhs);
            }

            EqConstraint(String lhs, String rhs) {
                this.lhs = lhs;
                this.rhs = rhs;
            }

            @Override
            public String toString() {
                return String.format("EqConstraint { %s == %s }", lhs, rhs);
            }
        }
    }

    static abstract class Instruction {
        abstract void generateCode(ImperativeFormProto proto, StringBuilder sb);
        static class MemSwap extends Instruction {
            String a, b;
            MemSwap(String a, String b) {
                this.a = a;
                this.b = b;
            }
            @Override public String toString() {
                return String.format("MemSwap { \"%s\", \"%s\" }", a, b );
            }
            @Override public void generateCode(ImperativeFormProto proto, StringBuilder sb) {
                sb.append("MemSwap(\"");
                sb.append(a);
                sb.append("\", \"");
                sb.append(b);
                sb.append("\")");
            }
        }
        static class Check extends Instruction {
            RsTerm guard;
            Check(RsTerm guard) {
                this.guard = guard;
            }
            @Override public String toString() {
                return String.format("Check(%s)", guard );
            }
            @Override public void generateCode(ImperativeFormProto proto, StringBuilder sb) {
                sb.append("Check(");
                guard.generateCode(sb);
                sb.append(")");
            }
        }
        static class CreateFromFunc extends Instruction {
            String resDest, func;
            List<String> resArgs;
            CreateFromFunc(String resDest, String func, List<String> resArgs) {
                this.resDest = resDest;
                this.func = func;
                this.resArgs = resArgs;
            }
            @Override public String toString() {
                return String.format("CreateFromFunc { resDest: %s, func: %s, args: resArgs }",
                        resDest, func, resArgs );
            }
            @Override public void generateCode(ImperativeFormProto proto, StringBuilder sb) {
                sb.append("CreateFromCall { dest: \"");
                sb.append(resDest);
                sb.append("\", func: \"");
                sb.append(func);
                sb.append("\", info: TypeInfo::of::<T");
                int i = proto.finishedInfo.uniqueClasses.indexOf(proto.finishedInfo.typeClasses.get(resDest));
                sb.append(String.valueOf(i));
                sb.append(">(), args: vec![");
                for (String arg: resArgs) { // TODO TERMS not RESOURCES here
                    sb.append("Named(\"");
                    sb.append(arg);
                    sb.append("\"), ");
                }
                sb.append("]}");
            }
        }
        static class CreateFromFormula extends Instruction {
            String resDest;
            RsTerm term;
            CreateFromFormula(String resDest, RsTerm term) {
                this.resDest = resDest;
                this.term = term;
            }
            @Override public void generateCode(ImperativeFormProto proto, StringBuilder sb) {
                sb.append("CreatFromFormula { dest: ");
                sb.append(resDest);
                sb.append(", term: ");
                sb.append(term.toString());
                sb.append(" }");
            }
        }
    }
    private static class MovementDest {
        Set<String> getters;
        boolean retains;
        MovementDest(boolean retains) {
            this.retains = retains;
            getters = new HashSet<>();
        }
        @Override public String toString() {
            return String.format("MovementDest { retains: %b, %s }", retains, getters );
        }
    }
    static class Predicate {
        Set<String> ready = new HashSet<>();
        Set<String> fullMem = new HashSet<>();
        Set<String> emptyMem = new HashSet<>();
        @Override public String toString() {
            return String.format("Predicate { %s, %s, %s }", ready, fullMem, emptyMem);
        }
    }
    static class RuleDef {
        Predicate predicate = new Predicate();
        List<Instruction> ins = new Vector<>();
        Map<String, MovementDest> movements = new HashMap<>();
        @Override public String toString() {
            return String.format("RuleDef { %s, %s, %s }", predicate, ins, movements);
        }
    }
    private static class TypeClass {
        Set<String> members;
        Set<String> rawConstraints;
        boolean isBoolean = false;
        TypeClass(String firstMember) {
            members = new HashSet<>();
            members.add(firstMember);
            rawConstraints = new HashSet<>();
        }
        @Override public String toString() {
            return String.format("TypeClass { members: %s, rawConstraints: %s }", members, rawConstraints );
        }
    }
    static class FuncTypeSig {
        String retTypeOf;
        List<String> argTypeOf;
        FuncTypeSig(String retTypeOf, List<String> argTypeOf) {
            this.retTypeOf = retTypeOf;
            this.argTypeOf = argTypeOf;
        }
    }
    static class NameDefs {
        Map<String, Boolean> ports = new HashMap<>();
        Map<String, Object> memWithInitial = new HashMap<>();
        Set<String> constants = new HashSet<>();
        Map<String, FuncTypeSig> funcTypeSigs = new HashMap<>();
        boolean constTrueUsed = false;
        boolean constFalseUsed = true;
    }
    class FinishedInfo {
        Map<String, TypeClass> typeClasses;
        List<TypeClass> uniqueClasses;
    }

    ////////////////////////////////

    List<TypeConstraint> typeConstraints = new Vector<>();
    List<RuleDef> ruleDefs = new Vector<>();
    NameDefs nameDefs = new NameDefs();
    int nextTempIndex = 0;
    FinishedInfo finishedInfo;
    String protoName;

    int nextTempPlease() {
        nextTempIndex += 1;
        return nextTempIndex - 1;
    }

    public ImperativeFormProto(Protocol proto) {
        // define all port names and memory cells
        protoName = proto.getName().toLowerCase();
        for (Port x : proto.getPorts()) {
            String name = x.getName();
            typeConstraints.add(new TypeConstraint.ExistsConstraint(name));
            nameDefs.ports.put(name, x.isInput());
        }
        for (Map.Entry<MemoryVariable, Term> e : proto.getInitial().entrySet()) {
            String name = e.getKey().getName();
            typeConstraints.add(new TypeConstraint.ExistsConstraint(name));
            nameDefs.memWithInitial.put(name, e.getValue());
            // TODO add from initial
        }

        int index = 0;
        for (Transition t : proto.getTransitions()) {
            addRule(index, t);
            index++;
        }
        finishedInfo = new FinishedInfo();
        finishedInfo.typeClasses = new HashMap<>();
        for (TypeConstraint tc: typeConstraints) {
            tc.applyTo(finishedInfo.typeClasses);
        }
        finishedInfo.uniqueClasses = new Vector<>(
            new HashSet<>(finishedInfo.typeClasses.values())
        );

        // System.out.printf("nameDefs: %s%n%n", nameDefs);
        // System.out.printf("typeConstraints: %s%n%n", typeConstraints);
        // System.out.printf("uniqueClasses: %s%n%n", finishedInfo.uniqueClasses);
    }

    private void addRule(int index, Transition t) {
        // 1. build the predicate
        RuleDef rd = new RuleDef();
        new MemReadTraverser(t.getGuard(), rd.predicate.fullMem);
        for (Port p : t.getInput()) {
            String name = p.getName();
            rd.predicate.ready.add(name);
            rd.movements.put(name, new MovementDest(false));
        }
        for (Map.Entry<PortVariable, Term> e : t.getOutput().entrySet()) {
            String name = e.getKey().getName();
            rd.predicate.ready.add(name);
            new MemReadTraverser(e.getValue(), rd.predicate.fullMem);
        }
        Set<String> memAssignedNull = new HashSet<>();
        for (Map.Entry<MemoryVariable, Term> e : t.getMemory().entrySet()) {
            String name = e.getKey().getName();
            typeConstraints.add(new TypeConstraint.ExistsConstraint(name));
            if (e.getValue() instanceof NullValue) {
                memAssignedNull.add(name);
                rd.predicate.fullMem.add(name); // TODO reconsider. must it be full?
            } else {
                rd.predicate.ready.add(name);
                rd.predicate.emptyMem.add(name);
                new MemReadTraverser(e.getValue(), rd.predicate.fullMem);
            }
        }

        // now predicate.fullMem and predicate.emptyMem may overlap!
        Set<String> rwMemory = new HashSet<>(rd.predicate.fullMem); // copy constructor
        rwMemory.retainAll(rd.predicate.emptyMem);
        rd.predicate.emptyMem.removeAll(rwMemory);

        for (String name : rd.predicate.fullMem) {
            rd.movements.put(name, new MovementDest(!memAssignedNull.contains(name)));
        }

        // now add all resource creations
        for (Map.Entry<PortVariable, Term> e : t.getOutput().entrySet()) {
            RsTerm rt = new RsTermBuilder(this, rd.predicate, e.getValue()).root;
            String resource = rt.newResource(this, rd);
            rd.movements.computeIfAbsent(resource, x -> new MovementDest(false));
            rd.movements.get(resource).getters.add(e.getKey().getName());
        }

        for(String s : rwMemory) {
            String name = s + "_Next";
            typeConstraints.add(new TypeConstraint.ExistsConstraint(name));
            rd.ins.add(new Instruction.MemSwap(s, name));
        }
        for (Map.Entry<MemoryVariable, Term> e : t.getMemory().entrySet()) {
            String name = e.getKey().getName();
            if (memAssignedNull.contains(name)) {
                continue; // TODO
            }
            RsTerm rt = new RsTermBuilder(this, rd.predicate, e.getValue()).root;
            String resource = rt.newResource(this, rd);
            rd.movements.computeIfAbsent(resource, x -> new MovementDest(false));
            if (rwMemory.contains(name)) {
                rd.movements.get(resource).getters.add(name + "_NEXT");
            } else {
                rd.movements.get(resource).getters.add(name);
            }
        }

        for (Map.Entry<String, MovementDest> e : rd.movements.entrySet()) {
            String putter = e.getKey();
            MovementDest md = e.getValue();
            for (String getter : md.getters) {
                typeConstraints.add(new TypeConstraint.EqConstraint(putter, getter));
            }
            int allowedGettersWithoutClone;
            if (md.retains) {
                allowedGettersWithoutClone = 0;
            } else {
                allowedGettersWithoutClone = 1;
            }
            if (md.getters.size() > allowedGettersWithoutClone) {
                typeConstraints.add(new TypeConstraint.TraitConstraint(putter, "Clone"));
            }
        }
        RsTerm guardBefore = new RsTermBuilder(this, rd.predicate, t.getGuard()).root;
        StringBuilder sb = new StringBuilder();
        guardBefore.generateCode(sb);
        sb.append("\n\n\n");
        RsTerm guard = guardBefore.simplify();
        guard.generateCode(sb);

        // System.out.printf(":: %s%n", sb.toString());
        if (!(guard instanceof RsTerm.RsTrue)) {
            rd.ins.add(new Instruction.Check(guard));
        }
        // System.out.printf("RD %d %s%n", index, rd);
        ruleDefs.add(rd);
    }


    public void generateCode(StringBuilder sb) {
        // C API builder
        sb.append("#[no_mangle]\n");
        sb.append("pub extern fn reors_generated_proto_create() -> CProtoHandle {\n");
        sb.append("    reo_rs::to_c_proto(proto_");
        sb.append(protoName);
        sb.append("_build_rust::<");
        for (int i = 0; i < finishedInfo.uniqueClasses.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("isize");
        }
        sb.append(">())\n");
        sb.append("}\n");
        sb.append("\n");

        // Rust API builder
        sb.append("pub fn proto_");
        sb.append(protoName);
        sb.append("_build_rust");
        sb.append("<");
        for (int i = 0; i < finishedInfo.uniqueClasses.size(); i++) {
            TypeClass tc = finishedInfo.uniqueClasses.get(i);
            if (i > 0) sb.append(", ");
            sb.append("T");
            sb.append(String.valueOf(i));
        }
        sb.append(">(\n");
        for (Map.Entry<String, FuncTypeSig> e: nameDefs.funcTypeSigs.entrySet()) {
            sb.append("    ");
            sb.append(e.getKey());
            sb.append(": fn(Outputter<T");
            FuncTypeSig fts = e.getValue();
            int destTypeIndex = finishedInfo.uniqueClasses.indexOf(finishedInfo.typeClasses.get(fts.retTypeOf));
            sb.append(String.valueOf(destTypeIndex));
            sb.append(">");
            for (String arg : fts.argTypeOf) {
                // System.out.println("" + arg);
                TypeClass tc = finishedInfo.typeClasses.get(arg);
                // System.out.println("" + tc);
                int argTypeIndex = finishedInfo.uniqueClasses.indexOf(tc);
                // System.out.println("... " + argTypeIndex);
                sb.append(", &T");
                sb.append(String.valueOf(argTypeIndex));
            }
            sb.append(") -> OutputToken<T");
            sb.append(String.valueOf(destTypeIndex));
            sb.append(">,\n");
        }
        sb.append(") -> ProtoHandle\n");
        sb.append("where\n");
        for (int i = 0; i < finishedInfo.uniqueClasses.size(); i++) {
            TypeClass tc = finishedInfo.uniqueClasses.get(i);
            sb.append("    T");
            sb.append(String.valueOf(i));
            sb.append(": ");
            sb.append("'static + Send + Sync + Sized");
            for (String c: tc.rawConstraints) {
                sb.append(" + ");
                sb.append(c);
            }
            sb.append(",\n");
        }
        sb.append("{\n");
        sb.append("    let name_defs = hashmap!{\n");
        for (Map.Entry<String, Boolean> e : nameDefs.ports.entrySet()) {
            sb.append("        \"");
            sb.append(e.getKey());
            sb.append("\" => ");
            sb.append("Port { is_putter: ");
            if (e.getValue()) {
                sb.append(" true");
            } else {
                sb.append("false");
            }
            sb.append(", type_info: TypeInfo::of::<T");
            TypeClass tc = finishedInfo.typeClasses.get(e.getKey());
            int i = finishedInfo.uniqueClasses.indexOf(tc);
            sb.append(String.valueOf(i));
            sb.append(">() },\n");
        }
        for (Map.Entry<String, Object> e : nameDefs.memWithInitial.entrySet()) {
            sb.append("        \"");
            sb.append(e.getKey());
            sb.append("\" => Mem(TypeInfo::of::<T");
            TypeClass tc = finishedInfo.typeClasses.get(e.getKey());
            int i = finishedInfo.uniqueClasses.indexOf(tc);
            sb.append(String.valueOf(i));
            sb.append(">()),\n");
        }
        for (String c: nameDefs.constants) {
            sb.append("        \"");
            sb.append(c);
            sb.append("\" => Mem(TypeInfo::of::<T");
            TypeClass tc = finishedInfo.typeClasses.get(c);
            int i = finishedInfo.uniqueClasses.indexOf(tc);
            sb.append(String.valueOf(i));
            sb.append(">()),\n");
        }
        if (nameDefs.constTrueUsed) {
            sb.append("        \"CONST_BOOL_TRUE\" => Mem(TypeInfo::of::<bool>),\n");
        }
        if (nameDefs.constTrueUsed) {
            sb.append("        \"CONST_BOOL_FALSE\" => Mem(TypeInfo::of::<bool>),\n");
        }
        for (Map.Entry<String, FuncTypeSig> e : nameDefs.funcTypeSigs.entrySet()) {
            sb.append("        \"");
            sb.append(e.getKey());
            sb.append("\" => Func(CallHandle::new_args");
            sb.append(String.valueOf(e.getValue().argTypeOf.size()));
            sb.append("(");
            sb.append(e.getKey());
            sb.append(")),\n");
        }
        // end of name def
        sb.append("    };\n");
        sb.append("    let rules = vec![\n");
        for (RuleDef r : ruleDefs) {
            sb.append("        RuleDef {\n");
            sb.append("            state_guard: StatePredicate {\n");
            sb.append("                ready_ports: hashset! {");
            for (String s: r.predicate.ready) {
                sb.append("\"");
                sb.append(s);
                sb.append("\", ");
            }
            sb.append("},\n");
            sb.append("                full_mem: hashset! {");
            for (String s: r.predicate.fullMem) {
                sb.append("\"");
                sb.append(s);
                sb.append("\", ");
            }
            sb.append("},\n");
            sb.append("                empty_mem: hashset! {");
            for (String s: r.predicate.emptyMem) {
                sb.append("\"");
                sb.append(s);
                sb.append("\", ");
            }
            sb.append("},\n");
            sb.append("            },\n"); // end predicate
            sb.append("            ins: vec![\n");
            for (Instruction i: r.ins) {
                sb.append("                ");
                i.generateCode(this, sb);
                sb.append(",\n");
            }
            sb.append("            ],\n");
            sb.append("            output: hashmap!{\n");
            for (Map.Entry<String, MovementDest> e : r.movements.entrySet()) {
                sb.append("                \"");
                sb.append(e.getKey());
                sb.append("\" => (");
                MovementDest md = e.getValue();
                if (md.retains) {
                    sb.append(" true");
                } else {
                    sb.append("false");
                }
                sb.append(", hashset!{");
                for (String getter: md.getters) {
                    sb.append("\"");
                    sb.append(getter);
                    sb.append("\", ");
                }
                sb.append("}),\n");
            }
            sb.append("            }\n");
            sb.append("        },\n"); // end rule
        }
        sb.append("    ];\n");
        sb.append("    let mem_init = MemInitial::default()");
        for (Map.Entry<String, Object> e : nameDefs.memWithInitial.entrySet()) {
            Object v = e.getValue();
            if (v == null) continue;
            String angleBracketPrefix, val;
            if (v instanceof BooleanValue) {
                angleBracketPrefix = "";
                BooleanValue v2 = (BooleanValue) v;
                if (v2.getValue()) {
                    val = "true";
                } else {
                    val = "false";
                }
            } else {
                angleBracketPrefix = "<T" + typeIndexOf(e.getKey()) + ">";
                val = "\"" + e.getValue() + "\".into()";
            }
            sb.append(String.format("%n        .with%s(\"%s\", %s)",
                    angleBracketPrefix, e.getKey(), val));
        }
        if (nameDefs.constTrueUsed) {
            sb.append("\n        .with(\"CONST_BOOL_TRUE\", true)");
        }
        if (nameDefs.constTrueUsed) {
            sb.append("\n        .with(\"CONST_BOOL_FALSE\", false)");
        }
        sb.append(";\n    ProtoDef {\n        name_defs,\n        rules\n    }.build(mem_init).expect(\"Oh no! Reo failed to build!\")\n}\n");
    }

    private int typeIndexOf(String s) {
        return finishedInfo.uniqueClasses.indexOf(
            finishedInfo.typeClasses.get(s)
        );
    }
}
