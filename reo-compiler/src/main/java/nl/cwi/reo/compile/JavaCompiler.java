package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.compile.components.Protocol;
import nl.cwi.reo.compile.components.Component;
import nl.cwi.reo.compile.components.ExtraReoTemplate;
import nl.cwi.reo.compile.components.ReoTemplate;
import nl.cwi.reo.compile.components.Transition;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.semantics.AutomatonSemantics;
import nl.cwi.reo.semantics.symbolicautomata.Conjunction;
import nl.cwi.reo.semantics.symbolicautomata.Disjunction;
import nl.cwi.reo.semantics.symbolicautomata.Existential;
import nl.cwi.reo.semantics.symbolicautomata.Formula;
import nl.cwi.reo.semantics.symbolicautomata.MemoryCell;
import nl.cwi.reo.semantics.symbolicautomata.Node;
import nl.cwi.reo.semantics.symbolicautomata.Term;
import nl.cwi.reo.semantics.symbolicautomata.Variable;

public class JavaCompiler {

	public static <T extends AutomatonSemantics<T>> ReoTemplate compile(ReoProgram<T> program, String packagename,
			T nodeFactory) {

		if (program == null)
			return null;

		ReoConnector<T> connector = program.getConnector().flatten().insertNodes(true, true, nodeFactory).integrate();

		List<Port> ports = new ArrayList<Port>();
		List<Component> components = new ArrayList<Component>();

		int c = 0;
		int i = 0;
		for (ReoConnectorAtom<T> atom : connector.getAtoms()) {
			ports.addAll(atom.getInterface());
			Map<Integer, Set<Transition>> out = new HashMap<Integer, Set<Transition>>();
			Integer initial = 0;
			Component defn = new Protocol("Component" + c++, atom.getInterface(), out, initial);
		}

		List<ReoConnectorComposite<T>> partitionMedium = new ArrayList<ReoConnectorComposite<T>>();
		List<ReoConnector<T>> partitionBig = new ArrayList<ReoConnector<T>>();
		List<List<ReoConnectorAtom<T>>> partition = new ArrayList<List<ReoConnectorAtom<T>>>();

		/*
		 * Partitioning
		 */

		for (ReoConnectorAtom<T> atoms : connector.getAtoms()) {
			if (atoms.getSourceCode().getFile() == null) {
				partition = partitionConnector(partition, atoms);
			} else {
				partitionBig.add(atoms);
			}
		}

		for (List<ReoConnectorAtom<T>> listAtoms : partition)
			partitionMedium.add(new ReoConnectorComposite("", listAtoms));

		for (ReoConnectorComposite<T> composite : partitionMedium) {
			partitionBig.add(getProduct(composite));
		}

		return new ReoTemplate(program.getFile(), packagename, program.getName(), components);
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
		return new ReoConnectorComposite<T>("", Arrays.asList(productAutomaton));
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
		
		Formula g = f.QE(q);
		
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
		
		
		return 	new Transition(g,ports,mems,inputs);
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
	
	public static Formula compose(List<Formula> list){
		Formula dnf=new Conjunction(list);
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
		setVariable.addAll(transition.getInput());
		
		return setVariable;
	}
	
	public static void generateCode(Formula automaton){
		Set<Transition> transitions = new HashSet<Transition>();
		Map<Integer,Set<Transition>> compTransition = new HashMap<Integer,Set<Transition>>();
		Set<Port> set = new HashSet<Port>();
		
		if(automaton instanceof Disjunction)
			for(Formula f : ((Disjunction) automaton).getClauses()){
				Transition transition = JavaCompiler.commandify(f);
				transitions.add(transition);
				for(Node n : transition.getInput() )
					set.add(n.getPort());
				for(Node n : transition.getOutput().keySet()){
					set.add(n.getPort());
				}
			}
		compTransition.put(new Integer(0), transitions); 
		
		Protocol p = new Protocol("protocol",set,  compTransition, new Integer(0));
		
		ReoTemplate reo = new ReoTemplate("testfile","packagetest", "main", Arrays.asList(p));
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
