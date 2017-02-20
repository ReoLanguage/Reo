package nl.cwi.reo.pr.autom;

import nl.cwi.reo.pr.autom.TermFactory.Term;
import nl.cwi.reo.pr.misc.IdObjectSpec;

public class MemoryCellSpec implements IdObjectSpec {
	private final Term term;

	//
	// CONSTRUCTORS
	//

	public MemoryCellSpec() {
		this.term = null;
	}

	public MemoryCellSpec(Term term) {
		if (term == null)
			throw new NullPointerException();

		this.term = term;
	}

	//
	// METHODS
	//

	public Term getTerm() {
		if (!hasTerm())
			throw new IllegalStateException();

		return term;
	}

	public boolean hasTerm() {
		return term != null;
	}
}
