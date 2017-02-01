package nl.cwi.reo.pr.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class ToolErrorAccumulator {
	private final List<ToolErrorAccumulator> children = new ArrayList<>();
	private final List<ToolError> errors = new ArrayList<>();
	private final String sourceFileLocation;;

	//
	// CONSTRUCTORS
	//

	public ToolErrorAccumulator(String sourceFileLocation) {
		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (new File(sourceFileLocation).isDirectory())
			throw new IllegalArgumentException();

		this.sourceFileLocation = sourceFileLocation;
	}

	public ToolErrorAccumulator(ToolErrorAccumulator parent) {
		if (parent == null)
			throw new NullPointerException();

		this.sourceFileLocation = parent.sourceFileLocation;
		parent.addChild(this);
	}

	//
	// METHODS - PUBLIC
	//

	public void addChild(ToolErrorAccumulator child) {
		if (child == null)
			throw new NullPointerException();

		children.add(child);
	}

	public void addError(String message, boolean throwError) {
		if (message == null)
			throw new NullPointerException();

		ToolError error = newError(message);
		errors.add(error);

		if (throwError)
			throw error;
	}

	public void addError(String message, Throwable cause, boolean throwError) {
		if (message == null)
			throw new NullPointerException();

		ToolError error = newError(message);
		errors.add(error);

		if (throwError)
			throw error;
	}

	public void addErrors(List<ToolError> errors) {
		if (errors == null)
			throw new NullPointerException();
		if (errors.contains(null))
			throw new NullPointerException();

		this.errors.addAll(errors);
	}

	public List<ToolError> getErrors() {
		List<ToolError> errors = new ArrayList<>(this.errors);
		for (ToolErrorAccumulator acc : children)
			errors.addAll(acc.getErrors());

		return errors;
	}

	public String getSourceFileLocation() {
		return sourceFileLocation;
	}

	public boolean hasErrors() {
		return !getErrors().isEmpty();
	}

	//
	// METHODS - PROTECTED
	//

	protected abstract ToolError newError(String message);

	protected abstract ToolError newError(String message, Throwable cause);
}
