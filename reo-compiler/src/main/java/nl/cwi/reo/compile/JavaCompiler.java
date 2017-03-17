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
}
