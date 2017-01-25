package nl.cwi.pr.autom;

import java.util.Collections;
import java.util.List;

import nl.cwi.pr.autom.MemoryCellFactory.MemoryCell;
import nl.cwi.pr.autom.TermFactory.Term;
import nl.cwi.pr.misc.IdObjectSpec;
import nl.cwi.pr.misc.PortFactory.Port;

public abstract class TermSpec implements IdObjectSpec {

	//
	// STATIC - CLASSES - PUBLIC
	//

	public static class DatumSpec extends TermSpec {
		private final Extralogical datum;

		//
		// CONSTRUCTORS
		//

		public DatumSpec(Extralogical datum) {
			if (datum == null)
				throw new NullPointerException();

			this.datum = datum;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof DatumSpec && equals((DatumSpec) obj);
		}

		public boolean equals(DatumSpec spec) {
			if (spec == null)
				throw new NullPointerException();

			return datum.equals(spec.datum);
		}

		@Override
		public int hashCode() {
			return datum.hashCode();
		}

		public Extralogical getDatum() {
			return datum;
		}
	}

	public static class FunctionSpec extends TermSpec {
		private final List<Term> arguments;
		private final Extralogical function;

		//
		// CONSTRUCTORS
		//

		public FunctionSpec(Extralogical function, List<Term> arguments) {
			if (function == null)
				throw new NullPointerException();
			if (arguments == null)
				throw new NullPointerException();
			if (arguments.contains(null))
				throw new NullPointerException();

			this.arguments = Collections.unmodifiableList(arguments);
			this.function = function;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof FunctionSpec && equals((FunctionSpec) obj);
		}

		public boolean equals(FunctionSpec spec) {
			if (spec == null)
				throw new NullPointerException();

			if (!function.equals(spec.function)
					|| arguments.size() != spec.arguments.size())

				return false;

			for (int i = 0; i < arguments.size(); i++)
				if (!arguments.get(i).equals(spec.arguments.get(i)))
					return false;

			return true;
		}

		@Override
		public int hashCode() {
			return function.hashCode();
		}

		public List<Term> getArguments() {
			return arguments;
		}

		public Extralogical getFunction() {
			return function;
		}
	}

	public static class PortVariableSpec extends TermSpec {
		private final Port port;

		//
		// CONSTRUCTORS
		//

		public PortVariableSpec(Port port) {
			if (port == null)
				throw new NullPointerException();

			this.port = port;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof PortVariableSpec
					&& equals((PortVariableSpec) obj);
		}

		public boolean equals(PortVariableSpec spec) {
			if (spec == null)
				throw new NullPointerException();

			return port.equals(spec.port);
		}

		@Override
		public int hashCode() {
			return port.hashCode();
		}

		public Port getPort() {
			return port;
		}
	}

	public static class PostVariableSpec extends TermSpec {
		private final MemoryCell memoryCell;

		//
		// CONSTRUCTORS
		//

		public PostVariableSpec(MemoryCell memoryCell) {
			if (memoryCell == null)
				throw new NullPointerException();

			this.memoryCell = memoryCell;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof PostVariableSpec
					&& equals((PostVariableSpec) obj);
		}

		public boolean equals(PostVariableSpec spec) {
			if (spec == null)
				throw new NullPointerException();

			return memoryCell.equals(spec.memoryCell);
		}

		@Override
		public int hashCode() {
			return memoryCell.hashCode();
		}

		public MemoryCell getMemoryCell() {
			return memoryCell;
		}
	}

	public static class PreVariableSpec extends TermSpec {
		private final MemoryCell memoryCell;

		//
		// CONSTRUCTORS
		//

		public PreVariableSpec(MemoryCell memoryCell) {
			if (memoryCell == null)
				throw new NullPointerException();

			this.memoryCell = memoryCell;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof PreVariableSpec
					&& equals((PreVariableSpec) obj);
		}

		public boolean equals(PreVariableSpec spec) {
			if (spec == null)
				throw new NullPointerException();

			return memoryCell.equals(spec.memoryCell);
		}

		@Override
		public int hashCode() {
			return memoryCell.hashCode();
		}

		public MemoryCell getMemoryCell() {
			return memoryCell;
		}
	}

	public static class QuantifiedVariableSpec extends TermSpec {
		private final int id = nextId++;

		//
		// METHODS - PUBLIC
		//

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof QuantifiedVariableSpec
					&& equals((QuantifiedVariableSpec) obj);
		}

		public boolean equals(QuantifiedVariableSpec spec) {
			if (spec == null)
				throw new NullPointerException();

			return id == spec.id;
		}

		@Override
		public int hashCode() {
			return id;
		}

		public int getId() {
			return id;
		}

		//
		// STATIC
		//

		private static int nextId = 0;
	}
}