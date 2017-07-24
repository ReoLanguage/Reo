package nl.cwi.reo.semantics.prautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Parameter;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class PRAutomaton.
 */
public class PRAutomaton implements Semantics<PRAutomaton> {

	/** The name. */
	private final String name;

	/** The parameter. */
	@Nullable
	private final Value parameter;

	/** The value. */
	@Nullable
	private Value value;

	/** The ports. */
	private final List<Port> ports;

	/**
	 * Instantiates a new PR automaton.
	 */
	public PRAutomaton() {
		this.name = "";
		this.parameter = null;
		this.value = null;
		this.ports = new ArrayList<Port>();
	}

	/**
	 * Instantiates a new PR automaton.
	 *
	 * @param name
	 *            the name
	 * @param variable
	 *            the variable
	 * @param value
	 *            the value
	 * @param port
	 *            the port
	 */
	public PRAutomaton(String name, @Nullable Value variable, @Nullable Value value, List<Port> port) {
		this.name = name;
		if (variable instanceof StringValue)
			this.parameter = new StringValue(variable.toString().replaceAll("\"", ""));
		else
			this.parameter = variable;
		this.value = value;
		this.ports = port;
	}

	/**
	 * Instantiates a new PR automaton.
	 *
	 * @param name
	 *            the name
	 * @param variable
	 *            the variable
	 * @param port
	 *            the port
	 */
	public PRAutomaton(String name, @Nullable Value variable, List<Port> port) {
		this.name = name;
		this.parameter = variable;
		this.value = null;
		this.ports = port;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@Nullable
	public Value getValue() {
		return value;
	}

	/**
	 * Gets the variable.
	 *
	 * @return the variable
	 */
	public Value getVariable() {
		if (name.equals("FifoFull") && parameter instanceof StringValue) {
			return new StringValue("\"" + parameter.toString() + "\"");
		} else
			return parameter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.PA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name + (parameter != null ? "[" + parameter + "]" : "") + "(" + ports + ")";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		return new LinkedHashSet<Port>(ports);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PRAutomaton getNode(Set<Port> node) {

		List<Port> P = new ArrayList<Port>();

		int counterI = 0;
		int counterO = 0;
		for (Port p : node) {
			switch (p.getType()) {
			case IN:
				counterI++;
				P.add(new Port(p.getName(), PortType.OUT, p.getPrioType(), p.getTypeTag(), !p.isHidden()));
				break;
			case OUT:
				counterO++;
				P.add(new Port(p.getName(), PortType.IN, p.getPrioType(), p.getTypeTag(), !p.isHidden()));
				break;
			default:
				break;
			}
		}

		if (counterI > counterO)
			return new PRAutomaton("Node", null, P);

		return new PRAutomaton("Node", null, P);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PRAutomaton rename(Map<Port, Port> r) {

		List<Port> P = new ArrayList<Port>();

		for (Port a : this.ports) {
			Port b = r.get(a);
			if (b == null)
				b = a;
			P.add(b);
		}

		return new PRAutomaton(name, parameter, P);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PRAutomaton evaluate(Scope s, Monitor m) {
		// Value v = s.get(variable);
		if (parameter != null) {
			Value l = s.get(new Parameter(parameter.toString(), new TypeTag("int")));
			this.value = l;

			return new PRAutomaton(name, l, l, ports);
		}
		return new PRAutomaton(name, null, null, ports);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PRAutomaton compose(List<PRAutomaton> automata) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PRAutomaton restrict(Collection<? extends Port> intface) {
		return null;
	}

	@Override
	public PRAutomaton getDefault(Set<Port> iface) {
		// TODO Auto-generated method stub
		return null;
	}

}
