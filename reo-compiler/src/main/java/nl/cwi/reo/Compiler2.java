package nl.cwi.reo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.interpret.Interpreter;
import nl.cwi.reo.interpret.InterpreterPA;
import nl.cwi.reo.interpret.semantics.FlatAssembly;
import nl.cwi.reo.portautomata.PortAutomaton;

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
    public void run() {
		directories.add(".");
		String comppath = System.getenv("COMPATH");
		if (comppath != null)
			directories.addAll(Arrays.asList(comppath.split(File.pathSeparator)));

		Interpreter<PortAutomaton> interpreter = new InterpreterPA(directories, params);

		FlatAssembly<PortAutomaton> program = interpreter.interpret(files);
		
		for (PortAutomaton X : program) System.out.println(X);
		
		if (!program.isEmpty()) {
			PortAutomaton product = program.get(0).compose(program.subList(1, program.size()));
			PortAutomaton hide = product.restrict(program.getInterface());
			
			System.out.println("Product automaton : \n");
			System.out.println(hide);
//			System.out.println("Product automaton : \n\n" + hide);
		}
		System.out.println("------");
//		// Generate the classes.
//		JavaCompiler JC = new JavaCompiler(name, "");
//		JC.compile(program);
}
}

