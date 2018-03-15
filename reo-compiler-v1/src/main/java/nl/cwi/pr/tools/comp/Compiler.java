package nl.cwi.pr.tools.comp;

import nl.cwi.pr.tools.CompilerError;
import nl.cwi.pr.tools.CompilerProgressMonitor;
import nl.cwi.pr.tools.CompilerSettings;
import nl.cwi.pr.tools.ToolError;
import nl.cwi.pr.tools.ToolErrorAccumulator;


public abstract class Compiler<Obj> extends ToolErrorAccumulator {

	//
	// FIELDS
	//

	private final CompilerSettings settings;

	//
	// CONSTRUCTORS
	//

	public Compiler(CompilerSettings settings) {
		super(settings.getSourceFileLocation());
		this.settings = settings;
	}

	public Compiler(Compiler<?> parent) {
		super(parent);
		this.settings = parent.settings;
	}

	//
	// METHODS - PUBLIC
	//

	public abstract Obj compile();

	public CompilerSettings getSettings() {
		return settings;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected ToolError newError(String message) {
		if (message == null)
			throw new NullPointerException();

		return new CompilerError(getSourceFileLocation(), message);
	}

	@Override
	protected ToolError newError(String message, Throwable cause) {
		if (message == null)
			throw new NullPointerException();
		if (cause == null)
			throw new NullPointerException();

		return new CompilerError(getSourceFileLocation(), message, cause);
	}
}