package nl.cwi.reo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.compile.JavaCompiler;
import nl.cwi.reo.compile.LykosCompiler;
import nl.cwi.reo.compile.components.ReoTemplate;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.interpret.interpreters.InterpreterCAM;
import nl.cwi.reo.interpret.interpreters.InterpreterPR;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.constraintautomata.ConstraintAutomaton;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;
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
	private boolean partitioning = true ;
	
	
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
    

    private void compilePR() {    	
    	
		Interpreter<PRAutomaton> interpreter = new InterpreterPR(directories, params, monitor);
		
		ReoProgram<PRAutomaton> program = interpreter.interpret(files.get(0));	
		
		if (program == null) return;	
		
		if (verbose) {
//			System.out.println(program.getConnector());
//			System.out.println(program.getConnector().flatten());
//			System.out.println(program.getConnector().flatten().insertNodes(true, true, new PRAutomaton()));
			
			ReoConnector<PRAutomaton> rc = program.getConnector().flatten().insertNodes(true, true, new PRAutomaton()).integrate();
			
			System.out.println(rc);
			
			// Build a connector
			Map<String, Object> c = new HashMap<String, Object>();
			c.put("name", program.getName());
			
			// Build a list of instances
			int i = 0;
			List<String> defs = new ArrayList<String>();
			List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> workers = new ArrayList<Map<String, Object>>();

			for (ReoConnectorAtom<PRAutomaton> atom : rc.getAtoms()) {

				List<String> ports = new ArrayList<String>();
				for (Port p : atom.getSemantics().getInterface())
					ports.add(p.getName());
				
				if (atom.getSemantics().getName().equals("identity")) {
					Map<String, Object> worker = new HashMap<String, Object>();
					defs.add("WORKER" + ++i + " = " + atom.getSourceCode().getFile().toString().replace("\"", ""));
					worker.put("ref", "WORKER" + i);
					worker.put("ports", ports);
					workers.add(worker);
					continue;
				}
				
				// Build an instance
				Map<String, Object> I = new HashMap<String, Object>();
				I.put("name", atom.getSemantics().getName());
				I.put("ports", ports);
				
				Value pv = atom.getSemantics().getValue();
				String param = null;
				if (pv != null)
					param = pv instanceof StringValue ? "\"" + pv.toString() + "\"" : pv.toString();
				if (param != null)
					I.put("params", Arrays.asList(param));
				
				instances.add(I);
			}

			c.put("defs", defs);
			c.put("instances", instances);
			c.put("workers", workers);

			
			STGroup group = new STGroupFile("PR.stg");
			ST temp = group.getInstanceOf("main");
			temp.add("c", c);
			System.out.println(temp.render());
		}
		/*
		 * Compiler Settings
		 */
		
		CompilerSettings settings = new CompilerSettings(files.get(0), Language.JAVA, false);
		settings.ignoreInput(false);
		settings.ignoreData(false);
		settings.partition(partitioning);
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

