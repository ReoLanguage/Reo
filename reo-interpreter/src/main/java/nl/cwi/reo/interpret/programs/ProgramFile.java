package nl.cwi.reo.interpret.programs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Semantics;

public final class ProgramFile<T extends Semantics<T>> implements ProgramExpression<T> {
	
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
	private final ComponentExpression<T> cexpr;
	
	public ProgramFile(String section, List<String> imports, String name, ComponentExpression<T> cexpr) {
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
	
	public ComponentExpression<T> getComponent() {
		return cexpr;
	}

	@Override
	public ProgramExpression<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		Map<VariableName, Expression> definitions = new HashMap<VariableName, Expression>();
		definitions.put(new VariableName(getName()), cexpr.evaluate(params));
		return new ProgramValue<T>(new Definitions(definitions), new InstanceList<T>());
	}
	
	@Override
	public String toString() {
		String imps = "";
		for (String comp : imports)
			imps += "import " + comp + ";";
		return "section " + section + ";" + imps + getName() + "=" + cexpr;
	}
}
