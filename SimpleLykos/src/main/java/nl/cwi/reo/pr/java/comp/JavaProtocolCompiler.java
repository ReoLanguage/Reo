package nl.cwi.reo.pr.java.comp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.reo.pr.autom.AutomatonFactory.AutomatonSet;
import nl.cwi.reo.pr.autom.StateFactory.State;
import nl.cwi.reo.pr.autom.TransitionFactory.Transition;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.java.comp.JavaMainCompiler;
import nl.cwi.reo.pr.java.comp.JavaProgramCompiler;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.pr.comp.InterpretedProtocol;
import nl.cwi.reo.pr.comp.ProtocolCompiler;
import nl.cwi.reo.pr.util.Timestamps;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class JavaProtocolCompiler extends
		ProtocolCompiler<JavaProgramCompiler> {

	//
	// CONSTRUCTORS
	//

	public JavaProtocolCompiler(JavaProgramCompiler parent,
			InterpretedProtocol protocol, AutomatonFactory automatonFactory) {

		super(parent, protocol, automatonFactory);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected Map<String, String> generateFiles() {
		InterpretedProtocol protocol = getGeneratee();
		CompilerSettings settings = getSettings();
		AutomatonSet automata = getAutomata();

		// String protocolPackageName = getParent().getProgramPackageName() +
		// "."
		// + protocol.getSignature().getName().toLowerCase() + ".t"
		// + Timestamps.getNext();
		// String protocolSimpleClassName = "Protocol";

		String protocolPackageName = getParent().getProgramPackageName();
		String protocolSimpleClassName = protocol.getSignature().getName()
				.getName();

		protocolSimpleClassName = "Protocol_" + Timestamps.getNext() + "_"
				+ protocolSimpleClassName.substring(0, 1).toUpperCase()
				+ protocolSimpleClassName.substring(1);

		String protocolClassName = protocolPackageName + "."
				+ protocolSimpleClassName;

		protocol.addAnnotation(JavaMainCompiler.ANNOTATION_CLASS_NAME,
				protocolClassName);

		
		/*
		 * Change the pathname to load template
		 */
		
		STGroupFile templates = new STGroupFile("/home/e-spin/workspace/Compiler/stg/java-protocol.stg");

		ST headerTemplate;
		ST protocolClassTemplate;
		ST automatonClassTemplate;
		ST stateClassTemplate;
		ST transitionClassTemplate;
		ST handlerClassTemplate;
		ST queueableHandlerClassTemplate;

		Map<String, String> files = new HashMap<String, String>();
		StringBuilder code = new StringBuilder();

		/*
		 * Render header
		 */

		headerTemplate = templates.getInstanceOf("header");
		headerTemplate.add("packageName", protocolPackageName);
		code.append(headerTemplate.render());

		/*
		 * Render protocol class
		 */

		protocolClassTemplate = templates.getInstanceOf("protocolClass");
		protocolClassTemplate.add("settings", settings);
		protocolClassTemplate.add("protocolSimpleClassName",
				protocolSimpleClassName);
		protocolClassTemplate.add("automata", automata);
		code.append("\n\n").append(protocolClassTemplate.render());

		/*
		 * Render automaton classes
		 */

		for (Automaton aut : automata.getSorted()) {
			aut.enableCache();

			/*
			 * Render automaton class
			 */

			automatonClassTemplate = templates.getInstanceOf("automatonClass");
			automatonClassTemplate.add("settings", settings);
			automatonClassTemplate.add("protocolSimpleClassName",
					protocolSimpleClassName);
			automatonClassTemplate.add("automaton", aut);
			code.append("\n\n").append(automatonClassTemplate.render());

			/*
			 * Render state classes
			 */

			for (State st : aut.getStates().getSorted()) {
				st.enableCache();

				stateClassTemplate = templates.getInstanceOf("stateClass");
				stateClassTemplate.add("settings", settings);
				stateClassTemplate.add("protocolSimpleClassName",
						protocolSimpleClassName);
				stateClassTemplate.add("automaton", aut);
				stateClassTemplate.add("state", st);
				code.append("\n\n").append(stateClassTemplate.render());

				st.disableCache();
			}

			/*
			 * Render transition classes
			 */

			if (!settings.partition() || aut.isMaster())
				for (Transition tr : aut.getTransitions().getSorted()) {
					tr.enableCache();

					transitionClassTemplate = templates
							.getInstanceOf("transitionClass");

					transitionClassTemplate.add("automaton", aut);
					transitionClassTemplate.add("protocolSimpleClassName",
							protocolSimpleClassName);
					transitionClassTemplate.add("transition", tr);
					transitionClassTemplate.add("settings", settings);
					code.append("\n\n")
							.append(transitionClassTemplate.render());

					tr.disableCache();
				}

			/*
			 * Render handler classes
			 */

			for (Port p : aut.getPublicPorts().getSorted()) {
				handlerClassTemplate = templates.getInstanceOf("handlerClass");

				handlerClassTemplate.add("settings", settings);
				handlerClassTemplate.add("protocolSimpleClassName",
						protocolSimpleClassName);
				handlerClassTemplate.add("automaton", aut);
				handlerClassTemplate.add("port", p);
				code.append("\n\n").append(handlerClassTemplate.render());
			}

			if (settings.partition() && aut.isMaster())
				for (Port p : aut.getPrivatePorts().getSorted()) {
					queueableHandlerClassTemplate = templates
							.getInstanceOf("queueableHandlerClass");

					queueableHandlerClassTemplate.add("settings", settings);
					queueableHandlerClassTemplate.add(
							"protocolSimpleClassName", protocolSimpleClassName);
					queueableHandlerClassTemplate.add("automaton", aut);
					queueableHandlerClassTemplate.add("port", p);
					code.append("\n\n").append(
							queueableHandlerClassTemplate.render());
				}

			aut.disableCache();
		}

		/*
		 * Return
		 */

		files.put(protocolClassName.replace(".", File.separator) + ".java",
				code.toString());

		return files;
	}
}
