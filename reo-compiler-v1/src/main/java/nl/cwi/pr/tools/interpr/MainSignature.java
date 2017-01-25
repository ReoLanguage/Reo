package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.cwi.pr.misc.Variable;
import nl.cwi.pr.misc.MainArgumentFactory.MainArgument;
import nl.cwi.pr.misc.PortFactory.Port;

public class MainSignature {
	private final List<Variable> variables;
	private final List<Port> freeInputPorts;
	private final List<Port> freeOutputPorts;
	private final List<Port> inputPorts;
	private final List<Port> outputPorts;
	private final List<MainArgument> mainArguments;

	//
	// CONSTRUCTORS
	//

	public MainSignature(List<Port> inputPorts, List<Port> outputPorts,
			List<Port> freeInputPorts, List<Port> freeOutputPorts) {

		if (inputPorts == null)
			throw new NullPointerException();
		if (inputPorts.contains(null))
			throw new NullPointerException();
		if (outputPorts == null)
			throw new NullPointerException();
		if (outputPorts.contains(null))
			throw new NullPointerException();
		if (freeInputPorts == null)
			throw new NullPointerException();
		if (freeInputPorts.contains(null))
			throw new NullPointerException();
		if (freeOutputPorts == null)
			throw new NullPointerException();
		if (freeOutputPorts.contains(null))
			throw new NullPointerException();

		this.variables = null;

		inputPorts = new ArrayList<>(inputPorts);
		outputPorts = new ArrayList<>(outputPorts);
		freeInputPorts = new ArrayList<>(freeInputPorts);
		freeOutputPorts = new ArrayList<>(freeOutputPorts);

		Collections.sort(inputPorts);
		Collections.sort(outputPorts);
		Collections.sort(freeInputPorts);
		Collections.sort(freeOutputPorts);

		this.freeInputPorts = Collections.unmodifiableList(freeInputPorts);
		this.freeOutputPorts = Collections.unmodifiableList(freeOutputPorts);
		this.inputPorts = Collections.unmodifiableList(inputPorts);
		this.outputPorts = Collections.unmodifiableList(outputPorts);
		this.mainArguments = new ArrayList<>();
	}

	public MainSignature(List<Variable> variables, List<Port> inputPorts,
			List<Port> outputPorts, List<Port> freeInputPorts,
			List<Port> freeOutputPorts, List<MainArgument> mainArguments) {

		if (variables == null)
			throw new NullPointerException();
		if (variables.contains(null))
			throw new NullPointerException();
		if (inputPorts == null)
			throw new NullPointerException();
		if (inputPorts.contains(null))
			throw new NullPointerException();
		if (outputPorts == null)
			throw new NullPointerException();
		if (outputPorts.contains(null))
			throw new NullPointerException();
		if (freeInputPorts == null)
			throw new NullPointerException();
		if (freeInputPorts.contains(null))
			throw new NullPointerException();
		if (freeOutputPorts == null)
			throw new NullPointerException();
		if (freeOutputPorts.contains(null))
			throw new NullPointerException();
		if (mainArguments == null)
			throw new NullPointerException();
		if (mainArguments.contains(null))
			throw new NullPointerException();

		this.variables = Collections.unmodifiableList(variables);

		inputPorts = new ArrayList<>(inputPorts);
		outputPorts = new ArrayList<>(outputPorts);
		freeInputPorts = new ArrayList<>(freeInputPorts);
		freeOutputPorts = new ArrayList<>(freeOutputPorts);
		mainArguments = new ArrayList<>(mainArguments);

		Collections.sort(inputPorts);
		Collections.sort(outputPorts);
		Collections.sort(freeInputPorts);
		Collections.sort(freeOutputPorts);
		Collections.sort(mainArguments);

		this.freeInputPorts = Collections.unmodifiableList(freeInputPorts);
		this.freeOutputPorts = Collections.unmodifiableList(freeOutputPorts);
		this.inputPorts = Collections.unmodifiableList(inputPorts);
		this.outputPorts = Collections.unmodifiableList(outputPorts);
		this.mainArguments = Collections.unmodifiableList(mainArguments);
	}

	//
	// METHODS - PUBLIC
	//

	public boolean addMainArgument(MainArgument mainArgument) {
		if (mainArgument == null)
			throw new NullPointerException();

		return mainArguments.add(mainArgument);
	}

	public List<Variable> getArguments() {
		return variables;
	}

	public List<Port> getFreeInputPorts() {
		return freeInputPorts;
	}

	public List<Port> getFreeOutputPorts() {
		return freeOutputPorts;
	}

	public List<Port> getInputPorts() {
		return inputPorts;
	}

	public List<Port> getOutputPorts() {
		return outputPorts;
	}

	public List<MainArgument> getMainArguments() {
		return mainArguments;
	}
}
