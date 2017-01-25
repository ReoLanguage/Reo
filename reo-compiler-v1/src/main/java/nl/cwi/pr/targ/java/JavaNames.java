package nl.cwi.pr.targ.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JavaNames {
	private final Set<String> identifiers = new HashSet<>();

	//
	// METHODS - PUBLIC
	//

	public String getFreshName(String identifier) {
		if (identifier == null)
			throw new NullPointerException();
		if (identifier.isEmpty())
			throw new IllegalArgumentException();
		
		String identifier0 = "";
		for (char ch : identifier.toCharArray())
			if (identifier0.isEmpty())
				identifier0 += Character.isJavaIdentifierStart(ch) ? ch : "_";
			else
				identifier0 += Character.isJavaIdentifierPart(ch) ? ch : "_";

		identifier = identifier0;
		int i = 0;
		while (identifiers.contains(identifier)
				|| JAVA_KEYWORDS.contains(identifier))

			identifier = identifier0 + "$" + ++i;
		
		return identifier;
	}

	//
	// STATIC
	//

	public static final Set<String> JAVA_KEYWORDS = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(new String[] {
					"abstract", "assert", "boolean", "break", "byte", "case",
					"catch", "char", "class", "const", "continue", "default",
					"do", "double", "else", "enum", "extends", "final",
					"finally", "float", "for", "if", "goto", "implements",
					"import", "instanceof", "int", "interface", "long",
					"native", "new", "package", "private", "protected",
					"public", "return", "short", "static", "strictfp", "super",
					"switch", "synchronized", "this", "throw", "throws",
					"transient", "try", "void", "volatile", "while", "true",
					"false", "null" })));
}
