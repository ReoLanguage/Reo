package nl.cwi.pr.tools.comp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.cwi.pr.autom.AutomatonFactory;
import nl.cwi.pr.autom.Extralogical;
import nl.cwi.pr.autom.UserDefinedAutomaton;
import nl.cwi.pr.autom.UserDefinedInitializer;
import nl.cwi.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.pr.autom.AutomatonFactory.AutomatonSet;
import nl.cwi.pr.autom.libr.Sync;
import nl.cwi.pr.misc.MemberSignature;
import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortSpec;
import nl.cwi.pr.misc.Member.Primitive;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.tools.Cancellation;
import nl.cwi.pr.tools.CompiledGeneratee;
import nl.cwi.pr.tools.CompilerProgressMonitor;
import nl.cwi.pr.tools.CompilerSettings;
import nl.cwi.pr.tools.InterpretedProtocol;

public abstract class ProtocolCompiler<C extends ProgramCompiler> extends
		GenerateeCompiler<C, InterpretedProtocol> {

	private final AutomatonSet automata;
	private final AutomatonFactory automatonFactory;

	//
	// CONSTRUCTORS
	//

	public ProtocolCompiler(C parent, InterpretedProtocol protocol,
			AutomatonFactory automatonFactory) {

		super(parent, protocol);

		if (automatonFactory == null)
			throw new NullPointerException();

		this.automata = automatonFactory.newSet();
		this.automatonFactory = automatonFactory;
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public CompiledGeneratee compile() {
		
		CompilerSettings settings = super.getSettings();
		Cancellation cancellation = null;

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
							throw new Cancellation(
									"Unpredictable neighbor detected");

				if (settings.inferQueues())
					masters.inferQueues();
				if (settings.commandify())
					automata.commandifyMasters();

				cancellation = null;
			}

			catch (Cancellation canc) {
				cancellation = canc;
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

			catch (Cancellation canc) {
				cancellation = canc;
			}

		/*
		 * Cancel
		 */

		if (cancellation != null)
			throw cancellation;

		/*
		 * Remove effectively empty automata
		 */

		for (Automaton aut : automata.getSorted())
			if (aut.getTransitions().getNonsilentSubset().isEmpty())
				automata.remove(aut);

		/*
		 * Return
		 */

		return new CompiledGeneratee(generateFiles());
	}

	public AutomatonSet getAutomata() {
		return automata;
	}

	//
	// METHODS - PRIVATE
	//

	private AutomatonSet loadSmallAutomata() {
		CompilerSettings settings = super.getSettings();
		AutomatonSet automata = automatonFactory.newSet();
		for (Primitive pr : getGeneratee().getMember().getPrimitives()) {
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
					signature.toString(), getSettings().ignoreData());

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
								.values(), signature.toString(), getSettings()
								.ignoreData());

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
						+ ";" + outputPort + ")", getSettings().ignoreData());

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

			addError("Initialization failure on class \"" + className
					+ "\" at location \"" + rootDirectoryLocation + "\"",
					exception, true);
		}

		return uda.getAutomaton();
	}
}