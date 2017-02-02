package nl.cwi.reo.pr.misc;

import nl.cwi.reo.pr.misc.MainArgumentFactory.MainArgument;
import nl.cwi.reo.pr.misc.MainArgumentFactory.MainVariableSet;

public abstract class MainArgumentFactory extends
		IdObjectFactory<MainArgument, MainVariableSet, MainArgumentSpec> {

	//
	// METHODS - PUBLIC
	//

	@Override
	public MainVariableSet newSet() {
		return new MainVariableSet();
	}

	//
	// CLASSES - PUBLIC
	//

	public class MainArgument
			extends
			IdObjectFactory<MainArgument, MainVariableSet, MainArgumentSpec>.IdObject
			implements Variable {

		//
		// CONSTRUCTORS
		//

		public MainArgument(int id, MainArgumentSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public int compareTo(MainArgument o) {
			if (o == null)
				throw new NullPointerException();

			return getName().compareTo(o.getName());
		}

		public String getName() {
			return getSpec().getName();
		}

		@Override
		public String toString() {
			return getName();
		}
	}

	public class MainVariableSet
			extends
			IdObjectFactory<MainArgument, MainVariableSet, MainArgumentSpec>.IdObjectSet {
	}
}
