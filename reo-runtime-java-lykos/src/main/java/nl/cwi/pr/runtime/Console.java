package nl.cwi.pr.runtime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;
import nl.cwi.pr.runtime.api.Port;

public class Console implements Runnable {

	//
	// FIELDS
	//

	/**
	 * The message buffer to store messages in before actually printing them to
	 * the console. Printing happens only once each execution iteration (namely
	 * at the end).
	 */
	private final MessageBuffer buffer = new MessageBuffer();

	/**
	 * Threads to perform get operations.
	 */
	private final Map<Port, IoThread> getThreads = new HashMap<>();

	/**
	 * A map from names to the input ports this console has access to.
	 */
	private final Map<String, Port> namesToInputPorts = new HashMap<>();

	/**
	 * A map from names to the output ports this console has access to.
	 */
	private final Map<String, Port> namesToOutputPorts = new HashMap<>();

	/**
	 * A map from the ports this console has access to to their names.
	 */
	private final Map<Port, String> portsToNames = new LinkedHashMap<>();

	/**
	 * Threads to perform put operations.
	 */
	private final Map<Port, IoThread> putThreads = new HashMap<>();

	//
	// CONSTRUCTORS
	//

	/**
	 * Constructs a console that has access to the input ports
	 * <code>inputPorts</code> and the output ports <code>outputPorts</code>.
	 * 
	 * @param inputPorts
	 *            The input ports. Not <code>null</code>.
	 * @param outputPorts
	 *            The output ports. Not <code>null</code>.
	 * @throws NullPointerException
	 *             If <code>inputPorts==null</code> or
	 *             <code>inputPorts.containsKey(null)</code> or
	 *             <code>inputPorts.containsValue(null)</code> or
	 *             <code>outputPorts==null</code> or
	 *             <code>outputPorts.containsKey(null)</code> or
	 *             <code>outputPorts.containsValue(null)</code>.
	 */
	public Console(Map<String, Port> inputPorts, Map<String, Port> outputPorts) {
		if (inputPorts == null)
			throw new NullPointerException();
		if (inputPorts.containsKey(null))
			throw new NullPointerException();
		if (inputPorts.containsValue(null))
			throw new NullPointerException();
		if (outputPorts == null)
			throw new NullPointerException();
		if (outputPorts.containsKey(null))
			throw new NullPointerException();
		if (outputPorts.containsValue(null))
			throw new NullPointerException();

		this.namesToInputPorts.putAll(inputPorts);
		this.namesToOutputPorts.putAll(outputPorts);

		List<String> portNames = new ArrayList<String>();
		portNames.addAll(inputPorts.keySet());
		portNames.addAll(outputPorts.keySet());
		Collections.sort(portNames);

		for (String s : portNames)
			this.portsToNames.put(inputPorts.containsKey(s) ? inputPorts.get(s)
					: outputPorts.get(s), s);

		for (Port p : inputPorts.values())
			putThreads.put(p, new IoThread());
		for (Port p : outputPorts.values())
			getThreads.put(p, new IoThread());
	}

	//
	// METHODS - PUBLIC
	//

	/**
	 * <em>Inherited documentation:</em>
	 * 
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 */
	public void run() {

		/*
		 * Create commands
		 */

		final Map<String, Command> commands = new HashMap<String, Command>();
		commands.put("", new CommandReport());
		commands.put("exit", new CommandExit());
		commands.put("get", new CommandGet());
		commands.put("inspect", new CommandInspect());
		commands.put("name", new CommandName());
		commands.put("put", new CommandPut());
		commands.put("report", new CommandReport());

		/*
		 * Loop
		 */

		for (IoThread thr : getThreads.values())
			thr.start();
		for (IoThread thr : putThreads.values())
			thr.start();

		Scanner scanner = new Scanner(System.in);

		System.out.println("\nHello.\n");
		boolean terminate = false;
		while (!terminate) {

			/*
			 * Take input
			 */

			System.out.print("> ");
			String line = scanner.nextLine();

			/*
			 * Process input
			 */

			String[] tokens = line.split(" ");
			String command = tokens[0];
			String[] arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
			try {
				if (commands.containsKey(command))
					terminate = commands.get(command).invokeOrThrow(arguments);
				else
					buffer.addControlMessage("I do not know the command \""
							+ command + "\".");

			} catch (CommandException exception) {
				buffer.addControlMessage("Error: " + exception.getMessage());
			}

			/*
			 * Write output
			 */

			System.out.println(buffer.getThenClear());
		}

		scanner.close();
		System.exit(0);
	}

