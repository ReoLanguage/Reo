package nl.cwi.pr.misc;

public class DefaultMainArgumentFactory extends MainArgumentFactory {

	//
	// METHODS - PUBLIC
	//

	@Override
	protected MainArgument newObject(int id, MainArgumentSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new MainArgument(id, spec);
	}

}
