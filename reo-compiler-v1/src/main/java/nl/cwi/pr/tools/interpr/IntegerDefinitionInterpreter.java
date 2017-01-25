package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.IntegerDefinitionContext;

public class IntegerDefinitionInterpreter extends
		Interpreter<IntegerDefinitionContext, Definitions> {

	//
	// FIELDS
	//

	private final String integerText;
	private final TypedName integerName;

	//
	// CONSTRUCTORS
	//

	public IntegerDefinitionInterpreter(Interpreter<?, ?> parent,
			IntegerDefinitionContext context) {

		super(parent, context);

		this.integerName = new TypedName(context.integerName().getText(),
				Type.INTEGER);
		this.integerText = context.integer().getText();
	}

	//
	// METHODS
	//

	@Override
	public Definitions interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);

		Integer integer = Integer.parseInt(integerText);
		definitions.putInteger(integerName, integer, this);
		return definitions;
	}
}