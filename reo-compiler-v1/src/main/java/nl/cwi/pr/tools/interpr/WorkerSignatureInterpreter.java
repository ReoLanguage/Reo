package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.Variable;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.InterpretedWorker;
import nl.cwi.pr.tools.pars.PrParser.WorkerArgumentContext;
import nl.cwi.pr.tools.pars.PrParser.WorkerSignatureContext;

public class WorkerSignatureInterpreter extends
		Interpreter<WorkerSignatureContext, InterpretedWorker> {

	//
	// FIELDS
	//

	private final List<WorkerArgumentInterpreter> workerArgumentInterpreters = new ArrayList<>();
	private final TypedName workerNameName;

	//
	// CONSTRUCTORS
	//

	public WorkerSignatureInterpreter(Interpreter<?, ?> parent,
			WorkerSignatureContext context) {

		super(parent, context);

		this.workerNameName = new TypedName(context.workerName().getText(),
				Type.WORKER_NAME);

		try {
			for (WorkerArgumentContext c : context.workerArgumentList()
					.workerArgument())
				workerArgumentInterpreters.add(WorkerArgumentInterpreter
						.newInstance(this, c));
		}

		catch (NullPointerException exc) {
		}
	}

	//
	// METHODS
	//

	@Override
	public InterpretedWorker interpret(Factories factories,
			Definitions definitions, Environment environment) {

		super.interpret(factories, definitions, environment);

		String workerName;
		if (definitions.containsWorkerName(workerNameName))
			workerName = definitions.getWorkerName(workerNameName);
		else
			workerName = workerNameName.getName();

		List<Variable> arguments = new ArrayList<Variable>();
		for (WorkerArgumentInterpreter interpr : workerArgumentInterpreters)
			arguments.add(interpr
					.interpret(factories, definitions, environment));

		return new InterpretedWorker(new WorkerSignature(workerName, arguments));
	}
}
