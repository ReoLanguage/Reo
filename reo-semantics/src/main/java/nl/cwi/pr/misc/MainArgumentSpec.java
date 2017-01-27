package nl.cwi.pr.misc;

public class MainArgumentSpec implements IdObjectSpec {
	private final String name;

	//
	// CONSTRUCTORS
	//

	public MainArgumentSpec(String name) {
		if (name == null)
			throw new NullPointerException();

		this.name = name;
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();

		return obj instanceof MainArgumentSpec
				&& equals((MainArgumentSpec) obj);
	}

	public boolean equals(MainArgumentSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return name.equals(spec.name);
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}