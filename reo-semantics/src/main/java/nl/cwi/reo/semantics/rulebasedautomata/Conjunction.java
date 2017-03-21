package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class Conjunction implements DataConstraint {
	
	private final DataConstraint g1;

	private final DataConstraint g2;
	
	public Conjunction(DataConstraint g1, DataConstraint g2) {
		this.g1 = g1;
		this.g2 = g2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataConstraint getGuard() {
		return new Conjunction(g1.getGuard(), g2.getGuard());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Port, DataTerm> getAssignment() {
		Map<Port, DataTerm> assignment1 = g1.getAssignment();
		Map<Port, DataTerm> assignment2 = g2.getAssignment();
		if (assignment1 == null || assignment2 == null)
			return null;
		for (Map.Entry<Port, DataTerm> pair : assignment2.entrySet())
			if (assignment1.put(pair.getKey(), pair.getValue()) != null)
				return null;
		return assignment1;
	}

	@Override
	public DataConstraint rename(Map<Port, Port> links) {
		return new Conjunction(g1.rename(links), g2.rename(links));
	}

}
