package nl.cwi.reo.errors;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ErrorListener extends BaseErrorListener {
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
			int line, int charPositionInLine, String msg, RecognitionException e) {
		List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
		Collections.reverse(stack);
		System.err.println("rule stack: " + stack);
		System.err.println("line " + line + ":" + charPositionInLine + " at " +	offendingSymbol + ": " + msg);
	}
}
