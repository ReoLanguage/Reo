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
import nl.cwi.reo.interpret.InterpreterPR;
import nl.cwi.reo.interpret.semantics.Component;
import nl.cwi.reo.interpret.semantics.FlatConnector;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;

/**
 * A compiler for the coordination language Reo.
 */
public class CompilerL {
		
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
		CompilerL compiler = new CompilerL();
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

		Interpreter<PRAutomaton> interpreter = new InterpreterPR(directories, params);
		
		FlatConnector<PRAutomaton> program = interpreter.interpret(files);
		
		
		for(Component<PRAutomaton> c: program){
			if(c.getSourceCode().getFile()!=null)
				System.out.println(c.getSourceCode().getFile().toString());
			else
				System.out.println(c.getAtom());
		}
		String pathLocation="../reo-runtime-java-v1/src/main/java";
		new LykosCompiler(program).compile(pathLocation);
		
		}
}

