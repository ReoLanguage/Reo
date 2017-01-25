package nl.cwi.pr.tools;


@SuppressWarnings("serial")
public class CompilerError extends ToolError {

	//
	// CONSTRUCTORS
	//

	public CompilerError(String location, String message) {
		super(location, null, null, null, null, message, null);
	}

	public CompilerError(String location, String message, Throwable cause) {
		super(location, null, null, null, null, message, cause);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected String getType() {
		return "Compile error";
	}
}