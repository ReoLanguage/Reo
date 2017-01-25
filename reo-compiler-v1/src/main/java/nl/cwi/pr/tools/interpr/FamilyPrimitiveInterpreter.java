package nl.cwi.pr.tools.interpr;

import java.io.File;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.Member.Primitive;
import nl.cwi.pr.tools.pars.PrParser.FamilyPrimitiveContext;

public class FamilyPrimitiveInterpreter extends
		Interpreter<FamilyPrimitiveContext, Primitive> {

	//
	// FIELDS
	//

	private final String primitiveText;

	//
	// CONSTRUCTORS
	//

	public FamilyPrimitiveInterpreter(Interpreter<?, ?> parent,
			FamilyPrimitiveContext context) {

		super(parent, context);

		this.primitiveText = context.any().getText();
	}

	//
	// METHODS
	//

	@Override
	public Primitive interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);

		String[] strings = primitiveText.split(":");
		if (strings.length != 2)
			addError("Invalid primitive", false);

		String rootLocationText = strings[0];
		String classNameText = strings[1];

		if (new File(rootLocationText).isAbsolute())
			return new Primitive(classNameText, rootLocationText);
		else
			return new Primitive(classNameText, getSourceFileLocation()
					+ File.separator + rootLocationText);
	}
}