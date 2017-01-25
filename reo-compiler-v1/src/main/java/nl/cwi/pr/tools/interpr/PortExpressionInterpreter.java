package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Array;
import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.PortExpressionArrayContext;
import nl.cwi.pr.tools.pars.PrParser.PortExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.PortExpressionPortContext;

public abstract class PortExpressionInterpreter extends
		Interpreter<PortExpressionContext, Port> {

	//
	// CONSTRUCTORS
	//

	public PortExpressionInterpreter(Interpreter<?, ?> parent,
			PortExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static PortExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, PortExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof PortExpressionPortContext)
			return new PortInterpreter(parent,
					(PortExpressionPortContext) context);

		else if (context instanceof PortExpressionArrayContext)
			return new ArrayInterpreter(parent,
					(PortExpressionArrayContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class PortInterpreter extends PortExpressionInterpreter {

		//
		// FIELDS
		//

		private final TypedName portOrPortName;

		//
		// CONSTRUCTORS
		//

		public PortInterpreter(Interpreter<?, ?> parent,
				PortExpressionPortContext context) {

			super(parent, context);
			this.portOrPortName = new TypedName(context.portOrPortName()
					.getText(), Type.PORT);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Port interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			Port port;
			if (environment.containsPort(portOrPortName))
				port = environment.getPort(portOrPortName);
			else
				port = factories.newOrGetPort(portOrPortName.getName() + "$"
						+ environment.getScope());

			environment.updateRole(port, this);
			return port;
		}
	}

	public static class ArrayInterpreter extends PortExpressionInterpreter {

		//
		// FIELDS
		//

		private final IntegerExpressionInterpreter integerExpressionInterpreter;
		private final TypedName arrayOrArrayName;

		//
		// CONSTRUCTORS
		//

		public ArrayInterpreter(Interpreter<?, ?> parent,
				PortExpressionArrayContext context) {

			super(parent, context);
			this.arrayOrArrayName = new TypedName(context.arrayOrArrayName()
					.getText(), Type.ARRAY);
			this.integerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.integerExpression());
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Port interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			Integer integer = integerExpressionInterpreter.interpret(factories,
					definitions, environment);

			Port port;
			if (environment.containsArray(arrayOrArrayName)) {
				Array array = environment.getArray(arrayOrArrayName);
				if (!array.containsKey(integer))
					addError("Unknown index \"" + integer + "\"", true);

				port = array.get(integer);
			}

			else {
				port = factories.newOrGetPort(arrayOrArrayName.getName() + "$"
						+ environment.getScope() + "$" + integer);
			}

			environment.updateRole(port, this);
			return port;
		}
	}
}
