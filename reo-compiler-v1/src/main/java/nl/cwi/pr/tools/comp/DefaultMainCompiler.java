package nl.cwi.pr.tools.comp;

import java.util.Collections;
import java.util.Map;

import nl.cwi.pr.tools.InterpretedMain;

public class DefaultMainCompiler extends MainCompiler<DefaultProgramCompiler> {

	//
	// CONSTRUCTORS
	//

	public DefaultMainCompiler(DefaultProgramCompiler parent,
			InterpretedMain main) {

		super(parent, main);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected Map<String, String> generateFiles() {
		return Collections.emptyMap();
	}

}
