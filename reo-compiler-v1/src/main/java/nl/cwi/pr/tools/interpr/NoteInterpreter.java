package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.tools.pars.PrParser.NoteContext;

public class NoteInterpreter extends Interpreter<NoteContext, String> {

	//
	// FIELDS
	//

	private final String noteText;

	//
	// CONSTRUCTORS
	//

	public NoteInterpreter(Interpreter<?, ?> parent, NoteContext context) {
		super(parent, context);
		this.noteText = context.content.getText();
	}

	//
	// METHODS
	//

	@Override
	public String interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);
		return noteText;
	}
}