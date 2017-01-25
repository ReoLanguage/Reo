package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.PortOrArray;
import nl.cwi.pr.tools.pars.PrParser.PortOrArrayExpressionArrayContext;
import nl.cwi.pr.tools.pars.PrParser.PortOrArrayExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.PortOrArrayExpressionPortContext;

public abstract class PortOrArrayExpressionInterpreter extends
		Interpreter<PortOrArrayExpressionContext, PortOrArray> {

	//
	// CONSTRUCTORS
	//

	public PortOrArrayExpressionInterpreter(Interpreter<?, ?> parent,
			PortOrArrayExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static PortOrArrayExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, PortOrArrayExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();
		
		if (context instanceof PortOrArrayExpressionPortContext)
			return new PortInterpreter(parent,
					(PortOrArrayExpressionPortContext) context);

		else if (context instanceof PortOrArrayExpressionArrayContext)
			return new ArrayInterpreter(parent,
					(PortOrArrayExpressionArrayContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class PortInterpreter extends
			PortOrArrayExpressionInterpreter {

		//
		// FIELDS
		//

		private final PortExpressionInterpreter portExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public PortInterpreter(Interpreter<?, ?> parent,
				PortOrArrayExpressionPortContext context) {

			super(parent, context);
			this.portExpressionInterpreter = PortExpressionInterpreter
					.newInstance(this, context.portExpression());
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public PortOrArray interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return portExpressionInterpreter.interpret(factories, definitions,
					environment);
		}
	}

	public static class ArrayInterpreter extends
			PortOrArrayExpressionInterpreter {

		//
		// FIELDS
		//

		private final ArrayExpressionInterpreter arrayExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public ArrayInterpreter(Interpreter<?, ?> parent,
				PortOrArrayExpressionArrayContext context) {

			super(parent, context);
			this.arrayExpressionInterpreter = ArrayExpressionInterpreter
					.newInstance(this, context.arrayExpression());
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public PortOrArray interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return arrayExpressionInterpreter.interpret(factories, definitions,
					environment);
		}
	}
}