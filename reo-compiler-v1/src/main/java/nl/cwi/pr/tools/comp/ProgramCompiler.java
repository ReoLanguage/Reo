package nl.cwi.pr.tools.comp;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.autom.AutomatonFactory;
import nl.cwi.pr.tools.CompiledGeneratee;
import nl.cwi.pr.tools.CompiledProgram;
import nl.cwi.pr.tools.CompilerProgressMonitor;
import nl.cwi.pr.tools.CompilerSettings;
import nl.cwi.pr.tools.InterpretedMain;
import nl.cwi.pr.tools.InterpretedProgram;
import nl.cwi.pr.tools.InterpretedProtocol;
import nl.cwi.pr.tools.InterpretedWorker;

import org.eclipse.core.runtime.NullProgressMonitor;

public abstract class ProgramCompiler extends Compiler<CompiledProgram> {
	private final MainCompiler<?> mainCompiler;
	private final List<ProtocolCompiler<?>> protocolCompilers;
	private final List<WorkerCompiler<?>> workerCompilers;

	//
	// CONSTRUCTORS
	//

	public ProgramCompiler(CompilerSettings settings,
			InterpretedProgram program, AutomatonFactory automatonFactory) {

		super(settings);

		if (program == null)
			throw new NullPointerException();
		if (automatonFactory == null)
			throw new NullPointerException();

		this.mainCompiler = newMainCompiler(program.getMain());
		this.protocolCompilers = new ArrayList<>();
		this.workerCompilers = new ArrayList<>();

		for (InterpretedProtocol pr : program.getMain().getProtocols())
			this.protocolCompilers
					.add(newProtocolCompiler(pr, automatonFactory));

		for (InterpretedWorker w : program.getMain().getWorkers())
			this.workerCompilers.add(newWorkerCompiler(w));

	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public CompiledProgram compile() {

		List<CompiledGeneratee> generatees = new ArrayList<>();

		if (!getSettings().ignoreInput()) {

			for (ProtocolCompiler<?> c : protocolCompilers)
				generatees.add(c
						.compile());


			for (WorkerCompiler<?> c : workerCompilers)
				generatees.add(c.compile());

			generatees.add(mainCompiler.compile());
		}

		return newCompiledProgram(getSourceRunTimeDirectoryLocation(),
				generatees);
	}

	//
	// METHODS - PROTECTED
	//

	protected abstract CompiledProgram newCompiledProgram(
			String sourceRunTimeDirectoryLocation,
			List<CompiledGeneratee> generatees);

	protected abstract MainCompiler<?> newMainCompiler(InterpretedMain main);

	protected abstract ProtocolCompiler<?> newProtocolCompiler(
			InterpretedProtocol protocol, AutomatonFactory automatonFactory);

	protected abstract WorkerCompiler<?> newWorkerCompiler(
			InterpretedWorker worker);

	protected abstract String getSourceRunTimeDirectoryLocation();
}