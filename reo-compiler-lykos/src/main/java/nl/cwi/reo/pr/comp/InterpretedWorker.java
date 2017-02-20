package nl.cwi.reo.pr.comp;

import nl.cwi.reo.lykos.WorkerSignature;

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
