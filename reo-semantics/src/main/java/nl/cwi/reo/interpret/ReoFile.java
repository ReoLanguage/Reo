package nl.cwi.reo.interpret;

import java.util.List;

import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.util.Location;

/**
 * Interpretation of a Reo source file.
 * @param <T> Reo semantics type
 */
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
	 * Main component.
	 */
	private final ComponentExpression<T> cexpr;
	
	/**
	 * Location of main component name.
	 */
	private final Location location;
	
	/**
	 * Constructs a new Reo source file.
	 * @param section	section name
	 * @param imports	list of imported components
	 * @param name		name of main component
	 * @param cexpr		main component definition.
	 */
	public ReoFile(String section, List<String> imports, String name, ComponentExpression<T> cexpr, Location location) {
		if (section == null || imports == null || name == null || cexpr == null)
			throw new NullPointerException();
		this.section = section;
		this.imports = imports;
		this.name = name;
		this.cexpr = cexpr;
		this.location = location;
	}

	/**
	 * Gets the list of imported components.
	 * @return list of imports
	 */
	public List<String> getImports() {
		return imports;
	}
	
	/**
	 * Gets the fully qualified name of this component.
	 * @return fully qualified name
	 */
	public String getName() {
		return section.equals("") ? name : section + "." + name; 
	}
	
	/**
	 * Gets the main component definition.
	 * @return main component definition
	 */
	public ComponentExpression<T> getComponent() {
		return cexpr;
	}
	
	/**
	 * Gets the location of the main component.
	 * @return location of main component
	 */
	public Location getMainLocation() {
		return location;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String imps = "";
		for (String comp : imports)
			imps += "import " + comp + ";";
		return "section " + section + ";" + imps + getName() + "=" + cexpr;
	}
}