	//
	// METHODS - PRIVATE
	//

	/**
	 * Gets the input port named <code>portName</code> accessible through this
	 * console.
	 * 
	 * @param portName
	 *            The port name. Not <code>null</code>.
	 * @return An input port. Never <code>null</code>.
	 * @throws NullPointerException
	 *             If <code>portName==null</code>.
	 * @throws IllegalStateException
	 *             If <code>!hasInputPort(portName)</code>.
	 * 
	 * @see #hasInputPort(String)
	 */
	private Port getInputPort(String portName) {
		if (portName == null)
			throw new NullPointerException();
		if (!hasInputPort(portName))
			throw new IllegalStateException();

		return namesToInputPorts.get(portName);
	}

	/**
	 * Gets the output port named <code>portName</code> accessible through this
	 * console.
	 * 
	 * @param portName
	 *            The port name. Not <code>null</code>.
	 * @return An output port. Never <code>null</code>.
	 * @throws NullPointerException
	 *             If <code>portName==null</code>.
	 * @throws IllegalStateException
	 *             If <code>!hasOutputPort(portName)</code>.
	 * 
	 * @see #hasOutputPort(String)
	 */
	private Port getOutputPort(String portName) {
		if (portName == null)
			throw new NullPointerException();
		if (!hasOutputPort(portName))
			throw new IllegalStateException();

		return namesToOutputPorts.get(portName);
	}

	/**
	 * Checks if this console has access to an input port named
	 * <code>portName</code>.
	 * 
	 * @param portName
	 *            The port name. Not <code>null</code>.
	 * @return <code>true</code> if this console has access to an input port
	 *         named <code>portName</code>; <code>false</code> otherwise.
	 */
	private boolean hasInputPort(String portName) {
		if (portName == null)
			throw new NullPointerException();

		return namesToInputPorts.containsKey(portName);
	}

	/**
	 * Checks if this console has access to an output port named
	 * <code>portName</code>.
	 * 
	 * @param portName
	 *            The port name. Not <code>null</code>.
	 * @return <code>true</code> if this console has access to an output port
	 *         named <code>portName</code>; <code>false</code> otherwise.
	 */
	private boolean hasOutputPort(String portName) {
		if (portName == null)
			throw new NullPointerException();

		return namesToOutputPorts.containsKey(portName);
	}

	//
	// CLASSES - PRIVATE
	//

	private abstract class Command {

		//
		// CONSTRUCTORS
		//

		/**
		 * Invokes this command. Throws an exception on failure.
		 * 
		 * @param arguments
		 *            The arguments of this command. Not <code>null</code>.
		 * @return <code>true</code> if the console running this command should
		 *         terminate afterwards; <code>false</code> otherwise.
		 * @throws CommandException
		 *             If something goes wrong while invoking.
		 * @throws NullPointerException
		 *             If <code>arguments==null</code> or
		 *             <code>[arguments[i]==null</code> for some <code>i]</code>
		 *             .
		 */
		protected boolean invokeOrThrow(String[] arguments)
				throws CommandException {

			if (arguments == null)
				throw new NullPointerException();
			for (String str : arguments)
				if (str == null)
					throw new NullPointerException();

			return false;
		}

		//
		// METHODS - PROTECTED
		//

		/**
		 * Extracts a datum text from the arguments <code>arguments</code>.
		 * Throws an exception on failure.
		 * 
		 * @param arguments
		 *            The arguments. Not <code>null</code>.
		 * @param index1
		 *            The index in <code>arguments</code> where the datum text
		 *            starts (inclusive).
		 * @param index2
		 *            The index in <code>arguments</code> where the datum text
		 *            stops (exclusive).
		 * @return A nonempty string. Never <code>null</code>.
		 * @throws CommandException
		 *             If something goes wrong while extracting.
		 * @throws IllegalArgumentException
		 *             If <code>index1&lt;0</code> or
		 *             <code>index2&gt;arguments.length</code> or
		 *             <code>index2&lt;index1</code>.
		 * @throws NullPointerException
		 *             If <code>arguments==null</code> or
		 *             <code>[arguments[i]==null</code> for some <code>i]</code>
		 *             .
		 */
		protected String extractDatumTextOrThrow(String[] arguments,
				int index1, int index2) throws CommandException {

			if (arguments == null)
				throw new NullPointerException();
			for (String str : arguments)
				if (str == null)
					throw new NullPointerException();

			if (index1 < 0 || index2 > arguments.length || index2 < index1)
				throw new IllegalArgumentException();

			StringBuilder builder = new StringBuilder();
			for (int i = index1; i < index2; i++)
				builder.append(arguments[i]).append(" ");

			if (builder.length() == 0)
				throw new CommandException(
						"Please specify a datum text (as arguments starting from position "
								+ index1 + ").");

			return builder.toString().toString().trim();
		}

