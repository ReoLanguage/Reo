package nl.cwi.reo.lykos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.pr.autom.AutomatonFactory.AutomatonSet;
import nl.cwi.pr.autom.StateFactory.State;
import nl.cwi.pr.autom.TransitionFactory.Transition;
import nl.cwi.pr.misc.MemberSignature;
import nl.cwi.pr.misc.Variable;
import nl.cwi.pr.misc.PortFactory.Port;

public class SimpleLykos {
	
	private final String mainPackageName = "com.example.myapp";	
	private final String mainClassName = "Main";	
	private final String protocolPackageName = "com.example.myapp";	
	private final String protocolClassName = "Protocol_SimpleName";	
	private final String workerPackageName = "com.example.myapp";	
	private final String workerClassName = "Worker_SimpleName";

	public void compile(String file) 
	{ 
		// Map assigning content to each file name
		Map<String, String> files = new HashMap<String, String>();
		
		files.putAll(generateMain());
		files.putAll(generateProtocols());
		files.putAll(generateWorkers());

		// Output all files
	}

	/**
	 * Generates the main class.
	 * @return Map assigning Java code to a file name
	 */
	private Map<String, String> generateMain() 
	{
		Map<String, String> files = new HashMap<String, String>();

		// TODO fill in main signature and protocol/worker signatures
		MainSignature signature = new MainSignature(null, null, null, null);
		Map<String, MemberSignature> protocolSignatures = new HashMap<>(); // pr.main.Protocol_d20170127_t103917_197_FifoK=FifoK[3](A$1;B$1)
		Map<String, WorkerSignature> workerSignatures = new HashMap<>();	
		
		// Get string templates for main
		STGroupFile mainTemplates = new STGroupFile("src/main/resources/java-main.stg");
		ST mainHeaderTemplate = mainTemplates.getInstanceOf("header");
		ST mainClassTemplate = mainTemplates.getInstanceOf("mainClass");

		String mainCode = "";

		// Generate header
		mainHeaderTemplate.add("packageName", mainPackageName);
		mainCode += mainHeaderTemplate.render();

		// Generate body
		mainClassTemplate.add("signature", signature);
		mainClassTemplate.add("protocolSignatures", protocolSignatures);
		mainClassTemplate.add("workerSignatures", workerSignatures);
		mainCode += "\n\n" + mainClassTemplate.render();

		files.put(mainClassName + ".java", mainCode);
		
		return files;
	}

	/**
	 * Generates the protocol classes.
	 * @return Map assigning Java code to a file name
	 */
	private Map<String, String> generateProtocols() 
	{
		Map<String, String> files = new HashMap<String, String>();
		
		CompilerSettings settings = new CompilerSettings();
		settings.ignoreInput(false);
		settings.ignoreData(false);
		settings.partition(true);
		settings.subtractSyntactically(true);
		settings.commandify(true);
		settings.inferQueues(true);
		settings.put("COUNT_PORTS", false);

		// TODO fill in automaton set from List<T> for some T
		Automata JavaAutomata = new Automata(settings);
		JavaAutomata.compile();
		AutomatonSet automata = JavaAutomata.getAutomata();

		// Get string templates for protocol classes
		STGroupFile templates = new STGroupFile("src/main/resources/java-protocol.stg");
		ST headerTemplate = templates.getInstanceOf("header");
		ST protocolClassTemplate = templates.getInstanceOf("protocolClass");
		ST automatonClassTemplate = templates.getInstanceOf("automatonClass");
		ST stateClassTemplate = templates.getInstanceOf("stateClass");
		ST transitionClassTemplate = templates.getInstanceOf("transitionClass");
		ST handlerClassTemplate = templates.getInstanceOf("handlerClass");
		ST queueableHandlerClassTemplate = templates.getInstanceOf("queueableHandlerClass");

		StringBuilder code = new StringBuilder();

		// Generate header
		headerTemplate.add("packageName", protocolPackageName);
		code.append(headerTemplate.render());

		// Generate protocol class
		protocolClassTemplate.add("settings", settings);
		protocolClassTemplate.add("protocolSimpleClassName", protocolClassName);
		protocolClassTemplate.add("automata", automata);
		code.append("\n\n").append(protocolClassTemplate.render());

		// Generate automaton classes
		for (Automaton aut : automata.getSorted()) {
			aut.enableCache();

			// Generate automaton class
			automatonClassTemplate.add("settings", settings);
			automatonClassTemplate.add("protocolSimpleClassName", protocolClassName);
			automatonClassTemplate.add("automaton", aut);
			code.append("\n\n").append(automatonClassTemplate.render());

			// Generate state classes
			for (State st : aut.getStates().getSorted()) {
				st.enableCache();

				stateClassTemplate.add("settings", settings);
				stateClassTemplate.add("protocolSimpleClassName",protocolClassName);
				stateClassTemplate.add("automaton", aut);
				stateClassTemplate.add("state", st);
				code.append("\n\n").append(stateClassTemplate.render());

				st.disableCache();
			}
			
			// Generate transition classes
			if (!settings.partition() || aut.isMaster())
				for (Transition tr : aut.getTransitions().getSorted()) {
					tr.enableCache();

					transitionClassTemplate.add("automaton", aut);
					transitionClassTemplate.add("protocolSimpleClassName", protocolClassName);
					transitionClassTemplate.add("transition", tr);
					transitionClassTemplate.add("settings", settings);
					code.append("\n\n")
							.append(transitionClassTemplate.render());

					tr.disableCache();
				}

			// Generate handler classes
			for (Port p : aut.getPublicPorts().getSorted()) {
				handlerClassTemplate.add("settings", settings);
				handlerClassTemplate.add("protocolSimpleClassName", protocolClassName);
				handlerClassTemplate.add("automaton", aut);
				handlerClassTemplate.add("port", p);
				code.append("\n\n").append(handlerClassTemplate.render());
			}

			if (settings.partition() && aut.isMaster())
				for (Port p : aut.getPrivatePorts().getSorted()) {
					queueableHandlerClassTemplate.add("settings", settings);
					queueableHandlerClassTemplate.add("protocolSimpleClassName", protocolClassName);
					queueableHandlerClassTemplate.add("automaton", aut);
					queueableHandlerClassTemplate.add("port", p);
					code.append("\n\n").append(
							queueableHandlerClassTemplate.render());
				}

			aut.disableCache();
		}

		files.put(protocolClassName + ".java", code.toString());
		
		return files;		
	}

	/**
	 * Generates the worker classes.
	 * @return Map assigning Java code to a file name
	 */
	private Map<String, String> generateWorkers() 
	{
		Map<String, String> files = new HashMap<String, String>();
		
		// TODO fill in signature of worker.
		WorkerSignature workerSignature = new WorkerSignature("", new ArrayList<Variable>());
				
		STGroupFile workerTemplates = new STGroupFile("src/main/resources/java-worker.stg");
		String workerCode = "";
		
		// Generate header
		ST workerHeaderTemplate = workerTemplates.getInstanceOf("header");
		workerHeaderTemplate.add("packageName", workerPackageName);
		workerCode += workerHeaderTemplate.render();

		// Generate body
		ST workerClassTemplate = workerTemplates.getInstanceOf("workerClass");
		workerClassTemplate.add("simpleClassName", workerClassName);
		workerClassTemplate.add("signature", workerSignature);
		workerCode += "\n\n" + workerClassTemplate.render();

		files.put(workerClassName + ".java", workerCode);
		
		return files;		
	}
}
