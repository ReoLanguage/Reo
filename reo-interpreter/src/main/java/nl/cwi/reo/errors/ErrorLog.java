package nl.cwi.reo.errors;

import java.util.ArrayList;
import java.util.List;

public class ErrorLog {
	
	private final List<ErrorMessage> msgs;
	
	public ErrorLog() {
		this.msgs = new ArrayList<ErrorMessage>();
	}
	
	public void add(ErrorType type, String msg, int line, int column) {
		msgs.add(new ErrorMessage(type, msg, line, column));
	}
	
	public void print() {
		for (ErrorMessage e : msgs)
			System.err.println(e.toString());
	}	
}
