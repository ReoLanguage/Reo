package nl.cwi.pr.tools.interpr;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.tools.InterpreterError;
import nl.cwi.pr.tools.ToolError;
import nl.cwi.pr.tools.ToolErrorAccumulator;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Interpreter<Cont extends ParserRuleContext, Obj> extends
		ToolErrorAccumulator {

	//
	// FIELDS
	//

	private final Cont context;
	private final int beginOffset;
	private final int rowOffset;

	//
	// CONSTRUCTORS
	//

	public Interpreter(String sourceFileLocation, Cont context) {
		super(sourceFileLocation);

		if (context == null)
			throw new NullPointerException();

		this.beginOffset = 0;
		this.rowOffset = 0;
		this.context = context;
	}

	public Interpreter(Interpreter<?, ?> parent, Cont context) {
		super(parent);

		if (context == null)
			throw new NullPointerException();

		this.beginOffset = parent.beginOffset;
		this.rowOffset = parent.rowOffset;
		this.context = context;
	}

	// public Interpreter(Interpreter<?, ?> parent, Cont context, int
	// beginOffset,
	// int rowOffset) {
	//
	// super(parent);
	//
	// if (context == null)
	// throw new NullPointerException();
	// if (beginOffset < 0)
	// throw new IllegalArgumentException();
	// if (rowOffset < 0)
	// throw new IllegalArgumentException();
	//
	// this.beginOffset = beginOffset;
	// this.context = context;
	// this.rowOffset = rowOffset;
	// }

	//
	// METHODS - PUBLIC
	//

	public Obj interpret(Factories factories, Definitions definitions,
			Environment environment) {

		if (factories == null)
			throw new NullPointerException();
		if (definitions == null)
			throw new NullPointerException();
		if (environment == null)
			throw new NullPointerException();

		return null;
	}

	//
	// METHODS - PROTECTED
	//

	protected Cont getContext() {
		return context;
	}

	@Override
	protected ToolError newError(String message) {
		if (message == null)
			throw new NullPointerException();

		return new InterpreterError(getSourceFileLocation(), context,
				beginOffset, rowOffset, message);
	}

	@Override
	protected ToolError newError(String message, Throwable cause) {
		if (message == null)
			throw new NullPointerException();
		if (cause == null)
			throw new NullPointerException();

		return new InterpreterError(getSourceFileLocation(), context, message,
				cause);
	}
}
