package nl.cwi.pr.misc;

public class IntegerDomain {

	//
	// FIELDS
	//

	private final int left;
	private final int right;

	//
	// CONSTRUCTORS
	//

	public IntegerDomain(int left, int right) {
		this.left = left;
		this.right = right;
	}

	//
	// METHODS
	//

	public int count() {
		return right - left + 1;
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getRight() {
		return right;
	}
}
