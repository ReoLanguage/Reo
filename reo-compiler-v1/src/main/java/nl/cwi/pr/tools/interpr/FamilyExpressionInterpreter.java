package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.IntegerDomain;
import nl.cwi.pr.misc.Member;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.Member.Composite;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.FamilyExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyExpressionIfContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyExpressionLetContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyExpressionMultContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyExpressionProdContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyExpressionScopeContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyExpressionValueContext;

public abstract class FamilyExpressionInterpreter extends
		Interpreter<FamilyExpressionContext, Member> {

	//
	// CONSTRUCTORS
	//

	public FamilyExpressionInterpreter(Interpreter<?, ?> parent,
			FamilyExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static FamilyExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, FamilyExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof FamilyExpressionValueContext)
			return new Value(parent, (FamilyExpressionValueContext) context);

		else if (context instanceof FamilyExpressionScopeContext)
			return new Scope(parent, (FamilyExpressionScopeContext) context);

		else if (context instanceof FamilyExpressionProdContext)
			return new Prod(parent, (FamilyExpressionProdContext) context);

		else if (context instanceof FamilyExpressionMultContext)
			return new Mult(parent, (FamilyExpressionMultContext) context);

		else if (context instanceof FamilyExpressionIfContext)
			return new If(parent, (FamilyExpressionIfContext) context);

		else if (context instanceof FamilyExpressionLetContext)
			return new Let(parent, (FamilyExpressionLetContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class Value extends FamilyExpressionInterpreter {

		//
		// FIELDS
		//

		private final MemberSignatureInterpreter memberSignatureInterpreter;

		//
		// CONSTRUCTORS
		//

		public Value(Interpreter<?, ?> parent,
				FamilyExpressionValueContext context) {

			super(parent, context);
			this.memberSignatureInterpreter = new MemberSignatureInterpreter(
					this, context.memberSignature());
		}

		//
		// METHODS
		//

		@Override
		public Member interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return memberSignatureInterpreter.interpret(factories, definitions,
					environment);
		}
	}

	public static class Scope extends FamilyExpressionInterpreter {

		//
		// FIELDS
		//

		private final FamilyExpressionInterpreter familyExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Scope(Interpreter<?, ?> parent,
				FamilyExpressionScopeContext context) {

			super(parent, context);
			this.familyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.familyExpression());
		}

		//
		// METHODS
		//

		@Override
		public Member interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);
			return familyExpressionInterpreter.interpret(factories,
					definitions, environment);
		}
	}

	public static class Prod extends FamilyExpressionInterpreter {

		//
		// FIELDS
		//

		private final FamilyExpressionInterpreter familyExpressionInterpreter;
		private final IntegerDomainInterpreter integerDomainInterpreter;
		private final TypedName integerName;

		//
		// CONSTRUCTORS
		//

		public Prod(Interpreter<?, ?> parent,
				FamilyExpressionProdContext context) {

			super(parent, context);

			this.integerDomainInterpreter = new IntegerDomainInterpreter(this,
					context.integerNameDeclaration().integerDomain());
			this.integerName = new TypedName(context.integerNameDeclaration()
					.integerName().getText(), Type.INTEGER);
			this.familyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.familyExpression());
		}

		//
		// METHODS
		//

		@Override
		public Member interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			IntegerDomain domain = integerDomainInterpreter.interpret(
					factories, definitions, environment);

			if (domain.count() == 0)
				integerDomainInterpreter.addError("Empty domain", true);

			Composite composite = new Composite();
			for (int i = domain.getLeft(); i <= domain.getRight(); i++) {
				Environment newEnvironment = new Environment(environment);
				newEnvironment.putInteger(integerName, i);
				Member member = familyExpressionInterpreter.interpret(
						factories, definitions, newEnvironment);

				composite.addChild(member);
			}

			return composite;
		}
	}

	public static class Mult extends FamilyExpressionInterpreter {

		//
		// FIELDS
		//

		private final FamilyExpressionInterpreter leftFamilyExpressionInterpreter;
		private final FamilyExpressionInterpreter rightFamilyExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Mult(Interpreter<?, ?> parent,
				FamilyExpressionMultContext context) {

			super(parent, context);

			this.leftFamilyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.left);
			this.rightFamilyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.right);
		}

		//
		// METHODS
		//

		@Override
		public Member interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			Composite composite = new Composite();
			composite.addChild(leftFamilyExpressionInterpreter.interpret(
					factories, definitions, environment));
			composite.addChild(rightFamilyExpressionInterpreter.interpret(
					factories, definitions, environment));

			return composite;
		}
	}

	public static class If extends FamilyExpressionInterpreter {

		//
		// FIELDS
		//

		private final BooleanExpressionInterpreter booleanExpressionInterpreter;

		private final FamilyExpressionInterpreter ifFamilyExpressionInterpreter;
		private final FamilyExpressionInterpreter elseFamilyExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public If(Interpreter<?, ?> parent, FamilyExpressionIfContext context) {

			super(parent, context);

			this.booleanExpressionInterpreter = BooleanExpressionInterpreter
					.newInstance(this, context.booleanExpression());
			this.ifFamilyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.ifBranche);
			this.elseFamilyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.elseBranche);
		}

		//
		// METHODS
		//

		@Override
		public Member interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			if (booleanExpressionInterpreter.interpret(factories, definitions,
					environment))

				return ifFamilyExpressionInterpreter.interpret(factories,
						definitions, environment);
			else
				return elseFamilyExpressionInterpreter.interpret(factories,
						definitions, environment);
		}
	}

	public static class Let extends FamilyExpressionInterpreter {

		//
		// FIELDS
		//

		private final FamilyExpressionInterpreter familyExpressionInterpreter;
		private final IntegerExpressionInterpreter integerExpressionInterpreter;
		private final String integerName;

		//
		// CONSTRUCTORS
		//

		public Let(Interpreter<?, ?> parent, FamilyExpressionLetContext context) {
			super(parent, context);

			this.familyExpressionInterpreter = FamilyExpressionInterpreter
					.newInstance(this, context.familyExpression());
			this.integerExpressionInterpreter = IntegerExpressionInterpreter
					.newInstance(this, context.integerExpression());
			this.integerName = context.integerName().getText();
		}

		//
		// METHODS
		//

		@Override
		public Member interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			Environment newEnvironment = new Environment(environment);
			newEnvironment.putInteger(new TypedName(integerName, Type.INTEGER),
					integerExpressionInterpreter.interpret(factories,
							definitions, newEnvironment));

			return familyExpressionInterpreter.interpret(factories,
					definitions, newEnvironment);
		}
	}
}