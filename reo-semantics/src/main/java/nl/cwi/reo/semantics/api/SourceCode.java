package nl.cwi.reo.semantics.api;

import java.io.File;

import nl.cwi.reo.pr.comp.Language;

public final class SourceCode {

	private final File file;
	
	private final String call;
	
	private Language targetLanguage;
	
	public SourceCode() {
		this.file = null;
		this.call = "";
		this.targetLanguage=null;
	}
	
	public SourceCode(String file, String language) {
		this.file = new File(file);
		this.call = "";
		if(language=="C11")
			this.targetLanguage=Language.C11;
		if(language=="JAVA")
			this.targetLanguage=Language.JAVA;
	}

	public File getFile() {
		return file;
	}

	public String getCall() {
		return call;
	}
	public Language getLanguage() {
		return targetLanguage;
	}
}
