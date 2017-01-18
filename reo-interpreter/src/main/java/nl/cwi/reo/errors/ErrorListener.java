package nl.cwi.reo.errors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ErrorListener extends BaseErrorListener {
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
			int line, int charPositionInLine, String msg, RecognitionException e) {		
		String source = recognizer.getInputStream().getSourceName();
		System.out.println(new Message(MessageType.ERROR, source, line, charPositionInLine, msg));
	}
}
