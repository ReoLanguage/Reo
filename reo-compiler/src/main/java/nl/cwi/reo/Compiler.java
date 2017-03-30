package nl.cwi.reo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.compile.GraphCompiler;
import nl.cwi.reo.compile.JavaCompiler;
import nl.cwi.reo.compile.LykosCompiler;
import nl.cwi.reo.compile.PRCompiler;
import nl.cwi.reo.compile.components.ReoTemplate;
import nl.cwi.reo.compile.components.TransitionRule;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.interpret.interpreters.InterpreterCAM;
import nl.cwi.reo.interpret.interpreters.InterpreterPR;
import nl.cwi.reo.interpret.interpreters.InterpreterSBA;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.pr.comp.CompilerSettings;

import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.constraintautomata.ConstraintAutomaton;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;
import nl.cwi.reo.semantics.symbolicautomata.Disjunction;
import nl.cwi.reo.semantics.symbolicautomata.Formula;
import nl.cwi.reo.semantics.symbolicautomata.MemoryCell;
import nl.cwi.reo.semantics.symbolicautomata.SymbolicAutomaton;
import nl.cwi.reo.util.Monitor;

/**
 * A compiler for the coordination language Reo.
 */
public class Compiler {
		
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
	 * List of provided Reo source files.
	 */
	@Parameter(names = {"-o", "--output-dir"}, description = "output directory")
	private String outdir = ".";

	/**
	 * Package
	 */
	@Parameter(names = {"-pkg", "--package"}, description = "target code package")
	private String packagename = "";
	
	/**
	 * Partitioning
	 */
	@Parameter(names = {"-pt", "--partitioning"}, description = "partition regarding synchronous and asynchronous sections")
	private boolean partitioning = false ;
	
	
	/**
	 * List of available options.
	 */
    @Parameter(names = {"-h", "--help"}, description = "show all available options", help = true)
    private boolean help;
   
    /**
     * Semantics type of Reo connectors.
     */
    @Parameter(names = {"-s", "--semantics"}, description = "used type of Reo semantics") 
    public SemanticsType semantics = SemanticsType.PR; 
    
    /**
     * Semantics type of Reo connectors.
     */
    @Parameter(names = {"-v", "--verbose"}, description = "show verbose output") 
    public boolean verbose = false; 
    
    /**
     * Message container.
     */
    private final Monitor monitor = new Monitor();

	public static void main(String[] args) {	
		Compiler compiler = new Compiler();
		JCommander jc = new JCommander(compiler, args);
		jc.setProgramName("reo"); 
		if (compiler.files.size() == 0) {
			jc.usage();
		} else {
	        compiler.run();			
		}
	}

    public void run() {
    	
    	// Get the root locations of Reo source files and libraries.
		directories.add(".");
		String comppath = System.getenv("COMPATH");
		if (comppath != null)
			directories.addAll(Arrays.asList(comppath.split(File.pathSeparator)));

		// Select the correct compiler.
		switch (semantics) {
		case CAM:
			compileCAM();
			break;
		case PA:
			compilePA();
			break;
		case PR:
			compilePR();
			break;
		case SA:
			compileSA();
			break;
		case WA:
			compileWA();
			break;
		case SBA:
			compileSBA();
			break;
		case PLAIN:
			compilePLAIN();
			break;
		default:
			monitor.add("Please specify the used semantics.");
			break;
		}

		// Print all messages.
		monitor.print();
	}

