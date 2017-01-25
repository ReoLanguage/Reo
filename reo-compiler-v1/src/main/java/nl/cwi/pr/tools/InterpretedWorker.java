package nl.cwi.pr.tools;

import nl.cwi.pr.tools.interpr.WorkerSignature;

public class InterpretedWorker extends InterpretedGeneratee {
	
	//
	// FIELDS
	//
	
	private final WorkerSignature signature;

	//
	// CONSTRUCTORS
	//

	public InterpretedWorker(WorkerSignature signature) {
		if (signature == null)
			throw new NullPointerException();
		
		this.signature = signature;
	}

	//
	// METHODS
	//

	public WorkerSignature getSignature() {
		return signature;
	}
	
	@Override
	public String toString() {
		return signature.toString();
	}
}
