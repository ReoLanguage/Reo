package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrLexer;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionBinop1Context;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionBinop2Context;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionNameContext;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionScopeContext;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionSizeContext;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionValueContext;

public abstract class IntegerExpressionInterpreter extends
		Interpreter<IntegerExpressionContext, Integer> {

	//
	// CONSTRUCTORS
	//

	public IntegerExpressionInterpreter(Interpreter<?, ?> parent,
			IntegerExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static IntegerExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, IntegerExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof IntegerExpressionValueContext)
			return new Value(parent, (IntegerExpressionValueContext) context);

		else if (context instanceof IntegerExpressionNameContext)
			return new Name(parent, (IntegerExpressionNameContext) context);

		else if (context instanceof IntegerExpressionSizeContext)
			return new Size(parent, (IntegerExpressionSizeContext) context);

		else if (context instanceof IntegerExpressionScopeContext)
			return new Scope(parent, (IntegerExpressionScopeContext) context);

		else if (context instanceof IntegerExpressionBinop1Context)
			return new Binop(parent, (IntegerExpressionBinop1Context) context);

		else if (context instanceof IntegerExpressionBinop2Context)
			return new Binop(parent, (IntegerExpressionBinop2Context) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class Value extends IntegerExpressionInterpreter {

		//
		// FIELDS
		//

		private final String integerText;

		//
		// CONSTRUCTORS
		//

		public Value(Interpreter<?, ?> parent,
				IntegerExpressionValueContext context) {

			super(parent, context);
			this.integerText = context.getText();
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Integer interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return Integer.parseInt(integerText);
		}
	}

	public static class Name extends IntegerExpressionInterpreter {

		//
		// FIELDS
		//

		private final TypedName integerName;

		//
		// CONSTRUCTORS
		//

		public Name(Interpreter<?, ?> parent,
				IntegerExpressionNameContext context) {

			super(parent, context);
			this.integerName = new TypedName(context.integerName().getText(),
					Type.INTEGER);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Integer interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			if (environment.containsInteger(integerName))
				return environment.getInteger(integerName);
			else if (definitions.containsInteger(integerName))
				return definitions.getInteger(integerName);
			else
				addError("Unbound integer name", true);

			throw new Error();
		}
	}

	public static class Size extends IntegerExpressionInterpreter {

		//
		// FIELDS
		//

		private final TypedName arrayOrArrayName;

		//
		// CONSTRUCTORS
		//

		public Size(Interpreter<?, ?> parent,
				IntegerExpressionSizeContext context) {

			super(parent, context);
			this.arrayOrArrayName = new TypedName(context.arrayOrArrayName()
					.getText(), Type.ARRAY);
		}

		//
		// METHODS
		//

		@Override
		public Integer interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			if (!environment.containsArray(arrayOrArrayName))
				addError("Unknown size", true);

			return environment.getArray(arrayOrArrayName).size();
		}

	}

	public static class Scope extends IntegerExpressionInterpreter {

		//
		// FIELDS
		//

		private final IntegerExpressionInterpreter integerExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Scope(Interpreter<?, ?> parent,
				IntegerExpressionScopeContext context) {

			super(parent, context);

			this.integerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.integerExpression());
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Integer interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return integerExpressionInterpreter.interpret(factories,
					definitions, environment);
		}
	}

	public static class Binop extends IntegerExpressionInterpreter {

		//
		// FIELDS
		//

		private final IntegerExpressionInterpreter leftIntegerExpressionInterpreter;
		private final IntegerExpressionInterpreter rightIntegerExpressionInterpreter;

		private final int operator;

		//
		// CONSTRUCTORS
		//

		public Binop(Interpreter<?, ?> parent,
				IntegerExpressionBinop1Context context) {

			super(parent, context);

			this.leftIntegerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.left);
			this.rightIntegerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.right);

			this.operator = context.binop.getType();
		}

		public Binop(Interpreter<?, ?> parent,
				IntegerExpressionBinop2Context context) {

			super(parent, context);

			this.leftIntegerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.left);
			this.rightIntegerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.right);

			operator = context.binop.getType();
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Integer interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			Integer leftInteger = leftIntegerExpressionInterpreter.interpret(
					factories, definitions, environment);
			Integer rightInteger = rightIntegerExpressionInterpreter.interpret(
					factories, definitions, environment);

			switch (operator) {
			case PrLexer.TIMES:
				return leftInteger * rightInteger;
			case PrLexer.OBELUS:
				if (rightInteger == 0)
					addError("Division by zero", true);
				return leftInteger / rightInteger;
			case PrLexer.MODULO:
				return leftInteger % rightInteger;
			case PrLexer.PLUS:
				return leftInteger + rightInteger;
			case PrLexer.MINUS:
				return leftInteger - rightInteger;
			default:
				throw new Error();
			}
		}
	}
}