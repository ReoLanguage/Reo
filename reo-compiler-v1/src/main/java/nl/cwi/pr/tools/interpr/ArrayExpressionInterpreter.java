package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.misc.Array;
import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.IntegerDomain;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.ArrayExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.ArrayExpressionExplicitContext;
import nl.cwi.pr.tools.pars.PrParser.ArrayExpressionImplicitContext;
import nl.cwi.pr.tools.pars.PrParser.PortExpressionContext;

public abstract class ArrayExpressionInterpreter extends
		Interpreter<ArrayExpressionContext, Array> {

	//
	// CONSTRUCTORS
	//

	public ArrayExpressionInterpreter(Interpreter<?, ?> parent,
			ArrayExpressionContext context) {

		super(parent, context);
	}

	//
	// STATIC - METHODS
	//

	public static ArrayExpressionInterpreter newInstance(
			Interpreter<?, ?> parent, ArrayExpressionContext context) {

		if (parent == null)
			throw new NullPointerException();
		if (context == null)
			throw new NullPointerException();

		if (context instanceof ArrayExpressionImplicitContext)
			return new Implicit(parent,
					(ArrayExpressionImplicitContext) context);

		else if (context instanceof ArrayExpressionExplicitContext)
			return new Explicit(parent,
					(ArrayExpressionExplicitContext) context);

		else
			throw new Error();
	}

	//
	// STATIC - CLASSES
	//

	public static class Implicit extends ArrayExpressionInterpreter {

		//
		// FIELDS
		//

		private final IntegerDomainInterpreter integerDomainInterpreter;
		private final TypedName arrayOrArrayName;

		//
		// CONSTRUCTORS
		//

		public Implicit(Interpreter<?, ?> parent,
				ArrayExpressionImplicitContext context) {

			super(parent, context);

			this.integerDomainInterpreter = new IntegerDomainInterpreter(this,
					context.integerDomain());

			this.arrayOrArrayName = new TypedName(context.arrayOrArrayName()
					.getText(), Type.ARRAY);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Array interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			IntegerDomain domain = integerDomainInterpreter.interpret(
					factories, definitions, environment);

			if (domain.count() <= 0)
				integerDomainInterpreter.addError("Empty domain", true);

			Array newArray = new Array();

			if (environment.containsArray(arrayOrArrayName)) {
				Array oldArray = environment.getArray(arrayOrArrayName);
				for (int i = domain.getLeft(); i <= domain.getRight(); i++) {
					if (!oldArray.containsKey(i))
						addError("Invalid index \"" + i + "\"", true);

					Port port = oldArray.get(i);
					environment.updateRole(port, this);
					newArray.put(i, port);
				}
			}

			else {
				for (int i = domain.getLeft(); i <= domain.getRight(); i++) {
					Port port = factories
							.newOrGetPort(arrayOrArrayName.getName() + "$"
									+ environment.getScope() + "$" + i);

					environment.updateRole(port, this);
					newArray.put(i, port);
				}
			}

			return newArray;
		}
	}

	public static class Explicit extends ArrayExpressionInterpreter {

		//
		// FIELDS
		//

		private final List<PortExpressionInterpreter> portExpressionInterpreters = new ArrayList<>();

		//
		// CONSTRUCTORS
		//

		public Explicit(Interpreter<?, ?> parent,
				ArrayExpressionExplicitContext context) {

			super(parent, context);

			try {
				for (PortExpressionContext c : context.portExpressionList()
						.portExpression())

					this.portExpressionInterpreters
							.add(PortExpressionInterpreter.newInstance(this, c));
			}

			catch (NullPointerException exc) {
			}
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public Array interpret(Factories factories, Definitions definitions,
				Environment environment) {

			super.interpret(factories, definitions, environment);

			if (portExpressionInterpreters.isEmpty())
				addError("Empty array", true);

			Array array = new Array();
			int i = 1;
			for (PortExpressionInterpreter interpr : portExpressionInterpreters)
				array.put(i++,
						interpr.interpret(factories, definitions, environment));

			return array;
		}
	}
}
