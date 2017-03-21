package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;

public class Equality implements DataConstraint {

	private final DataTerm t1;

	private final DataTerm t2;

	public Equality(DataTerm t1, DataTerm t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public DataConstraint getGuard() {
		if (t1 instanceof PortVariable && t2 instanceof PortVariable) {
			if (((PortVariable) t1).getPort().getType() != PortType.OUT)
				if (((PortVariable) t2).getPort().getType() != PortType.OUT)
					return this;
		}
		return new BooleanValue(true);
	}

	@Override
	public Map<Port, DataTerm> getAssignment() {
		Map<Port, DataTerm> map = new HashMap<Port, DataTerm>();
		if (t1 instanceof PortVariable && !t2.hadOutputs()) {
			Port p = ((PortVariable) t1).getPort();
			if (p.getType() != PortType.OUT)
				map.put(p, t2);
		} else if (t2 instanceof PortVariable && !t1.hadOutputs()) {
			Port p = ((PortVariable) t2).getPort();
			if (p.getType() != PortType.OUT)
				map.put(p, t1);
		}
		return null;
	}

	@Override
	public DataConstraint rename(Map<Port, Port> links) {
		DataTerm s1 = t1;
		if (t1 instanceof PortVariable) {
			Port b = links.get(((PortVariable) t1).getPort());
			if (b != null)
				s1 = new PortVariable(b);
		}

		DataTerm s2 = t2;
		if (t1 instanceof PortVariable) {
			Port b = links.get(((PortVariable) t2).getPort());
			if (b != null)
				s2 = new PortVariable(b);
		}
		return new Equality(s1, s2);
	}

}
