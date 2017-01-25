package nl.cwi.pr.tools.interpr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.pr.autom.Extralogical;
import nl.cwi.pr.misc.Array;
import nl.cwi.pr.misc.Definitions;
import nl.cwi.pr.misc.Environment;
import nl.cwi.pr.misc.Factories;
import nl.cwi.pr.misc.Family;
import nl.cwi.pr.misc.FamilySignature;
import nl.cwi.pr.misc.Member;
import nl.cwi.pr.misc.MemberSignature;
import nl.cwi.pr.misc.PortOrArray;
import nl.cwi.pr.misc.TypedName;
import nl.cwi.pr.misc.Environment.Mode;
import nl.cwi.pr.misc.Member.Composite;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortType;
import nl.cwi.pr.misc.TypedName.Type;
import nl.cwi.pr.tools.pars.PrParser.ExtralogicalExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.IntegerExpressionContext;
import nl.cwi.pr.tools.pars.PrParser.MemberSignatureContext;
import nl.cwi.pr.tools.pars.PrParser.PortOrArrayExpressionContext;

public class MemberSignatureInterpreter extends
		Interpreter<MemberSignatureContext, Member> {

	//
	// FIELDS
	//

	private final List<IntegerExpressionInterpreter> integerExpressionInterpreters = new ArrayList<>();
	private final List<ExtralogicalExpressionInterpreter> extralogicalExpressionInterpreters = new ArrayList<>();
	private final List<PortOrArrayExpressionInterpreter> inputPortOrArrayExpressionInterpreters = new ArrayList<>();
	private final List<PortOrArrayExpressionInterpreter> outputPortOrArrayExpressionInterpreters = new ArrayList<>();

	private final TypedName familyName;

	//
	// CONSTRUCTORS
	//

	public MemberSignatureInterpreter(Interpreter<?, ?> parent,
			MemberSignatureContext context) {

		super(parent, context);

		/*
		 * Initialize this.name
		 */

		this.familyName = new TypedName(context.memberName().getText(),
				Type.FAMILY);

		/*
		 * Initialize this.integerExpressionInterpreters
		 */

		try {
			for (IntegerExpressionContext c : context
					.memberIntegerExpressionList().integerExpressionList()
					.integerExpression())

				this.integerExpressionInterpreters
						.add(IntegerExpressionInterpreter.newInstance(this, c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.extralogicalExpressionInterpreters
		 */

		try {
			for (ExtralogicalExpressionContext c : context
					.memberExtralogicalExpressionList()
					.extralogicalExpressionList().extralogicalExpression())

				this.extralogicalExpressionInterpreters
						.add(ExtralogicalExpressionInterpreter.newInstance(
								this, c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.inputPortOrArrayExpressionInterpreters
		 */

		try {
			for (PortOrArrayExpressionContext c : context
					.memberPortOrArrayExpressionList().input
					.portOrArrayExpression())

				this.inputPortOrArrayExpressionInterpreters
						.add(PortOrArrayExpressionInterpreter.newInstance(this,
								c));
		}

		catch (NullPointerException exc) {
		}

		/*
		 * Initialize this.outputPortOrArrayExpressionInterpreters
		 */

		try {
			for (PortOrArrayExpressionContext c : context
					.memberPortOrArrayExpressionList().output
					.portOrArrayExpression())

				this.outputPortOrArrayExpressionInterpreters
						.add(PortOrArrayExpressionInterpreter.newInstance(this,
								c));
		}

		catch (NullPointerException exc) {
		}
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public Member interpret(Factories factories, Definitions definitions,
			Environment environment) {

		super.interpret(factories, definitions, environment);

		Environment newEnvironment = new Environment();
		Iterator<TypedName> iterator;

		/*
		 * Interpret name
		 */

		if (!definitions.containsFamily(familyName))
			addError("Unbound family name", true);

		Family family = definitions.getFamily(familyName);
		FamilySignature familySignature = family.getSignature();

		/*
		 * Interpret integer expressions
		 */

		Map<TypedName, Integer> integers = new LinkedHashMap<>();

		iterator = familySignature.getIntegerNames().iterator();
		for (IntegerExpressionInterpreter interpr : integerExpressionInterpreters) {
			if (!iterator.hasNext()) {
				interpr.addError("Superfluous parameter", false);
				continue;
			}

			TypedName integerName = iterator.next();
			Integer integer = interpr.interpret(factories, definitions,
					environment);

			integers.put(integerName, integer);
			newEnvironment.putInteger(integerName, integer);
		}

		if (iterator.hasNext())
			addError("Too few integer parameters", true);

		/*
		 * Interpret extralogical expressions
		 */

		Map<TypedName, Extralogical> extralogicals = new LinkedHashMap<>();

		iterator = familySignature.getExtralogicalNames().iterator();
		for (ExtralogicalExpressionInterpreter interpr : extralogicalExpressionInterpreters) {
			if (!iterator.hasNext()) {
				interpr.addError("Superfluous parameter", false);
				continue;
			}

			TypedName extralogicalName = iterator.next();
			Extralogical extralogical = interpr.interpret(factories,
					definitions, environment);

			extralogicals.put(extralogicalName, extralogical);
			newEnvironment.putExtralogical(extralogicalName, extralogical);
		}

		if (iterator.hasNext())
			addError("Too few extralogical parameters", true);

		/*
		 * Interpret input port or input array expressions
		 */

		environment.setMode(Mode.INPUT);
		newEnvironment.setMode(Mode.INPUT);

		Map<TypedName, PortOrArray> inputPortsOrArrays = interpretPortOrArrayExpressions(
				factories, definitions, environment, newEnvironment,
				familySignature.getInputPortOrArrayNames(),
				inputPortOrArrayExpressionInterpreters);

		/*
		 * Interpret output port or output array expressions
		 */

		environment.setMode(Mode.OUTPUT);
		newEnvironment.setMode(Mode.OUTPUT);

		Map<TypedName, PortOrArray> outputPortsOrArrays = interpretPortOrArrayExpressions(
				factories, definitions, environment, newEnvironment,
				familySignature.getOutputPortOrArrayNames(),
				outputPortOrArrayExpressionInterpreters);

		/*
		 * Return
		 */

		MemberSignature signature = new MemberSignature(familyName, integers,
				extralogicals, inputPortsOrArrays, outputPortsOrArrays,
				factories.getPortFactory());

		Interpreter<?, ? extends Member> interpreter = family.getInterpreter();
		Member member = interpreter.interpret(factories, definitions,
				newEnvironment);

		if (interpreter instanceof FamilyExpressionInterpreter)
			newEnvironment.validateRoles(interpreter);

		if (member.hasSignature()) {
			Composite composite = new Composite();
			composite.addChild(member);
			composite.setSignature(signature);
			return composite;
		}

		else {
			member.setSignature(signature);
			return member;
		}
	}

	//
	// METHODS - PRIVATE
	//

	private Map<TypedName, PortOrArray> interpretPortOrArrayExpressions(
			Factories factories,
			Definitions definitions,
			Environment environment,
			Environment newEnvironment,
			List<TypedName> names,
			List<PortOrArrayExpressionInterpreter> portOrArrayExpressionInterpreters) {

		Map<TypedName, PortOrArray> portsOrArrays = new LinkedHashMap<>();

		Iterator<TypedName> iterator = names.iterator();
		for (PortOrArrayExpressionInterpreter interpr : portOrArrayExpressionInterpreters) {
			if (!iterator.hasNext()) {
				interpr.addError("Superfluous parameter", true);
				continue;
			}

			TypedName portOrArrayName = iterator.next();
			PortOrArray portOrArray = interpr.interpret(factories, definitions,
					environment);

			portsOrArrays.put(portOrArrayName, portOrArray);

			final PortType type;
			switch (environment.getMode()) {
			case INPUT:
				type = PortType.INPUT;
				break;
			case OUTPUT:
				type = PortType.OUTPUT;
				break;
			case WORKER:
			default:
				throw new Error();
			}

			switch (portOrArrayName.getType()) {
			case ARRAY:
				if (portOrArray instanceof Port)
					interpr.addError("Illegal parameter type", true);

				Array array = (Array) portOrArray;
				newEnvironment.putArray(portOrArrayName, array);

				if (environment.getScope() == 1)
					for (Port p : array.values()) {
						if (definitions.containsPort(p))
							interpr.addError("Illegal parameter", false);
						else
							definitions.addPort(p);

						if (!p.hasAnnotation(Port.ANNOTATION_PORT_TYPE,
								PortType.class))
							p.addAnnotation(Port.ANNOTATION_PORT_TYPE, type);
					}

				break;

			case PORT:
				if (portOrArray instanceof Array)
					interpr.addError("Illegal parameter type", true);

				Port port = (Port) portOrArray;
				newEnvironment.putPort(portOrArrayName, port);

				if (environment.getScope() == 1) {
					if (definitions.containsPort(port))
						interpr.addError("Illegal parameter", false);
					else
						definitions.addPort(port);

					if (!port.hasAnnotation(Port.ANNOTATION_PORT_TYPE,
							PortType.class))
						port.addAnnotation(Port.ANNOTATION_PORT_TYPE, type);
				}

				break;
			default:
				throw new Error();
			}
		}

		if (iterator.hasNext())
			addError("Too few port/array parameters", true);

		return portsOrArrays;
	}
}
