package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ProgramFile implements ProgramExpression {
	
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
	private final ComponentExpression cexpr;
	
	public ProgramFile(String section, List<String> imports, String name, ComponentExpression cexpr) {
		if (section == null || imports == null || name == null || cexpr == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.section = section;
		this.imports = imports;
		this.name = name;
		this.cexpr = cexpr;
	}

	public List<String> getImports() {
		return imports;
	}
	
	public VariableName getVariableName() {
		return new VariableName(section.equals("") ? name : section + "." + name); 
	}
	
	public ComponentExpression getComponent() {
		return cexpr;
	}

	@Override
	public ProgramExpression evaluate(Map<VariableName, Expression> params)
			throws Exception {
		Map<VariableName, Expression> defs = new HashMap<VariableName, Expression>();
		defs.put(getVariableName(), cexpr.evaluate(params));
		return new ProgramValue(defs);
	}
	
	@Override
	public String toString() {
		String imps = "";
		for (String comp : imports)
			imps += "import " + comp + ";";
		return "Section " + section + ";" + imps + getVariableName().getName() + "=" + cexpr;
	}
}
