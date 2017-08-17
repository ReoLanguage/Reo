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
import java.util.SortedMap;
import java.util.TreeMap;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.commands.Command;
import nl.cwi.reo.commands.Commands;
import nl.cwi.reo.compile.CompilerType;
import nl.cwi.reo.compile.LykosCompiler;
import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.Reference;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.hypergraphs.ConstraintHypergraph;
import nl.cwi.reo.semantics.hypergraphs.ListenerCH;
import nl.cwi.reo.semantics.prautomata.ListenerPR;
import nl.cwi.reo.semantics.prba.ListenerPRBA;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.rulebasedautomata.ListenerRBA;
import nl.cwi.reo.semantics.rulebasedautomata.Rule;
import nl.cwi.reo.semantics.rulebasedautomata.RuleBasedAutomaton;
import nl.cwi.reo.templates.Atomic;
import nl.cwi.reo.templates.Component;
import nl.cwi.reo.templates.Protocol;
import nl.cwi.reo.templates.ReoTemplate;
import nl.cwi.reo.templates.Transition;
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.MessageType;
import nl.cwi.reo.util.Monitor;

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

	/** Scheduling. */
	@Parameter(names = { "-sch" }, description = "generate custom scheduling policy")
	private boolean scheduling = false;

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

		directories.add(".");
		String comppath = System.getenv("COMPATH");
		if (comppath != null)
			directories.addAll(Arrays.asList(comppath.split(File.pathSeparator)));

		switch (compilertype) {
		case LYKOS:
			compilePR();
			break;
		case CH:
			compileCH();
			break;
		case DEFAULT:
			compile();
			break;
		default:
			monitor.add("Please specify a compiler.");
			break;
		}

		monitor.print();
	}

	/**
	 * Compile PR.
	 */
	private void compilePR() {

		ListenerPR listener = new ListenerPR(monitor);
		Interpreter interpreter = new Interpreter(SemanticsType.PR, listener, directories, params, monitor);
		ReoProgram program = interpreter.interpret(files.get(0));

		if (program == null)
			return;

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

	/**
	 * Default compilation method.
	 */
	private void compile() {

		long t0 = System.nanoTime();
		
		Interpreter interpreter = getInterpreter(lang);

		ReoProgram program;
		if ((program = interpreter.interpret(files.get(0))) == null)
			return;
		
		long t1 = System.nanoTime();
		System.out.println("Interpret        : \t" + (t1-t0)/1E9);
		
		ReoConnector connector = addPortWindows(program.getConnector(), lang);
		connector = connector.propagate(monitor);
		connector = connector.flatten();
		connector = connector.insertNodes(true, false, new RuleBasedAutomaton());
		connector = connector.integrate();

		List<Component> components = buildAtomics(connector, lang);

		Set<Port> intface = getDualInterface(components);

		List<RuleBasedAutomaton> protocol = getProtocol(connector, lang, RuleBasedAutomaton.class);
		
		long t2 = System.nanoTime();
		System.out.println("Flatten          : \t" + (t2-t1)/1E9);

		RuleBasedAutomaton composition = new RuleBasedAutomaton().compose(protocol);

		composition = composition.restrict(intface);

		long t3 = System.nanoTime();
		System.out.println("Compose & hide   : \t" + (t3-t2)/1E9);

		Set<Transition> transitions = buildTransitions(composition);
		
		long t4 = System.nanoTime();
		System.out.println("Commandification : \t" + (t4-t3)/1E9);

		Set<Set<Transition>> partition = partition(transitions);

		List<Component> protocols = buildProtocols(composition, partition);

		components.addAll(protocols);

		ReoTemplate template = new ReoTemplate(program.getFile(), version, packagename, program.getName(), components);
		generateCode(template);

		long t5 = System.nanoTime();
		System.out.println("Template         : \t" + (t5-t4)/1E9);
		System.out.println("Total            : \t" + (t5-t1)/1E9);
	}

	private Interpreter getInterpreter(Language lang) {
		switch (lang) {
		case PRISM:
			ListenerPRBA listenerPRBA = new ListenerPRBA(monitor);
			return new Interpreter(SemanticsType.CH, listenerPRBA, directories, params, monitor);
		case JAVA:
		case C11:
			ListenerRBA listenerRBA = new ListenerRBA(monitor);
			return new Interpreter(SemanticsType.RBA, listenerRBA, directories, params, monitor);
		case MAUDE:
			break;
		case PRT:
			break;
		case TEXT:
			break;
		default:
			break;
		}
		return null;
	}
	
	/**
	 * Compile.
	 */
	private void compileCH() {

		Interpreter interpreter = getInterpreter(lang);

		ReoProgram program;
		if ((program = interpreter.interpret(files.get(0))) == null)
			return;

		ReoConnector connector = addPortWindows(program.getConnector(), lang);
		connector = connector.propagate(monitor);
		connector = connector.flatten();
		connector = connector.insertNodes(true, false, new RuleBasedAutomaton());
		connector = connector.integrate();

		List<Component> components = buildAtomics(connector, lang);

		Set<Port> intface = getDualInterface(components);

		List<RuleBasedAutomaton> protocol = getProtocol(connector, lang, RuleBasedAutomaton.class);
		
		List<ConstraintHypergraph> ch = new ArrayList<>();
		for(RuleBasedAutomaton rba : protocol)
			ch.add(new ConstraintHypergraph(rba.getAllRules()));			
		
		ConstraintHypergraph composition = new ConstraintHypergraph().compose(ch);

		composition = composition.restrict(intface);

		Set<Transition> transitions = buildTransitions(composition);

		Set<Set<Transition>> partition = partition(transitions);

		List<Component> protocols = buildProtocols(composition, partition);

		components.addAll(protocols);

		ReoTemplate template = new ReoTemplate(program.getFile(), version, packagename, program.getName(), components);
		generateCode(template);
	}

	/**
	 * Closes a given connector by attaching port windows to visible ports.
	 * 
	 * @param connector
	 *            potentially open connector
	 * @param lang
	 *            target language
	 * @return Connector with all ports hidden.
	 */
	private ReoConnector addPortWindows(ReoConnector connector, Language lang) {
		if (lang != Language.JAVA)
			return connector;
		List<ReoConnector> list = new ArrayList<>();
		list.add(connector);
		for (Port p : connector.getInterface()) {
			if (!p.isHidden()) {

				Value v = new StringValue(p.getName());
				List<Value> values = Arrays.asList(v);
				String call = p.isInput() ? "Windows.producer" : "Windows.consumer";
				Reference ref = new Reference(call, Language.JAVA, new ArrayList<>(), values);

				PortType t = p.isInput() ? PortType.OUT : PortType.IN;
				Port q = new Port(p.getName(), t, p.getPrioType(), new TypeTag("String"), true);
				Map<Port, Port> links = new HashMap<>();
				links.put(q, q);

				ReoConnectorAtom window = new ReoConnectorAtom("PortWindow", Arrays.asList(ref), links);
				list.add(window);
			}
		}
		return new ReoConnectorComposite(null, "", list).rename(new HashMap<>());
	}

	/**
	 * Builds the list of all atomic component templates.
	 * 
	 * @param connector
	 *            connector
	 * @param lang
	 *            target language
	 * @return list of atomic component templates
	 */
	private List<Component> buildAtomics(ReoConnector connector, Language lang) {
		List<Component> components = new ArrayList<>();
		int n_atom = 1;
		for (ReoConnectorAtom atom : connector.getAtoms()) {
			Reference r = atom.getReference(lang);
			if (r != null) {
				String call = r.getCall();
				String name = atom.getName();
				if (name == null)
					name = "Component";

				// TODO the string representation of parameter values is target
				// language dependent.
				List<String> params = new ArrayList<>();
				for (Value v : r.getValues()) {
					if (v instanceof BooleanValue) {
						params.add(((BooleanValue) v).getValue() ? "true" : "false");
					} else if (v instanceof StringValue) {
						params.add("\"" + ((StringValue) v).getValue() + "\"");
					} else if (v instanceof DecimalValue) {
						params.add(Double.toString(((DecimalValue) v).getValue()));
					}
				}

				components.add(new Atomic(name + n_atom++, params, atom.getInterface(), call));
			}
		}
		return components;
	}

	/**
	 * Gets the dual of the interface from atomic components to protocol
	 * components.
	 * 
	 * @param atoms
	 *            list of atomic components
	 * @return Dual interface
	 */
	private Set<Port> getDualInterface(List<Component> atomics) {
		Set<Port> intface = new HashSet<>();
		for (Component atom : atomics) {
			for (Port p : atom.getPorts()) {
				PortType t = p.isInput() ? PortType.OUT : PortType.IN;
				intface.add(new Port(p.getName(), t, p.getPrioType(), p.getTypeTag(), true));
			}
		}
		return intface;
	}

	/**
	 * Composes all protocol connector in a given connector. The protocol
	 * consists of all components without a reference to code in a given target
	 * language.
	 * 
	 * @param connector
	 *            connector
	 * @param lang
	 *            target language
	 * @param unit
	 *            unit
	 * @return Composition of all protocol connectors.
	 */
	private <T extends Semantics<T>> List<T> getProtocol(ReoConnector connector, Language lang, Class<T> type) {
		List<T> protocols = new ArrayList<>();
		for (ReoConnectorAtom atom : connector.getAtoms()) {
			Reference r = atom.getReference(lang);
			if (r == null) {
				T semantics = null;
				for (Atom x : atom.getSemantics())
					if (type.isInstance(x))
						semantics = type.cast(x);
				if (semantics == null) {
					monitor.add("Not all components have semantics.");
					return new ArrayList<>();
				}
				protocols.add(semantics);
			}
		}

		return protocols;
	}

	private Set<Transition> buildTransitions(ConstraintHypergraph protocol) {
		Set<Transition> transitions = new HashSet<>();
		for (Rule rule : protocol.getRules())
			transitions.add(RBACompiler.commandify(rule.getFormula()));
		return transitions;
	}
	
	private Set<Transition> buildTransitions(RuleBasedAutomaton protocol) {
		Set<Transition> transitions = new HashSet<>();
		for (Rule rule : protocol.getAllRules()) {
			Command cmd = Commands.commandify(rule.getFormula());
			transitions.add(cmd.toTransition());
		}
		return transitions;
	}

	/**
	 * Build the protocol templates from a constraint hypergraph and a
	 * partition.
	 * 
	 * @param protocol
	 *            composed protocol
	 * @param partition
	 *            partitioned set of transitions
	 * 
	 * @return list of protocol templates
	 */
	private List<Component> buildProtocols(RuleBasedAutomaton protocol, Set<Set<Transition>> partition) {
		List<Component> components = new ArrayList<>();
		int n_protocol = 1;
		for (Set<Transition> part : partition) {

			// Get the interface of this part
			Set<Port> ports = new HashSet<>();
			for (Transition t : part)
				ports.addAll(t.getInterface());

			// Find the type of each memory cell that occurs in this part
			Map<String, TypeTag> tags = new HashMap<>();
			for (Map.Entry<MemoryVariable, Term> init : protocol.getInitial().entrySet())
				tags.put(init.getKey().getName(), init.getValue().getTypeTag());
			for (Transition t : part)
				for (Map.Entry<MemoryVariable, Term> upd : t.getMemory().entrySet())
					tags.put(upd.getKey().getName(), upd.getValue().getTypeTag());

			// Get the initial value of each memory cell
			SortedMap<MemoryVariable, Object> initial = new TreeMap<>();
			for (Map.Entry<String, TypeTag> e : tags.entrySet())
				initial.put(new MemoryVariable(e.getKey(), false, e.getValue()), protocol.getInitial().get(e.getKey()));

			components.add(new Protocol("Protocol" + n_protocol++, ports, part, initial));
		}
		return components;
	}

	private List<Component> buildProtocols(ConstraintHypergraph protocol, Set<Set<Transition>> partition) {
		List<Component> components = new ArrayList<>();
		int n_protocol = 1;
		for (Set<Transition> part : partition) {

			// Get the interface of this part
			Set<Port> ports = new HashSet<>();
			for (Transition t : part)
				ports.addAll(t.getInterface());

			// Find the type of each memory cell that occurs in this part
			Map<String, TypeTag> tags = new HashMap<>();
			for (Map.Entry<MemoryVariable, Term> init : protocol.getInitials().entrySet())
				tags.put(init.getKey().getName(), init.getValue().getTypeTag());
			for (Transition t : part)
				for (Map.Entry<MemoryVariable, Term> upd : t.getMemory().entrySet())
					tags.put(upd.getKey().getName(), upd.getValue().getTypeTag());

			// Get the initial value of each memory cell
			Map<MemoryVariable, Object> initial = new HashMap<>();
			for (Map.Entry<String, TypeTag> e : tags.entrySet())
				initial.put(new MemoryVariable(e.getKey(), false, e.getValue()), protocol.getInitials().get(e.getKey()));

			components.add(new Protocol("Protocol" + n_protocol++, ports, part, initial));
		}
		return components;
	}

	/**
	 * Partitions the set of transitions into independent regions.
	 * 
	 * @param transitions
	 *            set of transitions
	 * @return partitioned set of transitions.
	 */
	private Set<Set<Transition>> partition(Set<Transition> transitions) {
		Set<Set<Transition>> partition = new HashSet<>();

		if (!transitions.isEmpty()) {
			// for (Transition t : transitions) {
			// Set<Set<Transition>> newPartition = new HashSet<>();
			// newPartition.add(new HashSet<>(Arrays.asList(t)));
			// for (Set<Transition> part : partition) {
			//
			// }
			// }
			partition.add(transitions);
		}

		return partition;
	}

	/**
	 * Generates code from the standard Reo template.
	 * 
	 * @param template
	 *            template with code
	 * @param name
	 *            name of generated file.
	 */
	private void generateCode(ReoTemplate template) {
		STGroup group = null;
		String extension = "";

		switch (lang) {
		case JAVA:
			group = new STGroupFile("Java.stg");
			extension = ".java";
			break;
		case MAUDE:
			group = new STGroupFile("Maude.stg");
			extension = ".maude";
			break;
		case PRISM:
			group = new STGroupFile("Prism.stg");
			extension = ".prism";
			break;
		default:
			return;
		}

		ST stringtemplate = group.getInstanceOf("main");
		stringtemplate.add("S", template);

		String code = stringtemplate.render(72);

		try {
			File file = new File(outdir + File.separator + template.getName() + extension);
			file.getParentFile().mkdirs();
			FileWriter out = new FileWriter(file);
			out.write(code);
			out.close();
		} catch (IOException e) {
		}
	}
}
