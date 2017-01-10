package nl.cwi.reo.interpret;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.semantics.Port;

public final class ProgramValue implements ProgramExpression, ComponentExpression {
	
	/**
	 * Definitions.
	 */
	private final Definitions definitions;
	
	/**
	 * Instances.
	 */
	private final InstanceList instances;

	/**
	 * Constructs an empty body of components and definitions.
	 */
	public ProgramValue() {
		this.definitions = new Definitions();
		this.instances = new InstanceList();
	}
	
	/**
	 * Constructs a program consisting of a set of definitions and an instance list.
	 * @param definitions
	 * @param instances
	 */
	public ProgramValue(Definitions definitions, InstanceList instances) {
		if (definitions == null || instances == null)
			throw new NullPointerException();
		this.definitions = new Definitions(definitions);
		this.instances = new InstanceList(instances);
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Map<VariableName, Expression> getDefinitions() {
		return Collections.unmodifiableMap(definitions);
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public List<Instance> getInstances() {
		return Collections.unmodifiableList(instances);
	}
	
	public ProgramValue remove(VariableName x) {
		Definitions _definitions = new Definitions(definitions);
		_definitions.remove(x);
		return new ProgramValue(_definitions, instances);
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public ProgramValue compose(List<ProgramValue> bodies) {
		Definitions _definitions = new Definitions(definitions);
		InstanceList _instances = new InstanceList(instances);
		for (ProgramValue body : bodies) {
			_definitions.putAll(body.definitions);
			_instances.compose(body.instances);
		}
		return new ProgramValue(_definitions, _instances);
	}
	
	public ProgramValue join(Map<Port, Port> iface) {
		Definitions _definitions = new Definitions();
		InstanceList _instances = new InstanceList(instances);
		Map<String, String> renaming = new HashMap<String, String>();		
		for (Map.Entry<VariableName, Expression> defn : definitions.entrySet()) {
			if (defn.getValue() instanceof VariableName) {
				String a = defn.getKey().getName();
				String b = ((VariableName)defn.getValue()).getName();
				boolean aIsExternal = false;
				boolean bIsExternal = false;
				for (Map.Entry<Port, Port> link : iface.entrySet()) {
					aIsExternal = aIsExternal || a.equals(link.getKey().getName());
					bIsExternal = bIsExternal || b.equals(link.getKey().getName());
				}
				if (aIsExternal) {
					if (bIsExternal) {
						VariableName x = new VariableName(iface.get(new Port(a)).getName());
						VariableName y = new VariableName(iface.get(new Port(b)).getName());
						_definitions.put(x, y);
					} else {
						renaming.put(b, a);
					}
				} else {
					renaming.put(a, b);
				}
			} else {
				_definitions.put(defn.getKey(), defn.getValue());
			}
		}
		for (Instance comp : _instances)
			comp.rename(renaming);
		return new ProgramValue(_definitions, _instances);
	}

	@Override
	public ProgramValue evaluate(Map<VariableName, Expression> params) throws Exception {
		Definitions definitions_p = definitions.evaluate(params);
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
		// TODO Add code to evaluate semantics too.
		return new ProgramValue(definitions_p, instances);
	}

	public ProgramValue instantiate(Map<Port, Port> iface) {
		Definitions _definitions = new Definitions(definitions);
		InstanceList _instances = new InstanceList(instances);
		_instances.instantiate(iface);
		return new ProgramValue(_definitions, _instances);
	}
	
	@Override
	public String toString() {
		return "" + definitions + instances;
	}
}
