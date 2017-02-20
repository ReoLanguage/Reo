package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.blocks.Assembly;
import nl.cwi.reo.interpret.oldstuff.Evaluable;
import nl.cwi.reo.interpret.oldstuff.Expression;
import nl.cwi.reo.interpret.oldstuff.Semantics;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.ComponentList;
import nl.cwi.reo.interpret.systems.ReoSystem;

public final class ReoFile<T extends Semantics<T>> implements Evaluable<Assembly<T>> {
	
	/**
	 * Section.
	 */
	private final String section;
	
	/**
	 * List of fully qualified names of imported components.
	 */
	private final List<String> imports;
	
	/**
	 * Name of main component.
	 */
	private final String name;
	
	/**
	 * Token of the main component name.
	 */
	private final Token token;
	
	/**
	 * Main component.
	 */
	private final ReoSystem<T> cexpr;
	
	public ReoFile(String section, List<String> imports, String name, ReoSystem<T> cexpr, Token token) {
		if (section == null || imports == null || name == null || cexpr == null || token == null)
			throw new NullPointerException();
		this.section = section;
		this.imports = imports;
		this.name = name;
		this.cexpr = cexpr;
		this.token = token;
	}

	public List<String> getImports() {
		return imports;
	}
	
	public String getName() {
		return section.equals("") ? name : section + "." + name; 
	}
	
	public ReoSystem<T> getComponent() {
		return cexpr;
	}
	
	public Token getToken() {
		return token;
	}

	@Override
	public Assembly<T> evaluate(Map<String, Expression> params) throws CompilationException {
		Map<String, Expression> definitions = new HashMap<String, Expression>();
		definitions.put(getName(), cexpr.evaluate(params));
		return new Assembly<T>(new Definitions(definitions), new ComponentList<T>());
	}
	
	@Override
	public String toString() {
		String imps = "";
		for (String comp : imports)
			imps += "import " + comp + ";";
		return "section " + section + ";" + imps + getName() + "=" + cexpr;
	}
}
