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
import nl.cwi.reo.semantics.hypergraphs.ConstraintHypergraph;
import nl.cwi.reo.semantics.prautomata.ListenerPR;
import nl.cwi.reo.semantics.prba.ListenerPRBA;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.rulebasedautomata.ListenerRBA;
import nl.cwi.reo.semantics.rulebasedautomata.RuleBasedAutomaton;
import nl.cwi.reo.templates.Atomic;
import nl.cwi.reo.templates.Component;
import nl.cwi.reo.templates.Protocol;
import nl.cwi.reo.templates.ReoTemplate;
import nl.cwi.reo.templates.Transition;
import nl.cwi.reo.templates.maude.MaudeAtomic;
import nl.cwi.reo.templates.maude.MaudeProtocol;
import nl.cwi.reo.templates.promela.PromelaAtomic;
import nl.cwi.reo.templates.promela.PromelaProtocol;
import nl.cwi.reo.templates.treo.TreoAtomic;
import nl.cwi.reo.templates.treo.TreoProtocol;
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

	private Interpreter getInterpreter(Language lang) {
		switch (lang) {
		case PRISM:
			ListenerPRBA listenerPRBA = new ListenerPRBA(monitor);
			return new Interpreter(SemanticsType.CH, listenerPRBA, directories, params, monitor);
		case JAVA:
		case C11:
		case PROMELA:
		case MAUDE:
		case TREO:
			ListenerRBA listenerRba = new ListenerRBA(monitor);
			return new Interpreter(SemanticsType.RBA, listenerRba, directories, params, monitor);
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
	private void compile() {

		Interpreter interpreter = getInterpreter(lang);

		ReoProgram program; 	
		if ((program = interpreter.interpret(files.get(0))) == null)
			return;
		
		ReoConnector connector = program.getConnector();
		connector = connector.propagate(monitor);
		connector = connector.flatten();
		connector = connector.insertNodes(true, true, new RuleBasedAutomaton());
		connector = connector.integrate();
		connector = compose(connector);
		
	  	Set<Transition> transitions = buildTransitions(connector);			
		Set<Set<Transition>> partition = partition(transitions,false);

		List<Component> components = new ArrayList<>();
		components.addAll(buildProtocols(connector, partition));
		components.addAll(buildAtomics(connector, lang));

		ReoTemplate template = new ReoTemplate(program.getFile(), version, packagename, program.getName(), components);
		generateCode(template);
	}
	
	/**
	 * Serialize ReoProgram to XML
	 * @param customer
	 * @return
	 */
/*
	private static void serializeXML(ReoProgram program) {

		XStream xstream = new XStream();

		String xml = xstream.toXML(program);
		try {
			File file = new File("../reo-runtime-java/src/main/java/"+program.getName()+".xml");
			FileWriter out = new FileWriter(file);
			out.write(xml);
			out.close();
		} catch (IOException e) {
		}
	}
	*/
	/**
	 * Composes all sub-connectors in a given connector. 
	 * Composition is defined per semantics. 
	 * The protocol consists of all components without a reference to code in a given target
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
	private ReoConnector compose(ReoConnector connector) {
		Atom composition = null;
		Set<SemanticsType> semType = new HashSet<>();
		for(ReoConnectorAtom reoConAtom : connector.getAtoms()) {
			for(Atom atom : reoConAtom.getSemantics()) {
				semType.add(atom.getType());
				}
			}
		List<ReoConnector> atomics = new ArrayList<>();
		Set<Port> nonHidden = new HashSet<>();
		
		/** Atoms with Java references */
		if(semType.contains(SemanticsType.REF)) {
			for (ReoConnectorAtom atom : connector.getAtoms()) {
				Reference r = atom.getReference(lang);
				if (r != null) {
					atomics.add(atom);
					nonHidden.addAll(atom.getInterface());
				}
			}
		}
		
		/** Composition using rule based automaton and constraint hypergraph */
		if(semType.contains(SemanticsType.RBA)) {
			RuleBasedAutomaton semantic = new RuleBasedAutomaton();
			List<RuleBasedAutomaton> protocols = new ArrayList<>();
			for (ReoConnectorAtom atom : connector.getAtoms()) {
				for (Atom x : atom.getSemantics()) {
					if (semantic.getType() == x.getType()) {
						protocols.add((RuleBasedAutomaton)x);
					}
				}
			}
			//Use of constraint hypergraph composition
			List<ConstraintHypergraph> ch = new ArrayList<>();
			for(RuleBasedAutomaton rba : protocols){
				ch.add(new ConstraintHypergraph(rba.getRules(),rba.getInitial()));
			}
			
			ConstraintHypergraph compositionCH = new ConstraintHypergraph().compose(ch);
			Set<Port> intface = connector.getInterface();
			intface.addAll(nonHidden);
			composition = compositionCH.restrict(intface);
		}
		
		ReoConnectorAtom protocol = new ReoConnectorAtom(connector.getName(), Arrays.asList(composition), connector.getLinks());
		atomics.add(protocol);
		return new ReoConnectorComposite("composite", "",atomics);
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
		Map<Port,Port> renaming = connector.getLinks();
		int n_atom = 1;

		/** Hide non hidden ports with i/o components */
		List<ReoConnectorAtom> list = new ArrayList<>();
		for (Port p : connector.getInterface()) {
			if (!p.isHidden()) {
				String call ="";
				if(lang == Language.JAVA){
					call = p.isInput() ? "Windows.producer" : "Windows.consumer";
				}
				if(lang == Language.MAUDE){
					call = p.isInput() ? "prod" : "cons";	
				}
				if(lang == Language.PROMELA){
					call = p.isInput() ? "prod" : "cons";
				}
				if(lang == Language.TREO){
					call = p.isInput() ? "prod" : "cons";
				}
				
				Value v = new StringValue(p.getName());
				List<Value> values = Arrays.asList(v);
				Reference ref = new Reference(call, lang, new ArrayList<>(), values);
				PortType t = p.isInput() ? PortType.OUT : PortType.IN;
				Port q = new Port(p.getName(), t, p.getPrioType(), new TypeTag("String"), true);
				Map<Port, Port> links = new HashMap<>();
				links.put(q.rename(p.getName()), q);
/*				links.put(q, q); */
				ReoConnectorAtom window = new ReoConnectorAtom("PortWindow", Arrays.asList(ref), links);
				list.add(window);
			}
		}
		
		/** Build components using constructors specific to each target languages */
		list.addAll(connector.getAtoms());
		for (ReoConnectorAtom atom : list) {
			Reference r = atom.getReference(lang);
			if (r != null) { 
				String call = r.getCall();
				String name = atom.getName();
				if (name == null)
					name = "Component";

//				 TODO the string representation of parameter values is target language dependent.
				
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
				if(lang == Language.JAVA)
					components.add(new Atomic(name + n_atom++, params, atom.rename(renaming).getInterface(), call));
				if(lang == Language.PROMELA)
					components.add(new PromelaAtomic(name + n_atom++, params, atom.rename(renaming).getInterface(), call));
				if(lang == Language.MAUDE)
					components.add(new MaudeAtomic(name + n_atom++, params, atom.rename(renaming).getInterface(), call));
				if(lang == Language.TREO)
					components.add(new TreoAtomic(name + n_atom++, params, atom.rename(renaming).getInterface(), call));

			}
		}
		
		return components;
	}

	private Set<Transition> buildTransitions(ReoConnector connector) {
		List<ReoConnectorAtom> protocol = connector.getAtoms();
		Set<Transition> transitions = new HashSet<>();
		for(ReoConnectorAtom atom : protocol) {
			if(atom.getSemantics().size()==1 && atom.getSemantics().get(0) instanceof ConstraintHypergraph) {
				ConstraintHypergraph _atom = (ConstraintHypergraph) atom.getSemantics().get(0);
				for (Formula f : _atom.getFormulas()){
					Command _cmd = new Commands().getCommand(f);
					if(_cmd !=null)
						transitions.add(_cmd.toTransition(lang));
				}
			}
		}
		return transitions;
	}

	private List<Component> buildProtocols(ReoConnector protocol, Set<Set<Transition>> partition) {
		List<Component> components = new ArrayList<>();
		int n_protocol = 1;
		for(ReoConnectorAtom atom : protocol.getAtoms()) {
			
			
			if(atom.getSemantics().size()==1 && atom.getSemantics().get(0) instanceof ConstraintHypergraph) {
				ConstraintHypergraph _atom = (ConstraintHypergraph) atom.getSemantics().get(0);
				for (Set<Transition> part : partition) {
		
					// Get the interface of this part
					Set<Port> ports = new HashSet<>();
					for (Transition t : part)
						ports.addAll(t.getInterface());
		
					//Add all memory variables
					Map<MemoryVariable, Object> initial = new HashMap<>();
					for (Transition t : part){
						initial.putAll(t.getInitial());
					}
					
					//Initialize value of some memory cells
					for (MemoryVariable mv : _atom.getInitials().keySet())
						initial.put(new MemoryVariable(mv.getName(),false,mv.getTypeTag()), _atom.getInitials().get(mv));
		
					if(lang == Language.JAVA)
						components.add(new Protocol("Protocol" + n_protocol++, ports, part, initial));
					if(lang == Language.MAUDE)
						components.add(new MaudeProtocol("Protocol" + n_protocol++, ports, part, initial));
					if(lang == Language.PROMELA)
						components.add(new PromelaProtocol("Protocol" + n_protocol++, ports, part, initial));
					if(lang == Language.TREO)
						components.add(new TreoProtocol("Protocol" + n_protocol++, ports, part, initial));
				}
			}
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
	private Set<Set<Transition>> partition(Set<Transition> transitions, Boolean max) {
		Set<Set<Transition>> partition = new HashSet<>();

		if (!transitions.isEmpty() ) {
			if(max){
				for(Transition t : transitions){
					Set<Transition> s = new HashSet<>();
					s.add(t);
					partition.add(s);
				}
			}
			else
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
		case PROMELA:
			group = new STGroupFile("Promela.stg");
			extension = ".pml";
			break;
		case PRISM:
			group = new STGroupFile("Prism.stg");
			extension = ".prism";
			break;
		case TREO:
			group = new STGroupFile("Treo.stg");
			extension = ".treo";
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
