package nl.cwi.reo.interpret.connectors;

import java.io.File;

public final class SourceCode {

	private final File file;
	
	private final String call;
	
	private Language lang;
	
	public SourceCode() {
		this.file = null;
		this.call = "";
		this.lang = null;
	}
	
	public SourceCode(String file, String language) {
		this.file = new File(file);
		this.call = "";
		if (language == "C11")
			this.lang = Language.C11;
		if (language == "JAVA")
			this.lang = Language.JAVA;
	}

	public File getFile() {
		return file;
	}

	public String getCall() {
		return call;
	}
	public Language getLanguage() {
		return lang;
	}
	@Override
	public String toString() {
		return call + (file == null ? "" : " at " + file.getPath());
	}
}
