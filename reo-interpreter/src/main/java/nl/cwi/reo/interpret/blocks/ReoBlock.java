package nl.cwi.reo.interpret.blocks;

import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Semantics;

public interface ReoBlock<T extends Semantics<T>> extends Evaluable<ReoBlock<T>> {
	
}
