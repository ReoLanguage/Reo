package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.compile.components.Automaton;
import nl.cwi.reo.compile.components.Behavior;
import nl.cwi.reo.compile.components.Transition;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;

public class JavaCompiler {

	public static void compile() {

		STGroup group = new STGroupFile("Java.stg");
		ST temp = group.getInstanceOf("component");

		Port a = new Port("a", PortType.IN, PrioType.NONE, new TypeTag("Integer"), true);
		Port b = new Port("b", PortType.OUT, PrioType.NONE, new TypeTag("Boolean"), true);
		Port c = new Port("c", PortType.IN, PrioType.NONE, new TypeTag("Double"), true);

		SortedSet<Port> N = new TreeSet<Port>();

		N.add(a);
		N.add(b);
		N.add(c);

		Map<String, String> ac = new HashMap<String, String>();
		ac.put("b", "d_a");
		ac.put("d", "m");

		Transition t = new Transition("q0", "q1", N, ac);

		List<Port> P = new ArrayList<Port>();
		P.add(a);
		P.add(b);
		P.add(c);

		Map<String, Set<Transition>> out = new HashMap<String, Set<Transition>>();
		out.put("q0", new HashSet<Transition>(Arrays.asList(t)));

		Automaton A = new Automaton("MyAutomaton", P, Behavior.PROACTIVE, "main.treo", out, "q0",
				"nl.cwi.reo.runtime.java");

		temp.add("A", A);

		System.out.println(temp.render());
		// outputClass(name, st.render());
	}

	// /**
	// * Produces java source file containing the game graph;
	// * @param name name of the java class
	// * @param code implementation of the java class
	// * @return <code>true</code> if the file is successfully written.
	// */
	// private static boolean outputClass(String name, String code) {
	// try {
	// FileWriter out = new FileWriter(name + ".java");
	// out.write(code);
	// out.close();
	// } catch (IOException e) {
	// return false;
	// }
	// return true;
	// }
}
