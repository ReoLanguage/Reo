package nl.cwi.reo.automata;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Void implements Label<Void> {

	@Override
	public Void compose(List<Void> lbls) {
		return null;
	}

	@Override
	public Void restrict(Set<String> iface) {
		return null;
	}

	@Override
	public Void rename(Map<String, String> links) {
		return null;
	}

}
