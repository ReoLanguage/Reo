package nl.cwi.reo.interpret.listeners;

import java.util.List;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.connectors.Semantics;


public final class ReoFile<T extends Semantics<T>> {

	
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
	private final ComponentExpression<T> cexpr;
	
	public ReoFile(String section, List<String> imports, String name, ComponentExpression<T> cexpr, Token token) {
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
	
	public ComponentExpression<T> getComponent() {
		return cexpr;
	}
	
	public Token getToken() {
		return token;
	}
	
	@Override
	public String toString() {
		String imps = "";
		for (String comp : imports)
			imps += "import " + comp + ";";
		return "section " + section + ";" + imps + getName() + "=" + cexpr;
	}
}
