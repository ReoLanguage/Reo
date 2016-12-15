package nl.cwi.reo.interpret;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Interpreter {

	/**
	 * Parses the Reo file.
	 * @param file		name of the file
	 * @return	program object defined in the Reo file, or null if the program could not be parsed.
	 */
	public Component interpret(String file, SemanticsType semantics) {
		
		Component program = null;
		
//		try {
//			// Get the input file
//			CharStream c = new ANTLRFileStream(file);
//
//			// Initialize lexer and parser
//			TreoLexer lexer = new TreoLexer(c); 
//			CommonTokenStream tokens = new CommonTokenStream(lexer);
//			TreoParser parser = new TreoParser(tokens);
//	
//			// Parse the file
//			ParseTree tree = parser.file();
//			
//			// Select the correct listener class
//			ListenerBase listener;
//			switch (semantics) {
//			case WA:
//				listener = new ListenerWorkAutomata();
//				break;
//			default:
//				listener = new ListenerBase();
//				break;				
//			}
//
//			// Walk over the ParseTree and listen to events
//			ParseTreeWalker walker = new ParseTreeWalker();
//			walker.walk(listener, tree);			
//			program = listener.getProgram();
//			
//		} catch (IOException e) {
//			System.out.println(file + " is invalid input.");
//		}
		
		return program;
	}
}
