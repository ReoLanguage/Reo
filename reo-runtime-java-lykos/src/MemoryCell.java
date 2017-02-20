package nl.cwi.pr.runtime;

public class MemoryCell {

	//
	// FIELDS
	//

	public volatile Object content = null;

	//
	// CONSTRUCTORS
	//

	public MemoryCell() {
	}

	public MemoryCell(final Object content) {
		this.content = content;
	}
}