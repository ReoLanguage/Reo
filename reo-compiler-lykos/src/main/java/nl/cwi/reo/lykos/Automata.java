package nl.cwi.reo.lykos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.autom.UserDefinedAutomaton;
import nl.cwi.reo.pr.autom.UserDefinedInitializer;
import nl.cwi.reo.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.reo.pr.autom.AutomatonFactory.AutomatonSet;
import nl.cwi.reo.pr.autom.libr.Sync;
import nl.cwi.reo.pr.comp.CompilerSettings;
import nl.cwi.reo.pr.comp.ProtocolCompiler;
import nl.cwi.reo.pr.misc.MemberSignature;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortSpec;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.misc.Member.Primitive;

public class Automata {
	
	private final AutomatonSet automata;
	private final AutomatonFactory automatonFactory;
	private final CompilerSettings settings;
	private final ProtocolCompiler<?> c;

	public Automata(CompilerSettings settings,ProtocolCompiler<?> c,AutomatonFactory automatonFactory) {

		this.c=c;
		this.automatonFactory = automatonFactory;
		this.automata = automatonFactory.newSet();
		this.settings=settings;
	}

	
	public void compile() {
		
		
		automata.clear();

		/*
		 * Partition
		 */

		AutomatonSet smallAutomata = loadSmallAutomata();
		List<AutomatonSet> partition = null;

		if (settings.partition()) {
			partition = smallAutomata.partition();
			if (partition.size() == 1) {
				smallAutomata = partition.get(0);
				settings.partition(false);
			}
		}

		/*
		 * Compute "medium" automata
		 */

		if (settings.partition())
			try {

				for (AutomatonSet s : partition) {
					Automaton mediumAutomaton = automatonFactory.multiplyAll(s,
							settings.subtractSyntactically());

					automata.add(mediumAutomaton);
				}

				AutomatonSet masters = automatonFactory.newSet();
				for (Automaton aut : automata)
					if (aut.isMaster())
						if (aut.hasPredictableNeighbors())
							masters.add(aut);
						else
							throw new Exception(
									"Unpredictable neighbor detected");

				if (settings.inferQueues())
					masters.inferQueues();
				if (settings.commandify())
					automata.commandifyMasters();

			}

			catch (Exception e) {
				e.printStackTrace();
			}

		/*
		 * Compute "big" automaton
		 */

		else
			try {
				Automaton bigAutomaton = automatonFactory.multiplyAll(
						smallAutomata, settings.subtractSyntactically());

				automata.add(bigAutomaton);

				if (settings.inferQueues())
					bigAutomaton.inferQueues();

				// System.err.println(bigAutomaton);

				if (settings.commandify())
					automata.commandify();
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		/*
		 * Cancel
		 */


		/*
		 * Remove effectively empty automata
		 */

		for (Automaton aut : automata.getSorted())
			if (aut.getTransitions().getNonsilentSubset().isEmpty())
			automata.remove(aut);

		/*
		 * Return
		 */
		
	}
	
	public AutomatonSet getAutomata() {
		return automata;
	}
	
	/*
	 * Private methods
	 */
	
	
	
	
	private AutomatonSet loadSmallAutomata() {
		
		AutomatonSet automata = automatonFactory.newSet();
//		List<Primitive> P = (List<Primitive>) new Member(); 
		for (Primitive pr : c.getGeneratee().getMember().getPrimitives()) {
			MemberSignature signature = pr.getSignature();
			String className = pr.getClassName();
			String rootDirectoryLocation = pr.getRootLocation();

			/*
			 * Load user-defined automaton for l
			 */

			UserDefinedAutomaton uda = new UserDefinedAutomaton(
					automatonFactory, signature.getPortFactory(),
					signature.getInputPorts(), signature.getOutputPorts(),
					signature.getExtralogicals().values(),
					signature.toString(), settings.ignoreData());

			Automaton automaton = loadUserDefinedAutomaton(uda, className,
					rootDirectoryLocation);

			/*
			 * Prepend/append Syncs if automaton is nonsynchronizing
			 */

			if (settings.partition() && !automaton.isSynchronizing()) {
				PortFactory portFactory = signature.getPortFactory();

				/*
				 * Prepend Sync to input ports
				 */

				List<Port> inputPorts = new ArrayList<>();
				for (Port p : signature.getInputPorts()) {
					Port port = portFactory.newOrGet(new PortSpec("$in"
							+ p.getName()));

					automata.add(loadSyncAutomaton(portFactory, p, port));
					inputPorts.add(port);
				}

				/*
				 * Append Sync to output ports
				 */

				List<Port> outputPorts = new ArrayList<>();
				for (Port p : signature.getOutputPorts()) {
					Port port = portFactory.newOrGet(new PortSpec("$out"
							+ p.getName()));

					automata.add(loadSyncAutomaton(portFactory, port, p));
					outputPorts.add(port);
				}

				/*
				 * Load updated user-defined automaton
				 */

				automatonFactory.dispose(automaton);

				uda = new UserDefinedAutomaton(automatonFactory, portFactory,
						inputPorts, outputPorts, signature.getExtralogicals()
								.values(), signature.toString(), settings.ignoreData());

				automaton = loadUserDefinedAutomaton(uda, className,
						rootDirectoryLocation);
			}

			automata.add(automaton);
		}

		return automata;
	}

	private Automaton loadSyncAutomaton(PortFactory portFactory,
			Port inputPort, Port outputPort) {

		if (portFactory == null)
			throw new NullPointerException();
		if (inputPort == null)
			throw new NullPointerException();
		if (outputPort == null)
			throw new NullPointerException();

		UserDefinedAutomaton uda = new UserDefinedAutomaton(automatonFactory,
				portFactory, Arrays.asList(inputPort),
				Arrays.asList(outputPort),
				Collections.<Extralogical> emptyList(), "/Sync(" + inputPort
						+ ";" + outputPort + ")", settings.ignoreData());

		new Sync().initialize(uda);
		return uda.getAutomaton();
	}

	private Automaton loadUserDefinedAutomaton(UserDefinedAutomaton uda,
			String className, String rootDirectoryLocation) {

		if (uda == null)
			throw new NullPointerException();
		if (className == null)
			throw new NullPointerException();
		if (rootDirectoryLocation == null)
			throw new NullPointerException();

		try {
			UserDefinedInitializer
					.newInstance(className, rootDirectoryLocation).initialize(
							uda);
		}

		catch (ClassNotFoundException | IllegalAccessException
				| InstantiationException | IOException exception) {

			System.out.println("Initialization failure on class \"" + className
					+ "\" at location \"" + rootDirectoryLocation + "\"");
		}

		return uda.getAutomaton();
	}

}
