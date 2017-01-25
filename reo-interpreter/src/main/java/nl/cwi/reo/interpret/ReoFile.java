package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.blocks.Assembly;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.ComponentList;
import nl.cwi.reo.interpret.systems.ReoSystem;
import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Semantics;

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
	 * Main component.
	 */
	private final ReoSystem<T> cexpr;
	
	public ReoFile(String section, List<String> imports, String name, ReoSystem<T> cexpr) {
		if (section == null || imports == null || name == null || cexpr == null)
			throw new NullPointerException();
		this.section = section;
		this.imports = imports;
		this.name = name;
		this.cexpr = cexpr;
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
