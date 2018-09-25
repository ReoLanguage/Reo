package nl.cwi.reo.interpret.connectors;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

/**
 * This class provides two operations on sets of links.
 */
public final class Links {

	/**
	 * Global integer used for generating fresh port names.
	 */
	static int id = 1;

	/**
	 * Renames the a given set of links by concatenating a renaming map. If the
	 * value of link is the key in a renaming map, then the value is renamed to
	 * the value of the renaming map. Otherwise, the port is hidden and renamed
	 * to fresh port.
	 *
	 * @param links
	 *            set of links.
	 * @param r
	 *            renaming map.
	 * @return the map
	 */
	public static Map<Port, Port> rename(Map<Port, Port> links, Map<Port, Port> r) {
		Map<Port, Port> newlinks = new LinkedHashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : links.entrySet()) {
			Port v = link.getValue();
			Port w = r.get(v);
			if (w != null) {
				v = w.join(v);
			} else {
				v = v.rename("$" + id++).hide();
			}
			newlinks.put(link.getKey(), v);
		}
		return newlinks;
	}
}
