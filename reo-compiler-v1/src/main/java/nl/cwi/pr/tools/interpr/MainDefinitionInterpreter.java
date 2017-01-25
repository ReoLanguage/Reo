package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.MainArgumentFactory.MainArgument;
import nl.cwi.pr.tools.InterpretedMain;
import nl.cwi.pr.tools.pars.PrParser.MainArgumentContext;
import nl.cwi.pr.tools.pars.PrParser.MainDefinitionContext;

public class MainDefinitionInterpreter extends
		Interpreter<MainDefinitionContext, InterpretedMain> {

	//
	// FIELDS
	//

	private final List<MainArgumentInterpreter> mainArgumentInterpreters = new ArrayList<>();
	private final MainExpressionInterpreter mainExpressionInterpreter;

	//
	// CONSTRUCTORS
	//

	public MainDefinitionInterpreter(Interpreter<?, ?> parent,
			MainDefinitionContext context) {

		super(parent, context);

		this.mainExpressionInterpreter = MainExpressionInterpreter.newInstance(
				this, context.mainExpression());

		try {
			for (MainArgumentContext c : context.mainArgument())
				mainArgumentInterpreters.add(new MainArgumentInterpreter(this,
						c));
		}

		catch (NullPointerException exc) {
		}
	}

	//
	// METHODS
	//

	@Override
	public InterpretedMain interpret(Factories factories,
			Definitions definitions, Environment environment) {

		super.interpret(factories, definitions, environment);

		/*
		 * Interpret main arguments
		 */

		List<MainArgument> mainArguments = new ArrayList<>();
		for (MainArgumentInterpreter interpr : mainArgumentInterpreters)
			definitions.addMainArgument(interpr.interpret(factories,
					definitions, environment));

		/*
		 * Interpret main expression
		 */

		InterpretedMain interpretedMain = mainExpressionInterpreter.interpret(
				factories, definitions, environment);

		/*
		 * Return
		 */

		for (MainArgument arg : mainArguments)
			interpretedMain.getSignature().addMainArgument(arg);

		return interpretedMain;
	}
}
