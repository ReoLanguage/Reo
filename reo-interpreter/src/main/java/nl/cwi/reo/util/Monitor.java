package nl.cwi.reo.util;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

// TODO: Auto-generated Javadoc
/**
 * A container for all messages generated during compilation.
 */
public final class Monitor {

	/**
	 * List of all messages.
	 */
	private final List<Message> messages;

	/**
	 * Indicates if this monitor contains error messages.
	 */
	private boolean hasErrors = false;

	/**
	 * Constructs a new compilation monitor.
	 */
	public Monitor() {
		messages = new ArrayList<Message>();
	}

	/**
	 * Adds a new message to this monitor.
	 * 
	 * @param location
	 *            Location
	 * @param msg
	 *            message
	 */
	public void add(@Nullable Location location, String msg) {
		hasErrors = true;
		messages.add(new Message(MessageType.ERROR, location, msg));
	}

	/**
	 * Adds a new message to this monitor.
	 * 
	 * @param msg
	 *            message
	 */
	public void add(String msg) {
		hasErrors = true;
		messages.add(new Message(MessageType.ERROR, msg));
	}

	/**
	 * Adds a new message to this monitor.
	 * 
	 * @param message
	 *            message object
	 */
	public void add(Message message) {
		if (message.getType() == MessageType.ERROR)
			hasErrors = true;
		if (!messages.contains(message))
			messages.add(message);
	}

	/**
	 * Gets the list of messages in the monitor.
	 * 
	 * @return list of messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * Print all messages to the standard output.
	 */
	public void print() {
		for (Message msg : messages)
			System.out.println(msg.toString());
	}

	/**
	 * Checks if this monitor has error messages.
	 * 
	 * @return true if this monitor contains an error message, and false,
	 *         otherwise.
	 */
	public boolean hasErrors() {
		return hasErrors;
	}

	/**
	 * Clears all messages in this monitor.
	 */
	public void clear() {
		messages.clear();
	}
}
