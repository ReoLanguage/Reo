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
import nl.cwi.reo.pr.misc.MemberSignature;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortSpec;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.targ.java.autom.JavaAutomatonFactory;
import nl.cwi.reo.pr.targ.java.autom.Member.Primitive;




public class Automata {
	
	private final AutomatonSet automata;
	private final AutomatonFactory automatonFactory;
	private final CompilerSettings settings;
	private final List<Primitive> ls;

	public Automata(CompilerSettings settings,List<Primitive> ls) {


		this.automatonFactory = new JavaAutomatonFactory();
		this.automata = automatonFactory.newSet();
		this.settings=settings;
		this.ls=ls;
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
/*

constraintFactory	JavaConstraintFactory  (id=106)	
ids	HashMap<K,V>  (id=110)	
javaNames	JavaNames  (id=112)	
literalFactory	JavaLiteralFactory  (id=114)	
memoryCellFactory	JavaMemoryCellFactory  (id=117)	
nextId	13	
nStatesMax	2048	
nTransitionsMax	2048	
portFactory	JavaPortFactory  (id=120)	
specs	HashMap<K,V>  (id=122)	
termFactory	JavaTermFactory  (id=126)
	
nl.cwi.reo.pr.targ.java.autom.JavaConstraintFactory@1c93084c
{2=/Sync(a$1;$ina$1) :: q1, <q1,[],a$1==$ina$1,q1>, 3=/Sync($outx$1;x$1) :: q1, <q1,[],$outx$1==x$1,q1>, 4=FifoK(a$1;x$1) :: null, , 6=/Sync(x$1;$inx$1) :: q1, <q1,[],x$1==$inx$1,q1>, 7=/Sync($outy$1;y$1) :: q1, <q1,[],$outy$1==y$1,q1>, 8=FifoK(x$1;y$1) :: null, , 10=/Sync(y$1;$iny$1) :: q1, <q1,[],y$1==$iny$1,q1>, 11=/Sync($outb$1;b$1) :: q1, <q1,[],$outb$1==b$1,q1>, 12=FifoK(y$1;b$1) :: null, }
nl.cwi.reo.pr.targ.java.JavaNames@6ef888f6
nl.cwi.reo.pr.targ.java.autom.JavaLiteralFactory@10e92f8f
nl.cwi.reo.pr.targ.java.autom.JavaMemoryCellFactory@78b66d36
13
2048
2048
nl.cwi.reo.pr.targ.java.autom.JavaPortFactory@5223e5ee
{FifoK(a$1;x$1)=FifoK(a$1;x$1) :: null, , /Sync($outy$1;y$1)=/Sync($outy$1;y$1) :: q1, <q1,[],$outy$1==y$1,q1>, /Sync(x$1;$inx$1)=/Sync(x$1;$inx$1) :: q1, <q1,[],x$1==$inx$1,q1>, FifoK(x$1;y$1)=FifoK(x$1;y$1) :: null, , /Sync(a$1;$ina$1)=/Sync(a$1;$ina$1) :: q1, <q1,[],a$1==$ina$1,q1>, /Sync(y$1;$iny$1)=/Sync(y$1;$iny$1) :: q1, <q1,[],y$1==$iny$1,q1>, /Sync($outb$1;b$1)=/Sync($outb$1;b$1) :: q1, <q1,[],$outb$1==b$1,q1>, /Sync($outx$1;x$1)=/Sync($outx$1;x$1) :: q1, <q1,[],$outx$1==x$1,q1>, FifoK(y$1;b$1)=FifoK(y$1;b$1) :: null, }
nl.cwi.reo.pr.targ.java.autom.JavaTermFactory@7ce3cb8e


nl.cwi.pr.targ.java.autom.JavaConstraintFactory@2f67a4d3
{2=/Sync(A$1;$inA$1) :: q1, <q1,[$inA$1,A$1],A$1==$inA$1,q1>, 3=/Sync($outB$1;B$1) :: q1, <q1,[$outB$1,B$1],$outB$1==B$1,q1>, 4=Fifo(A$1;B$1) :: q1, <q1,[$inA$1],$inA$1==mem2*,q2>, <q2,[$outB$1],$outB$1==*mem2,q1>}
nl.cwi.pr.targ.java.JavaNames@5e3f861
nl.cwi.pr.targ.java.autom.JavaLiteralFactory@2fb0623e
nl.cwi.pr.targ.java.autom.JavaMemoryCellFactory@49b2a47d
5
2048
2048
nl.cwi.pr.targ.java.autom.JavaPortFactory@5be1d0a4
{/Sync(A$1;$inA$1)=/Sync(A$1;$inA$1) :: q1, <q1,[$inA$1,A$1],A$1==$inA$1,q1>, Fifo(A$1;B$1)=Fifo(A$1;B$1) :: q1, <q1,[$inA$1],$inA$1==mem2*,q2>, <q2,[$outB$1],$outB$1==*mem2,q1>, /Sync($outB$1;B$1)=/Sync($outB$1;B$1) :: q1, <q1,[$outB$1,B$1],$outB$1==B$1,q1>}
nl.cwi.pr.targ.java.autom.JavaTermFactory@415b0b49

*/

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
		
		for (Primitive pr : ls) {
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
