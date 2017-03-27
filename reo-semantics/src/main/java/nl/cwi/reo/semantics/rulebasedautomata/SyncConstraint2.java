package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

public final class SyncConstraint2 {

	private final Set<Port> included;

	private final Set<Port> excluded;

	public SyncConstraint2(Set<Port> included, Set<Port> excluded) {
		this.included = included;
		this.excluded = excluded;
	}

	public Set<Port> getIncluded() {
		return included;
	}

	public Set<Port> getExcluded() {
		return excluded;
	}

	public boolean isSatisfyable() {
		return !new HashSet<Port>(included).removeAll(excluded);
	}
	
	public SyncConstraint2 rename (Map<Port, Port> links) {
		return null;
	}
}
