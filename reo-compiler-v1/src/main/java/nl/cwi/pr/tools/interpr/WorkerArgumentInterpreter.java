package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.Variable;
import nl.cwi.pr.misc.Environment.Mode;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.WorkerArgumentCodeContext;
import nl.cwi.pr.tools.pars.PrParser.WorkerArgumentContext;
import nl.cwi.pr.tools.pars.PrParser.WorkerArgumentIntegerContext;
import nl.cwi.pr.tools.pars.PrParser.WorkerArgumentNameContext;
import nl.cwi.pr.tools.pars.PrParser.WorkerArgumentPortContext;

public class WorkerArgumentInterpreter extends
		Interpreter<WorkerArgumentContext, Variable> {

	//
	// CONSTRUCTORS
	//

	public WorkerArgumentInterpreter(Interpreter<?, ?> parent,
			WorkerArgumentContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static WorkerArgumentInterpreter newInstance(
			Interpreter<?, ?> parent, WorkerArgumentContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof WorkerArgumentNameContext)
			return new Name(parent, (WorkerArgumentNameContext) context);

		else if (context instanceof WorkerArgumentIntegerContext)
			return new Integer(parent, (WorkerArgumentIntegerContext) context);

		else if (context instanceof WorkerArgumentPortContext)
			return new Port(parent, (WorkerArgumentPortContext) context);

		else if (context instanceof WorkerArgumentCodeContext)
			return new Code(parent, (WorkerArgumentCodeContext) context);

		else
			throw new Error();

	}

	//
	// STATIC - CLASSES
	//

	public static class Name extends WorkerArgumentInterpreter {

		//
		// FIELDS
		//

		private final TypedName integerName;
		private final TypedName portName;
		private final TypedName mainArgumentName;

		//
		// CONSTRUCTORS
		//

		public Name(Interpreter<?, ?> parent, WorkerArgumentNameContext context) {
			super(parent, context);

			this.integerName = new TypedName(context.IDENTIFIER().getText(),
					Type.INTEGER);
			this.portName = new TypedName(context.IDENTIFIER().getText(),
					Type.PORT);
			this.mainArgumentName = new TypedName(context.IDENTIFIER()
					.getText(), Type.MAIN_ARGUMENT);
		}

		//
		// METHODS
		//

		@Override
		public Variable interpret(Factories factories,
				final Definitions definitions, final Environment environment) {

			super.interpret(factories, definitions, environment);

			List<Variable> arguments = new ArrayList<>();

			/*
			 * Interpret integer (?)
			 */

			if (environment.containsInteger(integerName))
				arguments.add(new Variable() {
					@Override
					public String toString() {
						return environment.getInteger(integerName).toString();
					}
				});

			else if (definitions.containsInteger(integerName))
				arguments.add(new Variable() {
					@Override
					public String toString() {
						return definitions.getInteger(integerName).toString();
					}
				});

			/*
			 * Interpret port (?)
			 */

			environment.setMode(Mode.WORKER);

			if (environment.containsPort(portName))
				arguments.add(environment.getPort(portName));
			else if (factories.constructedPort(portName.getName() + "$"
					+ environment.getScope()))
				arguments.add(factories.getPort(portName.getName() + "$"
						+ environment.getScope()));

			/*
			 * Interpret main argument (?)
			 */

			if (factories.constructedMainArgument(mainArgumentName.getName()))
				arguments.add(factories.getMainArgument(mainArgumentName
						.getName()));

			/*
			 * Return
			 */

			if (arguments.isEmpty())
				addError("Unbound parameter", true);
			if (arguments.size() > 1)
				addError("Ambiguous parameter type", false);

			return arguments.get(0);
		}
	}

	public static class Integer extends WorkerArgumentInterpreter {

		//
		// FIELDS
		//

		private final IntegerExpressionInterpreter integerExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Integer(Interpreter<?, ?> parent,
				WorkerArgumentIntegerContext context) {

			super(parent, context);
			this.integerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.integerExpression());
		}

		//
		// METHODS
		//

		@Override
		public Variable interpret(final Factories factories,
				final Definitions definitions, final Environment environment) {

			super.interpret(factories, definitions, environment);
			return new Variable() {
				public String toString() {
					return integerExpressionInterpreter.interpret(factories,
							definitions, environment).toString();
				}
			};
		}
	}

	public static class Port extends WorkerArgumentInterpreter {

		//
		// FIELDS
		//

		private final PortExpressionInterpreter portExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Port(Interpreter<?, ?> parent, WorkerArgumentPortContext context) {
			super(parent, context);
			this.portExpressionInterpreter = PortExpressionInterpreter
					.newInstance(this, context.portExpression());
		}

		//
		// METHODS
		//

		@Override
		public Variable interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			environment.setMode(Mode.WORKER);
			nl.cwi.pr.misc.PortFactory.Port port = portExpressionInterpreter
					.interpret(factories, definitions, environment);

			if (!definitions.containsPort(port))
				addError("Unknown port parameter", false);

			return port;
		}
	}

	public static class Code extends WorkerArgumentInterpreter {

		//
		// FIELDS
		//

		private final String codeText;

		//
		// CONSTRUCTORS
		//

		public Code(Interpreter<?, ?> parent, WorkerArgumentCodeContext context) {
			super(parent, context);
			this.codeText = context.code().codeContent().getText();
		}

		//
		// METHODS
		//

		@Override
		public Variable interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return new nl.cwi.pr.misc.Code(codeText);
		}
	}
}
