package nl.cwi.pr.targ.java.autom;

import nl.cwi.pr.misc.MainArgumentFactory;
import nl.cwi.pr.misc.MainArgumentSpec;
import nl.cwi.pr.targ.java.JavaNames;

public class JavaMainArgumentFactory extends MainArgumentFactory {
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaMainArgumentFactory(JavaNames javaNames) {
		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected MainArgument newObject(int id, MainArgumentSpec spec) {
		if (spec == null)
			throw new NullPointerException();
		
		return new JavaVariable(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaVariable extends MainArgument implements
			nl.cwi.pr.targ.java.JavaVariable {

		private String variableName;

		//
		// CONSTRUCTORS
		//

		public JavaVariable(int id, MainArgumentSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getVariableName() {
			if (variableName == null)
				variableName = javaNames.getFreshName(getName());

			return variableName;
		}
	}
}
