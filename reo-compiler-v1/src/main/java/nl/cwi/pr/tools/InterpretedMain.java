package nl.cwi.pr.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.pr.misc.Array;
import nl.cwi.pr.misc.MemberSignature;
import nl.cwi.pr.misc.PortOrArray;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.tools.interpr.MainSignature;
import nl.cwi.pr.tools.interpr.WorkerSignature;

public class InterpretedMain extends InterpretedGeneratee {

	//
	// FIELDS
	//

	private final List<InterpretedProtocol> protocols = new ArrayList<>();
	private final List<InterpretedWorker> workers = new ArrayList<>();

	private final MainSignature signature;

	//
	// CONSTRUCTORS
	//

	public InterpretedMain() {
		this.signature = new MainSignature(Collections.<Port> emptyList(),
				Collections.<Port> emptyList(), Collections.<Port> emptyList(),
				Collections.<Port> emptyList());
	}

	public InterpretedMain(List<InterpretedProtocol> protocols,
			List<InterpretedWorker> workers) {

		if (protocols == null)
			throw new NullPointerException();
		if (protocols.contains(null))
			throw new NullPointerException();
		if (workers == null)
			throw new NullPointerException();
		if (workers.contains(null))
			throw new NullPointerException();

		this.protocols.addAll(protocols);
		this.workers.addAll(workers);

		Set<Port> inputPorts = new HashSet<>();
		Set<Port> outputPorts = new HashSet<>();

		for (InterpretedProtocol pr : protocols) {
			MemberSignature protocolSignature = pr.getSignature();

			for (PortOrArray pOrArr : protocolSignature.getInputPortsOrArrays()
					.values())

				if (pOrArr instanceof Port)
					inputPorts.add((Port) pOrArr);
				else
					inputPorts.addAll(((Array) pOrArr).values());

			for (PortOrArray pOrArr : protocolSignature
					.getOutputPortsOrArrays().values())

				if (pOrArr instanceof Port)
					outputPorts.add((Port) pOrArr);
				else
					outputPorts.addAll(((Array) pOrArr).values());
		}

		Set<Port> freeInputPorts = new HashSet<>(inputPorts);
		Set<Port> freeOutputPorts = new HashSet<>(outputPorts);

		for (InterpretedWorker w : workers) {
			WorkerSignature workerSignature = w.getSignature();

			Set<Port> workerPorts = new HashSet<>();
			workerPorts.addAll(workerSignature.getInputPorts());
			workerPorts.addAll(workerSignature.getOutputPorts());

			freeInputPorts.removeAll(workerPorts);
			freeOutputPorts.removeAll(workerPorts);
		}

		this.signature = new MainSignature(new ArrayList<>(inputPorts),
				new ArrayList<>(outputPorts), new ArrayList<>(freeInputPorts),
				new ArrayList<>(freeOutputPorts));
	}

	//
	// METHODS - PUBLIC
	//

	public List<InterpretedProtocol> getProtocols() {
		return protocols;
	}

	public MainSignature getSignature() {
		return signature;
	}

	public List<InterpretedWorker> getWorkers() {
		return workers;
	}

	public boolean isEmpty() {
		return protocols.isEmpty() && workers.isEmpty();
	}
}
