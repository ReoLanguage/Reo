package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
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

public class LykosCompiler extends ToolErrorAccumulator {
	
	CompilerSettings settings;
	PortFactory portFactory = null;
	AutomatonFactory automatonFactory = null;
	ReoConnector<PRAutomaton> program;
	Definitions defs = new Definitions();
	int counterWorker = 0;
	String outputpath;
	
	public LykosCompiler(ReoConnector<PRAutomaton> program, String outputpath) {
		super("test.treo");
		
		this.outputpath = outputpath;
		
		/*
		 * Compiler settings
		 */
			
		settings = new CompilerSettings("ex1.treo",Language.JAVA,false);
		settings.ignoreInput(false);
		settings.ignoreData(false);
		settings.partition(true);
		settings.subtractSyntactically(true);
		settings.commandify(true);
		settings.inferQueues(true);
		settings.put("COUNT_PORTS", false);
		
		// Define Language for compilation
		Language targetLanguage = Language.JAVA;
		
		/*
		 * Main program to compile with Lykos
		 */
		
		this.program = program;
		
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
	public void compile(){
		
		Composite c = setComposite();
		

		/*
		 * Set primitives and add them to the main composite
		 */
		
		List<InterpretedWorker> interpretedWorker = new ArrayList<InterpretedWorker>();
		
		System.out.println(program.flatten());
		
		System.out.println(program.flatten().insertNodes(true, true, new PRAutomaton()).integrate().getAtoms());
		List<ReoConnector<PRAutomaton>> protocol = new ArrayList<ReoConnector<PRAutomaton>>();
		List<ReoConnector<PRAutomaton>> worker = new ArrayList<ReoConnector<PRAutomaton>>();
		
		
		for (ReoConnectorAtom<PRAutomaton> X : program.flatten().getAtoms()) {
			if((X.getSourceCode())==(null) || X.getSourceCode().getFile()==null) {
				protocol.add(X);
			}
			else
				worker.add(X);
		}
			
		ReoConnectorComposite<PRAutomaton> progProtocol = new ReoConnectorComposite<PRAutomaton>(" ",protocol);
		ReoConnectorComposite<PRAutomaton> progWorker = new ReoConnectorComposite<PRAutomaton>(" ",worker);
				
				
		for (ReoConnectorAtom<PRAutomaton> proto : progProtocol.flatten().insertNodes(true, true, new PRAutomaton()).integrate().getAtoms()) {
			c.addChild(setPrimitive(proto.getSemantics()));
		}
			
		for (ReoConnectorAtom<PRAutomaton> work : progWorker.flatten().integrate().getAtoms()) {
			interpretedWorker.add(new InterpretedWorker(setWorker(work)));
		}
			
		List<InterpretedProtocol> interpretedProtocol= new ArrayList<InterpretedProtocol>();
		interpretedProtocol.add(new InterpretedProtocol(c));
			
		InterpretedMain interpretedMain = new InterpretedMain(interpretedProtocol,interpretedWorker);
		
	
		List<String> notes = new ArrayList<String>();
		InterpretedProgram interpretedProgram = new InterpretedProgram(settings.getSourceFileLocation(),defs,notes, interpretedMain);
		
		ProgramCompiler	programCompiler = new JavaProgramCompiler(settings,interpretedProgram, automatonFactory);
		
		/*
		 * Start compiling on simple Lykos project 
		 */
		
		SimpleLykos sL = new SimpleLykos(outputpath);
		sL.compile(programCompiler,automatonFactory);
	}
	
	/*
	 * Set the main composite of the treo program :
	 */
	public Composite setComposite(){
		Composite c = new Composite();
		Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();
		
		Set<Port> P = program.flatten().getInterface(); P.clear();
	//	Set links = new HashMap<Port,Port>();
//		List<Port> listPort = new ArrayList<Port>();
		for(ReoConnectorAtom<PRAutomaton> X : program.getAtoms())
			if(X.getSourceCode()!=null && X.getSourceCode().getFile()!=null)
				for(Port p : X.getLinks().keySet())
					P.add(X.getLinks().get(p));
		//= program.flatten().getLinks();
//		Set<Port> P = P1;
//		for(Port p : listPort){
//			if(p.isHidden())
//				P.add(p);
//		}
		int numberInPort=1;
		int numberOutPort=1;
		for(Port p: P){
			PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
			JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);
			if (p.getType()==PortType.IN){
				inputPortsOrArrays.put(new TypedName("in"+(numberInPort),Type.PORT),jp);
				numberInPort++;
			}
			else if (p.getType()==PortType.OUT){		
				outputPortsOrArrays.put(new TypedName("out"+(numberOutPort),Type.PORT),jp);
				numberOutPort++;
			} else {
				throw new RuntimeException("Port of a composite should be in or out ports");
			}
		}
		
		MemberSignature compositeSignature = new MemberSignature(new TypedName("main",Type.FAMILY),new LinkedHashMap<>(),
				new LinkedHashMap<>(),inputPortsOrArrays,outputPortsOrArrays,portFactory);

		c.setSignature(compositeSignature);
		
		return c;
		
	}
	
