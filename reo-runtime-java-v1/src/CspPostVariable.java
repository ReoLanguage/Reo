package nl.cwi.pr.runtime;

public class CspPostVariable extends CspVariable {
	
	//
	// FIELDS
	//
	
	private final MemoryCell memoryCell;

	//
	// CONSTRUCTORS
	//
	
	public CspPostVariable(final MemoryCell memoryCell) {
		this.memoryCell = memoryCell;
	}
	
	//
	// METHODS
	//

	@Override
	public void exportValue() {
		memoryCell.content = value == null ? new Object() : value;
	}

	@Override
	public void importValue() {
		value = null;
	}
}