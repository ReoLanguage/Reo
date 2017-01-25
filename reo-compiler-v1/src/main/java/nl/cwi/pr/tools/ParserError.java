package nl.cwi.pr.tools;


@SuppressWarnings("serial")
public class ParserError extends ToolError {

	//
	// CONSTRUCTORS
	//

	public ParserError(String sourceFileLocation, Integer begin, Integer end,
			Integer row, Integer column, String message) {

		super(sourceFileLocation, begin, end, row, column, message, null);
	}

	public ParserError(String sourceFileLocation, String message,
			Throwable cause) {

		super(sourceFileLocation, null, null, null, null, message, cause);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected String getType() {
		return "Parser error";
	}
}