package nl.cwi.reo.interpret.blocks;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.semantics.Semantics;

public interface ReoBlock<T extends Semantics<T>> extends Evaluable<ReoBlock<T>> {
	
}
