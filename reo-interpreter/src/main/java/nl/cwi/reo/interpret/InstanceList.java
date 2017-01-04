package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import nl.cwi.reo.semantics.Port;
/**
 * A program that consist of a set of component instances {@link nl.cwi.reo.semantics.Atom}.
 * Instances synchronize via put and get operations on shared nodes.
 */
public final class InstanceList implements Evaluable<InstanceList> {
	
	/**
	 * Instances.
	 */
	private final List<Instance> instances;
	
	/**
	 * Constructs an empty program.
	 */
	public InstanceList() {
		this.instances = new ArrayList<Instance>();
	}
	
	/**
	 * Constructs a single instance program.
	 */
	public InstanceList(Instance inst) {
		if (inst == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(inst);
		this.instances = Collections.unmodifiableList(instances);
	}

	public InstanceList(List<Instance> instances) {
		if (instances == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.instances = Collections.unmodifiableList(instances);
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
	public InstanceList compose(List<InstanceList> components) {
		List<Instance> newinstances = new ArrayList<Instance>();
		Integer i = 0;
		for (Instance comp : this.instances)
			newinstances.add(comp.renameHidden(i));
		for (InstanceList instlist : components)
			for (Instance comp : instlist.instances)
				newinstances.add(comp.renameHidden(i));
		return new InstanceList(newinstances);
	}
	
	/**
	 * Relabels the interface of this component.
	 * @param iface		maps old node names to new node names.
	 */
	public InstanceList restrictAndRename(Map<Port, Port> iface) {	
		List<Instance> newinstances = new ArrayList<Instance>();
		for (Instance comp : instances)			
			newinstances.add(comp.restrictAndRename(iface));
		return new InstanceList(newinstances);
	}	

	@Override
	public InstanceList evaluate(Map<VariableName, Expression> params) throws Exception {
		return this;
	}
	
	@Override
	public String toString() {
		return "" + instances;
	}
}
