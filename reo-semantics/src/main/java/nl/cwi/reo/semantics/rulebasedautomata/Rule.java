package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Terms;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.util.Monitor;

public class Rule {

	private final Map<Port, Boolean> sync;

	private final Formula f;

	private final int hash;

	/**
	 * Instantiates a new rule.
	 *
	 * @param sync
	 *            the sync
	 * @param f
	 *            the f
	 */
	public Rule(Map<Port, Boolean> sync, Formula f) {
		List<Formula> list = new ArrayList<>();
		for(Port p : sync.keySet()){
			if(sync.get(p))
				list.add(new Negation(new Equality(new PortVariable(p),Terms.Null)));
			else
				list.add(new Equality(new PortVariable(p),Terms.Null));
		}
		list.add(f);
		this.f = new Conjunction(list);
		Map<Port,Boolean> _sync = this.f.getSynchronousMap();
		if(_sync != null)
			this.sync = _sync;
		else
			this.sync = new HashMap<>();
		this.hash = Objects.hash(sync, f);
	}
	
	public Rule(Formula f) {
		this.f = f;
		Map<Port,Boolean> _sync = this.f.getSynchronousMap();
		if(_sync != null)
			this.sync = _sync;
		else
			this.sync = new HashMap<>();
		this.hash = Objects.hash(sync, f);
	}

	public Map<Port, Boolean> getSync() {
		return sync;
	}

	public Formula getFormula() {
		return f;
	}

	public Set<Port> getInterface() {
		return sync.keySet();
	}

	public Rule rename(Map<Port, Port> links) {
		Map<Port, Boolean> _sync = new HashMap<>();
		for (Map.Entry<Port, Boolean> x : sync.entrySet()) {
			Port p = links.get(x.getKey());
			if (p == null)
				p = x.getKey();
			_sync.put(p, x.getValue());
		}
		Formula _f = f.rename(links);
		return new Rule(_f);
	}

	public @Nullable Rule evaluate(Scope s, Monitor m) {
		Formula _f = f.evaluate(s, m);
		if (_f == null)
			return null;
		return new Rule( _f);
	}

	public Set<MemoryVariable> getMemoryCells() {
		Set<MemoryVariable> memory = new HashSet<>();
		for (Variable v : f.getFreeVariables())
			if (v instanceof MemoryVariable)
				memory.add((MemoryVariable) v);
		return memory;
	}

	public Rule renameMemory(Map<MemoryVariable, MemoryVariable> rename) {
		Formula _f = f;
		for (Map.Entry<MemoryVariable, MemoryVariable> entry : rename.entrySet())
			_f = _f.substitute(entry.getValue(), entry.getKey());
		return new Rule(_f);
	}

	public Rule restrict(Collection<? extends Port> intface) {
		Collection<Variable> V = new HashSet<>();
		for (Variable v : f.getFreeVariables())
			if (v instanceof PortVariable)
				if (!intface.contains(((PortVariable) v).getPort()))
					V.add(v);
		Formula _f = Formulas.eliminate(f, V);
		return new Rule(_f);
	}

	public Set<Port> getActivePorts() {
		Set<Port> N = new HashSet<>();
		for (Map.Entry<Port, Boolean> x : sync.entrySet())
			if (x.getValue())
				N.add(x.getKey());
		return N;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Rule))
			return false;
		Rule t = (Rule) other;
		return (Objects.equals(this.sync, t.sync) && Objects.equals(this.f, t.f));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return hash;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
//		String s = "";
//		Iterator<Map.Entry<Port, Boolean>> iter = this.sync.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<Port, Boolean> pair = iter.next();
//			s += pair.getValue() ? "!" : "\u00AC!";
//			s += pair.getKey().getName() + (iter.hasNext() ? " \u2227 " : "");
//		}
//		if (f != null)
//			s += " \u2227 " + f.toString();
		return f.toString();
	}
}
