package nl.cwi.reo.interpret.semantics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

public class InstanceList extends ArrayList<Instance> implements Evaluable<InstanceList> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4691832430455957713L;
	
	/**
	 * Counter for generating fresh port names.
	 */
	private int i;
	
	/**
	 * Constructs and empty list of instances.
	 */
	public InstanceList() { }
	
	/**
	 * Constructs a singleton list with 
	 * @param atom
	 */
	public InstanceList(Semantics<?> atom) {
		if (atom == null)
			throw new NullPointerException();
		super.add(new Instance(atom));
	}
	
	/**
	 * Copy constructor.
	 * @param instances
	 */
	public InstanceList(List<Instance> instances) {
		if (instances == null)
			throw new NullPointerException();
		for (Instance inst : instances) {
			if (inst == null)
				throw new NullPointerException();
			super.add(new Instance(inst));
		}
	}
	
	/**
	 * Composes this list of instances with another list of instances, and
	 * renames all hidden ports to avoid sharing of internal ports.
	 * @param list 		other list of instances
	 */
	public void compose(InstanceList list) {
		i = this.renameHidden(i);
		list.renameHidden(i);
		this.addAll(list);
	}
	
	/**
	 * Renames all hidden ports in this list of instances to an integer value, 
	 * starting from a given integer i.
	 * @param i		start value of hidden ports.
	 * @return the smallest integer greater or equal to i, that not used as a port name.
	 */
	private int renameHidden(int i) {
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Instance comp : this) {
			for (Map.Entry<String, Port> link : comp.entrySet()) 
				if (!links.containsKey(link.getValue().getName()))
					if (link.getValue().isHidden()) {
						links.put(link.getValue(), new Port("#" + i++));
					}
			comp.join(links);
		}
		return i;
	}

	@Override
	public InstanceList evaluate(Map<VariableName, Expression> params) throws Exception {
		InstanceList _instances = new InstanceList(this); 
		for (Instance comp : _instances)
			comp.evaluate(params);
		return _instances;
	}
}
