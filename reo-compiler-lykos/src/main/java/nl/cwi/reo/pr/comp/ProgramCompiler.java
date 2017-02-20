package nl.cwi.reo.pr.comp;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.comp.CompiledGeneratee;
import nl.cwi.reo.pr.comp.CompiledProgram;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.pr.comp.InterpretedMain;
import nl.cwi.reo.pr.comp.InterpretedProgram;
import nl.cwi.reo.pr.comp.InterpretedProtocol;
import nl.cwi.reo.pr.comp.InterpretedWorker;

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

	public MainCompiler<?> getMainCompiler(){
		return mainCompiler;
	}
	public List<ProtocolCompiler<?>> getProtocolCompiler(){
		return protocolCompilers;
	}
	public List<WorkerCompiler<?>> getWorkerCompiler(){
		return workerCompilers;
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