package nl.cwi.reo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.compile.CompilerType;
import nl.cwi.reo.compile.LykosCompiler;
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
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.semantics.hypergraphs.ConstraintHypergraph;
import nl.cwi.reo.semantics.hypergraphs.Rule;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.MessageType;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * A compiler for the coordination language Reo.
 */
public class Compiler {

	/** Version number. */
	private static String version = "1.0.1";

	/**
	 * List of provided Reo source files.
	 */
	@Parameter(description = ".treo files")
	private List<String> files = new ArrayList<String>();

	/**
	 * Compiler type.
	 */
	@Parameter(names = { "-c" }, description = "compiler")
	public CompilerType compilertype = CompilerType.DEFAULT;

	/** List of of directories that contain all necessary Reo components. */
	@Parameter(names = {
			"-cp" }, variableArity = true, description = "list of directories that contain all necessary Reo components")
	private List<String> directories = new ArrayList<String>();

	/**
	 * List of provided Reo source files.
	 */
	@Parameter(names = { "-o" }, description = "output directory")
	private String outdir = ".";

	/**
	 * List of parameters for the main component.
	 */
	@Parameter(names = {
			"-p" }, variableArity = true, description = "list of parameters to instantiate the main component")
	public List<String> params = new ArrayList<String>();

	/** Package. */
	@Parameter(names = { "-pkg" }, description = "target code package")
	private String packagename;

	/** Partitioning. */
	@Parameter(names = { "-pt" }, description = "synchronous region decomposition")
	private boolean partitioning = false;

	/**
	 * Target language.
	 */
	@Parameter(names = { "-t" }, variableArity = true, description = "target language")
	public Language lang = Language.JAVA;

	/**
	 * Message container.
	 */
	private final Monitor monitor = new Monitor();

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		Compiler compiler = new Compiler();
		try {
			JCommander jc = new JCommander(compiler, args);
			jc.setProgramName("reo");
			if (compiler.files.size() == 0) {
				System.out.println("Reo compiler v" + version + "\nDeveloped at CWI, Amsterdam\n");
				jc.usage();
			} else {
				compiler.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(new Message(MessageType.ERROR, e.toString()));
		}
	}

	/**
	 * Run.
	 */
	public void run() {

		// Get the root locations of Reo source files and libraries.
		directories.add(".");
		String comppath = System.getenv("COMPATH");
		if (comppath != null)
			directories.addAll(Arrays.asList(comppath.split(File.pathSeparator)));

		// Select the correct compiler.
		switch (compilertype) {
		case LYKOS:
			compilePR();
			break;
		case DEFAULT:
			compile();
			break;
		default:
			monitor.add("Please specify a compiler.");
			break;
		}

		// Print all messages.
		monitor.print();
	}

	/**
	 * Compile.
	 */
	private void compile() {

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
				String call = p.isInput() ? "Windows.producer" : "Windows.consumer";
				Reference src = new Reference(call, Language.JAVA, null, values);
				PortType t = p.isInput() ? PortType.OUT : PortType.IN;
				Port q = new Port(p.getName(), t, p.getPrioType(), new TypeTag("String"), true);
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
			r.put(p, p.rename("$" + i++).hide());
		}
		ReoConnector<ConstraintHypergraph> connector = new ReoConnectorComposite<>(null, "", list).rename(r);

		connector = connector.propagate(monitor);
		connector = connector.flatten();
		connector = connector.insertNodes(true, false, new ConstraintHypergraph());
		connector = connector.integrate();

		// Build the template.
		List<Component> components = new ArrayList<Component>();
		Set<Port> intface = new HashSet<Port>();

