package nl.cwi.reo.pr.misc;

import java.util.ArrayList;
import java.util.List;

public class FamilySignature {

	//
	// FIELDS
	//

	private final List<TypedName> extralogicalNames = new ArrayList<>();
	private final List<TypedName> inputPortOrArrayNames = new ArrayList<>();
	private final List<TypedName> integerNames = new ArrayList<>();
	private final List<TypedName> outputPortOrArrayNames = new ArrayList<>();

	private final TypedName name;

	//
	// CONSTRUCTORS
	//

	public FamilySignature(TypedName name, List<TypedName> integerNames,
			List<TypedName> extralogicalNames,
			List<TypedName> inputPortOrArrayNames,
			List<TypedName> outputPortOrArrayNames) {

		if (name == null)
			throw new NullPointerException();
		if (integerNames == null)
			throw new NullPointerException();
		if (extralogicalNames == null)
			throw new NullPointerException();
		if (inputPortOrArrayNames == null)
			throw new NullPointerException();
		if (outputPortOrArrayNames == null)
			throw new NullPointerException();
		if (integerNames.contains(null))
			throw new NullPointerException();
		if (extralogicalNames.contains(null))
			throw new NullPointerException();
		if (inputPortOrArrayNames.contains(null))
			throw new NullPointerException();
		if (outputPortOrArrayNames.contains(null))
			throw new NullPointerException();

		this.name = name;

		this.integerNames.addAll(integerNames);
		this.extralogicalNames.addAll(extralogicalNames);
		this.inputPortOrArrayNames.addAll(inputPortOrArrayNames);
		this.outputPortOrArrayNames.addAll(outputPortOrArrayNames);
	}

	//
	// METHODS
	//

	public List<TypedName> getExtralogicalNames() {
		return extralogicalNames;
	}

	public List<TypedName> getInputPortOrArrayNames() {
		return inputPortOrArrayNames;
	}

	public List<TypedName> getIntegerNames() {
		return integerNames;
	}

	public TypedName getName() {
		return name;
	}

	public List<TypedName> getOutputPortOrArrayNames() {
		return outputPortOrArrayNames;
	}
}