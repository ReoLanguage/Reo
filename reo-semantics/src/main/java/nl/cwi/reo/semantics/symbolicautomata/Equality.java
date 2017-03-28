package nl.cwi.reo.semantics.symbolicautomata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.util.Monitor;

public class Equality implements Formula {

	private final Term t1;

	private final Term t2;

	public Equality(Term t1, Term t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public Formula getGuard() {
		if (t1 instanceof Node && t2 instanceof Node) {
			if (((Node) t1).getPort().getType() != PortType.OUT)
				if (((Node) t2).getPort().getType() != PortType.OUT)
					return this;
		}
		return new BooleanValue(true);
	}

	@Override
	public Map<Port, Term> getAssignment() {
		Map<Port, Term> map = new HashMap<Port, Term>();
		if (t1 instanceof Node && !t2.hadOutputs()) {
			Port p = ((Node) t1).getPort();
			if (p.getType() != PortType.OUT)
				map.put(p, t2);
		} else if (t2 instanceof Node && !t1.hadOutputs()) {
			Port p = ((Node) t2).getPort();
			if (p.getType() != PortType.OUT)
				map.put(p, t1);
		}
		return null;
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		Term s1 = t1;
		if (t1 instanceof Node) {
			Port b = links.get(((Node) t1).getPort());
			if (b != null)
				s1 = new Node(b);
		}

		Term s2 = t2;
		if (t1 instanceof Node) {
			Port b = links.get(((Node) t2).getPort());
			if (b != null)
				s2 = new Node(b);
		}
		return new Equality(s1, s2);
	}

	@Override
	public Set<Port> getInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	@Override
	public Formula DNF() {
		return this;
	}

}