		/**
		 * Extracts a port from the arguments <code>arguments</code>. Throws an
		 * exception upon failure.
		 * 
		 * @param arguments
		 *            The arguments. Not <code>null</code>.
		 * @param index
		 * @return A port. Never <code>null</code>.
		 * @throws CommandException
		 *             If something goes wrong while extracting.
		 * @throws IllegalArgumentException
		 *             If <code>index&lt;0</code>.
		 * @throws NullPointerException
		 *             If <code>arguments==null</code> or
		 *             <code>[arguments[i]==null</code> for some <code>i]</code>
		 *             .
		 */
		protected Port extractPortOrThrow(String[] arguments, int index)
				throws CommandException {

			if (arguments == null)
				throw new NullPointerException();
			for (String str : arguments)
				if (str == null)
					throw new NullPointerException();

			if (index < 0)
				throw new IllegalArgumentException();

			/*
			 * Get portText
			 */

			if (arguments.length <= index)
				throw new CommandException(
						"Please specify a port name or reference (as an argument at position "
								+ (index + 1) + ").");

			String portText = arguments[index];

			/*
			 * Interpret portText as a reference
			 */

			try {
				Iterator<Port> iterator = portsToNames.keySet().iterator();
				for (int i = 0; i < Integer.parseInt(portText); i++)
					if (iterator.hasNext())
						iterator.next();

				if (iterator.hasNext())
					return iterator.next();

				throw new CommandException(
						"I failed to parse the argument at position "
								+ (index + 1)
								+ " to a port: I failed to dereference the provided reference.");
			}

			/*
			 * Interpret portText as a name
			 */

			catch (NumberFormatException exception) {
				if (!hasInputPort(portText) && !hasOutputPort(portText))
					throw new CommandException(
							"I failed to parse the argument at position "
									+ (index + 1)
									+ " to a port: I do not have access to a port named \""
									+ portText + "\".");

				return hasInputPort(portText) ? getInputPort(portText)
						: getOutputPort(portText);
			}
		}
	}

	@SuppressWarnings("serial")
	private class CommandException extends Exception {

		//
		// CONSTRUCTORS
		//

		private CommandException(final String message) {
			super(message);
		}
	}

	private class CommandExit extends Command {

		/**
		 * <em>Inherited documentation:</em>
		 * 
		 * <p>
		 * {@inheritDoc}
		 * </p>
		 */
		@Override
		protected boolean invokeOrThrow(String[] arguments)
				throws CommandException {

			super.invokeOrThrow(arguments);
			buffer.addControlMessage("Bye.");
			return true;
		}
	}

	private class CommandGet extends Command {

