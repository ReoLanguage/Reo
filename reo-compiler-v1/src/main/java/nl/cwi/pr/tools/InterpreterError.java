package nl.cwi.pr.tools;

import org.antlr.v4.runtime.ParserRuleContext;

@SuppressWarnings("serial")
public class InterpreterError extends ToolError {

	//
	// CONSTRUCTORS
	//

	public InterpreterError(String location, ParserRuleContext context,
			String message) {

		super(location, extractBegin(context), extractEnd(context),
				extractRow(context), extractColumn(context), message, null);
	}

	public InterpreterError(String location, ParserRuleContext context,
			int beginOffset, int rowOffset, String message) {

		super(location, beginOffset + extractBegin(context), beginOffset
				+ extractEnd(context), rowOffset + extractRow(context),
				extractColumn(context), message, null);
	}

	public InterpreterError(String location, ParserRuleContext context,
			String message, Throwable cause) {

		super(location, extractBegin(context), extractEnd(context),
				extractRow(context), extractColumn(context), message, cause);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected String getType() {
		return "Interpreter error";
	}

	//
	// STATIC - METHODS - PUBLIC
	//

	private static Integer extractBegin(ParserRuleContext context) {
		if (context == null || context.getStart() == null)
			return null;

		int start = context.getStart().getStartIndex();
		if (context.getStop() == null)
			return start;

		int stop = context.getStop().getStopIndex() + 1;
		return start == stop ? start - 1 : start;
	}

	private static Integer extractColumn(ParserRuleContext context) {
		return (context == null || context.getStart() == null) ? null : context
				.getStart().getCharPositionInLine();
	}

	private static Integer extractEnd(ParserRuleContext context) {
		return (context == null || context.getStop() == null) ? null : context
				.getStop().getStopIndex() + 1;
	}

	private static Integer extractRow(ParserRuleContext context) {
		return (context == null || context.getStart() == null) ? null : context
				.getStart().getLine();
	}
}