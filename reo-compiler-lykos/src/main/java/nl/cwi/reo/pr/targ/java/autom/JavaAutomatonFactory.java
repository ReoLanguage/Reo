package nl.cwi.reo.pr.targ.java.autom;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.cwi.reo.pr.autom.AutomatonFactory;
import nl.cwi.reo.pr.autom.AutomatonSpec;
import nl.cwi.reo.pr.autom.ConstraintFactory;
import nl.cwi.reo.pr.autom.LiteralFactory;
import nl.cwi.reo.pr.autom.MemoryCellFactory;
import nl.cwi.reo.pr.autom.StateFactory;
import nl.cwi.reo.pr.autom.TermFactory;
import nl.cwi.reo.pr.autom.TransitionFactory;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.targ.java.JavaClass;
import nl.cwi.reo.pr.targ.java.JavaNames;
import nl.cwi.reo.pr.targ.java.JavaVariable;

public class JavaAutomatonFactory extends AutomatonFactory {
	private JavaNames javaNames;

	//
	// METHODS - PUBLIC
	//

	public JavaNames getJavaNames() {
		if (javaNames == null)
			javaNames = new JavaNames();

		return javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected ConstraintFactory newConstraintFactory(
			LiteralFactory literalFactory) {

		if (literalFactory == null)
			throw new NullPointerException();
		if (!(literalFactory instanceof JavaLiteralFactory))
			throw new IllegalArgumentException();

		return new JavaConstraintFactory((JavaLiteralFactory) literalFactory,
				getJavaNames());
	}

	@Override
	protected LiteralFactory newLiteralFactory(TermFactory termFactory) {
		if (termFactory == null)
			throw new NullPointerException();
		if (!(termFactory instanceof JavaTermFactory))
			throw new IllegalArgumentException();

		return new JavaLiteralFactory((JavaTermFactory) termFactory,
				getJavaNames());
	}

	@Override
	protected MemoryCellFactory newMemoryCellFactory() {
		return new JavaMemoryCellFactory(getJavaNames());
	}

	@Override
	protected Automaton newObject(int id, AutomatonSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaAutomaton(id, spec);
	}

	@Override
	protected PortFactory newPortFactory() {
		return new JavaPortFactory(getJavaNames());
	}

	@Override
	protected TermFactory newTermFactory(PortFactory portFactory,
			MemoryCellFactory memoryCellFactory) {

		return new JavaTermFactory(portFactory, memoryCellFactory,
				getJavaNames());
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaAutomaton extends Automaton implements JavaClass,
			JavaVariable {

		private String className;
		private String variableName;

		//
		// CONSTRUCTORS
		//

		public JavaAutomaton(int id, AutomatonSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public Map<Port, Map<String, Object>> getMaskPerPort() {
			Map<Port, Map<String, Object>> portBitIndices = new HashMap<>();
			for (Entry<Port, Integer> entr : getIndexPerPublicPort().entrySet()) {
				Integer index = entr.getValue();
				Map<String, Object> bitIndex = new HashMap<>();
				bitIndex.put("wordIndex", index / 32);
				bitIndex.put(
						"mask",
						"0b"
								+ String.format(
										"%32s",
										Long.toBinaryString((long) Math.pow(2,
												index % 32))).replace(' ', '0'));

				portBitIndices.put(entr.getKey(), bitIndex);
			}

			return portBitIndices;
		}

		@Override
		public String getClassName() {
			if (className == null)
				className = javaNames.getFreshName("Automaton" + getId());

			return className;
		}

		@Override
		public String getVariableName() {
			if (variableName == null)
				variableName = javaNames.getFreshName("automaton" + getId());

			return variableName;
		}

		//
		// METHODS - PROTECTED
		//

		@Override
		protected StateFactory newStateFactory() {
			return new JavaStateFactory(javaNames);
		}

		@Override
		protected TransitionFactory newTransitionFactory() {
			return new JavaTransitionFactory(javaNames);
		}
	}
}
