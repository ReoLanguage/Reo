package nl.cwi.reo.automata;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.automata.LiteralFactory.Literal;
import nl.cwi.reo.automata.TermFactory.Term;

public abstract class Command {

	//
	// METHODS
	//

	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("ASSIGNMENT", isAssignment());
		properties.put("EXPORT", isExport());
		properties.put("GUARDED_FAILURE", isGuardedFailure());
		properties.put("IMPORT", isImport());

		return properties;
	}

	public boolean isAssignment() {
		return this instanceof Assignment;
	}

	public boolean isExport() {
		return this instanceof Export;
	}

	public boolean isGuardedFailure() {
		return this instanceof GuardedFailure;
	}

	public boolean isImport() {
		return this instanceof Import;
	}

	//
	// CLASSES
	//

	public static class Assignment extends Command {
		private Term variable;
		private Term definition;

		//
		// CONSTRUCTORS
		//

		public Assignment(Term variable, Term definition) {
			if (variable == null)
				throw new NullPointerException();
			if (definition == null)
				throw new NullPointerException();

			this.variable = variable;
			this.definition = definition;
		}

		//
		// METHODS
		//

		public Term getDefinition() {
			return definition;
		}

		public Term getVariable() {
			return variable;
		}

		@Override
		public String toString() {
			return variable + " := " + definition;
		}
	}

	public static class Export extends Command {
		private Term variable;

		//
		// CONSTRUCTORS
		//

		public Export(Term variable) {
			if (variable == null)
				throw new NullPointerException();

			this.variable = variable;
		}

		//
		// METHODS
		//

		public Term getVariable() {
			return variable;
		}
		
		@Override
		public String toString() {
			return "export " + variable;
		}
	}

	public static class GuardedFailure extends Command {
		private Literal guard;

		//
		// CONSTRUCTORS
		//

		public GuardedFailure(Literal guard) {
			if (guard == null)
				throw new NullPointerException();

			this.guard = guard;
		}

		//
		// METHODS
		//

		public Literal getGuard() {
			return guard;
		}

		@Override
		public String toString() {
			return "if " + guard + " -> skip";
		}
	}

	public static class Import extends Command {
		private Term variable;

		//
		// CONSTRUCTORS
		//

		public Import(Term variable) {
			if (variable == null)
				throw new NullPointerException();

			this.variable = variable;
		}

		//
		// METHODS
		//

		public Term getVariable() {
			return variable;
		}
		
		@Override
		public String toString() {
			return "import " + variable;
		}
	}
}