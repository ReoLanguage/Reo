package nl.cwi.reo.interpret.connectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import nl.cwi.reo.interpret.ports.Port;

/**
 * This class provides two operations on sets of links. 
 */
public final class Links {
	
	/**
	 * Renames the external ports, and hides all internal ports
	 * @param links		maps external ports to new ports.
	 * @param joins		maps external ports to new ports.
	 */
	public static Map<Port, Port> reconnect(Map<Port, Port> links, Map<Port, Port> joins) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : links.entrySet()) {
			
			Port x = link.getValue();
			boolean hide = true;
			for(Map.Entry<Port, Port> join : joins.entrySet()) {
				Port y = join.getValue();				
				if(join.getKey().equals(x)){
					hide = false;
					newlinks.put(link.getKey(), y.join(join.getKey()));
				}
			}
			
			Port y;
			if (hide){ 
				y = x.hide();
				newlinks.put(link.getKey(), y);
			}
		}
		return newlinks;
	}
	
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
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
				newlinks.put(link.getKey(), link.getValue().rename("#" + i++));
			else 
				newlinks.put(link.getKey(), link.getValue());
		return newlinks;
	}
	
	public static Map<Port, Port> markHidden(Map<Port, Port> links, Map<Port, Port> joins) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : links.entrySet()) {
			
			Port x = link.getValue();
			boolean hide = true;
			for(Map.Entry<Port, Port> join : joins.entrySet()) {		
				if(join.getKey().equals(x)){
					hide = false;
					newlinks.put(link.getKey(), link.getValue());
				}
			}
			
			Port y;
			if (hide){ 
				y = x.hide();
				newlinks.put(link.getKey(), y);
			}
		}
		return newlinks;
	}
}
