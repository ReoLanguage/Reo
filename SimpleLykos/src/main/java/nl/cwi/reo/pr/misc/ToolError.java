package nl.cwi.reo.pr.misc;

@SuppressWarnings("serial")
public abstract class ToolError extends Error {
	private final Integer begin;
	private final Integer column;
	private final Integer end;
	private final Integer row;
	private final String sourceFileLocation;

	//
	// CONSTRUCTORS
	//

	public ToolError(String sourceFileLocation, Integer begin, Integer end,
			Integer row, Integer column, String message, Throwable cause) {

		super(message, cause);

		if (sourceFileLocation == null)
			throw new NullPointerException();

		this.begin = begin;
		this.column = column;
		this.end = end;
		this.row = row;
		this.sourceFileLocation = sourceFileLocation;
	}

	//
	// METHODS
	//

	public final Integer getBegin() {
		if (!hasBegin())
			throw new IllegalStateException();

		return begin;
	}

	public final Integer getColumn() {
		if (!hasColumn())
			throw new IllegalStateException();

		return column;
	}

	public final Integer getEnd() {
		if (!hasEnd())
			throw new IllegalStateException();

		return end;
	}

	public final String getPosition() {
		if (!hasRow())
			throw new IllegalStateException();

		return getRow() + (hasColumn() ? ":" + getColumn() : "");
	}

	public final String getMessage() {
		if (!hasMessage())
			throw new IllegalStateException();

		return super.getMessage();
	}

	public final Integer getRow() {
		if (!hasRow())
			throw new IllegalStateException();

		return row;
	}

	public String getSourceFileLocation() {
		return sourceFileLocation;
	}

	public final boolean hasBegin() {
		return begin != null;
	}

	public final boolean hasColumn() {
		return column != null;
	}

	public final boolean hasEnd() {
		return end != null;
	}

	public final boolean hasMessage() {
		return super.getMessage() != null && !super.getMessage().isEmpty();
	}

	public final boolean hasRow() {
		return row != null;
	}

	@Override
	public final String toString() {
		return getType()
				+ (hasRow() ? " (at position " + getPosition() + ")" : "")
				+ (hasMessage() ? ": " + getMessage() : ".") + "\n["
				+ getSourceFileLocation() + "]";
	}

	//
	// METHODS - PROTECTED
	//

	protected String getType() {
		return "Error";
	}
}
