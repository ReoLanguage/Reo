package nl.cwi.reo.interpret.listeners;

import java.io.File;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving error events. The class that is
 * interested in processing a error event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addErrorListener</code> method. When the error event
 * occurs, that object's appropriate method is invoked.
 */
public class ErrorListener extends BaseErrorListener {

	/** The has error. */
	public boolean hasError = false;

	/** The m. */
	private final Monitor m;

	/**
	 * Instantiates a new error listener.
	 *
	 * @param m
	 *            the m
	 */
	public ErrorListener(Monitor m) {
		this.m = m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.antlr.v4.runtime.BaseErrorListener#syntaxError(org.antlr.v4.runtime.
	 * Recognizer, java.lang.Object, int, int, java.lang.String,
	 * org.antlr.v4.runtime.RecognitionException)
	 */
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		hasError = true;
		String source = new File(recognizer.getInputStream().getSourceName()).getName();
		Location location = new Location(source, line, charPositionInLine);
		m.add(location, msg);
	}
}
