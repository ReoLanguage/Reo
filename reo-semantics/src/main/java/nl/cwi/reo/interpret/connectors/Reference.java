package nl.cwi.reo.interpret.connectors;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Reference to an atomic component that is specified in a different language.
 */
public final class Reference implements Expression<Reference> {

	/** The ref. */
	@Nullable
	private final String ref;

	/** The lang. */
	@Nullable
	private final Language lang;

	/** The params. */
	private final List<? extends VariableExpression> params;

	/** The values. */
	@Nullable
	private List<Value> values;

	/**
	 * Instantiates a new reference.
	 */
	public Reference() {
		this.ref = null;
		this.lang = null;
		this.params = new ArrayList<>();
		this.values = null;
	}

	/**
	 * Instantiates a new reference.
	 *
	 * @param ref
	 *            the ref
	 * @param language
	 *            the language
	 */
	public Reference(@Nullable String ref, @Nullable Language language) {
		this.ref = ref;
		this.lang = language;
		this.params = new ArrayList<>();
		this.values = new ArrayList<>();
	}

	/**
	 * Instantiates a new reference.
	 *
	 * @param ref
	 *            the ref
	 * @param language
	 *            the language
	 * @param params
	 *            the params
	 */
	public Reference(@Nullable String ref, @Nullable Language language, List<? extends VariableExpression> params) {
		if (params == null)
			throw new NullPointerException();
		this.ref = ref;
		this.lang = language;
		this.params = params;
		this.values = new ArrayList<>();
	}

	/**
	 * Instantiates a new reference.
	 *
	 * @param ref
	 *            the ref
	 * @param language
	 *            the language
	 * @param params
	 *            the params
	 * @param values
	 *            the values
	 */
	public Reference(@Nullable String ref, @Nullable Language language, List<? extends VariableExpression> params,
			@Nullable List<Value> values) {
		if (params == null)
			throw new NullPointerException();
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
	@Nullable
	public String getCall() {
		return ref;
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	@Nullable
	public List<Value> getValues() {
		return values;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	@Nullable
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
	public @Nullable Reference evaluate(Scope s, Monitor m) {
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
}
