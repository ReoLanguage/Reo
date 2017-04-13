package nl.cwi.reo.semantics.predicates;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;

public class Node implements Variable  {
	
	public static final boolean node = true;
	
	private final Port p;
	
	public Node(Port p) {
		this.p = p;
	}

	public Port getPort() {
		return p;
	}

	@Override
	public boolean hadOutputs() {
		return p.getType() == PortType.OUT;
	}

	public String getName(){
		return p.getName();
	}
	
	@Override
	public Term rename(Map<Port, Port> links) {
		Port b = links.get(p);
		if (b != null)
			return new Node(b);
		return this;
	}
	public String toString(){
		return p.getName();
	}

	public boolean isInput() {
		return p.isInput();
	}

	@Override
	public Term Substitute(Term t, Variable x) {
		if(this.equals(x)){
			if(t instanceof Node)
				return new Node(new Port(((Node)t).getName(),((Node)x).getPort().getType(),((Node)x).getPort().getPrioType(),((Node)x).getPort().getTypeTag(),true));
			else
				return t;
		}
		return this;
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return new HashSet<Variable>(Arrays.asList(this));
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
		if (!(other instanceof Node))
			return false;
		Node p = (Node) other;
		return Objects.equals(this.p, p.p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.p);
	}
}
