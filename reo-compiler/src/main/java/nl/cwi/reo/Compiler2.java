package nl.cwi.reo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.interpret.Interpreter;
import nl.cwi.reo.interpret.InterpreterPA;
import nl.cwi.reo.interpret.InterpreterPR;
import nl.cwi.reo.interpret.semantics.FlatAssembly;
import nl.cwi.reo.lykos.SimpleLykos;
import nl.cwi.reo.portautomata.PortAutomaton;
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
import nl.cwi.reo.pr.misc.MainArgumentFactory;
import nl.cwi.reo.pr.misc.Member;
import nl.cwi.reo.pr.misc.MemberSignature;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortOrArray;
import nl.cwi.reo.pr.misc.PortSpec;
import nl.cwi.reo.pr.misc.TypedName;
import nl.cwi.reo.pr.misc.TypedName.Type;
import nl.cwi.reo.pr.misc.Member.Composite;
import nl.cwi.reo.pr.misc.Member.Primitive;
import nl.cwi.reo.pr.targ.java.autom.JavaAutomatonFactory;
import nl.cwi.reo.pr.targ.java.autom.JavaPortFactory.JavaPort;
import nl.cwi.reo.prautomata.PRAutomaton;

/**
 * A compiler for the coordination language Reo.
 */
public class Compiler2 {
		
	/**
	 * List of provided Reo source files.
	 */
	@Parameter(description = ".treo files")
	private List<String> files = new ArrayList<String>();
	
	/**
	 * List of parameters for the main component.
	 */
	@Parameter(names = {"-p", "--params"}, variableArity = true, description = "list of parameters to instantiate the main component")
	public List<String> params = new ArrayList<String>();

	/**
	 * List of of directories that contain all necessary Reo components
	 */
    @Parameter(names = {"-cp", "--compath"}, variableArity = true, description = "list of directories that contain all necessary Reo components")
    private List<String> directories = new ArrayList<String>();

	/**
	 * List of of directories that contain all necessary Reo components
	 */
    @Parameter(names = {"-h", "--help"}, description = "shows all available options", help = true)
    private boolean help;

	public static void main(String[] args) {	
		Compiler2 compiler = new Compiler2();
		JCommander jc = new JCommander(compiler, args);
		jc.setProgramName("reoc"); 
		if (compiler.files.size() == 0) {
			jc.usage();
		} else {
	        compiler.run();			
		}
	}
	
	/*

Fifo(A$1;P$11$1)

{}
[A$1]
{in:port=A$1}
{}
Fifo:family
[P$11$1]
{out:port=P$11$1}
nl.cwi.pr.targ.java.autom.JavaPortFactory@433defed
	{1=a$1, 2=x$1}
	nl.cwi.reo.pr.targ.java.JavaNames@52525845
	3
	{x$1=x$1, a$1=a$1}
	

Fifo(A$1;P$11$1)
 
	signature	MemberSignature  (id=64)	
	extralogicals	LinkedHashMap<K,V>  (id=69)	
	inputPorts	ArrayList<E>  (id=74)	
	inputPortsOrArrays	LinkedHashMap<K,V>  (id=75)	
	integers	LinkedHashMap<K,V>  (id=76)	
	name	TypedName  (id=77)	
	outputPorts	ArrayList<E>  (id=79)	
	outputPortsOrArrays	LinkedHashMap<K,V>  (id=80)	
	portFactory	JavaPortFactory  (id=81)	
	string	"FifoK(a$1;x$1)" (id=68)	

	 */
    public void run() {
		directories.add(".");
		String comppath = System.getenv("COMPATH");
		if (comppath != null)
			directories.addAll(Arrays.asList(comppath.split(File.pathSeparator)));

		Interpreter<PRAutomaton> interpreter = new InterpreterPR(directories, params);

		FlatAssembly<PRAutomaton> program = interpreter.interpret(files);
		
		/*
		 * Automaton and Port Factory to build members list :
		 * 
		 */
		

		CompilerSettings settings = new CompilerSettings("ex1.treo",Language.JAVA,false);
		settings.ignoreInput(false);
		settings.ignoreData(false);
		settings.partition(true);
		settings.subtractSyntactically(true);
		settings.commandify(true);
		settings.inferQueues(true);
		settings.put("COUNT_PORTS", false);
		
		/*
		 * Map current interpreted program on Lykos interpreted program
		 */
		AutomatonFactory automatonFactory = null;
		PortFactory portFactory = null;
		MainArgumentFactory mainArgumentFactory = null;
		
		Language targetLanguage = Language.JAVA;
		switch (targetLanguage) {
		case JAVA:
			automatonFactory = new JavaAutomatonFactory();
			portFactory = automatonFactory.getPortFactory();			
			break;

		case C11:

			break;
		}
		List<InterpretedProtocol> interpretedProtocol= new ArrayList<InterpretedProtocol>();
		List<InterpretedWorker> interpretedWorker= new ArrayList<InterpretedWorker>();
		
		
		List<Primitive> listPrimitive = new ArrayList<Primitive>();
		List<Member> listMember = new ArrayList<Member>();
		
		Composite c = new Composite();
		
		for (PRAutomaton X : program) {
			Map<TypedName, Extralogical> extralogicals = new LinkedHashMap<>();
			Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
			Map<TypedName, Integer> integers = new LinkedHashMap<>();
			Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();
			
			PortSpec p = new PortSpec(X.getSource().getName()+"$"+"1");
			JavaPort jp = (JavaPort) portFactory.newOrGet(p);		
			inputPortsOrArrays.put(new TypedName("in",Type.PORT),jp);
			
			p = new PortSpec(X.getDest().getName()+"$"+"1");
			jp = (JavaPort) portFactory.newOrGet(p);		
			outputPortsOrArrays.put(new TypedName("out",Type.PORT),jp);
			
			TypedName typedName = new TypedName(X.getName(),Type.FAMILY);
			
			MemberSignature signature = new MemberSignature(typedName,integers,extralogicals,inputPortsOrArrays,outputPortsOrArrays,portFactory);
			
			Primitive pr = new Primitive("nl.cwi.reo.pr.autom.libr."+X.getName(),"/home/e-spin/workspace/Reo/SimpleLykos/src/main/java");
			pr.setSignature(signature);
			
			c.addChild(pr);
			c.setSignature(signature);
//			System.out.println(X);
		}
		InterpretedProtocol interpretedP = new InterpretedProtocol(c);
		interpretedProtocol.add(interpretedP);
		
		InterpretedMain interpretedMain = new InterpretedMain(interpretedProtocol,interpretedWorker);
		
		Definitions defs = new Definitions();
		List<String> notes = new ArrayList<String>();
		InterpretedProgram interpretedProgram = new InterpretedProgram(settings.getSourceFileLocation(),defs,notes, interpretedMain);
		
		ProgramCompiler	programCompiler = new JavaProgramCompiler(settings,
				interpretedProgram, automatonFactory);
		
		SimpleLykos sL = new SimpleLykos();
		sL.compile("program",listPrimitive,programCompiler);
}
}

