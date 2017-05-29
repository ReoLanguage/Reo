package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemCell;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.util.Monitor;

public class ConstraintHypergraph implements Semantics<ConstraintHypergraph> {

	private final Set<HyperEdge> hyperedges;

	private final Map<Term,Term> initial;
	
	/**
	 * Constructs an automaton, with an empty set of rules.
	 */
	public ConstraintHypergraph() {
		this.hyperedges = new HashSet<HyperEdge>();
		initial = new HashMap<Term,Term>();
	}

	/**
	 * Constructs a new automaton from a given set of rules.
	 * 
	 * @param f
	 *            formula
	 */
	public ConstraintHypergraph(Set<Rule> s) {
		hyperedges = new HashSet<HyperEdge>();

		for(Rule r : s){	
			RuleNode rule = new RuleNode(r);
			for(Port v : r.getSync().keySet()){
				if(r.getSync().get(v)){
					if(!getHyperedges(v).isEmpty()){
						rule.addToHyperedge(getHyperedges(v).get(0));
					}
					else{
						Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
						ruleNodes.add(rule);
						hyperedges.add(new HyperEdge(new PortNode(v),ruleNodes));
					}
				}
			}
			
		}
		this.initial = new HashMap<Term,Term>();
	}

	/**
	 * Constructs a new automaton from a given set of rules and initial values.
	 * 
	 * @param f
	 *            formula
	 */
	public ConstraintHypergraph(List<HyperEdge> h, Map<Term,Term> initial) {
		this.hyperedges=new HashSet<HyperEdge>(h);
		this.initial = initial;
	}
	
	/**
	 * Constructs a new automaton from a given set of rules and initial values.
	 * 
	 * @param f
	 *            formula
	 */
	public ConstraintHypergraph(Set<Rule> s, Map<Term,Term> initial) {
		hyperedges = new HashSet<HyperEdge>();
		for(Rule r : s){	
			RuleNode rule = new RuleNode(r);
			for(Port v : r.getSync().keySet()){
				if(r.getSync().get(v)){
					if(!getHyperedges(v).isEmpty()){
						rule.addToHyperedge(getHyperedges(v).get(0));
					}
					else{
						Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
						ruleNodes.add(rule);
						hyperedges.add(new HyperEdge(new PortNode(v),ruleNodes));
					}
				}
			}
			
		}
		this.initial = initial;
	}
	
	public List<HyperEdge> getHyperedges(Port p){
		List<HyperEdge> hyperedgeList = new ArrayList<HyperEdge>();
		for(HyperEdge h : hyperedges){
			if(h.getRoot().getPort().equals(p)){
				hyperedgeList.add(h);
			}
		}
		return hyperedgeList;
	}
	
	public Set<HyperEdge> getHyperedges(){
		return hyperedges;
	}
	
	public Set<PortNode> getVariables(){
		Set<PortNode> s = new HashSet<>();
		for(HyperEdge h : hyperedges){
			s.add(h.getRoot());
		}
		return s;
	}
	
	

	/**
	 * Get rules for commandification
	 * @return
	 */
	public Set<Rule> getRules(){
		Set<Rule> s = new HashSet<>();	

		for(HyperEdge g : hyperedges){
			for(RuleNode r : g.getLeaves()){
				s.add(r.getRule());
			}
		}
				
		return s;
	}
	
	/**
	 * Get rules for commandification
	 * @return
	 */
	public Set<RuleNode> getRuleNodes(){
		Set<RuleNode> s = new HashSet<>();	

		for(HyperEdge g : hyperedges){
			s.addAll(g.getLeaves());
		}
				
		return s;
	}
	