		// Identify the atomic components in the connector.
		int n_atom = 1;
		List<ConstraintHypergraph> protocols = new ArrayList<ConstraintHypergraph>();
		for (ReoConnectorAtom<ConstraintHypergraph> atom : connector.getAtoms()) {
			if (atom.getSourceCode().getCall() != null) {

				// Add the dual port to the interface of the protocol.
				for (Port p : atom.getInterface()) {
					PortType t = p.isInput() ? PortType.OUT : PortType.IN;
					intface.add(new Port(p.getName(), t, p.getPrioType(), p.getTypeTag(), true));
				}

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

		// Compose the protocol into a single connector.
		ConstraintHypergraph circuit = new ConstraintHypergraph().compose(protocols);

		// Transform every rule in the circuit into a transition.
		Set<Transition> transitions = new HashSet<>();
		for (Rule rule : circuit.getRules()) {

			// Hide all internal ports
			Formula f = rule.getDataConstraint();
			for (Port p : rule.getAllPorts())
				if (!intface.contains(p))
					f = new Existential(new PortVariable(p), f).QE();
			
			// Commandify the formula
			Transition t = RBACompiler.commandify(f);

			if (!(t.getInput().isEmpty() && t.getMemory().isEmpty() && t.getOutput().isEmpty()))
				transitions.add(t);
		}

		// TODO Partition the set of transitions
		Set<Set<Transition>> partition = new HashSet<>();

		if (!transitions.isEmpty())
			partition.add(transitions);

		// Generate a protocol component for each part in the transition
		int n_protocol = 1;
		for (Set<Transition> part : partition) {
			Map<MemoryVariable, Object> initial = new HashMap<>();
			Set<Port> ports = new HashSet<>();

			Map<MemoryVariable, TypeTag> tags = new HashMap<>();
			for (Transition t : part) {
				for (Map.Entry<MemoryVariable, Term> m : t.getMemory().entrySet()) {
					MemoryVariable x = m.getKey();
					MemoryVariable x_prime = new MemoryVariable(x.getName(), !x.hasPrime());

					if ((!tags.containsKey(x) || tags.get(x) == null)
							&& (!tags.containsKey(x_prime) || tags.get(x_prime) == null)) {
						Term initialValueLHS = circuit.getInitials()
								.get(new MemoryVariable(m.getKey().getName(), false));
						Term initialValueRHS = null;
						if (m.getValue() instanceof MemoryVariable)
							initialValueRHS = circuit.getInitials()
									.get(new MemoryVariable(((MemoryVariable) m.getValue()).getName(), false));

						TypeTag tag = m.getValue().getTypeTag();
						for (PortVariable n : t.getOutput().keySet()) {
							if (t.getOutput().get(n) instanceof MemoryVariable
									&& ((MemoryVariable) t.getOutput().get(n)).getName().equals(m.getKey().getName())
									&& n.getPort().getTypeTag() != null) {
								tag = new TypeTag(n.getPort().getTypeTag().toString());
							}
						}
						if (initialValueLHS != null && initialValueLHS instanceof Function
								&& ((Function) initialValueLHS).getValue() instanceof String) {
							tag = new TypeTag("String");
						}
						if (initialValueRHS != null && initialValueRHS instanceof Function
								&& ((Function) initialValueRHS).getValue() instanceof String) {
							tag = new TypeTag("String");
						}
						if (initialValueLHS != null && initialValueLHS instanceof Function
								&& ((Function) initialValueLHS).getValue() instanceof Integer) {
							tag = new TypeTag("Integer");
						}
						if (initialValueRHS != null && initialValueRHS instanceof Function
								&& ((Function) initialValueRHS).getValue() instanceof Integer) {
							tag = new TypeTag("Integer");
						}
						tags.remove(x);
						tags.remove(x_prime);
						tags.put(x, tag);
						tags.put(x_prime, tag);

						if (m.getValue() instanceof MemoryVariable) {
							tags.remove((MemoryVariable) m.getValue());
							tags.remove(new MemoryVariable(((MemoryVariable) m.getValue()).getName(),
									!((MemoryVariable) m.getValue()).hasPrime()));
							tags.put((MemoryVariable) m.getValue(), tag);
							tags.put(new MemoryVariable(((MemoryVariable) m.getValue()).getName(),
									!((MemoryVariable) m.getValue()).hasPrime()), tag);
						}
					}
				}
			}

			for (Transition t : part) {
				ports.addAll(t.getInterface());

				for (Map.Entry<MemoryVariable, Term> m : t.getMemory().entrySet()) {
					Term initialValue = circuit.getInitials().get(new MemoryVariable(m.getKey().getName(), false));
					if (initialValue instanceof Function && ((Function) initialValue).getValue() instanceof Integer)
						initialValue = (initialValue != null ? new Function(((Function) initialValue).getName(),
								((Function) initialValue).getValue().toString(), new ArrayList<Term>()) : null);
					initial.put(m.getKey().setType(tags.get(m.getKey())), initialValue);
				}
			}

			components.add(new Protocol("Protocol" + n_protocol++, intface, part, initial));
		}

		// Fill in the template
		ReoTemplate template = new ReoTemplate(program.getFile(), packagename, program.getName(), components);

		// Generate Java code from the template
		Language L = Language.JAVA;
		STGroup group = null;
		String extension = "";

		switch (L) {
		case JAVA:
			group = new STGroupFile("Java.stg");
			extension = ".java";
			break;
		case MAUDE:
			group = new STGroupFile("Maude.stg");
			extension = ".maude";
			break;
		default:
			break;
		}

		ST stringtemplate = group.getInstanceOf("main");
		stringtemplate.add("S", template);

		String code = stringtemplate.render(72);

		// Write the code to a file
		try {
			File file = new File(outdir + File.separator + program.getName() + extension);
			file.getParentFile().mkdirs();
			FileWriter out = new FileWriter(file);
			out.write(code);
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Compile PR.
	 */
	private void compilePR() {

		Interpreter<PRAutomaton> interpreter = new InterpreterPR(directories, params, monitor);

		ReoProgram<PRAutomaton> program = interpreter.interpret(files.get(0));

		if (program == null)
			return;

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

	}
}
