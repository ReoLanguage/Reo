package nl.cwi.reo.interpret;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

public final class Instance {
	
	private final Semantics<?> atom;
	
	private final Map<String, Port> links;
	
	public Instance(Semantics<?> atom, Map<String, Port> links) {
		if (atom == null || links == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.atom = atom;
		this.links = Collections.unmodifiableMap(links);
	}
	
	public Semantics<?> getAtom() {
		return atom;
	}
	
	public Map<String, Port> getLinks() {
		return links;
	}
	
	public Instance renameHidden(Integer i) {
		Map<String, Port> newlinks = new HashMap<String, Port>();
		for (Map.Entry<String, Port> link : links.entrySet()) {
			String a = link.getKey();
			Port p = link.getValue();
			if (p.isHidden()) {
				newlinks.put(a, p.rename("#" + i++)); 
			} else {
				newlinks.put(a, p); 						
			}		
		}
		return new Instance(atom, newlinks);
	}
	
	public Instance restrictAndRename(Map<Port, Port> iface) {
		Map<String, Port> newlinks = new HashMap<String, Port>();
		for (Map.Entry<String, Port> link : links.entrySet()) {
			Port n = iface.get(link.getValue());
			if (n == null) n = link.getValue().hide();
			newlinks.put(link.getKey(), n);
		}
		return new Instance(atom, newlinks);	
	}
	
	/**
	 * Get the string representation of a program.
	 */
	public String toString() {
		return links + "\n" + atom + "\n";
	}
}
