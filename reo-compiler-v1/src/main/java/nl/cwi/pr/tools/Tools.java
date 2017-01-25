package nl.cwi.pr.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.autom.AutomatonFactory;
import nl.cwi.pr.misc.DefaultMainArgumentFactory;
import nl.cwi.pr.misc.DefaultPortFactory;
import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.MainArgumentFactory;
import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.targ.c.autom.CAutomatonFactory;
import nl.cwi.pr.targ.c.autom.CMainArgumentFactory;
import nl.cwi.pr.targ.java.autom.JavaAutomatonFactory;
import nl.cwi.pr.targ.java.autom.JavaMainArgumentFactory;
import nl.cwi.pr.targ.java.comp.JavaProgramCompiler;
import nl.cwi.pr.tools.comp.ProgramCompiler;
import nl.cwi.pr.tools.interpr.ProgramInterpreter;
import nl.cwi.pr.tools.pars.PrLexer;
import nl.cwi.pr.tools.pars.PrParser;
import nl.cwi.pr.tools.pars.PrParser.ProgramContext;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.eclipse.core.runtime.FileLocator;

public class Tools {

	//
	// STATIC - METHODS - PUBLIC
	//

	public static ToolResult<CompiledProgram> compile(
			CompilerSettings settings) {

		if (settings == null)
			throw new NullPointerException();

		String sourceFileLocation = settings.getSourceFileLocation();
		Language targetLanguage = settings.getTargetLanguage();

		ToolResult<InterpretedProgram> interpreterResult = null;
		ProgramCompiler programCompiler = null;

		try {

			/*
			 * Get language-dependent factories
			 */

			AutomatonFactory automatonFactory = null;
			PortFactory portFactory = null;
			MainArgumentFactory mainArgumentFactory = null;
			switch (targetLanguage) {
			case JAVA:
				automatonFactory = new JavaAutomatonFactory();
				portFactory = automatonFactory.getPortFactory();
				mainArgumentFactory = new JavaMainArgumentFactory(
						((JavaAutomatonFactory) automatonFactory)
								.getJavaNames());
				break;

			case C11:
				automatonFactory = new CAutomatonFactory();
				portFactory = automatonFactory.getPortFactory();
				mainArgumentFactory = new CMainArgumentFactory();
				break;
			default:
				return new ToolResult<CompiledProgram>(new CompilerError(
						sourceFileLocation, "Unsupported target language "
								+ targetLanguage));
			}

			/*
			 * Ignore input, if set
			 */

			InterpretedProgram interpretedProgram = null;
			if (settings.ignoreInput()) {
				interpretedProgram = new InterpretedProgram();
			}

			/*
			 * Chose your semantic to interpret your program :
			 * 		Create specific automaton
			 */
			
			if(settings.isKSemantic()){
				interpreterResult = interpret(sourceFileLocation, portFactory,
						mainArgumentFactory);				
			}
			
			else {
				interpreterResult = interpret(sourceFileLocation, portFactory,
						mainArgumentFactory);

				if (!interpreterResult.has() || interpreterResult.hasErrors())
					return new ToolResult<CompiledProgram>(
							interpreterResult.getErrors());

				interpretedProgram = interpreterResult.get();
			}

			/*
			 * Get language-dependent compiler
			 */

			switch (targetLanguage) {
			case JAVA:
				programCompiler = new JavaProgramCompiler(settings,
						interpretedProgram, automatonFactory);
				break;

			case C11:
				// programCompiler = new CProgramCompiler(settings,
				// interpretedProgram, automatonFactory);
				break;

			default:
				return new ToolResult<CompiledProgram>(new CompilerError(
						sourceFileLocation, "Unsupported target language "
								+ targetLanguage));
			}

			CompiledProgram compiledProgram = programCompiler.compile();
			if (programCompiler.hasErrors())
				return new ToolResult<CompiledProgram>(
						programCompiler.getErrors());
			else
				return new ToolResult<CompiledProgram>(compiledProgram);
		}

		catch (ToolError error) {
			List<ToolError> errors = new ArrayList<ToolError>();

			if (interpreterResult != null)
				errors.addAll(interpreterResult.getErrors());
			if (!errors.contains(error))
				errors.add((ToolError) error);

			return new ToolResult<CompiledProgram>(errors);
		}
	}

	public static ToolResult<InterpretedProgram> interpret(
			String sourceFileLocation) {

		if (sourceFileLocation == null)
			throw new NullPointerException();

		Reader reader;
		try {
			if (sourceFileLocation.startsWith("platform:"))
				reader = new InputStreamReader(FileLocator.find(
						new URL(sourceFileLocation)).openStream());
			else
				reader = new FileReader(sourceFileLocation);
		}

		catch (IOException exception) {
			return new ToolResult<InterpretedProgram>(new ParserError(
					sourceFileLocation, "Access failure on location \""
							+ sourceFileLocation + "\"", exception));
		}

		return interpret(sourceFileLocation, reader);
	}

	public static ToolResult<InterpretedProgram> interpret(
			String sourceFileLocation, PortFactory portFactory,
			MainArgumentFactory mainArgumentFactory) {

		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (portFactory == null)
			throw new NullPointerException();
		if (mainArgumentFactory == null)
			throw new NullPointerException();

		try {
			return interpret(sourceFileLocation, new FileReader(
					sourceFileLocation), portFactory, mainArgumentFactory);
		}

		catch (FileNotFoundException exception) {
			return new ToolResult<InterpretedProgram>(new ParserError(
					sourceFileLocation, "Access failure on location \""
							+ sourceFileLocation + "\"", exception));
		}
	}

