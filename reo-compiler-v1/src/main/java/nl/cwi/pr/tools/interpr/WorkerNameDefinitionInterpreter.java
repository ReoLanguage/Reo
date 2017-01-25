package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.WorkerNameDefinitionContext;

public class WorkerNameDefinitionInterpreter extends
		Interpreter<WorkerNameDefinitionContext, Definitions> {

	//
	// FIELDS
	//

	private final String workerNameText;
	private final TypedName workerNameName;

	//
	// CONSTRUCTORS
	//

	public WorkerNameDefinitionInterpreter(Interpreter<?, ?> parent,
			WorkerNameDefinitionContext context) {

		super(parent, context);

		this.workerNameName = new TypedName(context.workerNameName().getText(),
				Type.WORKER_NAME);
		this.workerNameText = context.workerName().getText();
	}

	//
	// METHODS
	//

	@Override
	public Definitions interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);

		definitions.putWorkerName(workerNameName, workerNameText, this);
		return definitions;
	}
}