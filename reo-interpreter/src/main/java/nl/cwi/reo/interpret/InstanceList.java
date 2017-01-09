package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

public class InstanceList extends ArrayList<Instance> implements Evaluable<InstanceList> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4691832430455957713L;
	
	/**
	 * Fresh variable counter.
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
	public InstanceList(Collection<? extends Instance> instances) {
		if (instances == null)
			throw new NullPointerException();
		for (Instance inst : instances) {
			if (inst == null)
				throw new NullPointerException();
			super.add(new Instance(inst));
		}
	}
	
	public int renameHidden(int i) {
		Map<String, String> renaming = new HashMap<String, String>();
		for (Instance comp : this) {
			for (Map.Entry<String, Port> link : comp.entrySet()) 
				if (!renaming.containsKey(link.getValue().getName()))
					if (link.getValue().isHidden()) {
						renaming.put(link.getValue().getName(), "#" + i++);
					}
			comp.rename(renaming);
		}
		return i;
	}
	
	public void compose(InstanceList list) {
		i = this.renameHidden(i);
		list.renameHidden(i);
		this.addAll(list);
	}
	
	/**
	 * Renames the external ports, and hides all internal ports
	 * @param iface		maps external ports to new ports.
	 */
	public void instantiate(Map<Port, Port> iface) {	
		for (Instance comp : this)			
			comp.instantiate(iface);
	}

	@Override
	public InstanceList evaluate(Map<VariableName, Expression> params) throws Exception {
		// TODO Add code to evaluate semantics too.
		return this;
	}
}
