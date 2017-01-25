package nl.cwi.pr.autom;

public class Extralogical {
	private final String symbol;

	//
	// CONSTRUCTORS
	//

	public Extralogical(String symbol) {
		if (symbol == null)
			throw new NullPointerException();

		this.symbol = symbol;
	}

	//
	// METHODS - PUBLIC
	//

	public boolean equals(Extralogical extralogical) {
		if (extralogical == null)
			throw new NullPointerException();

		return this == extralogical || (symbol.equals(extralogical.symbol));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();

		return obj instanceof Extralogical && equals((Extralogical) obj);
	}

	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		return symbol.hashCode();
	}

	@Override
	public String toString() {
		return "'" + symbol + "'";
	}
}
