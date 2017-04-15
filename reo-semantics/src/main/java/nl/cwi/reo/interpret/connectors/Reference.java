package nl.cwi.reo.interpret.connectors;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Reference to an atomic component that is specified in a different language.
 */
public final class Reference {

	@Nullable
	private final String ref;

	@Nullable
	private Language lang;
	
	public Reference() {
		this.ref = null;
		this.lang = null;
	}
	
	public Reference(String ref, String language) {
		this.ref = ref;
		if (language == "C11")
			this.lang = Language.C11;
		if (language == "JAVA")
			this.lang = Language.JAVA;
	}

	@Nullable
	public String getReference() {
		return ref;
	}
	
	@Nullable
	public Language getLanguage() {
		return lang;
	}
	
	@Override
	public String toString() {
		return ref != null ? ref : "";
	}
}
