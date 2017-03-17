package nl.cwi.reo.interpret.connectors;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

/**
 * This class provides two operations on sets of links.
 */
public final class Links {

	/**
	 * Renames the external ports, and hides all internal ports
	 * 
	 * @param links
	 *            set of links.
	 * @param r
	 *            renaming map.
	 */
	public static Map<Port, Port> rename(Map<Port, Port> links, Map<Port, Port> r) {
		int i = 1;
		Map<Port, Port> newlinks = new HashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : links.entrySet()) {
			Port v = link.getValue();
			Port w = r.get(v);
			if (w != null) {
				v = w.join(v);
			} else {
				v = v.rename("_" + i++).hide();
			}
			newlinks.put(link.getKey(), v);
		}
		return newlinks;
	}

//	/**
//	 * Renames all hidden ports in this list of instances to an integer value,
//	 * starting from a given integer i.
//	 * 
//	 * @param i
//	 *            start value of hidden ports.
//	 * @return the smallest integer greater or equal to i, that not used as a
//	 *         port name.
//	 */
//	public static Map<Port, Port> renameHidden(Map<Port, Port> links, Integer i) {
//		Map<Port, Port> newlinks = new HashMap<Port, Port>();
//		for (Map.Entry<Port, Port> link : links.entrySet())
//			if (link.getValue().isHidden())
//				newlinks.put(link.getKey(), link.getValue().rename("#" + i++));
//			else
//				newlinks.put(link.getKey(), link.getValue());
//		return newlinks;
//	}
}
