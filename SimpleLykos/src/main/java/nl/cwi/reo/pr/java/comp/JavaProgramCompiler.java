package nl.cwi.reo.pr.java.comp;

import java.util.List;

import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.java.comp.JavaCompiledProgram;
import nl.cwi.reo.pr.java.comp.JavaMainCompiler;
import nl.cwi.reo.pr.java.comp.JavaProtocolCompiler;
import nl.cwi.reo.pr.java.comp.JavaWorkerCompiler;
import nl.cwi.reo.pr.comp.CompiledGeneratee;
import nl.cwi.reo.pr.comp.CompiledProgram;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.pr.comp.InterpretedMain;
import nl.cwi.reo.pr.comp.InterpretedProgram;
import nl.cwi.reo.pr.comp.InterpretedProtocol;
import nl.cwi.reo.pr.comp.InterpretedWorker;
import nl.cwi.reo.pr.comp.MainCompiler;
import nl.cwi.reo.pr.comp.ProgramCompiler;
import nl.cwi.reo.pr.comp.ProtocolCompiler;
import nl.cwi.reo.pr.comp.WorkerCompiler;
import nl.cwi.reo.pr.util.Timestamps;

public class JavaProgramCompiler extends ProgramCompiler {
	private final String programPackageName;

	//
	// CONSTRUCTORS
	//

	public JavaProgramCompiler(CompilerSettings settings,
			InterpretedProgram program, AutomatonFactory automatonFactory) {

		super(settings, program, automatonFactory);
		this.programPackageName = "pr." + Timestamps.getNext();
//		this.programPackageName = "pr.main";

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
//		if (!Platform.hasFileInBundle("nl.cwi.reo.pr.runtime.java", "src"))
//			addError("Access failure on run-time library", true);

		return "/home/e-spin/workspace/nl/cwi/pr/runtime/java/src" ;
				//Platform.getFileInBundle("nl.cwi.reo.pr.runtime.java", "src")	.getAbsolutePath();
	}
}