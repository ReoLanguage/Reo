package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.FamilySignature;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.ExtralogicalNameContext;
import nl.cwi.pr.tools.pars.PrParser.FamilySignatureContext;
import nl.cwi.pr.tools.pars.PrParser.IntegerNameContext;
import nl.cwi.pr.tools.pars.PrParser.PortOrArrayNameArrayContext;
import nl.cwi.pr.tools.pars.PrParser.PortOrArrayNameContext;
import nl.cwi.pr.tools.pars.PrParser.PortOrArrayNamePortContext;

public class FamilySignatureInterpreter extends
		Interpreter<FamilySignatureContext, FamilySignature> {

	//
	// FIELDS
	//

	private final List<TypedName> extralogicalNames = new ArrayList<>();
	private final List<TypedName> inputPortOrArrayNames = new ArrayList<>();
	private final List<TypedName> integerNames = new ArrayList<>();
	private final List<TypedName> outputPortOrArrayNames = new ArrayList<>();

	private final TypedName name;

	//
	// CONSTRUCTORS
	//

	public FamilySignatureInterpreter(Interpreter<?, ?> parent,
			FamilySignatureContext context) {

		super(parent, context);

		/*
		 * Initialize this.name
		 */

		this.name = new TypedName(context.familyName().getText(), Type.FAMILY);

		/*
		 * Initialize this.integerNames
		 */

		try {
			for (IntegerNameContext c : context.familyIntegerNameList()
					.integerNameList().integerName())
				this.integerNames.add(new TypedName(c.getText(), Type.INTEGER));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.extralogicalNames
		 */

		try {
			for (ExtralogicalNameContext c : context
					.familyExtralogicalNameList().extralogicalNameList()
					.extralogicalName())
				this.extralogicalNames.add(new TypedName(c.getText(),
						Type.EXTRALOGICAL));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.inputPortOrArrayNames
		 */

		try {
			for (PortOrArrayNameContext c : context.familyPortOrArrayNameList().input
					.portOrArrayName())

				if (c instanceof PortOrArrayNamePortContext)
					this.inputPortOrArrayNames.add(new TypedName(c.getText(),
							Type.PORT));
				else if (c instanceof PortOrArrayNameArrayContext)
					this.inputPortOrArrayNames.add(new TypedName(
							((PortOrArrayNameArrayContext) c).IDENTIFIER()
									.getText(), Type.ARRAY));
				else
					throw new Error();
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.outputPortNames
		 */

		try {
			for (PortOrArrayNameContext c : context.familyPortOrArrayNameList().output
					.portOrArrayName())

				if (c instanceof PortOrArrayNamePortContext)
					this.outputPortOrArrayNames.add(new TypedName(c.getText(),
							Type.PORT));
				else if (c instanceof PortOrArrayNameArrayContext)
					this.outputPortOrArrayNames.add(new TypedName(
							((PortOrArrayNameArrayContext) c).IDENTIFIER()
									.getText(), Type.ARRAY));
				else
					throw new Error();
		}

		catch (NullPointerException exc) {
		}
	}

	//
	// METHODS
	//

	@Override
	public FamilySignature interpret(Factories factories,
			Definitions definitions, Environment environment) {

		super.interpret(factories, definitions, environment);

		if (inputPortOrArrayNames.isEmpty() && outputPortOrArrayNames.isEmpty())
			addError("Illegal family signature", false);

		return new FamilySignature(name, integerNames, extralogicalNames,
				inputPortOrArrayNames, outputPortOrArrayNames);
	}
}