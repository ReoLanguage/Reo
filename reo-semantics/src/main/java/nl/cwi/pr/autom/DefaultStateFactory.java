package nl.cwi.pr.autom;

public class DefaultStateFactory extends StateFactory {

	//
	// METHODS - PROTECTED
	//

	@Override
	protected State newObject(int id, StateSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new State(id, spec);
	}
}
