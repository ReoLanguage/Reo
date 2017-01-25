package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.IntegerDomain;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.InterpretedWorker;
import nl.cwi.pr.tools.pars.PrParser.WorkersExpressionAndContext;
import nl.cwi.pr.tools.pars.PrParser.WorkersExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.WorkersExpressionForallContext;
import nl.cwi.pr.tools.pars.PrParser.WorkersExpressionScopeContext;
import nl.cwi.pr.tools.pars.PrParser.WorkersExpressionValueContext;

public abstract class WorkersExpressionInterpreter extends
		Interpreter<WorkersExpressionContext, List<InterpretedWorker>> {

	//
	// CONSTRUCTORS
	//

	public WorkersExpressionInterpreter(Interpreter<?, ?> parent,
			WorkersExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static WorkersExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, WorkersExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof WorkersExpressionValueContext)
			return new Value(parent, (WorkersExpressionValueContext) context);

		else if (context instanceof WorkersExpressionScopeContext)
			return new Scope(parent, (WorkersExpressionScopeContext) context);

		else if (context instanceof WorkersExpressionForallContext)
			return new Forall(parent, (WorkersExpressionForallContext) context);

		else if (context instanceof WorkersExpressionAndContext)
			return new And(parent, (WorkersExpressionAndContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class Value extends WorkersExpressionInterpreter {

		//
		// FIELDS
		//

		private final WorkerSignatureInterpreter workerSignatureInterpreter;

		//
		// CONSTRUCTORS
		//

		public Value(Interpreter<?, ?> parent,
				WorkersExpressionValueContext context) {

			super(parent, context);
			this.workerSignatureInterpreter = new WorkerSignatureInterpreter(
					this, context.workerSignature());
		}

		//
		// METHODS
		//

		@Override
		public List<InterpretedWorker> interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return Arrays.asList(workerSignatureInterpreter.interpret(
					factories, definitions, environment));
		}
	}

	public static class Scope extends WorkersExpressionInterpreter {

		//
		// FIELDS
		//

		private final WorkersExpressionInterpreter workersExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Scope(Interpreter<?, ?> parent,
				WorkersExpressionScopeContext context) {

			super(parent, context);
			this.workersExpressionInterpreter = WorkersExpressionInterpreter
					.newInstance(this, context.workersExpression());
		}

		//
		// METHODS
		//

		@Override
		public List<InterpretedWorker> interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return workersExpressionInterpreter.interpret(factories,
					definitions, environment);
		}
	}

	public static class Forall extends WorkersExpressionInterpreter {

		//
		// FIELDS
		//

		private final IntegerDomainInterpreter integerDomainInterpreter;
		private final TypedName integerName;
		private final WorkersExpressionInterpreter workersExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Forall(Interpreter<?, ?> parent,
				WorkersExpressionForallContext context) {

			super(parent, context);

			this.integerDomainInterpreter = new IntegerDomainInterpreter(this,
					context.integerNameDeclaration().integerDomain());
			this.integerName = new TypedName(context.integerNameDeclaration()
					.integerName().getText(), Type.INTEGER);
			this.workersExpressionInterpreter = WorkersExpressionInterpreter
					.newInstance(this, context.workersExpression());
		}

		//
		// METHODS
		//

		@Override
		public List<InterpretedWorker> interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);

			IntegerDomain domain = integerDomainInterpreter.interpret(
					factories, definitions, environment);

			if (domain.count() == 0)
				addError("Empty domain", true);

			List<InterpretedWorker> workers = new ArrayList<>();

			for (int i = domain.getLeft(); i <= domain.getRight(); i++) {
				Environment newEnvironment = new Environment(environment);
				newEnvironment.putInteger(integerName, i);
				workers.addAll(workersExpressionInterpreter.interpret(
						factories, definitions, newEnvironment));
			}

			return workers;
		}
	}

	public static class And extends WorkersExpressionInterpreter {

		//
		// FIELDS
		//

		private final WorkersExpressionInterpreter leftWorkersExpressionInterpreter;
		private final WorkersExpressionInterpreter rightWorkersExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public And(Interpreter<?, ?> parent, WorkersExpressionAndContext context) {

			super(parent, context);

			this.leftWorkersExpressionInterpreter = WorkersExpressionInterpreter
					.newInstance(this, context.left);
			this.rightWorkersExpressionInterpreter = WorkersExpressionInterpreter
					.newInstance(this, context.right);
		}

		//
		// METHODS
		//

		@Override
		public List<InterpretedWorker> interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);

			List<InterpretedWorker> workers = new ArrayList<>();
			workers.addAll(leftWorkersExpressionInterpreter.interpret(
					factories, definitions, new Environment(environment)));
			workers.addAll(rightWorkersExpressionInterpreter.interpret(
					factories, definitions, new Environment(environment)));

			return workers;
		}
	}
}