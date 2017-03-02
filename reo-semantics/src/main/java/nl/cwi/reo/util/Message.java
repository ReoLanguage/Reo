package nl.cwi.reo.util;

/**
 * A message generated during compilation.
 */
public final class Message {
	
	/**
	 * Type of message.
	 */
	private final MessageType type;
	
	/**
	 * Location in Reo source file.
	 */
	private final Location location;
	
	/**
	 * Message.
	 */
	private final String message;
	
	/**
	 * Construct a new message of type error with source file location and message
	 * @param location	source file location
	 * @param message	message
	 */
	public Message(Location location, String message) {
		this.type = MessageType.ERROR;
		this.location = location;
		this.message = message;
	}
	
	/**
	 * Construct a new message of given type with a message 
	 * @param type		type
	 * @param message	message
	 */
	public Message(MessageType type, String message) {
		this.type = type;
		this.location = null;
		this.message = message;
	}

	/**
	 * Construct a new message.
	 * @param type		error level
	 * @param location	location in source file
	 * @param message	message
	 */
	public Message(MessageType type, Location location, String message) {
		this.type = type;
		this.location = location;
		this.message = message;
	}
	
	/**
	 * Gets the type of this message.
	 * @return type of this message.
	 */
	public MessageType getType() {
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return type + (location == null ? " " : " (" + location + ") " ) + message; 		
	}
}
