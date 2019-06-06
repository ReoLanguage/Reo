package nl.cwi.reo.interpret.connectors;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Reference to an atomic component that is specified in a different language.
 */
public final class Comment implements Atom {

	/** The comment */
	private final StringValue comment;

/*
	*//** The parameters *//*
	private final List<? extends VariableExpression> params;

	*//** The parameter values *//*
	private List<Value> values;*/

	/**
	 * Instantiates a new reference.
	 *
	 * @param ref
	 *            the reference
	 * @param language
	 *            the target language
	 */
	public Comment(String comment) {
		this.comment = new StringValue(comment);
	}


	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public StringValue getComment() {
		return comment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return comment != null ? comment.getValue() : "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Expression#evaluate(nl.cwi.reo.interpret.Scope,
	 * nl.cwi.reo.util.Monitor)
	 */
	@Override
	public @Nullable Atom evaluate(Scope s, Monitor m) {
		
		//TODO implements evaluation
		
		return new Comment(comment.getValue());
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.Atom#getType()
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.TXT;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.Atom#getInterface()
	 */
	@Override
	public Set<Port> getInterface() {
		return new HashSet<>();
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.Atom#rename(java.util.Map)
	 */
	@Override
	public Atom rename(Map<Port, Port> links) {
		return this;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.Atom#getNode(java.util.Set)
	 */
	@Override
	public Atom getNode(Set<Port> node) {
		return null;
	}
}
