package nl.cwi.reo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.interpret.semantics.FlatConnector;
import nl.cwi.reo.lykos.SimpleLykos;
import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.pr.comp.InterpretedMain;
import nl.cwi.reo.pr.comp.InterpretedProgram;
import nl.cwi.reo.pr.comp.InterpretedProtocol;
import nl.cwi.reo.pr.comp.InterpretedWorker;
import nl.cwi.reo.pr.comp.Language;
import nl.cwi.reo.pr.comp.ProgramCompiler;
import nl.cwi.reo.pr.java.comp.JavaProgramCompiler;
import nl.cwi.reo.pr.misc.Definitions;
import nl.cwi.reo.pr.misc.MemberSignature;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortOrArray;
import nl.cwi.reo.pr.misc.PortSpec;
import nl.cwi.reo.pr.misc.TypedName;
import nl.cwi.reo.pr.misc.Member.Composite;
import nl.cwi.reo.pr.misc.Member.Primitive;
import nl.cwi.reo.pr.misc.TypedName.Type;
import nl.cwi.reo.pr.targ.java.autom.JavaAutomatonFactory;
import nl.cwi.reo.pr.targ.java.autom.JavaPortFactory.JavaPort;
import nl.cwi.reo.prautomata.PRAutomaton;
import nl.cwi.reo.semantics.api.Port;
import nl.cwi.reo.semantics.api.PortType;

public class LykosCompiler {
	
	CompilerSettings settings;
	PortFactory portFactory = null;
	AutomatonFactory automatonFactory = null;
	FlatConnector<PRAutomaton> program;
	
	
	public LykosCompiler(FlatConnector<PRAutomaton> program){
			
		/*
		 * Compiler settings :
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
		
		this.program=program;
		
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
	 * Set the main composite of the treo program :
	 */
	public Composite setComposite(){
		Composite c = new Composite();
		Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();
		
		
		Set<Port> P = program.getInterface();
		int numberInPort=1;
		int numberOutPort=1;
		for(Port p: P){
			if(p.getType()==PortType.IN){
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);
				inputPortsOrArrays.put(new TypedName("in"+(numberInPort),Type.PORT),jp);
				numberInPort++;
			}
			else if(p.getType()==PortType.OUT){
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);		
				outputPortsOrArrays.put(new TypedName("out"+(numberOutPort),Type.PORT),jp);
				numberOutPort++;
			}
			else{
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
		
		SortedSet<Port> P = X.getInterface();
		int numberInPort=1;
		int numberOutPort=1;
		for(Port p: P){
			if(p.getType()==PortType.IN){
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);		
				inputPortsOrArrays.put(new TypedName("in"+(numberInPort),Type.PORT),jp);
				numberInPort++;
				
			}
			else if(p.getType()==PortType.OUT){
				PortSpec pSpec = new PortSpec(p.getName()+"$"+"1");
				JavaPort jp = (JavaPort) portFactory.newOrGet(pSpec);		
				outputPortsOrArrays.put(new TypedName("out"+(numberOutPort),Type.PORT),jp);
				numberOutPort++;
			}
			else{
				throw new RuntimeException("Port of atomic component should be in or out ports");
			}
		}

		
		TypedName typedName = new TypedName(X.getName(),Type.FAMILY);
		
		MemberSignature signature = new MemberSignature(typedName,integers,extralogicals,inputPortsOrArrays,outputPortsOrArrays,portFactory);
		
		Primitive pr = new Primitive("nl.cwi.reo.pr.autom.libr."+X.getName(),"../SimpleLykos/src/main/java");
		pr.setSignature(signature);
		
		return pr;
				
	}

	
	
	public void compile(){
		
	Composite c = setComposite();
	
	/*
	 * Set primitives and add them to the main composite
	 */
	for (PRAutomaton X : program) {
		
		c.addChild(setPrimitive(X));

	}
	
	List<InterpretedProtocol> interpretedProtocol= new ArrayList<InterpretedProtocol>();
	interpretedProtocol.add(new InterpretedProtocol(c));
	
	List<InterpretedWorker> interpretedWorker= new ArrayList<InterpretedWorker>();
	// interpretedWorker.add(new ...)
	
	InterpretedMain interpretedMain = new InterpretedMain(interpretedProtocol,interpretedWorker);
	
	Definitions defs = new Definitions();
	List<String> notes = new ArrayList<String>();
	InterpretedProgram interpretedProgram = new InterpretedProgram(settings.getSourceFileLocation(),defs,notes, interpretedMain);
	
	ProgramCompiler	programCompiler = new JavaProgramCompiler(settings,interpretedProgram, automatonFactory);
	/*
	 * Start compiling on simple Lykos project 
	 */
	
	SimpleLykos sL = new SimpleLykos();
	sL.compile("program", programCompiler);
	}
}
