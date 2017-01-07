package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.PortType;
import nl.cwi.reo.semantics.Semantics;

public final class ProgramValue implements ProgramExpression, ComponentExpression {
	
	/**
	 * Definitions.
	 */
	private final Map<VariableName, Expression> definitions;
	
	/**
	 * Instances.
	 */
	private final List<Instance> instances;

	/**
	 * Constructs an empty body of components and definitions.
	 */
	public ProgramValue() {
		this.definitions = new HashMap<VariableName, Expression>();
		this.instances = new ArrayList<Instance>();
	}
	
	public ProgramValue(Semantics<?> atom) {
		if (atom == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.definitions = new HashMap<VariableName, Expression>();
		Map<String, Port> links = new HashMap<String, Port>();
		for (String a : atom.getInterface()) 
			links.put(a, new Port(a, PortType.UNKNOWN));
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(new Instance(atom, links));
		this.instances = Collections.unmodifiableList(instances);
	}

	/**
	 * Constructs a collection of definitions and component instances.
	 * @param instance		single component instance
	 */
	public ProgramValue(Instance instance) {
		if (instance == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.definitions = Collections.unmodifiableMap(new HashMap<VariableName, Expression>());
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(instance);
		this.instances = Collections.unmodifiableList(instances);
	}
	
	public ProgramValue(List<Instance> instances) {
		if (instances == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.definitions = new HashMap<VariableName, Expression>();
		this.instances = Collections.unmodifiableList(instances);
	}
		
	/**
	 * Constructs a collection of definitions and component instances.
	 * @param definitions	map of definitions
	 * @param instance		single component instance
	 */
	public ProgramValue(Map<VariableName, Expression> definitions) {
		if (definitions == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.definitions = Collections.unmodifiableMap(definitions);
		this.instances = Collections.unmodifiableList(new ArrayList<Instance>());
	}

	/**
	 * Constructs a collection of definitions and component instances.
	 * @param definitions	map of definitions
	 * @param instance		single component instance
	 */
	public ProgramValue(Map<VariableName, Expression> definitions, Instance instance) {
		if (definitions == null || instance == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.definitions = Collections.unmodifiableMap(definitions);
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(instance);
		this.instances = Collections.unmodifiableList(instances);
	}

	/**
	 * Constructs a collection of definitions and component instances.
	 * @param definitions	map of definitions
	 * @param instances		list of component instances
	 */
	public ProgramValue(Map<VariableName, Expression> definitions, List<Instance> instances) {
		if (definitions == null || instances == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.definitions = Collections.unmodifiableMap(definitions);
		this.instances = Collections.unmodifiableList(instances);
	}
	
	public Expression get(VariableName x) {
		return definitions.get(x);
	}
	
	public ProgramValue remove(VariableName x) {
		Map<VariableName, Expression> defns = new HashMap<VariableName, Expression>();
		for (Map.Entry<VariableName, Expression> def : definitions.entrySet()) 
			if (def.getKey() != x)
				defns.put(def.getKey(), def.getValue());
		return new ProgramValue(defns, instances);
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Map<VariableName, Expression> getDefinitions() {
		return definitions;
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public List<Instance> getInstances() {
		return instances;
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public ProgramValue compose(ProgramValue body) {
		Map<VariableName, Expression> _defns = 
				new HashMap<VariableName, Expression>(definitions);
		_defns.putAll(body.definitions); // TODO IS THIS OK? e.g., in case of recursion, this code just overwrites the old definition.
		List<Instance> _instances = new ArrayList<Instance>();
		Integer i = 0;
		for (Instance comp : this.instances) 
			_instances.add(comp.renameHidden(i));
		for (Instance comp : body.instances)
			_instances.add(comp.renameHidden(i));
		return new ProgramValue(_defns, _instances);
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public ProgramValue compose(List<ProgramValue> bodies) {
		Map<VariableName, Expression> _defns = 
				new HashMap<VariableName, Expression>(definitions);
		List<Instance> _instances = new ArrayList<Instance>();
		Integer i = 0;
		for (Instance comp : this.instances) 
			_instances.add(comp.renameHidden(i));
		for (ProgramValue body : bodies) {
			_defns.putAll(body.definitions);
			for (Instance comp : body.instances)
				_instances.add(comp.renameHidden(i));
		}
		return new ProgramValue(_defns, _instances);
	}
	
	/**
	 * Relabels the interface of this component.
	 * @param iface		maps old node names to new node names.
	 */
	public ProgramValue restrictAndRename(Map<Port, Port> iface) {	
		List<Instance> newinstances = new ArrayList<Instance>();
		for (Instance comp : instances)			
			newinstances.add(comp.instantiate(iface));
		return new ProgramValue(newinstances);
	}

	@Override
	public ProgramValue evaluate(Map<VariableName, Expression> params) throws Exception {
		Map<VariableName, Expression> defns_p = new HashMap<VariableName, Expression>();
		for (Map.Entry<VariableName, Expression> def : definitions.entrySet()) 
			defns_p.put(def.getKey(), def.getValue().evaluate(params));
		return new ProgramValue(defns_p, instances);
		// TODO Add code to evaluate semantics too.
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
	}
	
	@Override
	public String toString() {
		return definitions + " " + instances;
	}
}