	/**
	 * Writes code to a file in the specified output directory.
	 * 
	 * @param name
	 *            file name with extension
	 * @param code
	 *            content of the file
	 * @return <code>true</code> if the files are successfully written.
	 */
	public boolean write(String name, String code) {
		try {
			FileWriter out = new FileWriter(outdir + File.separator + name);
			out.write(code);
			out.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
    
    private void compileCAM() {
    	
		Interpreter<ConstraintAutomaton> interpreter = new InterpreterCAM(directories, params, monitor);
		ReoProgram<ConstraintAutomaton> program = interpreter.interpret(files.get(0));
		
		ReoTemplate template = JavaCompiler.compile(program, packagename, new ConstraintAutomaton());
		
		if (template != null)
			System.out.println(template.getCode(Language.JAVA));
    }

    private void compilePA() {
		
//		STGroup group = new STGroupFile("Java.stg");
//		ST temp = group.getInstanceOf("component");
//
//		Port a = new Port("a", PortType.IN, PrioType.NONE, new TypeTag("Integer"), true);
//		Port b = new Port("b", PortType.OUT, PrioType.NONE, new TypeTag("Boolean"), true);
//		Port c = new Port("c", PortType.IN, PrioType.NONE, new TypeTag("Double"), true);
//
//		SortedSet<Port> N = new TreeSet<Port>();
//
//		N.add(a);
//		N.add(b);
//		N.add(c);
//
//		Map<String, String> ac = new HashMap<String, String>();
//		ac.put("b", "d_a");
//		ac.put("d", "m");
//
//		Transition t = new Transition("q0", "q1", N, ac);
//
//		List<Port> P = new ArrayList<Port>();
//		P.add(a);
//		P.add(b);
//		P.add(c);
//
//		Map<String, Set<Transition>> out = new HashMap<String, Set<Transition>>();
//		out.put("q0", new HashSet<Transition>(Arrays.asList(t)));
//
//		Automaton A = new Automaton("MyAutomaton", P, Behavior.PROACTIVE, "main.treo", out, "q0",
//				"nl.cwi.reo.runtime.java");
//
//		temp.add("A", A);
//
//		System.out.println(temp.render());
		// outputClass(name, st.render());
    }
    
    private void compileSBA() {    	
    	
    	Interpreter<SymbolicAutomaton> interpreterRba = new InterpreterSBA(directories, params, monitor);
		
		ReoProgram<SymbolicAutomaton> programRba = interpreterRba.interpret(files.get(0));	
		
		List<Formula> components = new ArrayList<Formula>();
		
		for(ReoConnectorAtom<SymbolicAutomaton> sba : programRba.getConnector().flatten().getAtoms()){
			components.add(sba.getSemantics().getFormula().rename(sba.getLinks()).NNF(false));
			
			System.out.println(sba.getSemantics().getFormula());
			System.out.println(sba.getSemantics().getInterface());
		};
		Formula automaton;
		automaton = JavaCompiler.compose(components);
		automaton = automaton.QE();
		JavaCompiler.generateCode(automaton);

    }

    private void compilePR() {    	
    	
		Interpreter<PRAutomaton> interpreter = new InterpreterPR(directories, params, monitor);
		
		ReoProgram<PRAutomaton> program = interpreter.interpret(files.get(0));	
		
//		ReoTemplate template = JavaCompiler.compile(program, packagename, new PRAutomaton());
		
		
		if (program == null) return;	
		
		if (verbose) {
			System.out.println(program.getConnector().flatten().insertNodes(true, true, new PRAutomaton()).integrate());
			System.out.println(PRCompiler.toPR(program));
			monitor.print();
//	    	GraphCompiler.visualize(program);

		}
		/*
		 * Compiler Settings
		 */
		
		CompilerSettings settings = new CompilerSettings(files.get(0), Language.JAVA, false);
		settings.ignoreInput(false);
		settings.ignoreData(false);
		settings.partition(!partitioning);
		settings.subtractSyntactically(true);
		settings.commandify(true);
		settings.inferQueues(true);
		settings.put("COUNT_PORTS", false);
		
		
		LykosCompiler c = new LykosCompiler(program, files.get(0), outdir, packagename, monitor,settings);

		  
    	
		c.compile();
    }

    private void compileSA() {
    	// TODO    	
    }

    private void compileWA() {
    	// TODO   	
    }

    private void compilePLAIN() {
    	// TODO   	
    }
}

