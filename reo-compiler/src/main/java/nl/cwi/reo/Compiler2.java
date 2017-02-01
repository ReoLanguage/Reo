package nl.cwi.reo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.interpret.Interpreter;
import nl.cwi.reo.interpret.InterpreterPA;
import nl.cwi.reo.interpret.InterpreterPR;
import nl.cwi.reo.interpret.semantics.FlatAssembly;
import nl.cwi.reo.lykos.SimpleLykos;
import nl.cwi.reo.portautomata.PortAutomaton;
import nl.cwi.reo.pr.targ.java.autom.Member.Primitive;
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
		
		List<Primitive> listPrimitive = new ArrayList<Primitive>();
		
		for (PRAutomaton X : program) {
			listPrimitive.add(X.getPrimitive());
			
			System.out.println(X);
		}
		
		SimpleLykos sL = new SimpleLykos();
		
		sL.compile("program",listPrimitive);
		
		
		if (!program.isEmpty()) {
			PRAutomaton product = program.get(0).compose(program.subList(1, program.size()));
			PRAutomaton hide = product.restrict(program.getInterface());
			
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

