package nl.cwi.reo.pr.targ.java.autom;

import nl.cwi.reo.pr.autom.StateFactory;
import nl.cwi.reo.pr.autom.StateSpec;
import nl.cwi.reo.pr.targ.java.JavaClass;
import nl.cwi.reo.pr.targ.java.JavaNames;
import nl.cwi.reo.pr.targ.java.JavaVariable;

public class JavaStateFactory extends StateFactory {
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaStateFactory(JavaNames javaNames) {
		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected State newObject(int id, StateSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaState(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaState extends State implements JavaClass, JavaVariable {
		private String className;
		private String variableName;

		//
		// CONSTRUCTORS
		//

		protected JavaState(int id, StateSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getClassName() {
			if (className == null)
				className = javaNames.getFreshName("State" + getId());

			return className;
		}

		@Override
		public String getVariableName() {
			if (variableName == null)
				variableName = javaNames.getFreshName("state" + getId());

			return variableName;
		}
	}
}
