package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.pr.misc.IdObjectFactory;
import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortSet;
import nl.cwi.reo.automata.MemoryCellFactory.MemoryCell;
import nl.cwi.reo.automata.MemoryCellFactory.MemoryCellSet;
import nl.cwi.reo.automata.TermFactory.Term;
import nl.cwi.reo.automata.TermFactory.TermSet;
import nl.cwi.reo.automata.TermSpec.DatumSpec;
import nl.cwi.reo.automata.TermSpec.FunctionSpec;
import nl.cwi.reo.automata.TermSpec.PortVariableSpec;
import nl.cwi.reo.automata.TermSpec.PostVariableSpec;
import nl.cwi.reo.automata.TermSpec.PreVariableSpec;
import nl.cwi.reo.automata.TermSpec.QuantifiedVariableSpec;

public abstract class TermFactory extends
		IdObjectFactory<Term, TermSet, TermSpec> {

	private final MemoryCellFactory memoryCellFactory;
	private final PortFactory portFactory;

	//
	// CONSTRUCTORS
	//

	public TermFactory(PortFactory portFactory,
			MemoryCellFactory memoryCellFactory) {

		if (portFactory == null)
			throw new NullPointerException();
		if (memoryCellFactory == null)
			throw new NullPointerException();

		this.memoryCellFactory = memoryCellFactory;
		this.portFactory = portFactory;
	}

	//
	// METHODS - PUBLIC
	//

	public MemoryCellFactory getMemoryCellFactory() {
		return memoryCellFactory;
	}

	public PortFactory getPortFactory() {
		return portFactory;
	}

	@Override
	public TermSet newSet() {
		return new TermSet();
	}

	public Term substitute(Term term, Map<Term, Term> substitutions) {
		if (term == null)
			throw new NullPointerException();
		if (substitutions == null)
			throw new NullPointerException();
		if (substitutions.containsKey(null))
			throw new NullPointerException();
		if (substitutions.containsValue(null))
			throw new NullPointerException();

		if (term.getFactory() != this)
			throw new IllegalStateException();

		if (substitutions.containsKey(term))
			return substitutions.get(term);

		if (term instanceof Function) {
			Function function = (Function) term;
			List<Term> arguments = new ArrayList<>();
			for (Term t : function.getArguments())
				arguments.add(substitute(t, substitutions));

			return newOrGet(new FunctionSpec(function.getFunction(), arguments));
		}

		return term;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Term newObject(int id, TermSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		if (spec instanceof DatumSpec)
			return newTerm(id, (DatumSpec) spec);
		if (spec instanceof FunctionSpec)
			return newTerm(id, (FunctionSpec) spec);
		if (spec instanceof PortVariableSpec)
			return newTerm(id, (PortVariableSpec) spec);
		if (spec instanceof PostVariableSpec)
			return newTerm(id, (PostVariableSpec) spec);
		if (spec instanceof PreVariableSpec)
			return newTerm(id, (PreVariableSpec) spec);
		if (spec instanceof QuantifiedVariableSpec)
			return newTerm(id, (QuantifiedVariableSpec) spec);

		throw new Error();
	}

	protected abstract Term newTerm(int id, DatumSpec spec);

	protected abstract Term newTerm(int id, FunctionSpec spec);

	protected abstract Term newTerm(int id, PortVariableSpec spec);

	protected abstract Term newTerm(int id, PostVariableSpec spec);

	protected abstract Term newTerm(int id, PreVariableSpec spec);

	protected abstract Term newTerm(int id, QuantifiedVariableSpec spec);

	//
	// CLASSES - PUBLIC
	//

	public class Datum extends Term {

		//
		// CONSTRUCTORS
		//

		public Datum(int id, DatumSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public Extralogical getDatum() {
			return ((DatumSpec) getSpec()).getDatum();
		}

		@Override
		public PortSet getPorts() {
			return getPortFactory().newSet();
		}

		@Override
		public MemoryCellSet getPostMemoryCells() {
			return getMemoryCellFactory().newSet();
		}

		@Override
		public MemoryCellSet getPreMemoryCells() {
			return getMemoryCellFactory().newSet();
		}

		@Override
		public Collection<Integer> getQuantifiedVariableIds() {
			return Collections.emptyList();
		}

		@Override
		public TermSet getVariables() {
			return getFactory().newSet();
		}

		@Override
		public String toString() {
			return getDatum().toString();
		}
	}

	public class Function extends Term {

		//
		// CONSTRUCTORS
		//

		public Function(int id, FunctionSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public List<Term> getArguments() {
			return ((FunctionSpec) getSpec()).getArguments();
		}

		public Extralogical getFunction() {
			return ((FunctionSpec) getSpec()).getFunction();
		}

		@Override
		public PortSet getPorts() {
			PortSet ports = getPortFactory().newSet();
			for (Term t : getArguments())
				ports.addAll(t.getPorts());

			return ports;
		}

		@Override
		public MemoryCellSet getPostMemoryCells() {
			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			for (Term t : getArguments())
				memoryCells.addAll(t.getPostMemoryCells());

			return memoryCells;
		}

		@Override
		public MemoryCellSet getPreMemoryCells() {
			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			for (Term t : getArguments())
				memoryCells.addAll(t.getPreMemoryCells());

			return memoryCells;
		}

		@Override
		public Collection<Integer> getQuantifiedVariableIds() {
			Set<Integer> ids = new HashSet<>();
			for (Term t : getArguments())
				ids.addAll(t.getQuantifiedVariableIds());

			return ids;
		}

		@Override
		public TermSet getVariables() {
			TermSet variables = getFactory().newSet();
			for (Term t : getArguments())
				variables.addAll(t.getVariables());

			return variables;
		}

		@Override
		public String toString() {
			String string = "";
			for (Term t : getArguments())
				string += "," + t.toString();

			return getFunction() + "("
					+ (string.isEmpty() ? "" : string.substring(1)) + ")";
		}
	}

	public class PortVariable extends Variable {

		//
		// CONSTRUCTORS
		//

		public PortVariable(int id, PortVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public Port getPort() {
			return ((PortVariableSpec) getSpec()).getPort();
		}

		@Override
		public PortSet getPorts() {
			PortSet ports = getPortFactory().newSet();
			ports.add(getPort());
			return ports;
		}

		@Override
		public String toString() {
			return getPort().toString();
		}
	}

	public class PostVariable extends Variable {

		//
		// CONSTRUCTORS
		//

		public PostVariable(int id, PostVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public MemoryCell getMemoryCell() {
			return ((PostVariableSpec) getSpec()).getMemoryCell();
		}

		@Override
		public MemoryCellSet getPostMemoryCells() {
			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			memoryCells.add(getMemoryCell());
			return memoryCells;
		}

		@Override
		public String toString() {
			return "mem" + getMemoryCell().toString() + "*";
		}
	}

	public class PreVariable extends Variable {

		//
		// CONSTRUCTORS
		//

		public PreVariable(int id, PreVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public MemoryCell getMemoryCell() {
			return ((PreVariableSpec) getSpec()).getMemoryCell();
		}

		@Override
		public MemoryCellSet getPreMemoryCells() {
			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			memoryCells.add(getMemoryCell());
			return memoryCells;
		}

		@Override
		public String toString() {
			return "*mem" + getMemoryCell().toString();
		}
	}

	public class QuantifiedVariable extends Variable {

		//
		// CONSTRUCTORS
		//

		public QuantifiedVariable(int id, QuantifiedVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public int getQvid() {
			return ((QuantifiedVariableSpec) getSpec()).getId();
		}

		@Override
		public Collection<Integer> getQuantifiedVariableIds() {
			return Arrays.asList(getQvid());
		}

		@Override
		public String toString() {
			return "%" + getQvid();
		}
	}

	public abstract class Term extends
			IdObjectFactory<Term, TermSet, TermSpec>.IdObject {

		//
		// CONSTRUCTORS
		//

		protected Term(int id, TermSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public abstract PortSet getPorts();

		public abstract MemoryCellSet getPostMemoryCells();

		public abstract MemoryCellSet getPreMemoryCells();

		public Map<String, Object> getProperties() {
			Map<String, Object> properties = new HashMap<>();
			properties.put("DATUM", isDatum());
			properties.put("FUNCTION", isFunction());
			properties.put("PORT_VARIABLE", isPortVariable());
			properties.put("POST_VARIABLE", isPostVariable());
			properties.put("PRE_VARIABLE", isPreVariable());
			properties.put("QUANTIFIED_VARIABLE", isQuantifiedVariable());
			properties.put("VARIABLE", isVariable());

			return properties;
		}

		public abstract Collection<Integer> getQuantifiedVariableIds();

		public abstract TermSet getVariables();

		public boolean isDatum() {
			return this instanceof Datum;
		}

		public boolean isFunction() {
			return this instanceof Function;
		}

		public boolean isPortVariable() {
			return this instanceof PortVariable;
		}

		public boolean isPostVariable() {
			return this instanceof PostVariable;
		}

		public boolean isPreVariable() {
			return this instanceof PreVariable;
		}

		public boolean isQuantifiedVariable() {
			return this instanceof QuantifiedVariable;
		}

		public boolean isVariable() {
			return this instanceof Variable;
		}
	}

	public class TermSet extends
			IdObjectFactory<Term, TermSet, TermSpec>.IdObjectSet {
	}

	public abstract class Variable extends Term {

		protected Variable(int id, TermSpec spec) {
			super(id, spec);
		}

		@Override
		public PortSet getPorts() {
			return getPortFactory().newSet();
		}

		@Override
		public MemoryCellSet getPostMemoryCells() {
			return getMemoryCellFactory().newSet();
		}

		@Override
		public MemoryCellSet getPreMemoryCells() {
			return getMemoryCellFactory().newSet();
		}

		@Override
		public Collection<Integer> getQuantifiedVariableIds() {
			return Collections.emptyList();
		}

		@Override
		public TermSet getVariables() {
			TermSet variables = getFactory().newSet();
			variables.add(this);
			return variables;
		}
	}
}
