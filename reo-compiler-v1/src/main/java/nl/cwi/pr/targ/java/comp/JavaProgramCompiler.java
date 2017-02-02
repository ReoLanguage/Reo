package nl.cwi.pr.targ.java.comp;

import java.util.List;

import nl.cwi.pr.Platform;
import nl.cwi.pr.autom.AutomatonFactory;
import nl.cwi.pr.tools.CompiledGeneratee;
import nl.cwi.pr.tools.CompiledProgram;
import nl.cwi.pr.tools.CompilerSettings;
import nl.cwi.pr.tools.InterpretedMain;
import nl.cwi.pr.tools.InterpretedProgram;
import nl.cwi.pr.tools.InterpretedProtocol;
import nl.cwi.pr.tools.InterpretedWorker;
import nl.cwi.pr.tools.comp.MainCompiler;
import nl.cwi.pr.tools.comp.ProgramCompiler;
import nl.cwi.pr.tools.comp.ProtocolCompiler;
import nl.cwi.pr.tools.comp.WorkerCompiler;
import nl.cwi.pr.util.Timestamps;


public class JavaProgramCompiler extends ProgramCompiler {
	private final String programPackageName;

	//
	// CONSTRUCTORS
	//

	public JavaProgramCompiler(CompilerSettings settings,
			InterpretedProgram program, AutomatonFactory automatonFactory) {

		super(settings, program, automatonFactory);
		this.programPackageName = "pr." + Timestamps.getNext();
	}

	//
	// METHODS - PUBLIC
	//

	public String getProgramPackageName() {
		return programPackageName;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected CompiledProgram newCompiledProgram(
			String sourceRunTimeDirectoryLocation,
			List<CompiledGeneratee> generatees) {

		if (sourceRunTimeDirectoryLocation == null)
			throw new NullPointerException();
		if (generatees == null)
			throw new NullPointerException();
		if (generatees.contains(null))
			throw new NullPointerException();

		return new JavaCompiledProgram(sourceRunTimeDirectoryLocation,
				generatees);
	}

	@Override
	protected MainCompiler<?> newMainCompiler(InterpretedMain main) {
		if (main == null)
			throw new NullPointerException();

		return new JavaMainCompiler(this, main);
	}

	@Override
	protected ProtocolCompiler<?> newProtocolCompiler(
			InterpretedProtocol protocol, AutomatonFactory automatonFactory) {

		if (protocol == null)
			throw new NullPointerException();
		if (automatonFactory == null)
			throw new NullPointerException();

		return new JavaProtocolCompiler(this, protocol, automatonFactory);
	}

	@Override
	protected WorkerCompiler<?> newWorkerCompiler(InterpretedWorker worker) {
		if (worker == null)
			throw new NullPointerException();

		return new JavaWorkerCompiler(this, worker);
	}

	/*
	 * Change the path
	 */
	@Override
	protected String getSourceRunTimeDirectoryLocation() {
//		if (!Platform.hasFileInBundle("nl.cwi.pr.runtime.java", "src"))
//			addError("Access failure on run-time library", true);

		return this.getSettings().getRunTimeLocation() ;
				//Platform.getFileInBundle("nl.cwi.pr.runtime.java", "src")	.getAbsolutePath();
	}
}