package nl.cwi.reo.pr.targ.java.autom;

import nl.cwi.reo.pr.autom.MemoryCellFactory;
import nl.cwi.reo.pr.autom.MemoryCellSpec;
import nl.cwi.reo.pr.targ.java.JavaNames;
import nl.cwi.reo.pr.targ.java.JavaVariable;

public class JavaMemoryCellFactory extends MemoryCellFactory {
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaMemoryCellFactory(JavaNames javaNames) {
		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected MemoryCell newObject(int id, MemoryCellSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaMemoryCell(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaMemoryCell extends MemoryCell implements JavaVariable {
		private String variableName;

		//
		// CONSTRUCTORS
		//

		protected JavaMemoryCell(int id, MemoryCellSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getVariableName() {
			if (variableName == null)
				variableName = javaNames.getFreshName("memoryCell" + getId());

			return variableName;
		}
	}
}
