package nl.cwi.reo.semantics.symbolicautomata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Existential implements Formula {

	private final Variable x;
	
	private final Formula f;

	public Existential(Variable x, Formula f) {
		this.x = x;
		this.f = f;
	}

	@Override
	public Formula getGuard() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<Port, Term> getAssignment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>(links);
		newlinks.remove(x); // this is pseudo code
		return new Existential(x, f.rename(newlinks));
	}

	@Override
	public Set<Port> getInterface() {
		Set<Port> P = f.getInterface();
		P.remove(x); // this is pseudo code
		return P;
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return null;
	}

	@Override
	public Disjunction DNF() {
		// first get all quantifiers in f to the front and then apply DNF
		return null;
	}

	@Override
	public Formula propNegation(boolean isNegative) {
		
		return null;
	}

}
