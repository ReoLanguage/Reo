package nl.cwi.pr.tools.comp;

import java.util.List;

import nl.cwi.pr.autom.AutomatonFactory;
import nl.cwi.pr.tools.CompiledGeneratee;
import nl.cwi.pr.tools.CompiledProgram;
import nl.cwi.pr.tools.CompilerSettings;
import nl.cwi.pr.tools.InterpretedMain;
import nl.cwi.pr.tools.InterpretedProgram;
import nl.cwi.pr.tools.InterpretedProtocol;
import nl.cwi.pr.tools.InterpretedWorker;

public class DefaultProgramCompiler extends ProgramCompiler {

	//
	// CONSTRUCTORS
	//

	public DefaultProgramCompiler(CompilerSettings settings,
			InterpretedProgram program, AutomatonFactory automatonFactory) {

		super(settings, program, automatonFactory);
	}

	//
	// METHODS - PUBLIC
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

		return new CompiledProgram(sourceRunTimeDirectoryLocation, generatees) {
			@Override
			public String getMain() {
				return "";
			}
		};
	}

	@Override
	protected MainCompiler<?> newMainCompiler(InterpretedMain main) {
		if (main == null)
			throw new NullPointerException();

		return new DefaultMainCompiler(this, main);
	}

	@Override
	protected ProtocolCompiler<?> newProtocolCompiler(
			InterpretedProtocol protocol, AutomatonFactory automatonFactory) {

		if (protocol == null)
			throw new NullPointerException();
		if (automatonFactory == null)
			throw new NullPointerException();

		return new DefaultProtocolCompiler(this, protocol, automatonFactory);
	}

	@Override
	protected WorkerCompiler<?> newWorkerCompiler(InterpretedWorker worker) {
		if (worker == null)
			throw new NullPointerException();

		return new DefaultWorkerCompiler(this, worker);
	}

	@Override
	protected String getSourceRunTimeDirectoryLocation() {
		return "";
	}

}
