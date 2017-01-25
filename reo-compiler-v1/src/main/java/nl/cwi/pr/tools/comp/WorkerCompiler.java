package nl.cwi.pr.tools.comp;

import nl.cwi.pr.tools.InterpretedWorker;

public abstract class WorkerCompiler<C extends ProgramCompiler> extends
		GenerateeCompiler<C, InterpretedWorker> {

	//
	// CONSTRUCTORS
	//

	public WorkerCompiler(C parent, InterpretedWorker worker) {
		super(parent, worker);
	}
}