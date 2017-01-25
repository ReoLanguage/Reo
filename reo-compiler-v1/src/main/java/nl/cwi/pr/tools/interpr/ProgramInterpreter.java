package nl.cwi.pr.tools.interpr;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.cwi.pr.autom.Extralogical;
import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.Family;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.tools.InterpretedMain;
import nl.cwi.pr.tools.InterpretedProgram;
import nl.cwi.pr.tools.pars.PrLexer;
import nl.cwi.pr.tools.pars.PrParser;
import nl.cwi.pr.tools.pars.PrParser.ExtralogicalDefinitionContext;
import nl.cwi.pr.tools.pars.PrParser.FamilyDefinitionContext;
import nl.cwi.pr.tools.pars.PrParser.IncludeContext;
import nl.cwi.pr.tools.pars.PrParser.IntegerDefinitionContext;
import nl.cwi.pr.tools.pars.PrParser.MainDefinitionContext;
import nl.cwi.pr.tools.pars.PrParser.NoteContext;
import nl.cwi.pr.tools.pars.PrParser.ProgramContext;
import nl.cwi.pr.tools.pars.PrParser.WorkerNameDefinitionContext;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class ProgramInterpreter extends
		Interpreter<ProgramContext, InterpretedProgram> {

	//
	// FIELDS
	//

	private final List<ExtralogicalDefinitionInterpreter> extralogicalDefinitionInterpreters = new ArrayList<>();
	private final List<FamilyDefinitionInterpreter> familyDefinitionInterpreters = new ArrayList<>();
	private final List<IncludeInterpreter> includeInterpreters = new ArrayList<>();
	private final List<IntegerDefinitionInterpreter> integerDefinitionInterpreters = new ArrayList<>();
	private final List<MainDefinitionInterpreter> mainDefinitionInterpreters = new ArrayList<>();
	private final List<NoteInterpreter> noteInterpreters = new ArrayList<>();
	private final List<WorkerNameDefinitionInterpreter> workerNameDefinitionInterpreters = new ArrayList<>();

	private final boolean isMain;

	//
	// CONSTRUCTORS
	//

	public ProgramInterpreter(String sourceFileLocation, ProgramContext context) {
		this(sourceFileLocation, context, new HashMap<String, Boolean>());
	}

	public ProgramInterpreter(String sourceFileLocation,
			ProgramContext context,
			Map<String, Boolean> sourceFileLocationsToInclude) {

		super(sourceFileLocation, context);

		if (sourceFileLocationsToInclude == null)
			throw new NullPointerException();
		if (sourceFileLocationsToInclude.containsKey(null))
			throw new NullPointerException();
		if (sourceFileLocationsToInclude.containsValue(null))
			throw new NullPointerException();

		/*
		 * Initialize this.isMain
		 */

		this.isMain = !sourceFileLocationsToInclude
				.containsKey(sourceFileLocation);

		/*
		 * Initialize this.includeInterpreters
		 */

 		if (this.isMain)
			try {
				this.includeInterpreters.add(new IncludeInterpreter(this,
						getCoreIncludeContext(), sourceFileLocationsToInclude));
			}

			catch (IOException exc) {
				addError("Access failure on standard library", exc, true);
			}

		try {
			for (IncludeContext c : context.include())
				this.includeInterpreters.add(new IncludeInterpreter(this, c,
						sourceFileLocationsToInclude));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.noteInterpreters
		 */

		try {
			for (NoteContext c : context.note())
				this.noteInterpreters.add(new NoteInterpreter(this, c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.integerDefinitionInterpreters
		 */

		try {
			for (IntegerDefinitionContext c : context.integerDefinition())
				this.integerDefinitionInterpreters
						.add(new IntegerDefinitionInterpreter(this, c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.extralogicalDefinitionInterpreters
		 */

		try {
			for (ExtralogicalDefinitionContext c : context
					.extralogicalDefinition())

				this.extralogicalDefinitionInterpreters
						.add(new ExtralogicalDefinitionInterpreter(this, c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.familyDefinitionInterpreters
		 */

		try {
			for (FamilyDefinitionContext c : context.familyDefinition())
				this.familyDefinitionInterpreters
						.add(FamilyDefinitionInterpreter.newInstance(this, c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.workerNameNameInterpreters
		 */

		try {
			for (WorkerNameDefinitionContext c : context.workerNameDefinition())
				this.workerNameDefinitionInterpreters
						.add(new WorkerNameDefinitionInterpreter(this, c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.mainDefinitionInterpreters
		 */

		try {
			for (MainDefinitionContext c : context.mainDefinition())
				this.mainDefinitionInterpreters
						.add(new MainDefinitionInterpreter(this, c));
		}

		catch (NullPointerException exc) {
			exc.printStackTrace();
		}
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public InterpretedProgram interpret(Factories factories,
			Definitions definitions, Environment environment) {

		super.interpret(factories, definitions, environment);

		/*
		 * Interpret includes
		 */

		Definitions newDefinitions = new Definitions();

		for (IncludeInterpreter interpr : includeInterpreters) {
			Definitions includeDefinitions = interpr.interpret(factories,
					new Definitions(), new Environment());

			Map<TypedName, Integer> integers;
			Map<TypedName, Extralogical> extralogicals;
			Map<TypedName, Family> families;
			Map<TypedName, String> workerNames;

			integers = includeDefinitions.getIntegers();
			extralogicals = includeDefinitions.getExtralogicals();
			families = includeDefinitions.getFamilies();
			workerNames = includeDefinitions.getWorkerNames();

			for (Entry<TypedName, Integer> entr : integers.entrySet())
				newDefinitions.putInteger(entr.getKey(), entr.getValue(), this);

			for (Entry<TypedName, Extralogical> entr : extralogicals.entrySet())
				newDefinitions.putExtralogical(entr.getKey(), entr.getValue(),
						this);

			for (Entry<TypedName, Family> entr : families.entrySet())
				newDefinitions.putFamily(entr.getKey(), entr.getValue(), this);

			for (Entry<TypedName, String> entr : workerNames.entrySet())
				newDefinitions.putWorkerName(entr.getKey(), entr.getValue(),
						this);
		}

		/*
		 * Interpret integer definitions
		 */

		for (IntegerDefinitionInterpreter interpr : integerDefinitionInterpreters)
			newDefinitions = interpr.interpret(factories, newDefinitions,
					environment);

		/*
		 * Interpret extralogical definitions
		 */

		for (ExtralogicalDefinitionInterpreter interpr : extralogicalDefinitionInterpreters)
			newDefinitions = interpr.interpret(factories, newDefinitions,
					environment);

		/*
		 * Interpret family definitions
		 */

		for (FamilyDefinitionInterpreter interpr : familyDefinitionInterpreters)
			newDefinitions = interpr.interpret(factories, newDefinitions,
					environment);

		/*
		 * Interpret worker name names
		 */

		for (WorkerNameDefinitionInterpreter interpr : workerNameDefinitionInterpreters)
			newDefinitions = interpr.interpret(factories, newDefinitions,
					environment);
		/*
		 * Interpret notes
		 */

		List<String> notes = new ArrayList<>();

		if (isMain)
			for (NoteInterpreter interpr : noteInterpreters)
				notes.add(interpr.interpret(factories, newDefinitions,
						environment));

		/*
		 * Interpret main definitions
		 */

		InterpretedMain main = null;

		if (isMain)
			for (MainDefinitionInterpreter interpr : mainDefinitionInterpreters)
				if (main == null)
					main = interpr.interpret(factories, newDefinitions,
							environment);
				else
					interpr.addError("Superfluous main definition", false);

		if (main == null)
			main = new InterpretedMain();

		/*
		 * Return
		 */

		return new InterpretedProgram(getSourceFileLocation(), newDefinitions,
				notes, main);
	}

	//
	// STATIC - FIELDS
	//
/*
 *private static String CORE_SOURCE_FILE_LOCATION = "platform:/plugin/nl.cwi.pr/src/nl/cwi/pr/autom/libr/Core.pr";
 */
	private static String CORE_SOURCE_FILE_LOCATION = "/ufs/dokter/workspace/Reo/reo-compiler-v1/src/main/java/nl/cwi/pr/autom/libr/Core.pr";

	//
	// STATIC - METHODS
	//

	private static IncludeContext getCoreIncludeContext() throws IOException {
		String includeString = "include(\"" + CORE_SOURCE_FILE_LOCATION + "\")";
		Reader reader = new StringReader(includeString);
		ANTLRInputStream stream = new ANTLRInputStream(reader);
		PrLexer lexer = new PrLexer(stream);
		PrParser parser = new PrParser(new CommonTokenStream(lexer));
		return parser.include();
	}
}
