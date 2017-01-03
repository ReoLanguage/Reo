package nl.cwi.reo.interpret;

import java.util.List;

public final class SourceFile implements Definition {
	
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
	
	public SourceFile(String section, List<String> imports, String name, ComponentExpression cexpr) {
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
	public DefinitionList evaluate(DefinitionList params)
			throws Exception {
		DefinitionList defs = new DefinitionList();
		defs.put(getVariableName(), cexpr.evaluate(params));
		return defs;
	}
}
