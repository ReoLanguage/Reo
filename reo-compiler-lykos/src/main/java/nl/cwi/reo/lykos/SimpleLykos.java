package nl.cwi.reo.lykos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.pr.java.comp.JavaMainCompiler;

import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.pr.comp.ProgramCompiler;
import nl.cwi.reo.pr.comp.ProtocolCompiler;
import nl.cwi.reo.pr.comp.WorkerCompiler;
import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.comp.InterpretedProtocol;
import nl.cwi.reo.pr.comp.InterpretedWorker;
import nl.cwi.reo.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.reo.pr.autom.AutomatonFactory.AutomatonSet;
import nl.cwi.reo.pr.autom.StateFactory.State;
import nl.cwi.reo.pr.autom.TransitionFactory.Transition;
import nl.cwi.reo.pr.comp.MainCompiler;
import nl.cwi.reo.pr.misc.MemberSignature;
import nl.cwi.reo.pr.misc.PortFactory.Port;

public class SimpleLykos {

	private CompilerSettings settings;
	private AutomatonFactory automatonFactory;
	
	private static final boolean eclipse = false;

	
	public SimpleLykos(){
	}
	
	public void compile(String file,ProgramCompiler	programCompiler, AutomatonFactory automaton) 
	{ 
		this.automatonFactory=automaton;
		
		this.settings=programCompiler.getSettings();		
		List<Map<String, String>> files = new ArrayList<Map<String, String>>();
		files = getGeneratee(programCompiler);

		for(Map<String, String> f : files)
			try {
				writeFiles(f, eclipse ? "../reo-runtime-java-lykos/src/main/java" : ".");
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	public void writeFiles(Map<String,String> files,String targetDirectoryLocation) throws IOException{
		
		if (targetDirectoryLocation == null)
			throw new NullPointerException();
		
		for (Entry<String, String> entr : files.entrySet()) {
			Path parentDirectory = Paths.get(targetDirectoryLocation,
					entr.getKey()).getParent();

			if (parentDirectory != null)
				Files.createDirectories(parentDirectory);

			Files.write(
					Paths.get(targetDirectoryLocation, entr.getKey()),
					Arrays.asList(entr.getValue()), Charset.defaultCharset());
		}
	}

	public List<Map<String,String>> getGeneratee(ProgramCompiler	programCompiler){
		MainCompiler<?> mainCompiler= programCompiler.getMainCompiler();
		List<ProtocolCompiler<?>> protocolCompilers = programCompiler.getProtocolCompiler();
		List<WorkerCompiler<?>> workerCompilers = programCompiler.getWorkerCompiler();
		List<Map<String,String>> generatees = new ArrayList<>();
		Map<String,String> file;

		if (!settings.ignoreInput()) {

			for (ProtocolCompiler<?> c : protocolCompilers){
				file=generateProtocols(c);
				generatees.add(file);
			}


			for (WorkerCompiler<?> c : workerCompilers){
				file=generateWorkers(c);
				generatees.add(file);
			}

			file=generateMain(mainCompiler);
			generatees.add(file);
		}

		return generatees;
	}
	
	/**
	 * Generates the main class.
	 * @return Map assigning Java code to a file name
	 */
	private Map<String, String> generateMain(MainCompiler<?> mainCompiler) 
	{
		Map<String, String> files = new HashMap<String, String>();
		String mainClassName = "Main";	
		
		MainSignature signature = mainCompiler.getGeneratee().getSignature();
		Map<String, MemberSignature> protocolSignatures = new HashMap<>();
		Map<String, WorkerSignature> workerSignatures = new HashMap<>();	
		
		for(InterpretedProtocol p : mainCompiler.getGeneratee().getProtocols())
			protocolSignatures.put((String) p.getAnnotation("className"),
					p.getSignature()); // pr.main.Protocol_d20170127_t103917_197_FifoK=FifoK[3](A$1;B$1)
		for(InterpretedWorker w : mainCompiler.getGeneratee().getWorkers())
			workerSignatures.put((String) w.getAnnotation("className"),
					w.getSignature());	
		
		// Get string templates for main
		STGroupFile mainTemplates = new STGroupFile(eclipse ? "src/main/resources/java-main.stg" : "java-main.stg");
		ST mainHeaderTemplate = mainTemplates.getInstanceOf("header");
		ST mainClassTemplate = mainTemplates.getInstanceOf("mainClass");

		String mainCode = "";

		// Generate header
//		mainHeaderTemplate.add("packageName", mainPackageName);
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
	private Map<String, String> generateProtocols(ProtocolCompiler<?> c) 
	{
		Map<String, String> files = new HashMap<String, String>();
		String protocolClassName;		

		Automata JavaAutomata = new Automata(settings,c,this.automatonFactory);
		JavaAutomata.compile();
		AutomatonSet automata = JavaAutomata.getAutomata();

		/*
		 * Names
		 */
		protocolClassName = c.getGeneratee().getSignature().getName()
				.getName();

		protocolClassName = "Protocol_" 
				+ protocolClassName.substring(0, 1).toUpperCase()
				+ protocolClassName.substring(1);


		c.getGeneratee().addAnnotation(JavaMainCompiler.ANNOTATION_CLASS_NAME,
				protocolClassName);
		
		// Get string templates for protocol classes
		STGroupFile templates = new STGroupFile(eclipse ? "src/main/resources/java-protocol.stg" : "java-protocol.stg");
		ST headerTemplate = templates.getInstanceOf("header");
		ST protocolClassTemplate = templates.getInstanceOf("protocolClass");
		ST automatonClassTemplate = templates.getInstanceOf("automatonClass");
		ST stateClassTemplate = templates.getInstanceOf("stateClass");
		ST transitionClassTemplate = templates.getInstanceOf("transitionClass");
		ST handlerClassTemplate = templates.getInstanceOf("handlerClass");
		ST queueableHandlerClassTemplate = templates.getInstanceOf("queueableHandlerClass");

		StringBuilder code = new StringBuilder();

		// Generate header
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
			automatonClassTemplate = templates.getInstanceOf("automatonClass");
			automatonClassTemplate.add("settings", settings);
			automatonClassTemplate.add("protocolSimpleClassName", protocolClassName);
			automatonClassTemplate.add("automaton", aut);
			code.append("\n\n").append(automatonClassTemplate.render());

			// Generate state classes
			for (State st : aut.getStates().getSorted()) {
				st.enableCache();

				stateClassTemplate = templates.getInstanceOf("stateClass");
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

					transitionClassTemplate = templates.getInstanceOf("transitionClass");
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
				handlerClassTemplate = templates.getInstanceOf("handlerClass");
				handlerClassTemplate.add("settings", settings);
				handlerClassTemplate.add("protocolSimpleClassName", protocolClassName);
				handlerClassTemplate.add("automaton", aut);
				handlerClassTemplate.add("port", p);
				code.append("\n\n").append(handlerClassTemplate.render());
			}

			if (settings.partition() && aut.isMaster())
				for (Port p : aut.getPrivatePorts().getSorted()) {
					queueableHandlerClassTemplate = templates.getInstanceOf("queueableHandlerClass");
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
	private Map<String, String> generateWorkers(WorkerCompiler<?> w) 
	{
		Map<String, String> files = new HashMap<String, String>();
		
		WorkerSignature workerSignature = w.getGeneratee().getSignature();		
		String[] identifiers = workerSignature.getName().split("\\.");
		String workerSimpleClassName = identifiers[identifiers.length-1];
		String workerClassName = "Worker_"
				+ workerSimpleClassName.substring(0, 1).toUpperCase()
				+ workerSimpleClassName.substring(1);
				
		
		STGroupFile workerTemplates = new STGroupFile(eclipse ? "src/main/resources/java-worker.stg" : "java-worker.stg");
		String workerCode = "";
		
		w.getGeneratee().addAnnotation("className",	workerClassName);

		// Generate header
		ST workerHeaderTemplate = workerTemplates.getInstanceOf("header");
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