	public Primitive setPrimitive(PRAutomaton X) {
		Map<TypedName, Extralogical> extralogicals = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();
		inputPortsOrArrays = new LinkedHashMap<>();
		Map<TypedName, Integer> integers = new LinkedHashMap<>();
		outputPortsOrArrays = new LinkedHashMap<>();
//		integers.put(new TypedName("k",TypedName.Type.INTEGER), new Integer(Integer.parseInt(X.getVariable().toString())));
		
		if(X.getVariable()!=null)
			extralogicals.put(new TypedName("d",TypedName.Type.EXTRALOGICAL), new Extralogical(X.getVariable().toString()));
		SortedSet<Port> P = X.getInterface();
		int numberInPort = 1;
		int numberOutPort = 1;
		for (Port p : P) {
			if (p.getType() == PortType.IN) {
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);		
				inputPortsOrArrays.put(new TypedName("in"+(numberInPort),Type.PORT),jp);
				numberInPort++;
			} else if (p.getType() == PortType.OUT) {
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);		
				outputPortsOrArrays.put(new TypedName("out"+(numberOutPort),Type.PORT),jp);
				numberOutPort++;
			} else {
				throw new RuntimeException("Port of atomic component should be in or out ports");
			}
		}

		
		TypedName typedName = new TypedName(X.getName(),Type.FAMILY);
		
		MemberSignature signature = new MemberSignature(typedName,integers,extralogicals,inputPortsOrArrays,outputPortsOrArrays,portFactory);
		
		Primitive pr = new Primitive("nl.cwi.reo.pr.autom.libr."+X.getName(),"../SimpleLykos/src/main/java");
		pr.setSignature(signature);
		
		return pr;
			
	}
	
	public WorkerSignature setWorker(ReoConnectorAtom<PRAutomaton> X){
		List<Variable> l = new ArrayList<Variable>();
		PRAutomaton Y = X.getSemantics();
		String name = "";
		for (Port p : Y.getInterface()) {
			if (p.getType() == PortType.IN) {
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);	
				jp.addAnnotation("portType", nl.cwi.reo.pr.misc.PortFactory.PortType.INPUT);
				l.add(jp);
				defs.addPort(jp);
				name=""+counterWorker;
				counterWorker++;
			} else if (p.getType() == PortType.OUT) {
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);		
				jp.addAnnotation("portType", nl.cwi.reo.pr.misc.PortFactory.PortType.OUTPUT);
				l.add(jp);
				defs.addPort(jp);
				name=""+counterWorker;
				counterWorker++;
			} else {
				throw new RuntimeException("Port of atomic component should be in or out ports");
			}
		}
		String nameWorker = X.getSourceCode().getFile().toString().substring(1, X.getSourceCode().getFile().toString().length()-1);
		WorkerSignature ws = new WorkerSignature(nameWorker,l);
		defs.putWorkerName(new TypedName(name,Type.WORKER_NAME), nameWorker, this);
		
		return ws;
		
	}
	


	@Override
	protected ToolError newError(String message) {
		return null;
	}

	@Override
	protected ToolError newError(String message, Throwable cause) {
		return null;
	}
}
