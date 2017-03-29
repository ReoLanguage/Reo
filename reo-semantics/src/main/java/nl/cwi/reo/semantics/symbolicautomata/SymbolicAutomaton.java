package nl.cwi.reo.semantics.symbolicautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.util.Monitor;

public class SymbolicAutomaton implements Semantics<SymbolicAutomaton> {
	
	private final Formula f;
	
	public SymbolicAutomaton(Formula f) {
		this.f = f;
	}

	@Override
	public @Nullable SymbolicAutomaton evaluate(Scope s, Monitor m) {
		// TODO In the future, we need to use this function for formulas that
		// contain parameters, such as a function F on terms.
		return this;
	}

	@Override
	public Set<Port> getInterface() {
		return f.getInterface();
	}

	@Override
	public SemanticsType getType() {
		return SemanticsType.SBA;
	}

	@Override
	public SymbolicAutomaton getNode(Set<Port> node) {
//		Set<Port> ports = new HashSet<Port>(node);
//
//		Set<Port> inps = new HashSet<Port>();
//		Set<Port> outs = new HashSet<Port>();
//
//		for (Port p : ports) {
//			if (p.getType() == PortType.IN) {
//				inps.add(p);
//			} else {
//				outs.add(p);
//			}
//		}
//
//		Set<TransitionRule> rules = new HashSet<TransitionRule>();
//
//		for (Port p : inps) {
//			Set<Port> included = new HashSet<Port>();
//			Set<Port> excluded = new HashSet<Port>();
//			Formula g = new BooleanValue(true);
//			for (Port x : inps) {
//				if (x.equals(p)) {
//					included.add(x);
//				} else {
//					excluded.add(x);
//				}
//			}
//			for (Port x : outs) {
//				included.add(x);
//				g = new Clause(g, new Equality(new Node(p), new Node(x)));
//			}
//
//			SyncConstraint N = new SyncConstraint(included, excluded);
//
//			rules.add(new TransitionRule(N, g));
//		}
//
//		return new RuleBasedAutomaton(ports, rules, new Object[0]);
		return null;
	}

	@Override
	public SymbolicAutomaton rename(Map<Port, Port> links) {
//		Set<Port> ports = new HashSet<Port>();
//		Set<TransitionRule> rules = new HashSet<TransitionRule>(); 
//		
//		// Rename the ports in the interface
//		for (Port a : this.ports) {
//			Port b = links.get(a);
//			if (b == null) b = a;
//			ports.add(b);
//		}
//		
//		// Add relabeled transitions to the set of transition
//		for (TransitionRule r : this.rules) 
//			rules.add(r.rename(links));
//		
//		return new RuleBasedAutomaton(ports, rules, initial);
		return null;
	}

	@Override
	public SymbolicAutomaton compose(List<SymbolicAutomaton> components) {
		List<Formula> list = new ArrayList<Formula>();
		list.add(this.f);
		for (SymbolicAutomaton A : components)
			list.add(A.f);
		return new SymbolicAutomaton(new Conjunction(list));
	}
	
	public Formula getFormula(){
		return f;
	}

	@Override
	public SymbolicAutomaton restrict(Collection<? extends Port> intface) {
		Set<Port> P = f.getInterface();
//		P.removeAll(o);
		return null;
	}

}
