package nl.cwi.pr.tools.interpr;

import java.util.Collections;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.tools.InterpretedMain;
import nl.cwi.pr.tools.InterpretedWorker;
import nl.cwi.pr.tools.pars.PrParser.MainExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.MainExpressionNoWorkersContext;
import nl.cwi.pr.tools.pars.PrParser.MainexpressionWorkersContext;

public abstract class MainExpressionInterpreter extends
		Interpreter<MainExpressionContext, InterpretedMain> {

	//
	// CONSTRUCTORS
	//

	public MainExpressionInterpreter(Interpreter<?, ?> parent,
			MainExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static MainExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, MainExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof MainExpressionNoWorkersContext)
			return new NoWorkers(parent,
					(MainExpressionNoWorkersContext) context);

		else if (context instanceof MainexpressionWorkersContext)
			return new Workers(parent, (MainexpressionWorkersContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class NoWorkers extends MainExpressionInterpreter {

		//
		// FIELDS
		//

		private final ProtocolsExpressionInterpreter protocolsExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public NoWorkers(Interpreter<?, ?> parent,
				MainExpressionNoWorkersContext context) {

			super(parent, context);
			this.protocolsExpressionInterpreter = ProtocolsExpressionInterpreter
					.newInstance(this, context.protocolsExpression());
		}

		//
		// METHODS
		//

		@Override
		public InterpretedMain interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return new InterpretedMain(
					protocolsExpressionInterpreter.interpret(factories,
							definitions, environment),
					Collections.<InterpretedWorker> emptyList());
		}
	}

	public static class Workers extends MainExpressionInterpreter {

		//
		// FIELDS
		//

		private final ProtocolsExpressionInterpreter protocolsExpressionInterpreter;
		private final WorkersExpressionInterpreter workersExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Workers(Interpreter<?, ?> parent,
				MainexpressionWorkersContext context) {

			super(parent, context);

			this.protocolsExpressionInterpreter = ProtocolsExpressionInterpreter
					.newInstance(this, context.protocolsExpression());
			this.workersExpressionInterpreter = WorkersExpressionInterpreter
					.newInstance(this, context.workersExpression());
		}

		//
		// METHODS
		//

		@Override
		public InterpretedMain interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return new InterpretedMain(
					protocolsExpressionInterpreter.interpret(factories,
							definitions, environment),
					workersExpressionInterpreter.interpret(factories,
							definitions, environment));
		}
	}
}
