package nl.cwi.pr.targ.java.comp;

import java.io.File;
import java.util.List;

import nl.cwi.pr.tools.CompiledGeneratee;
import nl.cwi.pr.tools.CompiledProgram;

public class JavaCompiledProgram extends CompiledProgram {

	//
	// CONSTRUCTORS
	//

	public JavaCompiledProgram(String sourceRunTimeDirectoryLocation,
			List<CompiledGeneratee> generatees) {
		super(sourceRunTimeDirectoryLocation, generatees);
	}

	//
	// METHODS
	//

	@Override
	public String getMain() {
		for (CompiledGeneratee g : generatees)
			for (String s : g.getFiles().keySet())
				if (s.endsWith(File.separator + "Main.java"))
					return s;

		throw new IllegalStateException();
	}
}
