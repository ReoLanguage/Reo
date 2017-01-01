package nl.cwi.reo.interpret;

import java.util.List;

public final class File implements DefinitionExpression {
	
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
	
	public File(String section, List<String> imports, String name, ComponentExpression cexpr) {
		this.section = section;
		this.imports = imports;
		this.name = name;
		this.cexpr = cexpr;
	}

	public List<String> getImports() {
		return imports;
	}

	@Override
	public DefinitionList evaluate(DefinitionList params)
			throws Exception {
		DefinitionList defs = new DefinitionList();
		defs.put(new VariableName(section + "." + name), cexpr.evaluate(params));
		return defs;
	}
}
