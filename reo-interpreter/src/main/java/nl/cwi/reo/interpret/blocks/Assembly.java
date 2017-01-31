package nl.cwi.reo.interpret.blocks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.Component;
import nl.cwi.reo.interpret.semantics.ComponentList;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Port;
import nl.cwi.reo.semantics.api.Semantics;

public final class Assembly<T extends Semantics<T>> implements ReoBlock<T> {
	
	/**
	 * Definitions.
	 */
	private final Definitions definitions;
	
	/**
	 * Instances.
	 */
	private final ComponentList<T> components;

	/**
	 * Constructs an empty body of components and definitions.
	 */
	public Assembly() {
		this.definitions = new Definitions();
		this.components = new ComponentList<T>();
	}
	
	/**
	 * Constructs a program consisting of a set of definitions and an instance list.
	 * @param definitions
	 * @param instances
	 */
	public Assembly(Definitions definitions, ComponentList<T> instances) {
		if (definitions == null || instances == null)
			throw new NullPointerException();
		this.definitions = new Definitions(definitions);
		this.components = new ComponentList<T>(instances);
	}
	
	/**
	 * Constructs a program consisting of a set of definitions and an instance list.
	 * @param definitions
	 * @param instances
	 */
	public Assembly(Definitions definitions, ComponentList<T> instances, String operator) {
		if (definitions == null || instances == null)
			throw new NullPointerException();
		this.definitions = new Definitions(definitions);
		this.components = new ComponentList<T>(instances, operator);
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Map<String, Expression> getDefinitions() {
		return Collections.unmodifiableMap(definitions);
	}

	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Map<String, Expression> getUnifications() {
		return Collections.unmodifiableMap(definitions.getUnifications());
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public ComponentList<T> getInstances() {
		return new ComponentList<T>(Collections.unmodifiableList(components), components.getOperator());
	}
	
	public Assembly<T> remove(VariableName x) {
		Definitions _definitions = new Definitions(definitions);
		_definitions.remove(x);
		return new Assembly<T>(definitions, components);
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public Assembly<T> compose(List<Assembly<T>> bodies) {
		Definitions _definitions = new Definitions(definitions);
		ComponentList<T> _instances = new ComponentList<T>(components);
		for (Assembly<T> body : bodies) {
			_definitions.putAll(body.definitions);
			_instances.compose(body.components);
		}
		return new Assembly<T>(_definitions, _instances);
	}
	
	/**
	 * Instantiates this program body by dropping all definitions (except necessary
	 * unifications), joining unified ports, hiding internal ports, and renaming external ports.
	 * @param iface		map assigning a new port to every external port (i.e., this map implicitly defines
	 * all internal ports)
	 * @return an instantiate ProgramValue.
	 */
	public Assembly<T> instantiate(Map<Port, Port> iface) {
		Definitions _definitions = new Definitions();
		ComponentList<T> _instances = new ComponentList<T>(components);
		Map<Port, Port> _iface = new HashMap<Port, Port>(iface);		
		
		// Collect all necessary unifications, and rename the variables in these definitions.
		for (Map.Entry<String, Expression> defn : definitions.entrySet()) {
			// if defn is a unification:
			if (defn.getValue() instanceof VariableName) {
				String a = defn.getKey();
				String b = ((VariableName)defn.getValue()).getName();
				
				Port a_new = iface.get(new Port(a));
				Port b_new = iface.get(new Port(b));
				
				if (a_new != null) {
					if (b_new != null) {
						String x = a_new.getName();
						VariableName y = new VariableName(b_new.getName(), null);
						_definitions.put(x, y);
					} else {
						_iface.put(new Port(b), a_new);
					}
				} else {
					if (b_new != null) {
						_iface.put(new Port(a), b_new);
					} else {
						Port ahidden = new Port(a);
						ahidden.hide();
						_iface.put(new Port(b), new Port(a).hide());
					}
				}
			}
		}

		// Instantiate  
		for (Component<T> inst : _instances)			
			inst.connect(_iface);
		
		return new Assembly<T>(_definitions, _instances);
	}

	@Override
	public Assembly<T> evaluate(Map<String, Expression> params) throws CompilationException {
		Definitions definitions_p = definitions.evaluate(params);
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
		// TODO Add code to evaluate semantics too.
		return new Assembly<T>(definitions_p, components);
	}
	
	@Override
	public String toString() {
		return "" + definitions + components;
	}
}