	public static ToolResult<InterpretedProgram> interpret(
			String sourceFileLocation, Reader sourceFileReader) {

		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (sourceFileReader == null)
			throw new NullPointerException();

		return interpret(sourceFileLocation, sourceFileReader,
				new DefaultPortFactory(), new DefaultMainArgumentFactory());
	}

	public static ToolResult<InterpretedProgram> interpret(
			String sourceFileLocation, Reader sourceFileReader,
			PortFactory portFactory, MainArgumentFactory mainArgumentFactory) {

		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (sourceFileReader == null)
			throw new NullPointerException();
		if (portFactory == null)
			throw new NullPointerException();
		if (mainArgumentFactory == null)
			throw new NullPointerException();

		return interpret(sourceFileLocation,
				parse(sourceFileLocation, sourceFileReader), portFactory,
				mainArgumentFactory);
	}

	public static ToolResult<InterpretedProgram> interpret(
			String sourceFileLocation, ToolResult<ParsedProgram> parsedProgram) {

		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (parsedProgram == null)
			throw new NullPointerException();

		return interpret(sourceFileLocation, parsedProgram,
				new DefaultPortFactory(), new DefaultMainArgumentFactory());
	}

	public static synchronized ToolResult<InterpretedProgram> interpret(
			String sourceFileLocation, ToolResult<ParsedProgram> parserResult,
			PortFactory portFactory, MainArgumentFactory mainArgumentFactory) {

		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (parserResult == null)
			throw new NullPointerException();
		if (portFactory == null)
			throw new NullPointerException();
		if (mainArgumentFactory == null)
			throw new NullPointerException();

		ProgramInterpreter programInterpreter = null;
		try {
			if (!parserResult.has() || parserResult.hasErrors())
				return new ToolResult<InterpretedProgram>(
						parserResult.getErrors());

			ParsedProgram parsedProgram = parserResult.get();
			programInterpreter = new ProgramInterpreter(sourceFileLocation,
					parsedProgram.getProgramContext());

			Environment.nextScope = 1;

			InterpretedProgram interpretedProgram = programInterpreter
					.interpret(new Factories(mainArgumentFactory, portFactory),
							new Definitions(), new Environment());

			if (programInterpreter.hasErrors())
				return new ToolResult<InterpretedProgram>(
						programInterpreter.getErrors());
			else
				return new ToolResult<InterpretedProgram>(interpretedProgram);
		}

		catch (ToolError error) {
			List<ToolError> errors = new ArrayList<ToolError>();

			if (parserResult != null)
				errors.addAll(parserResult.getErrors());
			if (programInterpreter != null)
				errors.addAll(programInterpreter.getErrors());
			if (!errors.contains(error))
				errors.add((ToolError) error);

			return new ToolResult<InterpretedProgram>(errors);
		}
	}

	public static ToolResult<ParsedProgram> parse(String sourceFileLocation) {
		if (sourceFileLocation == null)
			throw new NullPointerException();

		Reader reader;
		try {
			if (sourceFileLocation.startsWith("platform:"))
				reader = new InputStreamReader(FileLocator.find(
						new URL(sourceFileLocation)).openStream());
			else
				reader = new FileReader(sourceFileLocation);
		}

		catch (IOException exception) {
			return new ToolResult<ParsedProgram>(new ParserError(
					sourceFileLocation, "Access failure on location \""
							+ sourceFileLocation + "\"", exception));
		}

		return parse(sourceFileLocation, reader);
	}

	public static ToolResult<ParsedProgram> parse(
			final String sourceFileLocation, Reader sourceFileReader) {

		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (sourceFileReader == null)
			throw new NullPointerException();

		try {
			ANTLRInputStream stream = null;
			try {
				stream = new ANTLRInputStream(sourceFileReader);
			} catch (IOException exception) {
				return new ToolResult<ParsedProgram>(new ParserError(
						sourceFileLocation, "Access failure on location \""
								+ sourceFileLocation + "\"", exception));
			}

			PrLexer lexer = new PrLexer(stream);
			PrParser parser = new PrParser(new CommonTokenStream(lexer));

			final List<ToolError> errors = new ArrayList<ToolError>();
			parser.removeErrorListeners();
			parser.addErrorListener(new ConsoleErrorListener() {
				@Override
				public void syntaxError(Recognizer<?, ?> recognizer,
						Object offendingSymbol, int line,
						int charPositionInLine, String msg,
						RecognitionException e) {

					Integer begin, end;
					if (offendingSymbol instanceof Token) {
						Token token = (Token) offendingSymbol;
						begin = token.getStartIndex();
						end = token.getStopIndex();
					} else {
						begin = null;
						end = null;
					}

					errors.add(new ParserError(sourceFileLocation, begin,
							end + 1, line, charPositionInLine, msg));
				}
			});

			ProgramContext programContext = parser.program();
			if (errors.isEmpty())
				return new ToolResult<ParsedProgram>(new ParsedProgram(
						programContext));
			else
				return new ToolResult<ParsedProgram>(errors);
		}

		catch (ToolError error) {
			return new ToolResult<ParsedProgram>(error);
		}
	}
}