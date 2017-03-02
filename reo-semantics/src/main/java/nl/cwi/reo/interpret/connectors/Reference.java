package nl.cwi.reo.interpret.connectors;

import java.io.File;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Reference to an atomic component that is specified in a different language.
 */
public final class Reference {

	@Nullable
	private final File file;
	
	private final String call;

	@Nullable
	private Language lang;
	
	public Reference() {
		this.file = null;
		this.call = "";
		this.lang = null;
	}
	
	public Reference(String file, String language) {
		this.file = new File(file);
		this.call = "";
		if (language == "C11")
			this.lang = Language.C11;
		if (language == "JAVA")
			this.lang = Language.JAVA;
	}

	@Nullable
	public File getFile() {
		return file;
	}

	public String getCall() {
		return call;
	}
	
	@Nullable
	public Language getLanguage() {
		return lang;
	}
	
	@Override
	public String toString() {
		return call + (file == null ? "" : " at " + file.getPath());
	}
}
