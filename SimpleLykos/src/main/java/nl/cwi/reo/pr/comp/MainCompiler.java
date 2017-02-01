package nl.cwi.reo.pr.comp;

import nl.cwi.reo.pr.comp.InterpretedMain;

public abstract class MainCompiler<C extends ProgramCompiler> extends
		GenerateeCompiler<C, InterpretedMain> {

	//
	// CONSTRUCTORS
	//

	public MainCompiler(C parent, InterpretedMain main) {
		super(parent, main);
	}
}