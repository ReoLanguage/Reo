package nl.cwi.reo.interpret;

import java.util.List;
import java.util.Map;

public final class SourceFile implements ZDefinition {
	
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
	private final Component cexpr;
	
	public SourceFile(String section, List<String> imports, String name, Component cexpr) {
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
	
	public Component getComponent() {
		return cexpr;
	}

	@Override
	public ZDefinitionList evaluate(Map<VariableName, Expression> params)
			throws Exception {
		ZDefinitionList defs = new ZDefinitionList();
		defs.put(getVariableName(), cexpr.evaluate(params));
		return defs;
	}
}
