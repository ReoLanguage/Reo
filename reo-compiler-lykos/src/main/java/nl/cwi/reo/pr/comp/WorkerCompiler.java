package nl.cwi.reo.pr.comp;

import nl.cwi.reo.pr.comp.InterpretedWorker;

public abstract class WorkerCompiler<C extends ProgramCompiler> extends
		GenerateeCompiler<C, InterpretedWorker> {

	//
	// CONSTRUCTORS
	//

	public WorkerCompiler(C parent, InterpretedWorker worker) {
		super(parent, worker);
	}
}