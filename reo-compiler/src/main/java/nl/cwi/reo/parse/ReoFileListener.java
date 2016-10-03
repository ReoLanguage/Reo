package nl.cwi.reo.parse;

import nl.cwi.reo.semantics.Program;

public interface ReoFileListener extends ReoListener {
	
	/**
	 * Returns the parsed Reo program.
	 * @return parsed Reo program
	 */
	public Program getProgram();
}
