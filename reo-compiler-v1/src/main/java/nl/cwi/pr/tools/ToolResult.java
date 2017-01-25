package nl.cwi.pr.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ToolResult<O> {
	private final List<ToolError> errors;
	private final O object;

	//
	// CONSTRUCTORS
	//

	public ToolResult() {
		this.object = null;
		this.errors = Collections.emptyList();
	}

	public ToolResult(O object) {
		if (object == null)
			throw new NullPointerException();

		this.object = object;
		this.errors = Collections.emptyList();
	}

	public ToolResult(List<? extends ToolError> errors) {
		if (errors == null)
			throw new NullPointerException();
		if (errors.contains(null))
			throw new NullPointerException();

		this.object = null;
		this.errors = Collections.unmodifiableList(errors);
	}

	public ToolResult(ToolError error) {
		if (error == null)
			throw new NullPointerException();

		this.object = null;
		this.errors = Collections.unmodifiableList(Arrays.asList(error));
	}

	//
	// METHODS
	//

	public boolean has() {
		return object != null;
	}

	public O get() {
		if (!has())
			throw new IllegalStateException();

		return object;
	}

	public List<ToolError> getErrors() {
		return errors;
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}
}
