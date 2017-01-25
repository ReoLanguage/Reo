package nl.cwi.pr.tools.comp;

import java.util.Collections;
import java.util.Map;

import nl.cwi.pr.tools.InterpretedWorker;

public class DefaultWorkerCompiler extends
		WorkerCompiler<DefaultProgramCompiler> {

	//
	// CONSTRUCTORS
	//

	public DefaultWorkerCompiler(DefaultProgramCompiler parent,
			InterpretedWorker worker) {
		super(parent, worker);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected Map<String, String> generateFiles() {
		return Collections.emptyMap();
	}
}
