package nl.cwi.reo.automata;

public class DefaultTransitionFactory extends TransitionFactory {

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Transition newObject(int id, TransitionSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new Transition(id, spec);
	}

}
