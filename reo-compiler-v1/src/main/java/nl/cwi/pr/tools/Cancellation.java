package nl.cwi.pr.tools;

public class Cancellation extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public Cancellation() {
	}
	
	public Cancellation(String message) {
		super(message);
	}
}