		/**
		 * <em>Inherited documentation:</em>
		 * 
		 * <p>
		 * {@inheritDoc}
		 * </p>
		 */
		@Override
		protected boolean invokeOrThrow(final String[] arguments)
				throws CommandException {

			super.invokeOrThrow(arguments);

			/*
			 * Extract the output port to get from
			 */

			final Port port = extractPortOrThrow(arguments, 0);
			final String portName = Console.this.portsToNames.get(port);

			if (!Console.this.namesToOutputPorts.containsKey(portName))
				throw new CommandException("The port named \"" + portName
						+ "\" is not an output port.");

			/*
			 * Get
			 */

			buffer.addControlMessage("I will attempt to get a datum from the port named \""
					+ portName + "\".");

			getThreads.get(port).addTask(new IoTask() {
				@Override
				public void run() {
					try {
						Object datum = ((InputPort) port).get();
						buffer.addEventMessage("I got the datum "
								+ Datum.convertToString(datum)
								+ " from the port named \"" + portName + "\".");
					}

					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

			return false;
		}
	}

	private class CommandInspect extends Command {

		/**
		 * <em>Inherited documentation:</em>
		 * 
		 * <p>
		 * {@inheritDoc}
		 * </p>
		 */
		@Override
		protected boolean invokeOrThrow(String[] arguments)
				throws CommandException {

			super.invokeOrThrow(arguments);

			Port port = extractPortOrThrow(arguments, 0);
			Collection<IoTask> gets = getThreads.containsKey(port) ? getThreads
					.get(port).getTasks() : Collections.<IoTask> emptyList();
			Collection<IoTask> puts = putThreads.containsKey(port) ? putThreads
					.get(port).getTasks() : Collections.<IoTask> emptyList();

			StringBuilder builder = new StringBuilder()
					.append("The port named \"").append(portsToNames.get(port))
					.append("\" has ")
					.append(gets.size() == 0 ? "no" : gets.size())
					.append(" pending get operation")
					.append((gets.size() == 1 ? "" : "s")).append(" and ")
					.append(puts.size() == 0 ? "no" : "the following")
					.append(" pending put operation")
					.append(puts.size() == 1 ? "" : "s")
					.append(puts.size() == 0 ? "." : ":\n");

			for (IoTask t : puts)
				builder.append("\n  - " + Datum.convertToString(t.getDatum()));

			buffer.addControlMessage(builder.toString());
			return false;
		}
	}

	private class CommandName extends Command {

		/**
		 * <em>Inherited documentation:</em>
		 * 
		 * <p>
		 * {@inheritDoc}
		 * </p>
		 */
		@Override
		protected boolean invokeOrThrow(String[] arguments)
				throws CommandException {

			super.invokeOrThrow(arguments);

			StringBuilder builder = new StringBuilder();
			Iterator<String> iterator = Console.this.portsToNames.values()
					.iterator();

			switch (Console.this.portsToNames.size()) {
			case 0:
				builder.append("I do not have access to any ports.");
				break;
			case 1:
				builder.append("I have access to the port named \"")
						.append(iterator.next()).append("\" [0].");
				break;
			case 2:
				builder.append("I have access to the ports named \"")
						.append(iterator.next()).append("\" [0] and \"")
						.append(iterator.next()).append("\" [1].");
				break;
			default:
				builder.append("I have access to the following ports:\n - \"")
						.append(iterator.next()).append("\" [0]");
				int index = 1;
				while (iterator.hasNext())
					builder.append("\n - \"").append(iterator.next())
							.append("\" [").append(index++).append("]");
			}

			buffer.addControlMessage(builder.toString());
			return false;
		}
	}

	private class CommandPut extends Command {

		/**
		 * <em>Inherited documentation:</em>
		 * 
		 * <p>
		 * {@inheritDoc}
		 * </p>
		 */
		@Override
		protected boolean invokeOrThrow(final String[] arguments)
				throws CommandException {

			super.invokeOrThrow(arguments);

			/*
			 * Extract the input port to put to
			 */

			final Port port = extractPortOrThrow(arguments, 0);
			final String portName = Console.this.portsToNames.get(port);

			if (!Console.this.namesToInputPorts.containsKey(portName))
				throw new CommandException("The port named \"" + portName
						+ "\" is not an input port.");

			/*
			 * Extract the datum to put
			 */
			final String datumText = extractDatumTextOrThrow(arguments, 1,
					arguments.length);

			if (!Datum.canConvertToObject(datumText))
				throw new CommandException(
						"I failed to parse the arguments starting from position 2 to a datum.");

			final Serializable datum = Datum.convertToObject(datumText);

			/*
			 * Put
			 */

			buffer.addControlMessage("I will attempt to put the datum "
					+ datumText + " to the port named \"" + portName + "\".");

			putThreads.get(port).addTask(new IoTask(datum) {
				@Override
				public void run() {
					try {
						((OutputPort) port).put(datum);
						buffer.addEventMessage("I put the datum "
								+ Datum.convertToString(datum)
								+ " to the port named \"" + portName + "\".");
					}

					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

			return false;
		}
	}

	private class CommandReport extends Command {

		/**
		 * <em>Inherited documentation:</em>
		 * 
		 * <p>
		 * {@inheritDoc}
		 * </p>
		 */
		@Override
		protected boolean invokeOrThrow(final String[] arguments)
				throws CommandException {

			super.invokeOrThrow(arguments);
			return false;
		}
	}

	private class MessageBuffer {

		/**
		 * Buffer for the control messages added since the last invocation of
		 * {@link #getThenClear()}.
		 */
		private final List<Entry<Long, String>> controlBuffer = new ArrayList<>();

		/**
		 * Buffer for the event messages added since the last invocation of
		 * {@link #getThenClear()}.
		 */
		private final List<Entry<Long, String>> eventBuffer = new ArrayList<>();

		//
		// METHODS
		//

		/**
		 * Adds the control message <code>message</code> to this buffer.
		 * 
		 * @param message
		 *            The message. Not <code>null</code>.
		 * @throws NullPointerException
		 *             If <code>message==null</code>.
		 */
		public void addControlMessage(String message) {
			if (message == null)
				throw new NullPointerException();

			long time = System.currentTimeMillis();
			synchronized (controlBuffer) {
				controlBuffer.add(new SimpleEntry<Long, String>(time, message));
			}
		}

		/**
		 * Adds the event message <code>message</code> to this buffer.
		 * 
		 * @param message
		 *            The message. Not <code>null</code>.
		 * @throws NullPointerException
		 *             If <code>message==null</code>.
		 */
		public void addEventMessage(String message) {
			if (message == null)
				throw new NullPointerException();

			long time = System.currentTimeMillis();
			synchronized (eventBuffer) {
				eventBuffer.add(new SimpleEntry<Long, String>(time, message));
			}
		}

		/**
		 * Gets the buffered messages as a formatted string, then clears this
		 * message buffer.
		 * 
		 * @return A string. Never <code>null</code>.
		 */
		public String getThenClear() {
			StringBuilder builder = new StringBuilder();

			synchronized (controlBuffer) {
				for (Entry<Long, String> e : controlBuffer)
					builder.append("\n").append(e.getValue()).append("\n");

				controlBuffer.clear();
			}

			synchronized (eventBuffer) {
				if (builder.length() > 0 && eventBuffer.isEmpty())
					return builder.toString();

				builder.append("\n")
						.append(eventBuffer.isEmpty() ? "No" : "The following")
						.append(" event")
						.append(eventBuffer.size() == 1 ? "" : "s")
						.append(" occurred since our last interaction")
						.append(eventBuffer.isEmpty() ? "." : ":\n");

				for (Entry<Long, String> e : eventBuffer)
					builder.append("\n  - [")
							.append(new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss.SSS").format(new Date(
									e.getKey()))).append("] ")
							.append(e.getValue());

				eventBuffer.clear();
			}

			return builder.append("\n").toString();
		}
	}

	private abstract class IoTask implements Runnable {

		/**
		 * The datum involed in this task
		 */
		private Object datum;

		//
		// CONSTRUCTORS
		//

		/**
		 * Constructs an empty task.
		 */
		public IoTask() {
			this.datum = null;
		}

		/**
		 * Constructs a task involving the datum <code>datum</code>.
		 * 
		 * @param datum
		 *            The datum. Not <code>null</code>.
		 * @throws NullPointerException
		 *             If <code>datum==null</code>.
		 */
		public IoTask(Object datum) {
			if (datum == null)
				throw new NullPointerException();

			this.datum = datum;
		}

		//
		// METHODS - PUBLIC
		//

		/**
		 * Checks if this task has a datum.
		 * 
		 * @return <code>true</code> if this task has a datum;
		 *         <code>false</code> otherwise.
		 */
		public boolean hasDatum() {
			return datum != null;
		}

		/**
		 * Gets the datum involved in this task.
		 * 
		 * @return An object. Never <code>null</code>.
		 * @throws IllegalStateException
		 *             If <code>!hasDatum()</code>.
		 */
		public Object getDatum() {
			if (!hasDatum())
				throw new IllegalStateException();

			return datum;
		}
	}

	private class IoThread extends Thread {

		/**
		 * A semaphore to count the pending tasks in the queue.
		 */
		private final Semaphore semaphore = new Semaphore(0);

		/**
		 * A lock to protect the queue.
		 */
		private final Lock lock = new ReentrantLock();

		/**
		 * A queue of pending tasks.
		 */
		private final Queue<IoTask> queue = new LinkedList<>();

		//
		// METHODS - PUBLIC
		//

		/**
		 * Adds the task <code>task</code> to the queue of pending tasks of this
		 * thread.
		 * 
		 * @param task
		 *            The task. Not <code>null</code>.
		 * @throws NullPointerException
		 *             if <code>task==null</code>.
		 * 
		 */
		public void addTask(IoTask task) {
			if (task == null)
				throw new NullPointerException();

			lock.lock();
			queue.offer(task);
			lock.unlock();
			semaphore.release();
		}

		/**
		 * Gets the pending tasks of this thread.
		 * 
		 * @return A collection of tasks. Never <code>null</code>.
		 */
		public Collection<IoTask> getTasks() {
			return Collections.unmodifiableCollection(queue);
		}

		/**
		 * <em>Inherited documentation:</em>
		 * 
		 * <p>
		 * {@inheritDoc}
		 * </p>
		 */
		@Override
		public void run() {
			while (true) {
				semaphore.acquireUninterruptibly();
				lock.lock();
				IoTask task = queue.peek();
				lock.unlock();
				task.run();
				lock.lock();
				queue.poll();
				lock.unlock();
			}
		}
	}
}
