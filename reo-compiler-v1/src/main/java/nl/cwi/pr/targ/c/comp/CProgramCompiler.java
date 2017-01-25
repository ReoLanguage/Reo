package nl.cwi.pr.targ.c.comp;

import nl.cwi.pr.Platform;
import nl.cwi.pr.autom.AutomatonFactory;
import nl.cwi.pr.tools.CompilerSettings;
import nl.cwi.pr.tools.InterpretedProgram;
import nl.cwi.pr.tools.comp.DefaultProgramCompiler;

public class CProgramCompiler extends DefaultProgramCompiler {

	public CProgramCompiler(CompilerSettings settings,
			InterpretedProgram program, AutomatonFactory automatonFactory) {

		super(settings, program, automatonFactory);
	}

	@Override
	protected String getSourceRunTimeDirectoryLocation() {
		if (!Platform.hasFileInBundle("nl.cwi.pr.runtime.c", "src"))
			addError("Access failure on run-time library", true);

		return Platform.getFileInBundle("nl.cwi.pr.runtime.c", "src")
				.getAbsolutePath();
	}
}
