package nl.cwi.reo.semantics.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides two operations on sets of links. 
 */
public class Links {
	
	/**
	 * Renames the external ports, and hides all internal ports
	 * @param links		maps external ports to new ports.
	 * @param joins		maps external ports to new ports.
	 */
	public static Map<Port, Port> connect(Map<Port, Port> links, Map<Port, Port> joins) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : links.entrySet()) {
			Port x = link.getValue();
			Port y = joins.get(x);
			if (y == null) 
				y = x.hide();
			newlinks.put(link.getKey(), y.join(x));
		}
		return newlinks;
	}
	
	/**
	 * Renames all hidden ports in this list of instances to an 
	 * integer value, starting from a given integer i.
	 * @param i		start value of hidden ports.
	 * @return the smallest integer greater or equal to i, that 
	 * not used as a port name.
	 */
	public static Map<Port, Port> renameHidden(Map<Port, Port> links, Integer i) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : links.entrySet()) 
			if (link.getValue().isHidden())
				newlinks.put(link.getKey(), new Port("#" + i++));
		return newlinks;
	}

}
