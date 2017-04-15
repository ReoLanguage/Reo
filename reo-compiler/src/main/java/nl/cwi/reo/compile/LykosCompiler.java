package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.lykos.SimpleLykos;
import nl.cwi.reo.lykos.WorkerSignature;
import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.pr.comp.InterpretedMain;
import nl.cwi.reo.pr.comp.InterpretedProgram;
import nl.cwi.reo.pr.comp.InterpretedProtocol;
import nl.cwi.reo.pr.comp.InterpretedWorker;
import nl.cwi.reo.pr.comp.ProgramCompiler;
import nl.cwi.reo.pr.java.comp.JavaProgramCompiler;
import nl.cwi.reo.pr.misc.Definitions;
import nl.cwi.reo.pr.misc.MemberSignature;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortOrArray;
import nl.cwi.reo.pr.misc.PortSpec;
import nl.cwi.reo.pr.misc.ToolError;
import nl.cwi.reo.pr.misc.ToolErrorAccumulator;
import nl.cwi.reo.pr.misc.TypedName;
import nl.cwi.reo.pr.misc.Member.Composite;
import nl.cwi.reo.pr.misc.Member.Primitive;
import nl.cwi.reo.pr.misc.TypedName.Type;
import nl.cwi.reo.pr.misc.Variable;
import nl.cwi.reo.pr.targ.java.autom.JavaAutomatonFactory;
import nl.cwi.reo.pr.targ.java.autom.JavaPortFactory.JavaPort;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;
import nl.cwi.reo.util.Monitor;

public class LykosCompiler {

	private final CompilerSettings settings;
	private AutomatonFactory automatonFactory = null;
	private PortFactory portFactory = null;
	private final ReoConnector<PRAutomaton> program;
	private final Definitions defs = new Definitions();
	private int counterWorker = 0;

	private final String outputpath;
	private final String packagename;
	private final MyToolErrorAccumulator monitor;
	private final String name;

	public LykosCompiler(ReoProgram<PRAutomaton> program, String filename, String outputpath, String packagename, Monitor m,CompilerSettings settings) {

		this.outputpath = outputpath;
		this.packagename = packagename;
		this.monitor = new MyToolErrorAccumulator(filename, m);

		/*
		 * Compiler settings
		 */

		this.settings=settings;
		
		// Define Language for compilation
		Language targetLanguage = Language.JAVA;

		/*
		 * Main program to compile with Lykos
		 */

//		this.program = program.flatten();
		if(program!=null){
			this.program = program.getConnector().flatten().insertNodes(true, true, new PRAutomaton()).integrate();
			this.name=program.getName();
		}
		else
			throw new NullPointerException();
		/*
		 * Creation of different Factories for Lykos compilation
		 */
		switch (targetLanguage) {
		case JAVA:
			this.automatonFactory = new JavaAutomatonFactory();
			this.portFactory = automatonFactory.getPortFactory();
			break;
		case C11:
			break;
		default:
			break;
		}
	}

	/*
	 * Take Reo interpreted program and make it understandable for Lykos
	 */
	public void compile() {

		// Instantiate the protocol.
		Composite c = new Composite();

		// Protocol interface (union of all worker ports)
		Set<Port> P = new HashSet<Port>();

		// Instantiate a list of workers.
		List<InterpretedWorker> workers = new ArrayList<InterpretedWorker>();

		// Add primitives to the protocol or to the list of workers
//		for (ReoConnectorAtom<PRAutomaton> atom : program.insertNodes(true, true, new PRAutomaton()).integrate().getAtoms()) {
		for (ReoConnectorAtom<PRAutomaton> atom : program.getAtoms()) {
			if ((atom.getSourceCode()) == (null) || atom.getSourceCode().getReference() == null) {
				PRAutomaton X = atom.getSemantics();
				Primitive pr = new Primitive("nl.cwi.reo.pr.autom.libr." + X.getName(), "");
				String param = X.getVariable() != null ? X.getVariable().toString() : null;
				pr.setSignature(getMemberSignature(X.getName(), param, X.getInterface()));
				c.addChild(pr);		
			}
			else{
				workers.add(new InterpretedWorker(getWorkerSignature(atom)));
				for(Port p: atom.getInterface()){
					if(p.getType()==PortType.IN)
						P.add(new Port(p.getName(),PortType.OUT,p.getPrioType(),p.getTypeTag(),true));
					else
						P.add(new Port(p.getName(),PortType.IN,p.getPrioType(),p.getTypeTag(),true));
				}
			}
		}
		P.addAll(program.getInterface());

//		for(Map.Entry<Port,Port> p : program.getLinks().entrySet()){
//			if(p.getValue().getType()==p.getKey().getType())
//				P.add(p.getValue());
//		}
		

		c.setSignature(getMainMemberSignature(this.name, null, P));

		List<InterpretedProtocol> protocols = Arrays.asList(new InterpretedProtocol(c));

		InterpretedMain interpretedMain = new InterpretedMain(protocols, workers);

		InterpretedProgram interpretedProgram = new InterpretedProgram(settings.getSourceFileLocation(), defs,
				new ArrayList<String>(), interpretedMain);

		ProgramCompiler programCompiler = new JavaProgramCompiler(settings, interpretedProgram, automatonFactory);

		/*
		 * Start compiling on simple Lykos project
		 */

		SimpleLykos sL = new SimpleLykos(outputpath, packagename);
		sL.compile(programCompiler, automatonFactory);
	}

