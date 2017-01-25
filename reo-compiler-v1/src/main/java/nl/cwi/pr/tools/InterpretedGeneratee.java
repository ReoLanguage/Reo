package nl.cwi.pr.tools;

import java.util.HashMap;
import java.util.Map;

public class InterpretedGeneratee {
	private final Map<String, Object> annotations = new HashMap<>();

	//
	// METHODS - PUBLIC
	//

	public boolean hasAnnotation(String annotationName) {
		if (annotationName == null)
			throw new NullPointerException();

		return annotations.containsKey(annotationName);
	}

	public Object getAnnotation(String annotationName) {
		if (annotationName == null)
			throw new NullPointerException();
		if (!hasAnnotation(annotationName))
			throw new IllegalStateException();

		return annotations.get(annotationName);
	}

	public void addAnnotation(String annotationName, Object annotation) {
		if (annotationName == null)
			throw new NullPointerException();
		if (annotation == null)
			throw new NullPointerException();

		annotations.put(annotationName, annotation);
	}
}
