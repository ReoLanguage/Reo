package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtomExpression implements Expression<Atom> {
	
	private Atom atom;
	
	public AtomExpression(Atom atom) {
		this.atom = atom;
	}

	@Override
	public Atom evaluate(Map<String, Value> p) throws Exception {
		return this.atom;
	}

	@Override
	public List<String> variables() {
		return new ArrayList<String>();
	}

}
