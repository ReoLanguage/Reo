package nl.cwi.reo.pr.misc;

public class Family {

	//
	// FIELDS
	//

	private final TypedName signature;

	//
	// CONSTRUCTORS
	//

	public Family(TypedName signature) {

		if (signature == null)
			throw new NullPointerException();

		this.signature = signature;
	}

	//
	// METHODS
	//

	

	public TypedName getSignature() {
		return signature;
	}
}