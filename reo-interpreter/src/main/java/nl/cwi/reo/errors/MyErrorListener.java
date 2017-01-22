package nl.cwi.reo.errors;

import java.io.File;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class MyErrorListener extends BaseErrorListener {
//	@Override
//	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
//			int line, int charPositionInLine, String msg, RecognitionException e) {		
//		String source = recognizer.getInputStream().getSourceName();
//		System.err.println("test");
//		System.out.println(new Message(MessageType.ERROR, source, line, charPositionInLine, msg).toString());
//	}
	
	public boolean hasError = false;
	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
			int line, int charPositionInLine, String msg, RecognitionException e) {
		hasError = true;
		String source = new File(recognizer.getInputStream().getSourceName()).getName();
		System.err.println(new Message(MessageType.ERROR, source, line, charPositionInLine, msg).toString());
	}
}
