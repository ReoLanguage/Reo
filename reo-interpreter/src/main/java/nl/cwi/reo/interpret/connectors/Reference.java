package nl.cwi.reo.interpret.connectors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Reference to an atomic component that is specified in a different language.
 */
public final class Reference implements Atom {

	/** The reference */
	private final String ref;

	/** The target language */
	private final Language lang;

	/** The parameters */
	private final List<? extends VariableExpression> params;

	/** The parameter values */
	private List<Value> values;

	/**
	 * Instantiates a new reference.
	 *
	 * @param ref
	 *            the reference
	 * @param language
	 *            the target language
	 */
	public Reference(String ref, Language language) {
		this.ref = ref;
		this.lang = language;
		this.params = new ArrayList<>();
		this.values = new ArrayList<>();
	}

	/**
	 * Instantiates a new reference.
	 *
	 * @param ref
	 *            the reference
	 * @param language
	 *            the target language
	 * @param params
	 *            the parameters
	 */
	public Reference(String ref, Language language, List<? extends VariableExpression> params) {
		this.ref = ref;
		this.lang = language;
		this.params = params;
		this.values = new ArrayList<>();
	}

	/**
	 * Instantiates a new reference.
	 *
	 * @param ref
	 *            the reference
	 * @param language
	 *            the target language
	 * @param params
	 *            the parameters
	 * @param values
	 *            the parameter values
	 */
	public Reference(String ref, Language language, List<? extends VariableExpression> params, List<Value> values) {
		this.ref = ref;
		this.lang = language;
		this.params = params;
		this.values = values;
	}

	/**
	 * Gets the call.
	 *
	 * @return the call
	 */
	public String getCall() {
		return ref;
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public List<Value> getValues() {
		return values;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public Language getLanguage() {
		return lang;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ref != null ? ref : "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Expression#evaluate(nl.cwi.reo.interpret.Scope,
	 * nl.cwi.reo.util.Monitor)
	 */
	@Override
	public @Nullable Atom evaluate(Scope s, Monitor m) {
		List<Value> values = new ArrayList<>();
		for (VariableExpression v : params) {
			List<? extends Identifier> ids = v.evaluate(s, m);
			if (ids != null) {
				Value x;
				for (Identifier id : ids)
					if ((x = s.get(id)) != null)
						values.add(x);
			}
		}
		return new Reference(ref, lang, params, values);
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.Atom#getType()
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.REF;
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
		return null;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.Atom#getNode(java.util.Set)
	 */
	@Override
	public Atom getNode(Set<Port> node) {
		return null;
	}
}