	/**
	 * Constructs a new member signature.
	 * 
	 * @param name
	 *            name of the component
	 * @param variable
	 *            optional parameter
	 * @param ports
	 *            ports in the interface
	 * @return signature of a member.
	 */
	public MemberSignature getMemberSignature(String name, String variable, Set<Port> ports) {

		TypedName typedName = new TypedName(name, Type.FAMILY);
		Map<TypedName, Extralogical> extralogicals = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();

		if (variable != null)
			extralogicals.put(new TypedName("d", Type.EXTRALOGICAL), new Extralogical(variable));

		int numberInPort = 1;
		int numberOutPort = 1;
		int numberOPort = 0;
		int numberIPort = 0;
		for (Port p : ports) {
			if(p.getType()==PortType.IN)
				numberIPort++;
			if(p.getType()==PortType.OUT)
				numberOPort++;
		}
		for (Port p : ports) {
			PortSpec pSpec = new PortSpec("$" + p.getName());
			JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);
			switch (p.getType()) {
			case IN:
				inputPortsOrArrays.put(new TypedName("in" + ((numberIPort!=1)?(numberInPort++):("")) , Type.PORT), jp);
				break;
			case OUT:
				outputPortsOrArrays.put(new TypedName("out" + ((numberOPort!=1)?(numberOutPort++):("")), Type.PORT), jp);
				break;
			default:
				throw new RuntimeException("Port of atomic component should be in or out ports");
			}
		}

		return new MemberSignature(typedName, new LinkedHashMap<>(), extralogicals, inputPortsOrArrays,
				outputPortsOrArrays, portFactory);
	}

	public MemberSignature getMainMemberSignature(String name, String variable, Set<Port> ports) {

		TypedName typedName = new TypedName(name, Type.FAMILY);
		Map<TypedName, Extralogical> extralogicals = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();

		if (variable != null)
			extralogicals.put(new TypedName("d", Type.EXTRALOGICAL), new Extralogical(variable));

//		int numberInPort = 1;
//		int numberOutPort = 1;
		for (Port p : ports) {
			PortSpec pSpec = new PortSpec(p.getName());
			JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);
			switch (p.getType()) {
			case IN:
				inputPortsOrArrays.put(new TypedName(p.getName(), Type.PORT), jp);
				break;
			case OUT:
				outputPortsOrArrays.put(new TypedName(p.getName(), Type.PORT), jp);
				break;
			default:
				throw new RuntimeException("Port of atomic component should be in or out ports");
			}
		}

		return new MemberSignature(typedName, new LinkedHashMap<>(), extralogicals, inputPortsOrArrays,
				outputPortsOrArrays, portFactory);
	}
	
	/**
	 * Constructs a new worker signature
	 * 
	 * @param atom
	 *            atomic Reo connector.
	 * @return signature of a worker.
	 */
	public WorkerSignature getWorkerSignature(ReoConnectorAtom<PRAutomaton> atom) {

		List<Variable> list = new ArrayList<Variable>();

		String name = "";
		for (Port p : atom.getSemantics().getInterface()) {
			PortSpec pSpec = new PortSpec(p.getName());
			JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);
			
			switch (p.getType()) {
			case IN:
				jp.addAnnotation("portType", nl.cwi.reo.pr.misc.PortFactory.PortType.OUTPUT);
				break;
			case OUT:
				jp.addAnnotation("portType", nl.cwi.reo.pr.misc.PortFactory.PortType.INPUT);
				break;
			default:
				throw new RuntimeException("Port of atomic component should be in or out ports");
			}

			list.add(jp);
			defs.addPort(jp);
			name = "" + counterWorker++;
		}

		String nameWorker = atom.getSourceCode().getReference().toString().substring(1,
				atom.getSourceCode().getReference().toString().length() - 1);
		

		defs.putWorkerName(new TypedName(name, Type.WORKER_NAME), nameWorker, monitor);

		return new WorkerSignature(nameWorker, list);
	}

	private final class MyToolErrorAccumulator extends ToolErrorAccumulator {

		private final Monitor m;

		public MyToolErrorAccumulator(String sourceFileLocation, Monitor m) {
			super(sourceFileLocation);
			this.m = m;
		}

		@Override
		protected ToolError newError(String message) {
			m.add(message);
			return null;
		}

		@Override
		protected ToolError newError(String message, Throwable cause) {
			m.add(message);
			return null;
		}

	}
}
