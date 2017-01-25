package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.tools.InterpretedProtocol;
import nl.cwi.pr.tools.pars.PrParser.ProtocolsExpressionAndContext;
import nl.cwi.pr.tools.pars.PrParser.ProtocolsExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.ProtocolsExpressionScopeContext;
import nl.cwi.pr.tools.pars.PrParser.ProtocolsExpressionValueContext;

public abstract class ProtocolsExpressionInterpreter extends
		Interpreter<ProtocolsExpressionContext, List<InterpretedProtocol>> {

	//
	// CONSTRUCTORS
	//

	public ProtocolsExpressionInterpreter(Interpreter<?, ?> parent,
			ProtocolsExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static ProtocolsExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, ProtocolsExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof ProtocolsExpressionValueContext)
			return new Value(parent, (ProtocolsExpressionValueContext) context);

		else if (context instanceof ProtocolsExpressionScopeContext)
			return new Scope(parent, (ProtocolsExpressionScopeContext) context);

		else if (context instanceof ProtocolsExpressionAndContext)
			return new And(parent, (ProtocolsExpressionAndContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class Value extends ProtocolsExpressionInterpreter {

		//
		// FIELDS
		//

		private final MemberSignatureInterpreter memberSignatureInterpreter;

		//
		// CONSTRUCTORS
		//

		public Value(Interpreter<?, ?> parent,
				ProtocolsExpressionValueContext context) {

			super(parent, context);
			this.memberSignatureInterpreter = new MemberSignatureInterpreter(
					this, context.protocolSignature().memberSignature());
		}

		//
		// METHODS
		//

		@Override
		public List<InterpretedProtocol> interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);

			List<InterpretedProtocol> protocols = new ArrayList<>();
			protocols.add(new InterpretedProtocol(memberSignatureInterpreter
					.interpret(factories, definitions, new Environment(
							environment))));

			return protocols;
		}
	}

	public static class Scope extends ProtocolsExpressionInterpreter {

		//
		// FIELDS
		//

		private final ProtocolsExpressionInterpreter protocolsExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public Scope(Interpreter<?, ?> parent,
				ProtocolsExpressionScopeContext context) {

			super(parent, context);
			this.protocolsExpressionInterpreter = ProtocolsExpressionInterpreter
					.newInstance(this, context.protocolsExpression());
		}

		//
		// METHODS
		//

		@Override
		public List<InterpretedProtocol> interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);
			return protocolsExpressionInterpreter.interpret(factories,
					definitions, environment);
		}
	}

	public static class And extends ProtocolsExpressionInterpreter {

		//
		// FIELDS
		//

		private final ProtocolsExpressionInterpreter leftProtocolsExpressionInterpreter;
		private final ProtocolsExpressionInterpreter rightProtocolsExpressionInterpreter;

		//
		// CONSTRUCTORS
		//

		public And(Interpreter<?, ?> parent,
				ProtocolsExpressionAndContext context) {

			super(parent, context);

			this.leftProtocolsExpressionInterpreter = ProtocolsExpressionInterpreter
					.newInstance(this, context.left);
			this.rightProtocolsExpressionInterpreter = ProtocolsExpressionInterpreter
					.newInstance(this, context.right);
		}

		//
		// METHODS
		//

		@Override
		public List<InterpretedProtocol> interpret(Factories factories,
				Definitions definitions, Environment environment) {

			super.interpret(factories, definitions, environment);

			List<InterpretedProtocol> protocols = new ArrayList<>();
			protocols.addAll(leftProtocolsExpressionInterpreter.interpret(
					factories, definitions, environment));
			protocols.addAll(rightProtocolsExpressionInterpreter.interpret(
					factories, definitions, environment));

			return protocols;
		}
	}
}
