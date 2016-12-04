package nl.cwi.reo.automata;

import java.util.List;

public class EmptyLabel implements Label<EmptyLabel> {

	@Override
	public EmptyLabel compose(List<EmptyLabel> lbls) {
		return null;
	}

}
