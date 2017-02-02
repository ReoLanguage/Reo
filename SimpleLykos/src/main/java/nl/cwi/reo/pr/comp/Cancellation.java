package nl.cwi.reo.pr.comp;

public class Cancellation extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public Cancellation() {
	}
	
	public Cancellation(String message) {
		super(message);
	}
}
