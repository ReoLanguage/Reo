package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.IntegerDomain;
import nl.cwi.pr.tools.pars.PrParser.IntegerDomainContext;

public class IntegerDomainInterpreter extends
		Interpreter<IntegerDomainContext, IntegerDomain> {

	//
	// FIELDS
	//

	private final IntegerExpressionInterpreter leftIntegerExpressionInterpreter;
	private final IntegerExpressionInterpreter rightIntergerExpressionInterpreter;

	//
	// CONSTRUCTORS
	//

	public IntegerDomainInterpreter(Interpreter<?, ?> parent,
			IntegerDomainContext context) {

		super(parent, context);
		this.leftIntegerExpressionInterpreter = IntegerExpressionInterpreter
				.newInstance(this, context.integerExpression(0));
		this.rightIntergerExpressionInterpreter = IntegerExpressionInterpreter
				.newInstance(this, context.integerExpression(1));
	}

	@Override
	public IntegerDomain interpret(Factories factories,
			Definitions definitions, Environment environment) {

		super.interpret(factories, definitions, environment);

		int leftInteger = leftIntegerExpressionInterpreter.interpret(factories,
				definitions, environment);
		int rightInteger = rightIntergerExpressionInterpreter.interpret(
				factories, definitions, environment);
		return new IntegerDomain(leftInteger, rightInteger);
	}
}
