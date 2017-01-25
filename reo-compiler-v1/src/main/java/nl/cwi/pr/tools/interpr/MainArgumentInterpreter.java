package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.MainArgumentFactory.MainArgument;
import nl.cwi.pr.tools.pars.PrParser.MainArgumentContext;

public class MainArgumentInterpreter extends
		Interpreter<MainArgumentContext, MainArgument> {

	//
	// FIELDS
	//

	private final String mainArgumentText;

	//
	// CONSTRUCTORS
	//

	public MainArgumentInterpreter(Interpreter<?, ?> parent,
			MainArgumentContext context) {

		super(parent, context);
		this.mainArgumentText = context.getText();
	}

	//
	// METHODS
	//

	@Override
	public MainArgument interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);
		return factories.newOrGetMainArgument(mainArgumentText);
	}
}
