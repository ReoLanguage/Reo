package nl.cwi.pr.runtime;

public class CspPreVariable extends CspVariable {
	
	//
	// FIELDS
	//
	
	private final MemoryCell memoryCell;

	//
	// CONSTRUCTORS
	//
	
	public CspPreVariable(final MemoryCell memoryCell) {
		this.memoryCell = memoryCell;
	}
	
	//
	// METHODS
	//

	@Override
	public void exportValue() {
	}

	@Override
	public void importValue() {
		value = memoryCell.content;
	}
}