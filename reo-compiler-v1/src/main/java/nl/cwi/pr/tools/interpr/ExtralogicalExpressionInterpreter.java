package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.autom.Extralogical;
import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.ExtralogicalExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.ExtralogicalExpressionNameContext;
import nl.cwi.pr.tools.pars.PrParser.ExtralogicalExpressionValueContext;

public abstract class ExtralogicalExpressionInterpreter extends
		Interpreter<ExtralogicalExpressionContext, Extralogical> {

	//
	// CONSTRUCTORS
	//

	public ExtralogicalExpressionInterpreter(Interpreter<?, ?> parent,
			ExtralogicalExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS - PUBLIC
	//

	public static ExtralogicalExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, ExtralogicalExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof ExtralogicalExpressionValueContext)
			return new Value(parent,
					(ExtralogicalExpressionValueContext) context);

		else if (context instanceof ExtralogicalExpressionNameContext)
			return new Name(parent, (ExtralogicalExpressionNameContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES - PUBLIC
	//

	public static class Value extends ExtralogicalExpressionInterpreter {

		//
		// FIELDS
		//

		private final String extralogicalText;

		//
		// CONSTRUCTORS
		//

		public Value(Interpreter<?, ?> parent,
				ExtralogicalExpressionValueContext context) {

			super(parent, context);
			this.extralogicalText = context.extralogical().code().codeContent()
					.getText();
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Extralogical interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return new Extralogical(extralogicalText);
		}
	}

	public static class Name extends ExtralogicalExpressionInterpreter {

		//
		// FIELDS
		//

		private final TypedName extralogicalName;

		//
		// CONSTRUCTORS
		//

		public Name(Interpreter<?, ?> parent,
				ExtralogicalExpressionNameContext context) {

			super(parent, context);
			this.extralogicalName = new TypedName(context.extralogicalName()
					.getText(), Type.EXTRALOGICAL);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Extralogical interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);

			if (environment.containsExtralogical(extralogicalName))
				return environment.getExtralogical(extralogicalName);
			else if (definitions.containsExtralogical(extralogicalName))
				return definitions.getExtralogical(extralogicalName);
			else
				addError("Unbound extralogical name", true);

			throw new Error();
		}
	}
}
