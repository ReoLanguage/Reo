package nl.cwi.pr.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.cwi.pr.autom.Extralogical;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.tools.ToolErrorAccumulator;

public class Environment implements Cloneable {

	//
	// FIELDS
	//

	private final int scope;

	private final Map<Port, Roles> roles;
	private final Map<Port, TypedName> portsInverse = new HashMap<>();

	private final Map<TypedName, Array> arrays = new HashMap<>();
	private final Map<TypedName, Extralogical> extralogicals = new HashMap<>();
	private final Map<TypedName, Integer> integers = new HashMap<>();
	private final Map<TypedName, Port> ports = new HashMap<>();

	private Mode mode = Mode.INPUT;

	//
	// CONSTRUCTORS
	//

	public Environment() {
		this.roles = new HashMap<>();
		this.scope = nextScope++;
	}

	public Environment(Environment prototype) {
		if (prototype == null)
			throw new NullPointerException();

		this.roles = prototype.roles;
		this.scope = prototype.scope;
		this.mode = prototype.mode;

		this.arrays.putAll(prototype.arrays);
		this.extralogicals.putAll(prototype.extralogicals);
		this.integers.putAll(prototype.integers);
		this.ports.putAll(prototype.ports);
	}

	//
	// METHODS
	//

	public boolean containsArray(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return arrays.containsKey(name);
	}

	public boolean containsExtralogical(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return extralogicals.containsKey(name);
	}

	public boolean containsInteger(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return integers.containsKey(name);
	}

	public boolean containsPort(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return ports.containsKey(name);
	}

	public Array getArray(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsArray(name))
			throw new IllegalStateException();

		return arrays.get(name);
	}

	public Extralogical getExtralogical(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsExtralogical(name))
			throw new IllegalStateException();

		return extralogicals.get(name);
	}

	public Integer getInteger(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsInteger(name))
			throw new IllegalStateException();

		return integers.get(name);
	}

	public Mode getMode() {
		return mode;
	}

	public Port getPort(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsPort(name))
			throw new IllegalStateException();

		return ports.get(name);
	}

	public int getScope() {
		return scope;
	}

	public Array putArray(TypedName name, Array array) {
		if (name == null)
			throw new NullPointerException();
		if (array == null)
			throw new NullPointerException();
		if (array.containsKey(null))
			throw new NullPointerException();
		if (array.containsValue(null))
			throw new NullPointerException();

		for (Port p : array.values()) {
			final Role role;
			switch (mode) {
			case INPUT:
				role = Role.OUTPUT_PORT;
				break;
			case OUTPUT:
				role = Role.INPUT_PORT;
				break;
			case WORKER:
				role = null;
				break;
			default:
				throw new Error();
			}

			if (role != null) {
				if (!hasRoles(p))
					addRoles(p);

				getRoles(p).add(role);
			}
		}

		return arrays.put(name, array);
	}

	public Extralogical putExtralogical(TypedName name,
			Extralogical extralogical) {
		if (name == null)
			throw new NullPointerException();
		if (extralogical == null)
			throw new NullPointerException();

		return extralogicals.put(name, extralogical);
	}

	public Integer putInteger(TypedName name, Integer integer) {
		if (name == null)
			throw new NullPointerException();
		if (integer == null)
			throw new NullPointerException();

		return integers.put(name, integer);
	}

	public Port putPort(TypedName name, Port port) {
		if (name == null)
			throw new NullPointerException();
		if (port == null)
			throw new NullPointerException();

		final Role role;
		switch (mode) {
		case INPUT:
			role = Role.OUTPUT_PORT;
			break;
		case OUTPUT:
			role = Role.INPUT_PORT;
			break;
		case WORKER:
			role = null;
			break;
		default:
			throw new Error();
		}

		if (role != null) {
			if (!hasRoles(port))
				addRoles(port);

			getRoles(port).add(role);
		}

		portsInverse.put(port, name);
		return ports.put(name, port);
	}

	public void setMode(Mode newMode) {
		if (mode == null)
			throw new NullPointerException();

		mode = newMode;
	}

	public void updateRole(Port port, ToolErrorAccumulator accumulator) {
		if (port == null)
			throw new NullPointerException();
		if (accumulator == null)
			throw new NullPointerException();

		final Role role;
		switch (mode) {
		case INPUT:
			role = Role.INPUT_PORT;
			break;
		case OUTPUT:
			role = Role.OUTPUT_PORT;
			break;
		case WORKER:
			role = null;
			break;
		default:
			throw new Error();
		}

		if (role != null) {
			if (!hasRoles(port))
				addRoles(port);

			Roles roles = getRoles(port);
			if (roles.contains(role))
				accumulator.addError("Illegal role", false);
			else
				roles.add(role);
		}
	}

	public void validateRoles(ToolErrorAccumulator accumulator) {
		if (accumulator == null)
			throw new NullPointerException();

		for (Entry<Port, Roles> entr : roles.entrySet()) {
			Port port = entr.getKey();
			Roles roles = entr.getValue();
			if (roles.size() == 1)
				accumulator
						.addError(
								"Too few occurrences of port \""
										+ (portsInverse.containsKey(port) ? portsInverse
												.get(port).getName() : port)
										+ "\"", false);
		}
	}

	//
	// METHODS - PRIVATE
	//

	private void addRoles(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (hasRoles(port))
			throw new IllegalStateException();

		roles.put(port, new Roles());
	}

	private Roles getRoles(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (!hasRoles(port))
			throw new IllegalStateException();

		return roles.get(port);
	}

	private boolean hasRoles(Port port) {
		if (port == null)
			throw new NullPointerException();

		return roles.containsKey(port);
	}

	//
	// STATIC - FIELDS
	//

	public static int nextScope = 1;

	//
	// STATIC - ENUMS
	//

	public static enum Mode {
		INPUT, OUTPUT, WORKER
	}
}

class Roles {

	//
	// FIELDS
	//

	private final Set<Role> roles = new HashSet<>();

	//
	// METHODS
	//

	public void add(Role role) {
		if (role == null)
			throw new NullPointerException();
		if (contains(role))
			throw new IllegalStateException();

		roles.add(role);
	}

	public boolean contains(Role role) {
		if (role == null)
			throw new NullPointerException();

		return roles.contains(role);
	}

	public int size() {
		return roles.size();
	}

	@Override
	public String toString() {
		return roles.toString();
	}
}

enum Role {
	INPUT_PORT, OUTPUT_PORT;
}