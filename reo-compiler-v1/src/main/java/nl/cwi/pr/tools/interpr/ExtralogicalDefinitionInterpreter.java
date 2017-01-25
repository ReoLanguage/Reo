package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.autom.Extralogical;
import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.ExtralogicalDefinitionContext;

public class ExtralogicalDefinitionInterpreter extends
		Interpreter<ExtralogicalDefinitionContext, Definitions> {

	//
	// FIELDS
	//

	private final TypedName extralogicalName;
	private final String extralogicalText;

	//
	// CONSTRUCTORS
	//

	public ExtralogicalDefinitionInterpreter(Interpreter<?, ?> parent,
			ExtralogicalDefinitionContext context) {

		super(parent, context);

		this.extralogicalName = new TypedName(context.extralogicalName()
				.getText(), Type.EXTRALOGICAL);
		this.extralogicalText = context.extralogical().code().codeContent()
				.getText();
	}

	//
	// METHODS
	//

	@Override
	public Definitions interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);

		Extralogical extralogical = new Extralogical(extralogicalText);
		definitions.putExtralogical(extralogicalName, extralogical, this);
		return definitions;
	}
}