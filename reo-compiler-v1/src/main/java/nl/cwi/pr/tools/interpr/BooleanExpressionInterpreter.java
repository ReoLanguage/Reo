package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.tools.pars.PrLexer;
import nl.cwi.pr.tools.pars.PrParser.BooleanExpressionBinopContext;
import nl.cwi.pr.tools.pars.PrParser.BooleanExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.BooleanExpressionEqualsContext;
import nl.cwi.pr.tools.pars.PrParser.BooleanExpressionFalseContext;
import nl.cwi.pr.tools.pars.PrParser.BooleanExpressionNegationContext;
import nl.cwi.pr.tools.pars.PrParser.BooleanExpressionScopeContext;
import nl.cwi.pr.tools.pars.PrParser.BooleanExpressionTrueContext;

public abstract class BooleanExpressionInterpreter extends
		Interpreter<BooleanExpressionContext, Boolean> {

	//
	// CONSTRUCTORS
	//

	public BooleanExpressionInterpreter(Interpreter<?, ?> parent,
			BooleanExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static BooleanExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, BooleanExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof BooleanExpressionTrueContext)
			return new True(parent, (BooleanExpressionTrueContext) context);

		else if (context instanceof BooleanExpressionFalseContext)
			return new False(parent, (BooleanExpressionFalseContext) context);

		else if (context instanceof BooleanExpressionEqualsContext)
			return new Equals(parent, (BooleanExpressionEqualsContext) context);

		else if (context instanceof BooleanExpressionScopeContext)
			return new Scope(parent, (BooleanExpressionScopeContext) context);

		else if (context instanceof BooleanExpressionNegationContext)
			return new Negation(parent,
					(BooleanExpressionNegationContext) context);

		else if (context instanceof BooleanExpressionBinopContext)
			return new Binop(parent, (BooleanExpressionBinopContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class True extends BooleanExpressionInterpreter {

		//
		// CONSTRUCTORS
		//

		public True(Interpreter<?, ?> parent,
				BooleanExpressionTrueContext context) {

			super(parent, context);
		}

		//
		// METHODS
		//

		@Override
		public Boolean interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return true;
		}
	}

	public static class False extends BooleanExpressionInterpreter {

		//
		// CONSTRUCTORS
		//

		public False(Interpreter<?, ?> parent,
				BooleanExpressionFalseContext context) {

			super(parent, context);
		}

		//
		// METHODS
		//

		@Override
		public Boolean interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return false;
		}
	}

	public static class Equals extends BooleanExpressionInterpreter {

		//
		// FIELDS
		//

		private final IntegerExpressionInterpreter leftBooleanExpressionInterpreter;
		private final IntegerExpressionInterpreter rightBooleanExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Equals(Interpreter<?, ?> parent,
				BooleanExpressionEqualsContext context) {

			super(parent, context);

			leftBooleanExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.left);
			rightBooleanExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.right);
		}

		//
		// METHODS
		//

		@Override
		public Boolean interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			Integer leftBoolean = leftBooleanExpressionInterpreter.interpret(
					factories, definitions, environment);
			Integer rightBoolean = rightBooleanExpressionInterpreter.interpret(
					factories, definitions, environment);

			return leftBoolean.equals(rightBoolean);
		}
	}

	public static class Scope extends BooleanExpressionInterpreter {

		//
		// FIELDS
		//

		private final BooleanExpressionInterpreter booleanExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Scope(Interpreter<?, ?> parent,
				BooleanExpressionScopeContext context) {

			super(parent, context);
			this.booleanExpressionInterpreter = BooleanExpressionInterpreter
					.newInstance(this, context.booleanExpression());
		}

		//
		// METHODS
		//

		@Override
		public Boolean interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return booleanExpressionInterpreter.interpret(factories,
					definitions, environment);
		}
	}

	public static class Negation extends BooleanExpressionInterpreter {

		//
		// FIELDS
		//

		private final BooleanExpressionInterpreter booleanExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Negation(Interpreter<?, ?> parent,
				BooleanExpressionNegationContext context) {

			super(parent, context);
			this.booleanExpressionInterpreter = BooleanExpressionInterpreter
					.newInstance(this, context.booleanExpression());
		}

		//
		// METHODS
		//

		@Override
		public Boolean interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return !booleanExpressionInterpreter.interpret(factories,
					definitions, environment);
		}
	}

	public static class Binop extends BooleanExpressionInterpreter {

		//
		// FIELDS
		//

		private final BooleanExpressionInterpreter leftBooleanExpressionInterpreter;
		private final BooleanExpressionInterpreter rightBooleanExpressionInterpreter;

		private final int operator;

		//
		// CONSTRUCTORS
		//

		public Binop(Interpreter<?, ?> parent,
				BooleanExpressionBinopContext context) {

			super(parent, context);

			this.leftBooleanExpressionInterpreter = BooleanExpressionInterpreter
					.newInstance(this, context.left);
			this.rightBooleanExpressionInterpreter = BooleanExpressionInterpreter
					.newInstance(this, context.right);

			this.operator = context.binop.getType();
		}

		//
		// METHODS
		//

		@Override
		public Boolean interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			Boolean leftBoolean = leftBooleanExpressionInterpreter.interpret(
					factories, definitions, environment);
			Boolean rightBoolean = rightBooleanExpressionInterpreter.interpret(
					factories, definitions, environment);

			switch (operator) {
			case PrLexer.CONJUNCTION:
				return leftBoolean && rightBoolean;
			case PrLexer.DISJUNCTION:
				return leftBoolean || rightBoolean;
			default:
				throw new Error();
			}
		}
	}
}
