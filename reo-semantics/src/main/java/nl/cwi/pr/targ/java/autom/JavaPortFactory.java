package nl.cwi.pr.targ.java.autom;

import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortSpec;
import nl.cwi.pr.targ.java.JavaNames;
import nl.cwi.pr.targ.java.JavaVariable;

public class JavaPortFactory extends PortFactory {
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaPortFactory(JavaNames javaNames) {
		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Port newObject(int id, PortSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaPort(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaPort extends Port implements JavaVariable {
		private String variableName;

		//
		// CONSTRUCTORS
		//

		protected JavaPort(int id, PortSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getVariableName() {
			if (variableName == null)
				variableName = javaNames.getFreshName(getName()
						.replaceAll("\\[", "\\$").replaceAll("\\]", "\\$")
						.replaceAll("\\$\\$", "\\$"));

			return variableName;
		}
	}
}
