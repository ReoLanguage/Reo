package nl.cwi.pr.tools.interpr;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.tools.InterpretedProgram;
import nl.cwi.pr.tools.ParsedProgram;
import nl.cwi.pr.tools.ToolResult;
import nl.cwi.pr.tools.Tools;
import nl.cwi.pr.tools.pars.PrParser.IncludeContext;

public class IncludeInterpreter extends
		Interpreter<IncludeContext, Definitions> {

	//
	// FIELDS
	//

	private final Map<String, Boolean> sourceFileLocationsToInclude;
	private final String sourceFileLocationToInclude;

	//
	// CONSTRUCTORS
	//

	public IncludeInterpreter(Interpreter<?, ?> parent, IncludeContext context,
			Map<String, Boolean> sourceFileLocationsToInclude) {

		super(parent, context);

		if (sourceFileLocationsToInclude == null)
			throw new NullPointerException();
		if (sourceFileLocationsToInclude.containsKey(null))
			throw new NullPointerException();
		if (sourceFileLocationsToInclude.containsValue(null))
			throw new NullPointerException();

		/*
		 * Initialize this.sourceFileLocationToInclude
		 */

		String sourceFileLocationToInclude = context.location.getText();
		File sourceFile = new File(sourceFileLocationToInclude);

		if (sourceFileLocationToInclude.startsWith("platform:")) {
			this.sourceFileLocationToInclude = sourceFileLocationToInclude;
		}

		else {
			if (!sourceFile.isAbsolute())
				sourceFile = new File(new File(getSourceFileLocation())
						.getParentFile().getAbsolutePath()
						+ File.separator
						+ sourceFileLocationToInclude);

			try {
				sourceFile = sourceFile.getCanonicalFile();
			} catch (IOException exc) {
				addError("Access failure on location \""
						+ sourceFileLocationToInclude + "\"", exc, true);
			}

			this.sourceFileLocationToInclude = sourceFile.getAbsolutePath();
		}

		/*
		 * Initialize this.sourceFileLocationsToInclude
		 */

		if (!sourceFileLocationsToInclude
				.containsKey(this.sourceFileLocationToInclude))
			sourceFileLocationsToInclude.put(this.sourceFileLocationToInclude,
					false);

		this.sourceFileLocationsToInclude = sourceFileLocationsToInclude;
	}

	//
	// METHODS
	//

	@Override
	public Definitions interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);

		if (sourceFileLocationsToInclude.get(sourceFileLocationToInclude)) {
			return new Definitions();
		}

		else {
			sourceFileLocationsToInclude.put(sourceFileLocationToInclude, true);

			/*
			 * Parse include
			 */

			ToolResult<ParsedProgram> result = Tools
					.parse(this.sourceFileLocationToInclude);

			if (!result.has() || result.hasErrors()) {
				addErrors(result.getErrors());
				addError("Erroneous include \""
						+ this.sourceFileLocationToInclude + "\"", true);
			}

			ProgramInterpreter programInterpreter = new ProgramInterpreter(
					this.sourceFileLocationToInclude, result.get()
							.getProgramContext(),
					this.sourceFileLocationsToInclude);

			addChild(programInterpreter);

			/*
			 * Interpret include
			 */

			InterpretedProgram program = programInterpreter.interpret(
					factories, new Definitions(), new Environment());

			addErrors(programInterpreter.getErrors());
			return program.getDefinitions();
		}
	}
}
