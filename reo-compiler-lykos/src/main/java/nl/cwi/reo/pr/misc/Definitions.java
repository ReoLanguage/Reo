package nl.cwi.reo.pr.misc;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.misc.MainArgumentFactory.MainArgument;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.misc.ToolErrorAccumulator;

public class Definitions {

	//
	// FIELDS
	//

	private final Map<TypedName, Extralogical> extralogicals = new HashMap<>();
	private final Map<TypedName, Family> families = new HashMap<>();
	private final Map<TypedName, Integer> integers = new HashMap<>();
	private final Map<TypedName, String> workerNames = new HashMap<>();

	private final Set<MainArgument> mainArguments = new HashSet<>();
	private final Set<Port> ports = new HashSet<>();

	//
	// METHODS
	//

	public boolean addMainArgument(MainArgument mainArgument) {
		if (mainArgument == null)
			throw new NullPointerException();
		if (containsMainArgument(mainArgument))
			throw new IllegalStateException();

		return mainArguments.add(mainArgument);
	}

	public boolean addPort(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (containsPort(port))
			throw new IllegalStateException();

		return ports.add(port);
	}

	public boolean containsExtralogical(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return extralogicals.containsKey(name);
	}

	public boolean containsFamily(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return families.containsKey(name);
	}

	public boolean containsInteger(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return integers.containsKey(name);
	}

	public boolean containsMainArgument(MainArgument mainArgument) {
		if (mainArgument == null)
			throw new NullPointerException();

		return mainArguments.contains(mainArgument);
	}

	public boolean containsPort(Port port) {
		if (port == null)
			throw new NullPointerException();

		return ports.contains(port);
	}

	public boolean containsWorkerName(TypedName name) {
		if (name == null)
			throw new NullPointerException();

		return workerNames.containsKey(name);
	}

	public Extralogical getExtralogical(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsExtralogical(name))
			throw new IllegalStateException();

		return extralogicals.get(name);
	}

	public Map<TypedName, Extralogical> getExtralogicals() {
		return Collections.unmodifiableMap(extralogicals);
	}

	public Family getFamily(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsFamily(name))
			throw new IllegalStateException();

		return families.get(name);
	}

	public Map<TypedName, Family> getFamilies() {
		return Collections.unmodifiableMap(families);
	}

	public Integer getInteger(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsInteger(name))
			throw new IllegalStateException();

		return integers.get(name);
	}

	public Map<TypedName, Integer> getIntegers() {
		return Collections.unmodifiableMap(integers);
	}

	public String getWorkerName(TypedName name) {
		if (name == null)
			throw new NullPointerException();
		if (!containsWorkerName(name))
			throw new IllegalStateException();

		return workerNames.get(name);
	}

	public Map<TypedName, String> getWorkerNames() {
		return Collections.unmodifiableMap(workerNames);
	}

	public Extralogical putExtralogical(TypedName name,
			Extralogical extralogical, ToolErrorAccumulator accumulator) {

		if (name == null)
			throw new NullPointerException();
		if (extralogical == null)
			throw new NullPointerException();
		if (accumulator == null)
			throw new NullPointerException();

		if (containsExtralogical(name))
			accumulator
					.addError("Duplicate extralogical definition name", true);

		return extralogicals.put(name, extralogical);
	}

	public Family putFamily(TypedName name, Family family,
			ToolErrorAccumulator accumulator) {

		if (name == null)
			throw new NullPointerException();
		if (family == null)
			throw new NullPointerException();
		if (accumulator == null)
			throw new NullPointerException();

		if (containsFamily(name))
			accumulator.addError("Duplicate family definition name", true);

		return families.put(name, family);
	}

	public Integer putInteger(TypedName name, Integer integer,
			ToolErrorAccumulator accumulator) {

		if (name == null)
			throw new NullPointerException();
		if (integer == null)
			throw new NullPointerException();
		if (accumulator == null)
			throw new NullPointerException();

		if (containsInteger(name))
			accumulator.addError("Duplicate integer definition name", true);

		return integers.put(name, integer);
	}

	public String putWorkerName(TypedName name, String workerName,
			ToolErrorAccumulator accumulator) {

		if (name == null)
			throw new NullPointerException();
		if (workerName == null)
			throw new NullPointerException();
		if (accumulator == null)
			throw new NullPointerException();

		if (containsWorkerName(name))
			accumulator.addError("Duplicate worker name name", true);

		return workerNames.put(name, workerName);
	}
}