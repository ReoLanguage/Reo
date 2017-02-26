package nl.cwi.reo.interpret.listeners;

import java.io.File;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.MessageType;

public class ErrorListener extends BaseErrorListener {
	
	public boolean hasError = false;
	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
			int line, int charPositionInLine, String msg, RecognitionException e) {
		hasError = true;
		String source = new File(recognizer.getInputStream().getSourceName()).getName();
		Location location = new Location(source, line, charPositionInLine);
		System.err.println(new Message(MessageType.ERROR, location, msg).toString());
	}
}
