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

import nl.cwi.reo.compile.components.Protocol;
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
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Predicate;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;

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
		Map<Variable,Term> map = new HashMap<Variable,Term>();
		
		System.out.println(f);
		map = f.getAssignment();
		
		Map<MemoryCell,Term> mems = new HashMap<MemoryCell,Term>();
		Map<Node,Term> ports = new HashMap<Node,Term>();
		Set<Node> inputs = new HashSet<Node>();
		Set<Node> quantifiers = new HashSet<Node>();
		
		for(Variable v : map.keySet()){
			if(v instanceof MemoryCell)
				mems.put((MemoryCell)v,map.get(v));
			if(v instanceof Node && !((Node) v).isInput()){
				ports.put((Node)v, map.get(v));
			}
			if(map.get(v) instanceof Node && ((Node)map.get(v)).isInput())
				inputs.add((Node)map.get(v));
		}
		quantifiers.addAll(inputs);
		quantifiers.addAll(ports.keySet());
		Set<Term> q = new HashSet<Term>();
		q.addAll(quantifiers);
		
		
		Formula g = null; //f.QE(q);
		
		map = substitute(map,q);
		mems.clear();
		ports.clear();
		inputs.clear();
		
		for(Variable v : map.keySet()){
			if(v instanceof MemoryCell)
				mems.put((MemoryCell)v,map.get(v));
			if(v instanceof Node && !((Node) v).isInput()){
				ports.put((Node)v, map.get(v));
			}
			if(map.get(v) instanceof Node && ((Node)map.get(v)).isInput())
				inputs.add((Node)map.get(v));
		}

		Set<Port> inputs1 = new HashSet<Port>();
		for (Node x : inputs)
			inputs1.add(x.getPort());
		
		return 	new Transition(g,ports,mems,inputs1);
	}
	
	public static Map<Variable,Term> substitute(Map<Variable,Term> map, Set<Term> q){
		Map<Variable,Term> mapSubst = new HashMap<Variable,Term>();
		mapSubst.putAll(map);
		for(Variable v : map.keySet()){
			if((v instanceof MemoryCell && !((MemoryCell)v).hasPrime()))
				mapSubst.remove(v);
			if(q.contains(v) ){
				for(Variable var : map.keySet()){
					if(map.get(var).equals(v)){
						Term term = map.get(v);
						mapSubst.remove(v);
						mapSubst.put(var,term);
					}	
				}
			}
		}
		return mapSubst;
	}

	public static Formula compose(List<Formula> list) {
		Formula dnf = new Conjunction(list);
		return dnf.DNF();
	}
	
	public static Map<Set<Variable>,Set<Transition>> partition(List<Transition> transitions){
	
		Map<Set<Variable>,Set<Transition>> map = new HashMap<Set<Variable>,Set<Transition>>();
		
		Queue<Transition> queue = new LinkedList<Transition>(transitions);

		Transition tr = queue.poll();
		map.put(getVariables(tr), new HashSet<Transition>(Arrays.asList(tr)));
		
		while(!queue.isEmpty()){
			tr = queue.poll();
			Set<Variable> v = getVariables(tr);
			for(Set<Variable> s : map.keySet()){
				if(v.retainAll(s)){
					Set<Transition> transitionSet = map.get(s);
					transitionSet.add(tr);
					map.remove(s);
					s.addAll(getVariables(tr));
					map.put(s,transitionSet);
				}
				else{
					map.put(getVariables(tr), new HashSet<Transition>(Arrays.asList(tr)));
				}
			}
		}
		
		
		return map;
	}
	
	public static Set<Variable> getVariables(Transition transition){
		Set<Variable> setVariable = new HashSet<Variable>();
		setVariable.addAll(transition.getMemory().keySet());
		setVariable.addAll(transition.getOutput().keySet());
		for (Port p : transition.getInput())
			setVariable.add(new Node(p));
		
		return setVariable;
	}
	
	public static void generateCode(Formula automaton){
		Set<Transition> transitions = new HashSet<Transition>();
		Set<Port> set = new HashSet<Port>();
		Set<MemoryCell> mem = new HashSet<MemoryCell>();
		
		if(automaton instanceof Disjunction)
			for(Formula f : ((Disjunction) automaton).getClauses()){
				Transition transition = JavaCompiler.commandify(f);
				transitions.add(transition);
				for(Port n : transition.getInput() )
					set.add(n);
				for(Node n : transition.getOutput().keySet()){
					set.add(n.getPort());
				}
				for(MemoryCell m: transition.getMemory().keySet()){
					mem.add(m);
				}
			}
		
		Protocol p = new Protocol("protocol", set, transitions, mem, new HashMap<MemoryCell, Object>());
		
		ReoTemplate reo = new ReoTemplate("testfile","", "test", Arrays.asList(p));
		System.out.println(reo.generateCode(Language.JAVA));

//		Set<MemoryCell> mem = new HashSet<MemoryCell>();
		
//		for(Transition tr : transitions){
//			mem.addAll(tr.getMemory());
//		}
		
//		Map<Set<Variable>,Set<TransitionRule>> list = partition(transitions);
		
//		for(MemoryCell m : mem){
//			for(TransitionRule tr : transitions){
//				for(Port p :tr.getAction().keySet()){
//					if(tr.getAction().get(p).equals(m)){
//						PortType portType = ((m.hasPrime())?PortType.IN:PortType.OUT);
//						tr.getAction().replace(p, new Node(new Port(m.getName(),portType,PrioType.NONE, new TypeTag(m.getType()),true)));
//					}
//				}
//			}
//		}
//		
//		Set<Port> s = new HashSet<Port>();
//		s.addAll(automaton.getInterface());
//		System.out.println(new ExtraReoTemplate("testfile", "packagetest", "test",s, transitions,mem).getCode(Language.JAVA));
		
	}

}
