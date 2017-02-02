package nl.cwi.reo.pr.misc;

public class Code implements Variable {
	private final String code;

	//
	// CONSTRUCTORS
	//

	public Code(String code) {
		if (code == null)
			throw new NullPointerException();

		this.code = code;
	}

	//
	// METHODS - PUBLIC
	//

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "'" + code + "'";
	}
}
