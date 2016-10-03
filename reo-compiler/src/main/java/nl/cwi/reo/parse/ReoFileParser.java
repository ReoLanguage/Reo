package nl.cwi.reo.parse;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.cwi.reo.semantics.Program;

public class ReoFileParser {
	
	/**
	 * Reo file listener.
	 */
	private final ReoFileListener listener;
	
	/**
	 * Constructor.
	 * @param listener	listener
	 */
	public ReoFileParser(ReoFileListener listener) {
		this.listener = listener;
	}

	/**
	 * Parses the Reo file.
	 * @param file		name of the file
	 * @return	program object defined in the Reo file, or null if the program could not be parsed.
	 */
	public Program parse(String file) {
		
		Program program = null;
		
		try {
			// Get the input file
			CharStream c = new ANTLRFileStream(file);

			// Initialize lexer and parser
			ReoLexer lexer = new ReoLexer(c); 
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			ReoParser parser = new ReoParser(tokens);
	
			// Parse the file
			ParseTree tree = parser.file();
	
			// Walk over the ParseTree and listen to events
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(listener, tree);
			
			program = listener.getProgram();
		} catch (IOException e) {
			System.out.println(file + " is invalid input.");
		}
		
		return program;
	}
}
