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

/**
 * Reference to an atomic component that is specified in a different language.
 */
public final class Reference implements Expression<Reference> {

	@Nullable
	private final String ref;

	@Nullable
	private final Language lang;

	private final List<? extends VariableExpression> params;

	@Nullable
	private List<Value> values;

	public Reference() {
		this.ref = null;
		this.lang = null;
		this.params = new ArrayList<>();
		this.values = null;
	}

	public Reference(String ref, Language language) {
		this.ref = ref;
		this.lang = language;
		this.params = new ArrayList<>();
		this.values = new ArrayList<>();
	}

	public Reference(String ref, Language language, List<? extends VariableExpression> params) {
		this.ref = ref;
		this.lang = language;
		this.params = params;
		this.values = new ArrayList<>();
	}

	public Reference(String ref, Language language, List<? extends VariableExpression> params, List<Value> values) {
		this.ref = ref;
		this.lang = language;
		this.params = params;
		this.values = values;
	}

	@Nullable
	public String getCall() {
		return ref;
	}
	
	public List<Value> getValues() {
		return values;
	}

	@Nullable
	public Language getLanguage() {
		return lang;
	}

	@Override
	public String toString() {
		return ref != null ? ref : "";
	}

	@Override
	public @Nullable Reference evaluate(Scope s, Monitor m) {
		List<Value> values = new ArrayList<>();
		for (VariableExpression v : params) {
			List<? extends Identifier> ids = v.evaluate(s, m);
			for (Identifier id : ids)
				values.add(s.get(id));
		}
		return new Reference(ref, lang, params, values);
	}
}
