package nl.cwi.reo.lykos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.cwi.reo.pr.misc.Code;
import nl.cwi.reo.pr.misc.Variable;
import nl.cwi.reo.pr.misc.MainArgumentFactory.MainArgument;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.misc.PortFactory.PortType;

public class WorkerSignature {
	private final List<Variable> variables;
	private final List<Port> inputPorts;
	private final String name;
	private final List<Port> outputPorts;
	private final List<MainArgument> mainArguments;

	//
	// CONSTRUCTORS
	//

	public WorkerSignature(String name, List<Variable> variables) {
		if (name == null)
			throw new NullPointerException();
		if (variables == null)
			throw new NullPointerException();
		if (variables.contains(null))
			throw new NullPointerException();

		this.variables = Collections.unmodifiableList(variables);
		this.name = name;

		List<Port> inputPorts = new ArrayList<>();
		List<Port> outputPorts = new ArrayList<>();
		List<MainArgument> mainArguments = new ArrayList<>();

		for (Variable v : variables)
			if (v instanceof Port) {
				Port port = (Port) v;
				if (port.hasAnnotation(Port.ANNOTATION_PORT_TYPE,
						PortType.class))
					switch (port.getAnnotation(Port.ANNOTATION_PORT_TYPE,
							PortType.class)) {

					case INPUT:
						inputPorts.add(port);
						break;
					case OUTPUT:
						outputPorts.add(port);
						break;
					default:
						break;
					}
			}

			else if (v instanceof MainArgument)
				mainArguments.add((MainArgument) v);

			else if (v instanceof Code)
				;

			else
				// arg is an instance of an anonymous inner class
				;

		Collections.sort(inputPorts);
		Collections.sort(outputPorts);
		Collections.sort(mainArguments);

		this.inputPorts = Collections.unmodifiableList(inputPorts);
		this.outputPorts = Collections.unmodifiableList(outputPorts);
		this.mainArguments = Collections.unmodifiableList(mainArguments);
	}

	//
	// METHODS
	//

	public List<Port> getInputPorts() {
		return inputPorts;
	}

	public List<MainArgument> getMainArguments() {
		return mainArguments;
	}

	public String getName() {
		return name;
	}

	public List<Port> getOutputPorts() {
		return outputPorts;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	@Override
	public String toString() {
		return name + getVariables().toString().replaceAll(" ", "");
	}
}
