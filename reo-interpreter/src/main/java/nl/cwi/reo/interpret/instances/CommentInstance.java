package nl.cwi.reo.interpret.instances;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Comment;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductInstance.
 *
 * @param <T>
 *            the generic type
 */
public final class CommentInstance implements InstanceExpression {

	/**
	 * Composition operator name.
	 */
	private final String comment;


	/**
	 * Constructs a new composition of instances.
	 *
	 * @param operator
	 *            composition operator
	 * @param first
	 *            first instance
	 * @param second
	 *            second instance
	 * @param location
	 *            the location
	 */
	public CommentInstance(String comment) {
		this.comment = comment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance evaluate(Scope s, Monitor m) {

		ReoConnector connector = new ReoConnectorAtom("comment", Arrays.asList(new Comment(comment)), new HashMap<>());
		Set<Set<Identifier>> unifications = new HashSet<Set<Identifier>>();

		return new Instance(connector, unifications);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> union = new HashSet<>();
		return union;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return comment;
	}

}
