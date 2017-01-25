package nl.cwi.pr.misc;

import nl.cwi.pr.tools.interpr.Interpreter;

public class Family {

	//
	// FIELDS
	//

	private final Interpreter<?, ? extends Member> interpreter;
	private final FamilySignature signature;

	//
	// CONSTRUCTORS
	//

	public Family(FamilySignature signature,
			Interpreter<?, ? extends Member> interpreter) {

		if (signature == null)
			throw new NullPointerException();
		if (interpreter == null)
			throw new NullPointerException();

		this.signature = signature;
		this.interpreter = interpreter;
	}

	//
	// METHODS
	//

	public Interpreter<?, ? extends Member> getInterpreter() {
		return interpreter;
	}

	public FamilySignature getSignature() {
		return signature;
	}
}