package nl.cwi.reo.pr.misc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.misc.PortFactory.Port;

public class MemberSignature {

	//
	// FIELDS
	//

	private final List<Port> inputPorts = new ArrayList<>();
	private final List<Port> outputPorts = new ArrayList<>();

	private final Map<TypedName, Extralogical> extralogicals = new LinkedHashMap<>();
	private final Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
	private final Map<TypedName, Integer> integers = new LinkedHashMap<>();
	private final Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();

	private final PortFactory portFactory;
	private final String string;
	private final TypedName name;

	//
	// CONSTRUCTORS
	//

	public MemberSignature(TypedName name, Map<TypedName, Integer> integers,
			Map<TypedName, Extralogical> extralogicals,
			Map<TypedName, PortOrArray> inputPortsOrArrays,
			Map<TypedName, PortOrArray> outputPortsOrArrays,
			PortFactory portFactory) {

		if (name == null)
			throw new NullPointerException();
		if (integers == null)
			throw new NullPointerException();
		if (extralogicals == null)
			throw new NullPointerException();
		if (inputPortsOrArrays == null)
			throw new NullPointerException();
		if (outputPortsOrArrays == null)
			throw new NullPointerException();
		if (portFactory == null)
			throw new NullPointerException();
		if (integers.containsKey(null))
			throw new NullPointerException();
		if (extralogicals.containsKey(null))
			throw new NullPointerException();
		if (inputPortsOrArrays.containsKey(null))
			throw new NullPointerException();
		if (outputPortsOrArrays.containsKey(null))
			throw new NullPointerException();
		if (integers.containsValue(null))
			throw new NullPointerException();
		if (extralogicals.containsValue(null))
			throw new NullPointerException();
		if (inputPortsOrArrays.containsValue(null))
			throw new NullPointerException();
		if (outputPortsOrArrays.containsValue(null))
			throw new NullPointerException();

		this.name = name;
		this.portFactory = portFactory;

		this.integers.putAll(integers);
		this.extralogicals.putAll(extralogicals);
		this.inputPortsOrArrays.putAll(inputPortsOrArrays);
		this.outputPortsOrArrays.putAll(outputPortsOrArrays);

		for (PortOrArray pOrArr : inputPortsOrArrays.values())
			if (pOrArr instanceof Port)
				this.inputPorts.add((Port) pOrArr);
			else
				this.inputPorts.addAll(((Array) pOrArr).values());

		for (PortOrArray pOrArr : outputPortsOrArrays.values())
			if (pOrArr instanceof Port)
				this.outputPorts.add((Port) pOrArr);
			else
				this.outputPorts.addAll(((Array) pOrArr).values());

		/*
		 * Initialize this.string
		 */

		String integersString = "";
		String extralogicalsString = "";
		String inputPortsOrArraysString = "";
		String outputPortsOrArraysString = "";

		if (!integers.isEmpty())
			integersString = integers.values().toString().replaceAll(" ", "");

		if (!extralogicals.isEmpty())
			extralogicalsString = extralogicals.values().toString()
					.replaceAll(" ", "").replace('[', '<').replace(']', '>');

		if (!inputPortsOrArrays.isEmpty())
			inputPortsOrArraysString = inputPortsOrArrays.values().toString()
					.replaceAll(" ", "");

		if (!outputPortsOrArrays.isEmpty())
			outputPortsOrArraysString = outputPortsOrArrays.values().toString()
					.replaceAll(" ", "");

		inputPortsOrArraysString = inputPortsOrArraysString.isEmpty() ? ""
				: inputPortsOrArraysString.substring(1,
						inputPortsOrArraysString.length() - 1);
		outputPortsOrArraysString = outputPortsOrArraysString.isEmpty() ? ""
				: outputPortsOrArraysString.substring(1,
						outputPortsOrArraysString.length() - 1);

		this.string = name.getName() + integersString + extralogicalsString
				+ "(" + inputPortsOrArraysString + ";"
				+ outputPortsOrArraysString + ")";
	}

	//
	// METHODS
	//

	@Override
	public boolean equals(Object obj) {
		return obj instanceof MemberSignature && equals((MemberSignature) obj);
	}

	public boolean equals(MemberSignature signature) {
		return signature != null && toString().equals(signature.toString());
	}

	public Map<TypedName, Extralogical> getExtralogicals() {
		return extralogicals;
	}

	public List<Port> getInputPorts() {
		return inputPorts;
	}

	public Map<TypedName, PortOrArray> getInputPortsOrArrays() {
		return inputPortsOrArrays;
	}

	public List<Port> getInputPortsSorted() {
		return portFactory.newSet(inputPorts).getSorted();
	}

	public Map<TypedName, Integer> getIntegers() {
		return integers;
	}

	public TypedName getName() {
		return name;
	}

	public List<Port> getOutputPorts() {
		return outputPorts;
	}

	public Map<TypedName, PortOrArray> getOutputPortsOrArrays() {
		return outputPortsOrArrays;
	}

	public List<Port> getOutputPortsSorted() {
		return portFactory.newSet(outputPorts).getSorted();
	}

	public PortFactory getPortFactory() {
		return portFactory;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return string;
	}
}