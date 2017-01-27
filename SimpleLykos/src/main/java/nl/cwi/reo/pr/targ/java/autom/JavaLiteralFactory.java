package nl.cwi.reo.pr.targ.java.autom;

import nl.cwi.reo.pr.autom.LiteralFactory;
import nl.cwi.reo.pr.autom.LiteralSpec.EqualitySpec;
import nl.cwi.reo.pr.autom.LiteralSpec.RelationSpec;
import nl.cwi.reo.pr.autom.TermFactory.Term;
import nl.cwi.reo.pr.targ.java.JavaExpression;
import nl.cwi.reo.pr.targ.java.JavaNames;

public class JavaLiteralFactory extends LiteralFactory {
	@SuppressWarnings("unused")
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaLiteralFactory(JavaTermFactory termFactory, JavaNames javaNames) {
		super(termFactory);

		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected Literal newLiteral(int id, EqualitySpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaEquality(id, spec);
	}

	@Override
	protected Literal newLiteral(int id, RelationSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaRelation(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaEquality extends Equality implements JavaExpression {

		//
		// CONSTRUCTORS
		//

		public JavaEquality(int id, EqualitySpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getExpression() {
			return (super.isPositive() ? "" : "!")
					+ ((JavaExpression) getArgument1()).getExpression()
					+ ".equals("
					+ ((JavaExpression) getArgument2()).getExpression() + ")";
		}
	}

	public class JavaRelation extends Relation implements JavaExpression {

		//
		// CONSTRUCTORS
		//

		public JavaRelation(int id, RelationSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getExpression() {
			String arguments = "";
			for (Term t : getArguments())
				arguments += ", " + ((JavaExpression) t).getExpression();

			return (super.isPositive() ? "" : "!") + getRelation().getSymbol()
					+ "(" + arguments.substring(2) + ")";
		}
	}
}
