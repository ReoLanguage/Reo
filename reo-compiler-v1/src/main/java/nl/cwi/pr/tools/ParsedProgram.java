package nl.cwi.pr.tools;

import nl.cwi.pr.tools.pars.PrParser.ProgramContext;

public class ParsedProgram {

	//
	// FIELDS
	//

	private final ProgramContext programContext;

	//
	// CONSTRUCTORS
	//

	public ParsedProgram(ProgramContext programContext) {
		if (programContext == null)
			throw new NullPointerException();

		this.programContext = programContext;
	}

	//
	// METHODS - PUBLIC
	//

	public ProgramContext getProgramContext() {
		return programContext;
	}

	public boolean hasMainDefinitionContext() {
		return !programContext.mainDefinition().isEmpty();
	}
}
