package nl.cwi.reo.pr.targ.java.autom;

import nl.cwi.reo.pr.autom.MemoryCellFactory;
import nl.cwi.reo.pr.autom.TermFactory;
import nl.cwi.reo.pr.autom.TermSpec.DatumSpec;
import nl.cwi.reo.pr.autom.TermSpec.FunctionSpec;
import nl.cwi.reo.pr.autom.TermSpec.PortVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.PostVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.PreVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.QuantifiedVariableSpec;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.targ.java.JavaExpression;
import nl.cwi.reo.pr.targ.java.JavaNames;
import nl.cwi.reo.pr.targ.java.JavaVariable;

public class JavaTermFactory extends TermFactory {
	@SuppressWarnings("unused")
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaTermFactory(PortFactory portFactory,
			MemoryCellFactory memoryCellFactory, JavaNames javaNames) {

		super(portFactory, memoryCellFactory);

		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Term newTerm(int id, DatumSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaDatum(id, spec);
	}

	@Override
	protected Term newTerm(int id, FunctionSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaFunction(id, spec);
	}

	@Override
	protected Term newTerm(int id, PortVariableSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaPortVariable(id, spec);
	}

	@Override
	protected Term newTerm(int id, PostVariableSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaPostVariable(id, spec);
	}

	@Override
	protected Term newTerm(int id, PreVariableSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaPreVariable(id, spec);
	}

	@Override
	protected Term newTerm(int id, QuantifiedVariableSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaQuantifiedVariable(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaDatum extends Datum implements JavaExpression {

		//
		// CONSTRUCTORS
		//

		public JavaDatum(int id, DatumSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getExpression() {
			return getDatum().getSymbol();
		}
	}

	public class JavaFunction extends Function implements JavaExpression {

		//
		// CONSTRUCTORS
		//

		public JavaFunction(int id, FunctionSpec spec) {
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

			return getFunction().getSymbol() + "("
					+ (arguments.length() > 0 ? arguments.substring(2) : "")
					+ ")";
		}
	}

	public class JavaPortVariable extends PortVariable implements
			JavaExpression, JavaVariable {

		//
		// CONSTRUCTORS
		//

		public JavaPortVariable(int id, PortVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getExpression() {
			return getVariableName() + ".getValue()";
		}

		@Override
		public String getVariableName() {
			return ((JavaVariable) getPort()).getVariableName();
		}
	}

	public class JavaPostVariable extends PostVariable implements
			JavaExpression, JavaVariable {

		//
		// CONSTRUCTORS
		//

		public JavaPostVariable(int id, PostVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getExpression() {
			return getVariableName() + ".getValue()";
		}

		@Override
		public String getVariableName() {
			return ((JavaVariable) getMemoryCell()).getVariableName() + "$post";
		}
	}

	public class JavaPreVariable extends PreVariable implements JavaExpression,
			JavaVariable {

		//
		// CONSTRUCTORS
		//

		public JavaPreVariable(int id, PreVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getExpression() {
			return getVariableName() + ".getValue()";
		}

		@Override
		public String getVariableName() {
			return ((JavaVariable) getMemoryCell()).getVariableName() + "$pre";
		}
	}

	public class JavaQuantifiedVariable extends QuantifiedVariable implements
			JavaExpression, JavaVariable {

		//
		// CONSTRUCTORS
		//

		public JavaQuantifiedVariable(int id, QuantifiedVariableSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public String getExpression() {
			return getVariableName() + ".getValue()";
		}

		@Override
		public String getVariableName() {
			return "$" + getQvid();
		}
	}
}
