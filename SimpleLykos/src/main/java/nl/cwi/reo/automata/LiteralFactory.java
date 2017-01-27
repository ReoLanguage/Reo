package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.pr.misc.IdObjectFactory;
import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortFactory.PortSet;
import nl.cwi.reo.automata.LiteralFactory.Literal;
import nl.cwi.reo.automata.LiteralFactory.LiteralSet;
import nl.cwi.reo.automata.LiteralSpec.EqualitySpec;
import nl.cwi.reo.automata.LiteralSpec.RelationSpec;
import nl.cwi.reo.automata.MemoryCellFactory.MemoryCellSet;
import nl.cwi.reo.automata.TermFactory.Datum;
import nl.cwi.reo.automata.TermFactory.Term;
import nl.cwi.reo.automata.TermFactory.TermSet;

public abstract class LiteralFactory extends
		IdObjectFactory<Literal, LiteralSet, LiteralSpec> {

	private final TermFactory termFactory;

	//
	// CONSTRUCTORS
	//

	public LiteralFactory(TermFactory termFactory) {
		if (termFactory == null)
			throw new NullPointerException();

		this.termFactory = termFactory;
	}

	//
	// METHODS - PUBLIC
	//

	public MemoryCellFactory getMemoryCellFactory() {
		return termFactory.getMemoryCellFactory();
	}

	public PortFactory getPortFactory() {
		return termFactory.getPortFactory();
	}

	public TermFactory getTermFactory() {
		return termFactory;
	}

	@Override
	public LiteralSet newSet() {
		return new LiteralSet();
	}

	public Literal substitute(Literal literal, Map<Term, Term> substitutions) {
		if (literal == null)
			throw new NullPointerException();
		if (substitutions == null)
			throw new NullPointerException();
		if (substitutions.containsKey(null))
			throw new NullPointerException();
		if (substitutions.containsKey(null))
			throw new NullPointerException();

		if (literal.getFactory() != this)
			throw new IllegalStateException();

		if (literal instanceof Equality) {
			Equality equality = (Equality) literal;
			return newOrGet(new EqualitySpec(equality.isPositive(),
					termFactory.substitute(equality.getArgument1(),
							substitutions), termFactory.substitute(
							equality.getArgument2(), substitutions)));
		}

		if (literal instanceof Relation) {
			Relation relation = (Relation) literal;
			List<Term> arguments = new ArrayList<>();
			for (Term t : relation.getArguments())
				arguments.add(termFactory.substitute(t, substitutions));

			return newOrGet(new RelationSpec(relation.isPositive(),
					relation.getRelation(), arguments));
		}

		return literal;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Literal newObject(int id, LiteralSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		if (spec instanceof EqualitySpec)
			return newLiteral(id, (EqualitySpec) spec);
		if (spec instanceof RelationSpec)
			return newLiteral(id, (RelationSpec) spec);
		else
			throw new Error();
	}

	protected abstract Literal newLiteral(int id, EqualitySpec spec);

	protected abstract Literal newLiteral(int id, RelationSpec spec);

	//
	// CLASSES - PUBLIC
	//

	public class Equality extends Literal {

		//
		// CONSTRUCTORS
		//

		public Equality(int id, EqualitySpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public Term getArgument1() {
			return ((EqualitySpec) getSpec()).getArgument1();
		}

		public Term getArgument2() {
			return ((EqualitySpec) getSpec()).getArgument2();
		}

		@Override
		public PortSet getPorts() {
			PortSet ports = getPortFactory().newSet();
			ports.addAll(getArgument1().getPorts());
			ports.addAll(getArgument2().getPorts());
			return ports;
		}

		@Override
		public MemoryCellSet getPostMemoryCells() {
			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			memoryCells.addAll(getArgument1().getPostMemoryCells());
			memoryCells.addAll(getArgument2().getPostMemoryCells());
			return memoryCells;
		}

		@Override
		public MemoryCellSet getPreMemoryCells() {
			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			memoryCells.addAll(getArgument1().getPreMemoryCells());
			memoryCells.addAll(getArgument2().getPreMemoryCells());
			return memoryCells;
		}

		@Override
		public Collection<Integer> getQuantifiedVariableIds() {
			Set<Integer> quantifiedVariableIds = new HashSet<>();

			quantifiedVariableIds.addAll(getArgument1()
					.getQuantifiedVariableIds());
			quantifiedVariableIds.addAll(getArgument2()
					.getQuantifiedVariableIds());

			return quantifiedVariableIds;
		}

		@Override
		public TermSet getVariables() {
			TermSet variables = getTermFactory().newSet();
			variables.addAll(getArgument1().getVariables());
			variables.addAll(getArgument2().getVariables());
			return variables;
		}

		@Override
		public Collection<Datum> getStaticDomain() {
			Set<Datum> domain = new HashSet<>();
			if (isPositive()) {
				if (getArgument1() instanceof Datum)
					domain.add((Datum) getArgument1());
				if (getArgument2() instanceof Datum)
					domain.add((Datum) getArgument2());
			}

			return domain;
		}

		public boolean isPositive() {
			return ((EqualitySpec) getSpec()).isPositive();
		}

		@Override
		public String toString() {
			return getArgument1() + (isPositive() ? "=" : "!") + "="
					+ getArgument2();
		}
	}

	public class Relation extends Literal {

		//
		// CONSTRUCTORS
		//

		public Relation(int id, RelationSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public List<Term> getArguments() {
			return ((RelationSpec) getSpec()).getArguments();
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
			Set<Integer> quantifiedVariableIds = new HashSet<>();
			for (Term t : getArguments())
				quantifiedVariableIds.addAll(t.getQuantifiedVariableIds());

			return quantifiedVariableIds;
		}

		public Extralogical getRelation() {
			return ((RelationSpec) getSpec()).getRelation();
		}

		@Override
		public Collection<Datum> getStaticDomain() {
			return Collections.emptySet();
		}

		@Override
		public TermSet getVariables() {
			TermSet variables = getTermFactory().newSet();
			for (Term t : getArguments())
				variables.addAll(t.getVariables());

			return variables;
		}

		public boolean isPositive() {
			return ((RelationSpec) getSpec()).isPositive();
		}

		@Override
		public String toString() {
			String string = "";
			for (Term t : getArguments())
				string += "," + t.toString();

			return (isPositive() ? "" : "!") + getRelation() + "("
					+ (string.isEmpty() ? "" : string.substring(1)) + ")";
		}
	}

	public abstract class Literal extends
			IdObjectFactory<Literal, LiteralSet, LiteralSpec>.IdObject {

		//
		// CONSTRUCTORS
		//

		protected Literal(int id, LiteralSpec spec) {
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
			properties.put("EQUALITY", isEquality());
			properties.put("RELATION", isRelation());

			return properties;
		}

		public abstract Collection<Integer> getQuantifiedVariableIds();

		public abstract Collection<Datum> getStaticDomain();

		public abstract TermSet getVariables();

		public boolean isEquality() {
			return this instanceof Equality;
		}

		public boolean isPositiveEquality() {
			return isEquality() && ((Equality) this).isPositive();
		}

		public boolean isRelation() {
			return this instanceof Relation;
		}

		public boolean isTautology() {
			if (!isPositiveEquality())
				return false;

			Equality equality = (Equality) this;
			return equality.getArgument1().equals(equality.getArgument2());
		}
	}

	public class LiteralSet extends
			IdObjectFactory<Literal, LiteralSet, LiteralSpec>.IdObjectSet {
	}
}
