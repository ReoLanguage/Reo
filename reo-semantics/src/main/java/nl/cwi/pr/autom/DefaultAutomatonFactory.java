package nl.cwi.pr.autom;

import nl.cwi.pr.misc.DefaultPortFactory;
import nl.cwi.pr.misc.PortFactory;

public class DefaultAutomatonFactory extends AutomatonFactory {

	//
	// METHODS - PROTECTED
	//

	@Override
	protected ConstraintFactory newConstraintFactory(
			LiteralFactory literalFactory) {

		if (literalFactory == null)
			throw new NullPointerException();
		if (!(literalFactory instanceof DefaultLiteralFactory))
			throw new IllegalArgumentException();

		return new DefaultConstraintFactory(
				(DefaultLiteralFactory) literalFactory);
	}

	@Override
	protected LiteralFactory newLiteralFactory(TermFactory termFactory) {
		if (termFactory == null)
			throw new NullPointerException();
		if (!(termFactory instanceof DefaultTermFactory))
			throw new IllegalArgumentException();

		return new DefaultLiteralFactory((DefaultTermFactory) termFactory);
	}

	@Override
	protected MemoryCellFactory newMemoryCellFactory() {
		return new DefaultMemoryCellFactory();
	}

	@Override
	protected Automaton newObject(int id, AutomatonSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new EmptyAutomaton(id, spec);
	}

	@Override
	protected PortFactory newPortFactory() {
		return new DefaultPortFactory();
	}

	@Override
	protected TermFactory newTermFactory(PortFactory portFactory,
			MemoryCellFactory memoryCellFactory) {

		return new DefaultTermFactory(portFactory, memoryCellFactory);
	}

	//
	// CLASSES - PUBLIC
	//

	public class EmptyAutomaton extends Automaton {

		//
		// CONSTRUCTORS
		//

		public EmptyAutomaton(int id, AutomatonSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PROTECTED
		//

		@Override
		protected StateFactory newStateFactory() {
			return new DefaultStateFactory();
		}

		@Override
		protected TransitionFactory newTransitionFactory() {
			return new DefaultTransitionFactory();
		}
	}
}
