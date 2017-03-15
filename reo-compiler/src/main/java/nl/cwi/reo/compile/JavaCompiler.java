package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.compile.components.ActiveAutomaton;
import nl.cwi.reo.compile.components.Definition;
import nl.cwi.reo.compile.components.Instance;
import nl.cwi.reo.compile.components.ReoTemplate;
import nl.cwi.reo.compile.components.Transition;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.AutomatonSemantics;

public class JavaCompiler {

	public static <T extends AutomatonSemantics<T>> ReoTemplate compile(ReoProgram<T> program, String packagename,
			T nodeFactory) {
		
		System.out.println("compiling..");

		if (program == null)
			return null;

		ReoConnector<T> connector = program.getConnector().flatten().insertNodes(true, true, nodeFactory).integrate();

		List<Port> ports = new ArrayList<Port>();
		List<Instance> instances = new ArrayList<Instance>();
		List<Definition> definitions = new ArrayList<Definition>();

		int c = 0;
		int i = 0;
		for (ReoConnectorAtom<T> atom : connector.getAtoms()) {
			List<Port> atom_ports = new ArrayList<Port>(atom.getInterface());
			ports.addAll(atom_ports);
			Map<Integer, Set<Transition>> out = new HashMap<Integer, Set<Transition>>();
			Integer initial = 0;
			Definition defn = new ActiveAutomaton("Component" + c++, atom_ports, out, initial);
			instances.add(new Instance("Instance" + i++, defn, atom_ports));
		}

		return new ReoTemplate(program.getFile(), packagename, program.getName(), ports, instances, definitions);
	}
	//
	// public static void compile1() {
	//
	// STGroup group = new STGroupFile("JavaMain.stg");
	// ST temp = group.getInstanceOf("component");
	//
	// Port a = new Port("a", PortType.IN, PrioType.NONE, new
	// TypeTag("Integer"), true);
	// Port b = new Port("b", PortType.OUT, PrioType.NONE, new
	// TypeTag("Boolean"), true);
	// Port c = new Port("c", PortType.IN, PrioType.NONE, new TypeTag("Double"),
	// true);
	//
	// SortedSet<Port> N = new TreeSet<Port>();
	//
	// N.add(a);
	// N.add(b);
	// N.add(c);
	//
	// Map<String, String> ac = new HashMap<String, String>();
	// ac.put("b", "d_a");
	// ac.put("d", "m");
	//
	// Transition t = new Transition("q0", "q1", N, ac);
	//
	// List<Port> P = new ArrayList<Port>();
	// P.add(a);
	// P.add(b);
	// P.add(c);
	//
	// Map<String, Set<Transition>> out = new HashMap<String,
	// Set<Transition>>();
	// out.put("q0", new HashSet<Transition>(Arrays.asList(t)));
	//
	// ActiveAutomaton A = new ActiveAutomaton("MyAutomaton", P, out, "q0");
	//
	// temp.add("A", A);
	//
	// System.out.println(temp.render());
	// // outputClass(name, st.render());
	// }

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
