package nl.cwi.reo.interpret.blocks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.Instance;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

public final class Program<T extends Semantics<T>> implements Statement<T> {
	
	/**
	 * Definitions.
	 */
	private final Definitions definitions;
	
	/**
	 * Instances.
	 */
	private final InstanceList<T> instances;

	/**
	 * Constructs an empty body of components and definitions.
	 */
	public Program() {
		this.definitions = new Definitions();
		this.instances = new InstanceList<T>();
	}
	
	/**
	 * Constructs a program consisting of a set of definitions and an instance list.
	 * @param definitions
	 * @param instances
	 */
	public Program(Definitions definitions, InstanceList<T> instances) {
		if (definitions == null || instances == null)
			throw new NullPointerException();
		this.definitions = new Definitions(definitions);
		this.instances = new InstanceList<T>(instances);
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
	public Map<VariableName, Expression> getUnifications() {
		return Collections.unmodifiableMap(definitions.getUnifications());
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public InstanceList<T> getInstances() {
		return new InstanceList<T>(Collections.unmodifiableList(instances));
	}
	
	public Program<T> remove(VariableName x) {
		Definitions _definitions = new Definitions(definitions);
		_definitions.remove(x);
		return new Program<T>(_definitions, instances);
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public Program<T> compose(List<Program<T>> bodies) {
		Definitions _definitions = new Definitions(definitions);
		InstanceList<T> _instances = new InstanceList<T>(instances);
		for (Program<T> body : bodies) {
			_definitions.putAll(body.definitions);
			_instances.compose(body.instances);
		}
		return new Program<T>(_definitions, _instances);
	}
	
	/**
	 * Instantiates this program body by dropping all definitions (except necessary
	 * unifications), joining unified ports, hiding internal ports, and renaming external ports.
	 * @param iface		map assigning a new port to every external port (i.e., this map implicitly defines
	 * all internal ports)
	 * @return an instantiate ProgramValue.
	 */
	public Program<T> instantiate(Map<Port, Port> iface) {
		Definitions _definitions = new Definitions();
		InstanceList<T> _instances = new InstanceList<T>(instances);
		Map<Port, Port> _iface = new HashMap<Port, Port>(iface);		
		
		// Collect all necessary unifications, and rename the variables in these definitions.
		for (Map.Entry<VariableName, Expression> defn : definitions.entrySet()) {
			if (defn.getValue() instanceof VariableName) {
				String a = defn.getKey().getName();
				String b = ((VariableName)defn.getValue()).getName();
				
				Port a_new = iface.get(new Port(a));
				Port b_new = iface.get(new Port(b));
				
				if (a_new != null) {
					if (b_new != null) {
						VariableName x = new VariableName(a_new.getName());
						VariableName y = new VariableName(b_new.getName());
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
		for (Instance<T> inst : _instances)			
			inst.joinAndHide(_iface);
		
		return new Program<T>(_definitions, _instances);
	}

	@Override
	public Program<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		Definitions definitions_p = definitions.evaluate(params);
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
		// TODO Add code to evaluate semantics too.
		return new Program<T>(definitions_p, instances);
	}
	
	@Override
	public String toString() {
		return "" + definitions + instances;
	}
}
