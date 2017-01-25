package nl.cwi.pr.tools.comp;

import nl.cwi.pr.tools.InterpretedMain;

public abstract class MainCompiler<C extends ProgramCompiler> extends
		GenerateeCompiler<C, InterpretedMain> {

	//
	// CONSTRUCTORS
	//

	public MainCompiler(C parent, InterpretedMain main) {
		super(parent, main);
	}
}