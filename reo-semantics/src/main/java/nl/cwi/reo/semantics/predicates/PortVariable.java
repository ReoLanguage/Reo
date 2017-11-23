package nl.cwi.reo.semantics.predicates;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * A variable the represents the data observed at a port.
 */
public final class PortVariable implements Variable {

	/**
	 * Flag for string template.
	 */
	public static final boolean node = true;

	/**
	 * Flag for string template.
	 */
	private final Port p;

	/**
	 * Constructs a new port variable from a given port.
	 * 
	 * @param p
	 *            port
	 */
	public PortVariable(Port p) {
		this.p = p;
	}

	/**
	 * Gets the port of this port variable.
	 * 
	 * @return the port of this variable.
	 */
	public Port getPort() {
		return p;
	}

	/**
	 * Checks if the port of this variable is an input port.
	 * 
	 * @return true if the port is an input port
	 */
	public boolean isInput() {
		return p.isInput();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public TypeTag getTypeTag() {
		return this.p.getTypeTag();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return p.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PortVariable rename(Map<Port, Port> links) {
		Port b = links.get(p);
		if (b != null)
			return new PortVariable(b);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term substitute(Term t, Variable x) {
		if (this.equals(x)){
			if(t instanceof PortVariable)
//				return new PortVariable(this.getPort().rename(((PortVariable)t).getName()));
				return new PortVariable(((PortVariable)t).getPort());	
			return t;
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<>();
		vars.add(this);
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return p.toString();
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
		if (!(other instanceof PortVariable))
			return false;
		PortVariable p = (PortVariable) other;
		return Objects.equals(this.p, p.p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.p);
	}

	/**
	 * Evaluates this port variable. Currently, it updates only if a decimal
	 * value is assigned.
	 */
	@Override
	public @Nullable Term evaluate(Scope s, Monitor m) {
		Value v = s.get(new Identifier(p.getName()));
		if (v != null)
			return new Constant(v);
		return this;
	}

	@Override
	public Term setTypeTag(TypeTag t) {
		if(getTypeTag()!=null && getTypeTag()!=TypeTags.Object){
			if(getTypeTag()!=p.getTypeTag()){
				try {
					throw new Exception("type mismatch");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return this;
		}
		else 
			return new PortVariable(p.setTag(t));
	}
}
