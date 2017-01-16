package nl.cwi.reo.interpret.semantics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

public class Assembly<T extends Semantics<T>> extends ArrayList<T> {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1924756329606594871L;
	
	private String name;
	
	private Set<Port> iface;
	
	public Assembly(Collection<T> components, String name, Set<Port> iface) {
		if (components == null || name == null || iface == null)
			throw new NullPointerException();
		for (T x : components) {
			if (x == null)
				throw new NullPointerException();
			super.add(x);
		}
		this.name = name;
		Set<Port> _iface = new HashSet<Port>();
		for (Port p : iface) {
			if (p == null)
				throw new NullPointerException();
			_iface.add(p);
		}
		this.iface = Collections.unmodifiableSet(_iface);
	}

	public String getName() {
		return name;
	}

	public Set<Port> getInterface() {
		return iface;
	}
}