	public Map<Term,Term> getInitials(){
		return initial;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable ConstraintHypergraph evaluate(Scope s, Monitor m) {
		for (RuleNode r : getRuleNodes()) {
			r.evaluate(s,m);
		}

		for(Term t : initial.keySet()){
			if(s.get(new Identifier(initial.get(t).toString()))!=null){
				Value v = s.get(new Identifier(initial.get(t).toString()));
				if(v instanceof StringValue)
					initial.put(t,new Function("constant",((StringValue) v).getValue(),new ArrayList<Term>()));
				if(v instanceof BooleanValue)
					initial.put(t,new Function("constant",((BooleanValue) v).getValue(),new ArrayList<Term>()));
				if(v instanceof IntegerValue)
					initial.put(t,new Function("constant",((IntegerValue) v).getValue(),new ArrayList<Term>()));
				if(v instanceof DecimalValue)
					initial.put(t,new Function("constant",((DecimalValue) v).getValue(),new ArrayList<Term>()));
			}
		}
 		return new ConstraintHypergraph(getRules(),initial);
 		//If a new constraint hypergraph is not created, all instances of the same component definition will have shared rules and shared hyperedges. 
// 		return new ConstraintHypergraph(new ArrayList<>(hyperedges),initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> p = new HashSet<Port>();
		for (Rule r : getRules()) {
			p.addAll(r.getFiringPorts());
		}
		return p;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.RBA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph getNode(Set<Port> node) {

		Set<Port> ports = new HashSet<Port>(node);

		Set<Port> inps = new HashSet<Port>();
		Set<Port> outs = new HashSet<Port>();

		for (Port p : ports) {
			if (p.getType() == PortType.IN) {
				outs.add(new Port(p.getName(), (p.isInput() ? PortType.OUT : PortType.IN), p.getPrioType(),
						p.getTypeTag(), true));
			} else {
				inps.add(new Port(p.getName(), (p.isInput() ? PortType.OUT : PortType.IN), p.getPrioType(),
						p.getTypeTag(), false));
			}
		}

		Set<Rule> rules = new HashSet<Rule>();
		/*
		 * Instantiate merger/relicator
		 */
		for (Port p : inps) {
			Formula transition = null;
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : inps) {
				if (!x.equals(p)) {
					map.put(x, false);
				}
			}
			for (Port x : outs) {
				map.put(x, true);
				Formula eq = new Equality(new Node(p), new Node(x));
				if (transition == null)
					transition = eq;
				else
					transition = new Conjunction(Arrays.asList(transition, eq));
			}
			rules.add(new Rule(map, transition));
		}

		return new ConstraintHypergraph(rules,initial);
	}

	public ConstraintHypergraph getDefault(Set<Port> ports) {

		Set<Rule> rules = new HashSet<Rule>();

		for (Port p : ports) {
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : ports)
				if (!x.equals(p))
					map.put(x, false);
			Formula guard = new Relation("true", "true", null);
			rules.add(new Rule(map, guard));
		}

		return new ConstraintHypergraph(rules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph rename(Map<Port, Port> links) {
		for (RuleNode r : getRuleNodes()) {
			r.rename(links);
		}
		for(PortNode p : getVariables()){
			p.rename(links);
		}
		return new ConstraintHypergraph(new ArrayList<>(hyperedges),initial);
	}


	@Override
	public ConstraintHypergraph compose(List<ConstraintHypergraph> components) {

		// Rename all memory cells and put *all* components into a list.
		List<ConstraintHypergraph> list = new ArrayList<>(components);
		int i = 1;
		for (ConstraintHypergraph A : list) {
			Map<String, String> rename = new HashMap<>();
			for (Rule r : A.getRules()) {
				for (Variable v : r.getFormula().getFreeVariables()) {
					if (v instanceof MemCell) {
						String name = ((MemCell) v).getName();
						if (!rename.containsKey(name))
							rename.put(name, "m" + i++);
					}
				}
			}
			for (RuleNode r : A.getRuleNodes()) {
				r.substitute(rename);
			}
			Map<Term,Term> init = new HashMap<>(A.getInitials());
			for(Term t : init.keySet()){
				if(t instanceof MemCell){
					A.getInitials().put(new MemCell(rename.get(((MemCell) t).getName()),((MemCell) t).hasPrime()), A.getInitials().get(t));
				}
			}
		}

		// Compose the list of RBAs into a single list of rules.

//		Map<Term,Term> initialValue = new HashMap<Term,Term>();

		ConstraintHypergraph composedAutomaton = list.get(0);
		list.remove(0);
		for(ConstraintHypergraph h : list){
			if(!h.getRules().isEmpty()){
				composedAutomaton.getHyperedges().addAll(h.getHyperedges());
				composedAutomaton.getInitials().putAll(h.getInitials());
			}

		}
		composedAutomaton.distributeSingleEdge();
		composedAutomaton.distributeMultiEdge();
		
		return composedAutomaton;
//		return new ConstraintHypergraph(composedAutomaton.getRules(),initialValue);

	}
	
	
	/**
	 * Distribute single hyperedges
	 * @return
	 */
	public void distributeSingleEdge(){
		Set<Port> variables = new HashSet<>();
		for(HyperEdge h : hyperedges){
			variables.add(h.getRoot().getPort());
		}
		
		for(Port p : variables){
			List<HyperEdge> singleEdge = new ArrayList<>();
			List<HyperEdge> multiEdge = new ArrayList<>();
			
			for(HyperEdge h : getHyperedges(p)){
				if(h.getLeaves().size()==1)
					singleEdge.add(h);
				else
					multiEdge.add(h);
			}
			if(singleEdge.size()>1){
				HyperEdge e = singleEdge.get(0);
				singleEdge.remove(0);
				for(HyperEdge h : singleEdge){
					e.compose(h);
				}
				hyperedges.removeAll(singleEdge);
				singleEdge.clear();
				singleEdge.add(e);
				removeEmptyHyperedge();
			}
			if(!multiEdge.isEmpty()){
				HyperEdge e = multiEdge.get(0);
				for(HyperEdge h : singleEdge){
					e.compose(h);

				}
				if(!multiEdge.isEmpty()){
					hyperedges.removeAll(singleEdge);
				}
				removeEmptyHyperedge();
			}	
		}	
	}
	
	/**
	 * Distribute multi rules hyperedges
	 * @return
	 */
	public void distributeMultiEdge(){
		Set<Port> variables = new HashSet<>();
		for(HyperEdge h : hyperedges){
			variables.add(h.getRoot().getPort());
		}
		
		for(Port p : variables){
			List<HyperEdge> multiEdge = new ArrayList<>();
		
			multiEdge.addAll(getHyperedges(p));
			HyperEdge toDistribute = multiEdge.get(0);
			multiEdge.remove(0);
			
			for(HyperEdge h : multiEdge){
				toDistribute = h.compose(toDistribute);
			}
			removeEmptyHyperedge();
		}
	}
	
	public void removeEmptyHyperedge(){
		Set<HyperEdge> s = new HashSet<>();
		Queue<HyperEdge> q = new LinkedList<>(hyperedges);
		while(!q.isEmpty()){
			HyperEdge e = q.poll();
			if(e.getLeaves().size()==0){
				e.getRoot().rmHyperedge(e);
			}
			else
				s.add(e);
		}
		hyperedges.clear();
		hyperedges.addAll(s);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
//	@Override
//	public String toString() {
//		ST st = new ST("<hyperedges; separator=\"\n\">");
//		st.add("hyperedges", getHyperedges());
//		return st.render();
//	}
	
	public String toString(){
			Set<Port> variables = new HashSet<>();
			for(HyperEdge h : hyperedges){
				variables.add(h.getRoot().getPort());
			}
			String s = "";
			for(Port var : variables){
				s = s+ "Root : "+var.toString()+"\n{";
				int i=getHyperedges(var).size();
				for(HyperEdge h : getHyperedges(var)){
					s=s+h.toString()+(i>1?" && \n":"");
					i--;
				}
				s=s+"}\n \n";
			}
			return s;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph restrict(Collection<? extends Port> intface) {
		Set<Rule> setRules = new HashSet<Rule>();
		for (Rule r : getRules()) {
			Formula g = r.getFormula();
			for (Port p : r.getFiringPorts())
				if (!intface.contains(p))
					g = new Existential(new Node(p), g);
			setRules.add(new Rule(r.getSync(), g));
		}
		return new ConstraintHypergraph(setRules,initial);
	}

}
