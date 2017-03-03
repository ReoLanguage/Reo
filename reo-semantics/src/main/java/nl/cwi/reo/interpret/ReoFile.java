package nl.cwi.reo.interpret;

import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.semantics.Semantics;
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
	 * Component definitions.
	 */
	private final Map<String, ComponentExpression<T>> definitions;
	
	/**
	 * Location of main component name.
	 */
	private final Location location;
	
	/**
	 * Constructs a new Reo source file.
	 * @param section		section name
	 * @param imports		list of imported components
	 * @param name			file name without extension
	 * @param definitions	component definitions
	 */
	public ReoFile(String section, List<String> imports, String name, Map<String, ComponentExpression<T>> definitions, Location location) {
		if (section == null || imports == null || name == null || definitions == null)
			throw new NullPointerException();
		this.section = section;
		this.imports = imports;
		this.name = name;
		this.definitions = definitions;
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
	public ComponentExpression<T> getMain() {
		return definitions.get(name);
	}
	
	public Map<String, ComponentExpression<T>> getDefinition(){
		return definitions;
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
		ST st = new ST("<section:{ s | section s;\n\n}><imports:{ i | import i;\n }>\n<definitions; separator='\n\n'>");
		st.add("section", "section " + section);
		st.add("imports", imports);
		st.add("definitions", definitions);
		return st.render();
	}
}
