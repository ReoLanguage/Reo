package nl.cwi.pr.targ.java.autom;

import nl.cwi.pr.autom.ConstraintFactory;
import nl.cwi.pr.autom.ConstraintSpec;
import nl.cwi.pr.targ.java.JavaNames;

public class JavaConstraintFactory extends ConstraintFactory {
	@SuppressWarnings("unused")
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaConstraintFactory(JavaLiteralFactory literalFactory,
			JavaNames javaNames) {

		super(literalFactory);

		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Constraint newObject(int id, ConstraintSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaConstraint(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaConstraint extends Constraint {

		//
		// CONSTRUCTORS
		//

		protected JavaConstraint(int id, ConstraintSpec spec) {
			super(id, spec);
		}
	}
}
