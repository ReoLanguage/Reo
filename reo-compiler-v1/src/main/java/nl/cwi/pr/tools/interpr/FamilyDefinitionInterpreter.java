package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.Family;
import nl.cwi.pr.tools.pars.PrParser.FamilyDefinitionContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyDefinitionExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyDefinitionPrimitiveContext;

public abstract class FamilyDefinitionInterpreter extends
		Interpreter<FamilyDefinitionContext, Definitions> {

	//
	// CONSTRUCTORS
	//

	public FamilyDefinitionInterpreter(Interpreter<?, ?> parent,
			FamilyDefinitionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static FamilyDefinitionInterpreter newInstance(
			Interpreter<?, ?> parent, FamilyDefinitionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof FamilyDefinitionExpressionContext)
			return new ExpressionInterpreter(parent,
					(FamilyDefinitionExpressionContext) context);

		else if (context instanceof FamilyDefinitionPrimitiveContext)
			return new PrimitiveInterpreter(parent,
					(FamilyDefinitionPrimitiveContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class ExpressionInterpreter extends
			FamilyDefinitionInterpreter {

		//
		// FIELDS
		//

		private final FamilySignatureInterpreter familySignatureInterpreter;
		private final FamilyExpressionInterpreter familyExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public ExpressionInterpreter(Interpreter<?, ?> parent,
				FamilyDefinitionExpressionContext context) {

			super(parent, context);

			this.familySignatureInterpreter = new FamilySignatureInterpreter(
					this, context.familySignature());
			this.familyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.familyExpression());
		}

		//
		// METHODS
		//

		@Override
		public Definitions interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);

			Family family = new Family(familySignatureInterpreter.interpret(
					factories, definitions, environment),
					familyExpressionInterpreter);

			definitions
					.putFamily(family.getSignature().getName(), family, this);

			return definitions;
		}
	}

	public static class PrimitiveInterpreter extends
			FamilyDefinitionInterpreter {

		//
		// FIELDS
		//

		private final FamilySignatureInterpreter familySignatureInterpreter;
		private final FamilyPrimitiveInterpreter familyPrimitiveInterpreter;

		//
		// CONSTRUCTORS
		//

		public PrimitiveInterpreter(Interpreter<?, ?> parent,
				FamilyDefinitionPrimitiveContext context) {

			super(parent, context);

			this.familySignatureInterpreter = new FamilySignatureInterpreter(
					this, context.familySignature());
			this.familyPrimitiveInterpreter = new FamilyPrimitiveInterpreter(
					this, context.familyPrimitive());
		}

		//
		// METHODS
		//

		@Override
		public Definitions interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);

			Family family = new Family(familySignatureInterpreter.interpret(
					factories, definitions, environment),
					familyPrimitiveInterpreter);

			definitions
					.putFamily(family.getSignature().getName(), family, this);

			return definitions;
		}
	}
}