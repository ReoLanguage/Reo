package nl.cwi.reo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.compile.LykosCompiler;
import nl.cwi.reo.compile.PRCompiler;
import nl.cwi.reo.compile.RBACompiler;
import nl.cwi.reo.compile.components.Atomic;
import nl.cwi.reo.compile.components.Component;
import nl.cwi.reo.compile.components.Protocol;
import nl.cwi.reo.compile.components.ReoTemplate;
import nl.cwi.reo.compile.components.Transition;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.Reference;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.interpret.interpreters.InterpreterPR;
import nl.cwi.reo.interpret.interpreters.InterpreterRBA;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.pr.comp.CompilerSettings;

import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemCell;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.semantics.rbautomaton.Rule;
import nl.cwi.reo.semantics.rbautomaton.ConstraintHypergraph;
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.MessageType;
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
	@Parameter(names = { "-p",
			"--params" }, variableArity = true, description = "list of parameters to instantiate the main component")
	public List<String> params = new ArrayList<String>();

	/**
	 * List of of directories that contain all necessary Reo components
	 */
	@Parameter(names = { "-cp",
			"--compath" }, variableArity = true, description = "list of directories that contain all necessary Reo components")
	private List<String> directories = new ArrayList<String>();

	/**
	 * List of provided Reo source files.
	 */
	@Parameter(names = { "-o", "--output-dir" }, description = "output directory")
	private String outdir = ".";

	/**
	 * Package
	 */
	@Parameter(names = { "-pkg", "--package" }, description = "target code package")
	private String packagename = "";

	/**
	 * Partitioning
	 */
	@Parameter(names = { "-pt", "--partitioning" }, description = "synchronous region decomposition")
	private boolean partitioning = false;

	/**
	 * List of available options.
	 */
	@Parameter(names = { "-h", "--help" }, description = "show all available options", help = true)
	private boolean help;

	/**
	 * Semantics type of Reo connectors.
	 */
	@Parameter(names = { "-s", "--semantics" }, description = "used type of Reo semantics")
	public SemanticsType semantics = SemanticsType.PR;

	/**
	 * Semantics type of Reo connectors.
	 */
	@Parameter(names = { "-v", "--verbose" }, description = "show verbose output")
	public boolean verbose = false;

	/**
	 * Message container.
	 */
	private final Monitor monitor = new Monitor();

	public static void main(String[] args) {
		Compiler compiler = new Compiler();
		try {
			JCommander jc = new JCommander(compiler, args);
			jc.setProgramName("reo");
			if (compiler.files.size() == 0) {
				jc.usage();
			} else {
				compiler.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(new Message(MessageType.ERROR, e.getMessage()));
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
			break;
		case P:
			compileP();
			break;
		case PA:
			break;
		case PLAIN:
			break;
		case PR:
			compilePR();
			break;
		case RBA:
			compileRBA();
			break;
		case SA:
			break;
		case WA:
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
			File file = new File(outdir + File.separator + name);
			file.getParentFile().mkdirs();
			FileWriter out = new FileWriter(file);
			out.write(code);
			out.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private void compileRBA() {
		Long t1 = System.nanoTime();

		// Interpret the Reo program
		Interpreter<ConstraintHypergraph> interpreter = new InterpreterRBA(directories, params, monitor);
		ReoProgram<ConstraintHypergraph> program = interpreter.interpret(files.get(0));

		if (program == null)
			return;

		// If necessary, add port windows
		List<ReoConnector<ConstraintHypergraph>> list = new ArrayList<>();
		list.add(program.getConnector());
		for (Port p : program.getConnector().getInterface()) {
			if (!p.isHidden()) {
				String name = "PortWindow";
				Value v = new StringValue(p.getName().toString());
				List<Value> values = Arrays.asList(v);

				Reference src = new Reference(p.isInput() ? "Windows.producer" : "Windows.consumer", Language.JAVA,
						null, values);
				Port q = null;
				if (p.isInput())
					q = new Port(p.getName(), PortType.OUT, p.getPrioType(), new TypeTag("String"), true);
				else
					q = new Port(p.getName(), PortType.IN, p.getPrioType(), new TypeTag("String"), true);
				Set<Port> iface = new HashSet<Port>(Arrays.asList(q));
				ConstraintHypergraph atom = new ConstraintHypergraph().getDefault(iface);
				ReoConnectorAtom<ConstraintHypergraph> window = new ReoConnectorAtom<>(name, atom, src);
				list.add(window);
			}
		}

		// Hide all ports
		int i = 1;
		Map<Port, Port> r = new HashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : program.getConnector().getLinks().entrySet()) {
			Port p = link.getValue();
			r.put(p, p.rename("_" + i++).hide());
		}
		ReoConnector<ConstraintHypergraph> connector = new ReoConnectorComposite<>(null, "", list).rename(r);

		Long t2 = System.nanoTime();
		
		connector = connector.propagate(monitor);
		connector = connector.flatten();
		connector = connector.insertNodes(true, false, new ConstraintHypergraph());
		connector = connector.integrate();


		Long t3 = System.nanoTime();
		// Build the template.
		List<Component> components = new ArrayList<Component>();
		Set<Port> intface = new HashSet<Port>();

		// Identify the atomic components in the connector.
		int n_atom = 1;
		List<ConstraintHypergraph> protocols = new ArrayList<ConstraintHypergraph>();
		for (ReoConnectorAtom<ConstraintHypergraph> atom : connector.getAtoms()) {
			if (atom.getSourceCode().getCall() != null) {
				intface.addAll(atom.getInterface());
				String name = atom.getName();
				if (name == null)
					name = "Component";

				// TODO the string representation of parameter values is target
				// language dependent.
				List<String> params = new ArrayList<>();
				for (Value v : atom.getSourceCode().getValues()) {
					if (v instanceof BooleanValue) {
						params.add(((BooleanValue) v).getValue() ? "true" : "false");
					} else if (v instanceof StringValue) {
						params.add("\"" + ((StringValue) v).getValue() + "\"");
					} else if (v instanceof DecimalValue) {
						params.add(Double.toString(((DecimalValue) v).getValue()));
					}
				}
				components
						.add(new Atomic(name + n_atom++, params, atom.getInterface(), atom.getSourceCode().getCall()));
			} else {
				protocols.add(atom.getSemantics());
			}
		}

		Long t4 = System.nanoTime();
		
		// Compose the protocol into a single connector.
		ConstraintHypergraph circuit = new ConstraintHypergraph().compose(protocols);
//		RulesBasedAutomaton circuit = new RulesBasedAutomaton().compose(protocols);

		Long t5 = System.nanoTime();
		
		// Transform every disjunctive clause into a transition.
		Set<Transition> transitions = new HashSet<>();
		for (Rule rule : circuit.getRules()) {

			// Hide all internal ports
			Formula f = rule.getFormula();
			Set<Variable> vars = f.getFreeVariables();
			Set<Port> ports = new HashSet<>();
			for(Variable v : vars){
				if(v instanceof Node){
					ports.add(((Node) v).getPort());
				}
			}
			Set<Port> losingPorts = new HashSet<Port>();
			Map<Node,Term> losingData = new HashMap<Node,Term>(); 
			Set<Formula> negativePortGuard=new HashSet<Formula>(); 
			for (Port p : rule.getAllPorts()){
				if (!intface.contains(p))
					f = new Existential(new Node(p), f);
				else if(rule.getSync().get(p)){ //&& !ports.contains(p)){
					losingPorts.add(p);
					losingData.put(new Node(new Port("null"+p.getName())), new Node(p));
				}
				/*
				 * Add negative information in guard :
				 */
//				if(!rule.getSync().get(p)){
//					for(Rule _rule : circuit.getRules()){
//						if(_rule.getSync().get(p)!=null && _rule.getSync().get(p)){
//							for(Port port : _rule.getFiringPorts()){
//								if(intface.contains(port) && rule.getSync().get(port)==null){
//									negativePortGuard.add(new Equality(new Node(port),new Function("false",false, new ArrayList<>())));
//								}
//							}
//						}
//					}
//				}
			}

			// Commandify the formula:
			Transition t = RBACompiler.commandify(f);
			
			Set<Port> portList = new HashSet<Port>(losingPorts);
			for(Port p : portList){
				if(!t.getInput().contains(p) && !t.getOutput().containsKey(new Node(p))){
					losingPorts.addAll(t.getInput());
					losingData.putAll(t.getOutput());
				}
				else{
					losingPorts.remove(p);
					losingData.remove(new Node(p));
				}
			}
			
			negativePortGuard.add(t.getGuard());
			if(!negativePortGuard.isEmpty())
				t = new Transition(new Conjunction(new ArrayList<>(negativePortGuard)), t.getOutput(), t.getMemory(), t.getInput());

			if(!losingData.isEmpty() && !losingPorts.isEmpty())
				t = new Transition(new Conjunction(new ArrayList<>(negativePortGuard)), losingData, t.getMemory(), losingPorts);			
			
			if(!(t.getInput().isEmpty()&&t.getMemory().isEmpty()&&t.getOutput().isEmpty()))
				transitions.add(t);
		}

		Long t6 = System.nanoTime();
		
		// TODO Partition the set of transitions
		Set<Set<Transition>> partition = new HashSet<Set<Transition>>();

		partition.add(transitions);

		// Generate a protocol component for each part in the transition

		int n_protocol = 1;
		for (Set<Transition> T : partition) {
			Map<MemCell, Object> initial = new HashMap<>();
			Set<Port> ports = new HashSet<>();

			Map<MemCell, TypeTag> tags = new HashMap<>();
			for (Transition t : T) {				
				for (Map.Entry<MemCell, Term> m : t.getMemory().entrySet()) {
					MemCell x = m.getKey();
					MemCell x_prime = new MemCell(x.getName(),!x.hasPrime());
					
					if ((!tags.containsKey(x) || tags.get(x) == null) && (!tags.containsKey(x_prime) || tags.get(x_prime)==null)) {
						Term initialValueLHS = circuit.getInitials().get(new MemCell(m.getKey().getName(),false));
						Term initialValueRHS = null;
						if(m.getValue() instanceof MemCell)
							initialValueRHS = circuit.getInitials().get(new MemCell(((MemCell)m.getValue()).getName(),false));
						
						TypeTag tag=m.getValue().getTypeTag();
						for(Node n : t.getOutput().keySet()){
							if(t.getOutput().get(n) instanceof MemCell && ((MemCell)t.getOutput().get(n)).getName().equals(m.getKey().getName())&&n.getPort().getTypeTag()!=null){
								tag = new TypeTag(n.getPort().getTypeTag().toString());
							}
						}
						if(initialValueLHS !=null && initialValueLHS instanceof Function && ((Function)initialValueLHS).getValue() instanceof String){
							tag = new TypeTag("String");
						}
						if(initialValueRHS !=null && initialValueRHS instanceof Function && ((Function)initialValueRHS).getValue() instanceof String){
							tag = new TypeTag("String");
						}
						if(initialValueLHS !=null && initialValueLHS instanceof Function && ((Function)initialValueLHS).getValue() instanceof Integer){
							tag = new TypeTag("Integer");
						}
						if(initialValueRHS !=null && initialValueRHS instanceof Function && ((Function)initialValueRHS).getValue() instanceof Integer){
							tag = new TypeTag("Integer");
						}
						tags.remove(x);
						tags.remove(x_prime);
						tags.put(x, tag);
						tags.put(x_prime, tag);
						
						if(m.getValue() instanceof MemCell){
							tags.remove((MemCell)m.getValue());
							tags.remove(new MemCell(((MemCell)m.getValue()).getName(),!((MemCell)m.getValue()).hasPrime()));
							tags.put((MemCell)m.getValue(), tag);
							tags.put(new MemCell(((MemCell)m.getValue()).getName(),!((MemCell)m.getValue()).hasPrime()), tag);
						}
					}
				}
			}
			
			for (Transition t : T) {
				ports.addAll(t.getInterface());
				
				for (Map.Entry<MemCell, Term> m : t.getMemory().entrySet()) {
					Term initialValue = circuit.getInitials().get(new MemCell(m.getKey().getName(),false));
					if(initialValue instanceof Function && ((Function) initialValue).getValue() instanceof Integer)
						initialValue = (initialValue!=null?new Function(((Function)initialValue).getName(),"\""+((Function)initialValue).getValue().toString()+"\"", new ArrayList<Term>()):null);
					initial.put(m.getKey().setType(tags.get(m.getKey())), initialValue);
				}
			}
			
			components.add(new Protocol("Protocol" + n_protocol++, ports, T, initial));
		}


		System.out.println("interpret   " + (t2 - t1)/1000000000 + " seconds");
		System.out.println("flattening  " + (t3 - t2)/1000000000 + " seconds");
		System.out.println("workers     " + (t4 - t3)/1000000000 + " seconds");
		System.out.println("composition " + (t5 - t4)/1000000000 + " seconds");
		System.out.println("commandify  " + (t6 - t5)/1000000000 + " seconds");
//		System.out.println("template    " + (t7 - t6)/1000000000 + " seconds");
		

		// Fill in the template
		ReoTemplate template = new ReoTemplate(program.getFile(), packagename, program.getName(), components);

		// Generate Java code from the template
		String code = template.generateCode(Language.JAVA);
		write(program.getName() + ".java", code);

		Long t7 = System.nanoTime();
	
//		if(true){
//			try{
//				PrintWriter writer = new PrintWriter(outdir+"compilation_time.txt", "UTF-8");
//				writer.println("Compilation time : "+(t7-t1) + " nanosecondes");
//				writer.close();
//			} catch (IOException e) { // do something  
//			}
//		}

		
	}

	private void compileP() {

		// // Interpret the Reo program
		// Interpreter<Predicate> interpreter = new InterpreterP(directories,
		// params, monitor);
		// ReoProgram<Predicate> program = interpreter.interpret(files.get(0));
		//
		// if (program == null)
		// return;
		//
		// ReoConnector<Predicate> connector =
		// program.getConnector().flatten().insertNodes(true, false, new
		// Predicate())
		// .integrate();
		//
		// // Build the template.
		// List<Component> components = new ArrayList<Component>();
		// Set<Port> intface = new HashSet<Port>();
		//
		// // Identify the atomic components in the connector.
		// int n_atom = 0;
		// List<Predicate> protocols = new ArrayList<Predicate>();
		// for (ReoConnectorAtom<Predicate> atom : connector.getAtoms()) {
		// if (atom.getSourceCode().getCall() != null) {
		// intface.addAll(atom.getInterface());
		// String name = atom.getName();
		// if (name == null)
		// name = "Component";
		// components.add(new Atomic(name + n_atom++, new ArrayList<>(),
		// atom.getInterface(),
		// atom.getSourceCode().getCall()));
		// } else {
		// protocols.add(atom.getSemantics());
		// }
		// }
		//
		// // Formula automaton = JavaCompiler.compose(components);
		// // JavaCompiler.generateCode(automaton);
		//
		// // Compose the protocol into a single connector.
		// Predicate circuit = new
		// Predicate().compose(protocols).restrict(intface);
		//
		// // Put the obtained formula in (a quantifier-free) disjunctive normal
		// // form.
		// Formula f = circuit.getFormula().NNF().DNF().QE();
		//
		// // Transform every disjunctive clause into a transition.
		// Set<Transition> transitions = new HashSet<Transition>();
		// if (f instanceof Disjunction) {
		// for (Formula clause : ((Disjunction) f).getClauses()) {
		//
		// // Commandify the formula:
		// Transition t = RBACompiler.commandify(clause);
		//
		// transitions.add(t);
		// // Compute the guard by existential quantification on all output
		// // and next memory cells in clause and QE().
		// Formula guard = null;
		//
		// // Similar to guard, except do not hide one output port.
		// // if the resulting formula is of the form output = term over
		// // inputs, then add this the the map.
		// Map<Node, Term> output = new HashMap<Node, Term>();
		//
		// // Similar to output.
		// Map<MemoryCell, Term> memory = new HashMap<MemoryCell, Term>();
		//
		// // transitions.add(new Transition(guard, output, memory));
		// }
		// }
		//
		// // TODO Partition the set of transitions
		// Set<Set<Transition>> partition = new HashSet<Set<Transition>>();
		//
		// partition.add(transitions);
		//
		// // Generate a protocol component for each part in the transition
		//
		// Map<MemCell, Object> initial = new HashMap<MemCell, Object>();
		// int n_protocol = 0;
		// for (Set<Transition> T : partition) {
		// Set<Port> ports = new HashSet<Port>();
		// for (Transition t : T) {
		// ports.addAll(t.getInterface());
		// for (MemCell m : t.getMemory().keySet()) {
		// initial.put(m, 0);
		// }
		// }
		//
		// // TODO For convenience, we should be able to specify the initial
		// // value of each memory cell (particularly handy for fifofull)
		//
		// components.add(new Protocol("Protocol" + n_protocol++, ports, T,
		// initial));
		// }
		//
		// // Fill in the template
		// ReoTemplate template = new ReoTemplate(program.getFile(),
		// packagename, program.getName(), components);
		//
		// // Generate Java code from the template
		// String code = template.generateCode(Language.JAVA);
		// System.out.println(code);
		// write(program.getName(), code);
	}

	private void compilePR() {
		Long t1 = System.nanoTime();

		Interpreter<PRAutomaton> interpreter = new InterpreterPR(directories, params, monitor);

		ReoProgram<PRAutomaton> program = interpreter.interpret(files.get(0));

		if (program == null)
			return;

		if (verbose) {
			System.out.println(program.getConnector().flatten().insertNodes(true, true, new PRAutomaton()).integrate());
			System.out.println(PRCompiler.toPR(program));
			monitor.print();
			// GraphCompiler.visualize(program);

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

		LykosCompiler c = new LykosCompiler(program, files.get(0), outdir, packagename, monitor, settings);

		c.compile();
		Long t2 = System.nanoTime();

//		if(true){
//			try{
//				PrintWriter writer = new PrintWriter(outdir+"compilation_time_lykos.txt", "UTF-8");
//				writer.println("Compilation time : "+(t2-t1) + " nanosecondes");
//				writer.close();
//			} catch (IOException e) { // do something  
//			}
//		}
	}
}
