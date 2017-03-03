package nl.cwi.reo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.compile.LykosCompiler;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.interpret.interpreters.InterpreterPR;
import nl.cwi.reo.interpret.listeners.Listener;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.portautomata.PortAutomaton;
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
    	// TODO
    }

    private void compilePA() {
		// Interpret the program
		Interpreter<PortAutomaton> interpreter = new Interpreter<PortAutomaton>(SemanticsType.PA, new Listener<PortAutomaton>(monitor), directories, params, monitor);
		interpreter.interpret(files.get(0));
		
//		if (program != null) {
//			for (Component<PortAutomaton> X : program.getComponents()) System.out.println(X);
//
//			
//			if (!program.isEmpty()) {
//				PortAutomaton product = program.getComponents().get(0).compose(program.getComponents().subList(1, program.getComponents().size()));
//				PortAutomaton hide = product.restrict(program.getInterface());
				
//				System.out.println("Product automaton : \n");
//				System.out.println(hide);
//			}
//		}
//		// Generate the classes.
//		JavaCompiler JC = new JavaCompiler(name, "");
//		JC.compile(program);
    }
    

    private void compilePR() {    	
		Interpreter<PRAutomaton> interpreter = new InterpreterPR(directories, params, monitor);
		ReoConnector<PRAutomaton> program = interpreter.interpret(files.get(0));
		monitor.print();
		if (program == null) return;	
		LykosCompiler c = new LykosCompiler(program, outdir, verbose);
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

