package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import nl.cwi.reo.compile.components.ExtraReoTemplate;
import nl.cwi.reo.compile.components.ReoTemplate;
import nl.cwi.reo.compile.components.Transition;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.AutomatonSemantics;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemoryCell;
import nl.cwi.reo.semantics.predicates.Predicate;
import nl.cwi.reo.semantics.predicates.Term;

@Deprecated
public class JavaCompiler {

	public static ReoTemplate compile(ReoProgram<Predicate> program, String packagename, Predicate nodeFactory) {

		// // Separate the protocol from the atoms
		// List<ReoConnectorComposite<Predicate>> partitionMedium = new
		// ArrayList<ReoConnectorComposite<Predicate>>();
		// List<ReoConnector<Predicate>> partitionBig = new
		// ArrayList<ReoConnector<Predicate>>();
		// List<List<ReoConnectorAtom<Predicate>>> partition = new
		// ArrayList<List<ReoConnectorAtom<Predicate>>>();
		//
		// for (ReoConnectorAtom<Predicate> atoms : connector.getAtoms()) {
		// if (atoms.getSourceCode().getFile() == null) {
		// partition = partitionConnector(partition, atoms);
		// } else {
		// partitionBig.add(atoms);
		// }
		// }
		//
		// for (List<ReoConnectorAtom<Predicate>> listAtoms : partition)
		// partitionMedium.add(new ReoConnectorComposite("", listAtoms));
		//
		// for (ReoConnectorComposite<Predicate> composite : partitionMedium) {
		// partitionBig.add(getProduct(composite));
		// }

		return null;
	}

	public static <T extends AutomatonSemantics<T>> ReoConnectorComposite<T> getProduct(
			ReoConnectorComposite<T> reoConnector) {
		ReoConnectorAtom<T> productAutomaton;
		Queue<ReoConnectorAtom<T>> atoms = new LinkedList<ReoConnectorAtom<T>>(reoConnector.getAtoms());
		productAutomaton = atoms.poll();
		while (!atoms.isEmpty()) {
			productAutomaton = new ReoConnectorAtom<T>(
					productAutomaton.getSemantics().compose(Arrays.asList(atoms.poll().getSemantics())));
		}
		return new ReoConnectorComposite<T>(null, "", Arrays.asList(productAutomaton));
	}

	public static <T extends AutomatonSemantics<T>> List<List<ReoConnectorAtom<T>>> partitionConnector(
			List<List<ReoConnectorAtom<T>>> reoConnectors, ReoConnectorAtom<T> reoConnector) {

		List<List<ReoConnectorAtom<T>>> listConnector = new ArrayList<List<ReoConnectorAtom<T>>>();
		boolean contain = false;
		listConnector.add(Arrays.asList(reoConnector));
		for (List<ReoConnectorAtom<T>> connector : reoConnectors) {
			for (ReoConnectorAtom<T> atom : connector) {
				if (reoConnector.getInterface().remove(atom.getInterface())) {
					contain = true;
					break;
				}
			}
			if (contain)
				listConnector.get(0).addAll(connector);
			else
				listConnector.add(connector);
			contain = false;
		}

		return listConnector;
	}

	public static List<Set<Port>> partitionPort(List<Set<Port>> setPorts, Set<Port> s) {
		List<Set<Port>> set = new ArrayList<Set<Port>>();
		boolean contain = false;
		set.add(s);
		for (Set<Port> setPort : setPorts) {
			for (Port p : s) {
				if (setPort.contains(p)) {
					contain = true;
					break;
				}
			}
			if (contain)
				set.get(0).addAll(setPort);
			else
				set.add(setPort);
			contain = false;
		}
		return set;
	}

	// public static List<T> partition(ReoConnector<T> connector){
	// return new ArrayList<ReoConnector<T>>();
	// }

	/**
	 * Computes a transition from a formula that consists of a conjunction of
	 * literals.
	 * 
	 * @param f
	 *            conjunction of literals
	 * @return a transition if the formula is a conjunction of literals, or null
	 *         otherwise.
	 */
	public static Transition commandify(Formula f) {
		Map<Port, Term> map = new HashMap<Port, Term>();

		System.out.println(f);
		map = f.getAssignment();
		Set<Port> s = f.getInterface();
		return null;// new Transition(f.getInterface(),f.getAssignment());
	}

	public static Formula compose(List<Formula> list) {
		Formula dnf = new Conjunction(list);
		return dnf.DNF();
	}

	public static void generateCode(Formula automaton) {
		List<Transition> transitions = new ArrayList<Transition>();
		if (automaton instanceof Disjunction)
			for (Formula f : ((Disjunction) automaton).getClauses())
				transitions.add(JavaCompiler.commandify(f));
		Set<MemoryCell> mem = new HashSet<MemoryCell>();
		for (Transition tr : transitions) {
			// for(Term t :tr.getAction().values()){
			// if(t instanceof MemoryCell)
			// mem.add(((MemoryCell) t));
			// }
		}

		// for(MemoryCell m : mem){
		// for(TransitionRule tr : transitions){
		// for(Port p :tr.getAction().keySet()){
		// if(tr.getAction().get(p).equals(m)){
		// PortType portType = ((m.hasPrime())?PortType.IN:PortType.OUT);
		// tr.getAction().replace(p, new Node(new
		// Port(m.getName(),portType,PrioType.NONE, new
		// TypeTag(m.getType()),true)));
		// }
		// }
		// }
		// }
		//
		Set<Port> s = new HashSet<Port>();
		s.addAll(automaton.getInterface());
		System.out.println(
				new ExtraReoTemplate("testfile", "packagetest", "test", s, transitions, mem).getCode(Language.JAVA));

	}

}
