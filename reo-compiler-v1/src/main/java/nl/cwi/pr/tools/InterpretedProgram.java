package nl.cwi.pr.tools;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.misc.Definitions;

public class InterpretedProgram {

	//
	// FIELDS
	//

	private final Definitions definitions;
	private final InterpretedMain main;
	private final List<String> notes = new ArrayList<>();
	private final String sourceFileLocation;

	//
	// CONSTRUCTORS
	//

	public InterpretedProgram() {
		this.main = new InterpretedMain();
		this.definitions = new Definitions();
		this.sourceFileLocation = "";
	}

	public InterpretedProgram(String sourceFileLocation,
			Definitions definitions, List<String> notes, InterpretedMain main) {

		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (definitions == null)
			throw new NullPointerException();
		if (notes == null)
			throw new NullPointerException();
		if (main == null)
			throw new NullPointerException();
		if (notes.contains(null))
			throw new NullPointerException();

		this.main = main;
		this.notes.addAll(notes);
		this.definitions = definitions;
		this.sourceFileLocation = sourceFileLocation;
	}

	//
	// METHODS - PUBLIC
	//

	public Definitions getDefinitions() {
		return definitions;
	}

	public InterpretedMain getMain() {
		return main;
	}

	public List<String> getNotes() {
		return notes;
	}

	public String getSourceFileLocation() {
		return sourceFileLocation;
	}
}