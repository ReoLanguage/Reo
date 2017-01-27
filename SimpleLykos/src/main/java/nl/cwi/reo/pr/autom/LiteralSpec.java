package nl.cwi.reo.pr.autom;

import java.util.Collections;
import java.util.List;

import nl.cwi.reo.pr.autom.TermFactory.Term;
import nl.cwi.reo.pr.misc.IdObjectSpec;

public abstract class LiteralSpec implements IdObjectSpec {
	// private final MemoryCellFactory memoryCellFactory;
	// private final AutomatonPortFactory portFactory;
	//
	// //
	// // CONSTRUCTORS
	// //
	//
	// public LiteralSpec(MemoryCellFactory memoryCellFactory,
	// AutomatonPortFactory portFactory) {
	//
	// if (memoryCellFactory == null)
	// throw new NullPointerException();
	// if (portFactory == null)
	// throw new NullPointerException();
	//
	// this.memoryCellFactory = memoryCellFactory;
	// this.portFactory = portFactory;
	// }
	//
	// //
	// // METHODS - PUBLIC
	// //
	//
	// public MemoryCellFactory getMemoryCellFactory() {
	// return memoryCellFactory;
	// }
	//
	// public AutomatonPortFactory getPortFactory() {
	// return portFactory;
	// }

	//
	// CLASSES - PUBLIC
	//

	public static class EqualitySpec extends LiteralSpec {
		private final Term argument1;
		private final Term argument2;
		private final boolean isPositive;

		//
		// CONSTRUCTORS
		//

		public EqualitySpec(boolean isPositive, Term argument1, Term argument2) {

			// super(memoryCellFactory, portFactory);

			if (argument1 == null)
				throw new NullPointerException();
			if (argument2 == null)
				throw new NullPointerException();
			// if (argument1.getMemoryCellFactory() != memoryCellFactory)
			// throw new IllegalArgumentException();
			// if (argument1.getPortFactory() != portFactory)
			// throw new IllegalArgumentException();
			// if (argument2.getMemoryCellFactory() != memoryCellFactory)
			// throw new IllegalArgumentException();
			// if (argument2.getPortFactory() != portFactory)
			// throw new IllegalArgumentException();

			this.argument1 = argument1;
			this.argument2 = argument2;
			this.isPositive = isPositive;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof EqualitySpec && equals((EqualitySpec) obj);
		}

		public boolean equals(EqualitySpec spec) {
			if (spec == null)
				throw new NullPointerException();

			return isPositive == spec.isPositive
					&& argument1.equals(spec.argument1)
					&& argument2.equals(spec.argument2);
		}

		public Term getArgument1() {
			return argument1;
		}

		public Term getArgument2() {
			return argument2;
		}

		@Override
		public int hashCode() {
			return argument1.hashCode() + argument2.hashCode();
		}

		public boolean isPositive() {
			return isPositive;
		}
	}

	public static class RelationSpec extends LiteralSpec {
		private final List<Term> arguments;
		private final Extralogical relation;
		private final boolean isPositive;

		//
		// CONSTRUCTORS
		//

		public RelationSpec(boolean isPositive, Extralogical relation,
				List<Term> arguments) {

			// super(memoryCellFactory, portFactory);

			if (relation == null)
				throw new NullPointerException();
			if (arguments == null)
				throw new NullPointerException();
			if (arguments.contains(null))
				throw new NullPointerException();
			// for (Term t : arguments)
			// if (t.getMemoryCellFactory() != memoryCellFactory)
			// throw new IllegalArgumentException();
			// for (Term t : arguments)
			// if (t.getPortFactory() != portFactory)
			// throw new IllegalArgumentException();

			this.arguments = Collections.unmodifiableList(arguments);
			this.relation = relation;
			this.isPositive = isPositive;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return super.equals(obj);
		}

		public boolean equals(RelationSpec spec) {
			if (spec == null)
				throw new NullPointerException();

			if (isPositive != spec.isPositive
					|| !relation.equals(spec.relation)
					|| arguments.size() != spec.arguments.size())

				return false;

			for (int i = 0; i < arguments.size(); i++)
				if (!arguments.get(i).equals(spec.arguments.get(i)))
					return false;

			return true;
		}

		public List<Term> getArguments() {
			return arguments;
		}

		public Extralogical getRelation() {
			return relation;
		}

		@Override
		public int hashCode() {
			return relation.hashCode();
		}

		public boolean isPositive() {
			return isPositive;
		}
	}
}