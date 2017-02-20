package nl.cwi.reo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for all messages generated during compilation.
 */
public final class Monitor {

	/**
	 * List of all messages.
	 */
	private final List<Message> messages;
	
	/**
	 * Constructs a new compilation monitor.
	 */
	public Monitor() {
		messages = new ArrayList<Message>();
	}
	
	/**
	 * Add a new message to this monitor.
	 * @param location		Location 
	 * @param msg
	 */
	public void add(Location location, String msg) {
		messages.add(new Message(MessageType.ERROR, location, msg));
	}
	
	/**
	 * Print all messages to the standard output.
	 * @return true if there are some messages printed, and false otherwise.
	 */
	public boolean print() {
		if (messages.isEmpty()) 
			return false;
		for (Message msg : messages)
			System.out.println(msg.toString());
		return true;			
	}
}
