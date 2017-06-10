package nl.cwi.reo.semantics.hypergraphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.util.Monitor;

public class Rule {

	private Map<Port, Boolean> sync;
	private Formula f;

	public Rule(Map<Port, Boolean> sync, Formula f) {
		this.sync = sync;
		this.f = f;
	}

	public Map<Port, Boolean> getSync() {
		return sync;
	}

	public Formula getFormula() {
		return f;
	}

	public Set<Port> getFiringPorts() {
		Set<Port> setPort = new HashSet<Port>();
		for (Port p : sync.keySet()) {
			if (sync.get(p)) {
				setPort.add(p);
			}
		}
		return setPort;
	}

	public Set<Port> getAllPorts() {
		return new HashSet<>(sync.keySet());
	}

	public Rule rename(Map<Port, Port> links) {
		HashMap<Port, Boolean> map = new HashMap<>();
		for (Port p : sync.keySet()) {
			if (links.containsKey(p)) {
				map.put(links.get(p), sync.get(p));
			} else
				map.put(p, sync.get(p));
		}
		
		return new Rule(map, f.rename(links));
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	public boolean canSync(Rule r) {

		boolean hasEdge = false;
		for (Port p : sync.keySet()) {
			if (sync.get(p)) {
				if (r.getSync().get(p)!=null && r.getSync().get(p)) {
					hasEdge = true;
				} else if(r.getSync().get(p)!=null && !r.getSync().get(p)){ 
					return false;
				}
			}
			else if(r.getSync()!=null && r.getSync().get(p)!=null &&r.getSync().get(p))
				return false;
		}
		return hasEdge;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "{";
		Iterator<Map.Entry<Port, Boolean>> iter = this.sync.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Port, Boolean> pair = iter.next();
			s += pair.getValue() ? "" : "~";
			s += pair.getKey() + (iter.hasNext() ? ", " : "");
		}
		s += "} ";
		if (f != null)
			s += f.toString();
		return s;
	}

	public @Nullable Rule evaluate(Scope s, Monitor m) {

		return new Rule(sync, f.evaluate(s, m));
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
		Rule rule = (Rule) other;
		return (Objects.equals(this.sync, rule.sync) && Objects.equals(this.f, rule.f));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.sync,this.f);
		
	}
}
