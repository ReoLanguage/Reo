package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.components.ComponentDefinition;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.components.ComponentVariable;
import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.connectors.Reference;
import nl.cwi.reo.interpret.instances.ComponentInstance;
import nl.cwi.reo.interpret.instances.ProductInstance;
import nl.cwi.reo.interpret.instances.InstanceExpression;
import nl.cwi.reo.interpret.ports.PortExpression;
import nl.cwi.reo.interpret.ports.PortListExpression;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.sets.SetAtom;
import nl.cwi.reo.interpret.sets.SetComposite;
import nl.cwi.reo.interpret.sets.SetExpression;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.statements.TruthValue;
import nl.cwi.reo.interpret.statements.Universal;
import nl.cwi.reo.interpret.statements.Conjunction;
import nl.cwi.reo.interpret.statements.Disjunction;
import nl.cwi.reo.interpret.statements.Existential;
import nl.cwi.reo.interpret.statements.Membership;
import nl.cwi.reo.interpret.statements.Negation;
import nl.cwi.reo.interpret.statements.PredicateExpression;
import nl.cwi.reo.interpret.statements.Relation;
import nl.cwi.reo.interpret.statements.RelationSymbol;
import nl.cwi.reo.interpret.statements.PredicateVariable;
import nl.cwi.reo.interpret.terms.FunctionExpression;
import nl.cwi.reo.interpret.terms.FunctionSymbol;
import nl.cwi.reo.interpret.terms.ComponentTermExpression;
import nl.cwi.reo.interpret.terms.InstanceTermExpression;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.terms.Range;
import nl.cwi.reo.interpret.terms.VariableTermExpression;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.NodeExpression;
import nl.cwi.reo.interpret.variables.ParameterExpression;
import nl.cwi.reo.interpret.variables.ParameterType;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;
import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.ReoBaseListener;
import nl.cwi.reo.interpret.ReoFile;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.Component_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Component_compositeContext;
import nl.cwi.reo.interpret.ReoParser.Component_variableContext;
import nl.cwi.reo.interpret.ReoParser.DefnContext;
import nl.cwi.reo.interpret.ReoParser.FileContext;
import nl.cwi.reo.interpret.ReoParser.Formula_binaryrelationContext;
import nl.cwi.reo.interpret.ReoParser.Formula_trueContext;
import nl.cwi.reo.interpret.ReoParser.Formula_falseContext;
import nl.cwi.reo.interpret.ReoParser.Formula_componentdefnContext;
import nl.cwi.reo.interpret.ReoParser.Formula_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Formula_disjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Formula_existentialContext;
import nl.cwi.reo.interpret.ReoParser.Formula_implicationContext;
import nl.cwi.reo.interpret.ReoParser.Formula_membershipContext;
import nl.cwi.reo.interpret.ReoParser.Formula_negationContext;
import nl.cwi.reo.interpret.ReoParser.Formula_structdefnContext;
import nl.cwi.reo.interpret.ReoParser.Formula_universalContext;
import nl.cwi.reo.interpret.ReoParser.Formula_variableContext;
import nl.cwi.reo.interpret.ReoParser.ImpsContext;
import nl.cwi.reo.interpret.ReoParser.Instance_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Instance_productContext;
import nl.cwi.reo.interpret.ReoParser.Instance_semicolonContext;
import nl.cwi.reo.interpret.ReoParser.Instance_sumContext;
import nl.cwi.reo.interpret.ReoParser.ListContext;
import nl.cwi.reo.interpret.ReoParser.MultisetContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_constraintContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_setbuilderContext;
import nl.cwi.reo.interpret.ReoParser.NodeContext;
import nl.cwi.reo.interpret.ReoParser.NodesContext;
import nl.cwi.reo.interpret.ReoParser.ParamContext;
import nl.cwi.reo.interpret.ReoParser.ParamsContext;
import nl.cwi.reo.interpret.ReoParser.PortContext;
import nl.cwi.reo.interpret.ReoParser.PortsContext;
import nl.cwi.reo.interpret.ReoParser.Ref_cContext;
import nl.cwi.reo.interpret.ReoParser.Ref_javaContext;
import nl.cwi.reo.interpret.ReoParser.SecnContext;
import nl.cwi.reo.interpret.ReoParser.SignContext;
import nl.cwi.reo.interpret.ReoParser.TermContext;
import nl.cwi.reo.interpret.ReoParser.Term_booleanContext;
import nl.cwi.reo.interpret.ReoParser.Term_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Term_componentdefnContext;
import nl.cwi.reo.interpret.ReoParser.Term_decimalContext;
import nl.cwi.reo.interpret.ReoParser.Term_exponentContext;
import nl.cwi.reo.interpret.ReoParser.Term_instanceContext;
import nl.cwi.reo.interpret.ReoParser.Term_listContext;
import nl.cwi.reo.interpret.ReoParser.Term_naturalContext;
import nl.cwi.reo.interpret.ReoParser.Term_operationContext;
import nl.cwi.reo.interpret.ReoParser.Term_rangeContext;
import nl.cwi.reo.interpret.ReoParser.Term_stringContext;
import nl.cwi.reo.interpret.ReoParser.Term_unaryminContext;
import nl.cwi.reo.interpret.ReoParser.Term_variableContext;
import nl.cwi.reo.interpret.ReoParser.TypeContext;
import nl.cwi.reo.interpret.ReoParser.VarContext;

/**
 * Listens to events triggered by a
 * {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 */
public class BaseListener extends ReoBaseListener {

	/** The monitor. */
	protected final Monitor m;

	/** The name of the Treo source file. */
	private String filename = "";

	/** The parsed program in the Treo file. */
	@Nullable
	private ReoFile program;

	/** The imports. */
	private Set<String> imports = new HashSet<String>();

	/** The definitions. */
	private ParseTreeProperty<Relation> definitions = new ParseTreeProperty<>();

	/** The section. */
	private ParseTreeProperty<String> section = new ParseTreeProperty<>();

	/** The components. */
	private ParseTreeProperty<ComponentExpression> components = new ParseTreeProperty<>();

	/** The component names. */
	private ParseTreeProperty<String> componentnames = new ParseTreeProperty<>();

	/** The predicates. */
	private ParseTreeProperty<PredicateExpression> predicates = new ParseTreeProperty<>();

	/** The instances. */
	private ParseTreeProperty<InstanceExpression> instances = new ParseTreeProperty<>();

	/** The terms. */
	private ParseTreeProperty<TermExpression> terms = new ParseTreeProperty<>();

	/** The lists of terms. */
	private ParseTreeProperty<ListExpression> lists = new ParseTreeProperty<>();

	/** The signature expressions. */
	private ParseTreeProperty<SignatureExpression> signatureExpressions = new ParseTreeProperty<>();

	/** The parameter lists. */
	private ParseTreeProperty<List<ParameterExpression>> parameterlists = new ParseTreeProperty<>();

	/** The parameters. */
	private ParseTreeProperty<ParameterExpression> parameters = new ParseTreeProperty<>();

	/** The node lists. */
	private ParseTreeProperty<List<NodeExpression>> nodelists = new ParseTreeProperty<>();

	/** The nodes. */
	private ParseTreeProperty<NodeExpression> nodes = new ParseTreeProperty<>();

	/** The type tags. */
	private ParseTreeProperty<TypeTag> typetags = new ParseTreeProperty<>();

	/** The port lists. */
	private ParseTreeProperty<List<PortExpression>> portlists = new ParseTreeProperty<>();

	/** The ports. */
	private ParseTreeProperty<PortExpression> ports = new ParseTreeProperty<>();

	/** The variables. */
	private ParseTreeProperty<VariableExpression> variables = new ParseTreeProperty<>();

	/** The atoms. */
	protected ParseTreeProperty<Atom> atoms = new ParseTreeProperty<>();

	/**
	 * Constructs a new generic listener.
	 * 
	 * @param m
	 *            message container
	 */
	public BaseListener(Monitor m) {
		this.m = m;
	}

	/**
	 * Gets the program expression.
	 * 
	 * @return program expression
	 */
	@Nullable
	public ReoFile getMain() {
		return program;
	}

	/**
	 * Sets the file name, and clears all parse tree properties.
	 *
	 * @param filename
	 *            the new file name
	 */
	public void setFileName(String filename) {
		this.filename = filename;
		imports.clear();
		section = new ParseTreeProperty<>();
		components = new ParseTreeProperty<>();
		predicates = new ParseTreeProperty<>();
		instances = new ParseTreeProperty<>();
		terms = new ParseTreeProperty<>();
		lists = new ParseTreeProperty<>();
		signatureExpressions = new ParseTreeProperty<>();
		parameterlists = new ParseTreeProperty<>();
		parameters = new ParseTreeProperty<>();
		nodelists = new ParseTreeProperty<>();
		nodes = new ParseTreeProperty<>();
		typetags = new ParseTreeProperty<>();
		portlists = new ParseTreeProperty<>();
		ports = new ParseTreeProperty<>();
		variables = new ParseTreeProperty<>();
		atoms = new ParseTreeProperty<>();
	}

	/*
	 * File structure
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterFile(nl.cwi.reo.interpret.
	 * ReoParser.FileContext)
	 */
	@Override
	public void enterFile(FileContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitFile(nl.cwi.reo.interpret.
	 * ReoParser.FileContext)
	 */
	@Override
	public void exitFile(FileContext ctx) {
		String sec = "";
		if (ctx.secn() != null)
			sec = section.get(ctx.secn());
		List<PredicateExpression> conjunctions = new ArrayList<PredicateExpression>();
		for (DefnContext defn_ctx : ctx.defn())
			conjunctions.add(definitions.get(defn_ctx));
		Conjunction c = new Conjunction(conjunctions);
		Location l = new Location(ctx.start, filename);
		program = new ReoFile(sec, imports, filename, c, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitSecn(nl.cwi.reo.interpret.
	 * ReoParser.SecnContext)
	 */
	@Override
	public void exitSecn(SecnContext ctx) {
		section.put(ctx, ctx.name().getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitImps(nl.cwi.reo.interpret.
	 * ReoParser.ImpsContext)
	 */
	@Override
	public void exitImps(ImpsContext ctx) {
		imports.add(ctx.name().getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterDefn(nl.cwi.reo.interpret.
	 * ReoParser.DefnContext)
	 */
	@Override
	public void enterDefn(DefnContext ctx) {
		componentnames.put(ctx.component(), ctx.ID().getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitDefn(nl.cwi.reo.interpret.
	 * ReoParser.DefnContext)
	 */
	@Override
	public void exitDefn(DefnContext ctx) {
		VariableExpression e = new VariableExpression(ctx.ID().getText(), new ArrayList<TermExpression>(),
				new Location(ctx.ID().getSymbol(), filename));
		TermExpression t1 = new VariableTermExpression(e);
		ComponentTermExpression t2 = new ComponentTermExpression(components.get(ctx.component()));
		definitions.put(ctx, new Relation(RelationSymbol.EQ, Arrays.asList(t1, t2), new Location(ctx.start, filename)));
	}

	/*
	 * Components
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitComponent_variable(nl.cwi.reo.
	 * interpret.ReoParser.Component_variableContext)
	 */
	@Override
	public void exitComponent_variable(Component_variableContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		components.put(ctx, new ComponentVariable(var));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitComponent_atomic(nl.cwi.reo.
	 * interpret.ReoParser.Component_atomicContext)
	 */
	@Override
	public void exitComponent_atomic(Component_atomicContext ctx) {
		SignatureExpression sign = signatureExpressions.get(ctx.sign());
		String name = componentnames.get(ctx);

		List<Atom> _atoms = new ArrayList<>();
		for (AtomContext atom_ctx : ctx.atom()) {
			Atom x = atoms.get(atom_ctx);
			if (x instanceof Reference) {
				Reference r = (Reference) x;
				_atoms.add(new Reference(r.getCall(), r.getLanguage(), sign.getParameters()));
			} else if (x != null) {
				_atoms.add(x);
			}
		}

		components.put(ctx, new ComponentDefinition(sign, new SetAtom(name, _atoms)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRef_java(nl.cwi.reo.interpret.
	 * ReoParser.Ref_javaContext)
	 */
	@Override
	public void exitRef_java(Ref_javaContext ctx) {
		String call = ctx.STRING().getText().replace("\"", "");
		atoms.put(ctx, new Reference(call, Language.JAVA));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRef_c(nl.cwi.reo.interpret.
	 * ReoParser.Ref_cContext)
	 */
	@Override
	public void exitRef_c(Ref_cContext ctx) {
		String call = ctx.STRING().getText().replace("\"", "");
		atoms.put(ctx, new Reference(call, Language.C11));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterComponent_composite(nl.cwi.reo.
	 * interpret.ReoParser.Component_compositeContext)
	 */
	@Override
	public void enterComponent_composite(Component_compositeContext ctx) {
		componentnames.put(ctx.multiset(), componentnames.get(ctx));
		SignatureExpression sign = signatureExpressions.get(ctx.sign());
		components.put(ctx, new ComponentDefinition(sign, (SetComposite) instances.get(ctx.multiset())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitComponent_composite(nl.cwi.reo.
	 * interpret.ReoParser.Component_compositeContext)
	 */
	@Override
	public void exitComponent_composite(Component_compositeContext ctx) {
		components.put(ctx, new ComponentDefinition(signatureExpressions.get(ctx.sign()),
				(SetComposite) instances.get(ctx.multiset())));
	}

	/*
	 * Sets
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitMultiset_constraint(nl.cwi.reo.
	 * interpret.ReoParser.Multiset_constraintContext)
	 */
	@Override
	public void exitMultiset_constraint(Multiset_constraintContext ctx) {
		InstanceExpression i = instances.get(ctx.instance());
		instances.put(ctx, i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitMultiset_setbuilder(nl.cwi.reo.
	 * interpret.ReoParser.Multiset_setbuilderContext)
	 */
	@Override
	public void exitMultiset_setbuilder(Multiset_setbuilderContext ctx) {
		String name = componentnames.get(ctx);
		List<InstanceExpression> stmtlist = new ArrayList<InstanceExpression>();
		for (MultisetContext stmt_ctx : ctx.multiset())
			stmtlist.add(instances.get(stmt_ctx));
		TermExpression operator = terms.get(ctx.term());
		if (operator == null)
			operator = new StringValue("");
		PredicateExpression P = predicates.get(ctx.formula());
		if (P == null)
			P = new TruthValue(true);
		instances.put(ctx, new SetComposite(name, operator, stmtlist, P, new Location(ctx.start, filename)));
	}

	/*
	 * Instances
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitInstance_atomic(nl.cwi.reo.
	 * interpret.ReoParser.Instance_atomicContext)
	 */
	@Override
	public void exitInstance_atomic(Instance_atomicContext ctx) {
		ComponentExpression cexpr = components.get(ctx.component());
		ListExpression list = lists.get(ctx.list());
		if (list == null)
			list = new ListExpression(new ArrayList<>());
		List<PortExpression> v = new ArrayList<>();
		for (PortExpression p : portlists.get(ctx.ports()))
			v.add(p);
		PortListExpression var = new PortListExpression(v);
		instances.put(ctx, new ComponentInstance(cexpr, list, var));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitInstance_product(nl.cwi.reo.
	 * interpret.ReoParser.Instance_productContext)
	 */
	@Override
	public void exitInstance_product(Instance_productContext ctx) {
		InstanceExpression i1 = instances.get(ctx.instance(0));
		InstanceExpression i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue("*");
		instances.put(ctx, new ProductInstance(s, i1, i2, new Location(ctx.start, filename)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitInstance_sum(nl.cwi.reo.
	 * interpret.ReoParser.Instance_sumContext)
	 */
	@Override
	public void exitInstance_sum(Instance_sumContext ctx) {
		InstanceExpression i1 = instances.get(ctx.instance(0));
		InstanceExpression i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue("+");
		instances.put(ctx, new ProductInstance(s, i1, i2, new Location(ctx.start, filename)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitInstance_semicolon(nl.cwi.reo.
	 * interpret.ReoParser.Instance_semicolonContext)
	 */
	@Override
	public void exitInstance_semicolon(Instance_semicolonContext ctx) {
		InstanceExpression i1 = instances.get(ctx.instance(0));
		InstanceExpression i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue(";");
		instances.put(ctx, new ProductInstance(s, i1, i2, new Location(ctx.start, filename)));
	}

	/*
	 * Predicates.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitFormula_true(nl.cwi.reo.
	 * interpret.ReoParser.Formula_trueContext)
	 */
	@Override
	public void exitFormula_true(Formula_trueContext ctx) {
		predicates.put(ctx, new TruthValue(true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitFormula_false(nl.cwi.reo.
	 * interpret.ReoParser.Formula_falseContext)
	 */
	@Override
	public void exitFormula_false(Formula_falseContext ctx) {
		predicates.put(ctx, new TruthValue(false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_componentdefn(nl.cwi.reo
	 * .interpret.ReoParser.Formula_componentdefnContext)
	 */
	@Override
	public void exitFormula_componentdefn(Formula_componentdefnContext ctx) {
		List<TermExpression> arguments = Arrays.asList(new VariableTermExpression(variables.get(ctx.var())),
				new ComponentTermExpression(components.get(ctx.component())));
		Relation p = new Relation(RelationSymbol.EQ, arguments, new Location(ctx.start, filename));
		predicates.put(ctx, p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_structdefn(nl.cwi.reo.
	 * interpret.ReoParser.Formula_structdefnContext)
	 */
	@Override
	public void exitFormula_structdefn(Formula_structdefnContext ctx) {
		List<ParameterExpression> params = new ArrayList<>();
		for (ParamContext p : ctx.param())
			params.add(parameters.get(p));
		SignatureExpression sign = new SignatureExpression(params, new ArrayList<>(),
				new Location(ctx.start, filename));
		SetExpression set = new SetComposite();
		VariableExpression var = new VariableExpression(ctx.ID().getText(), new ArrayList<>(),
				new Location(ctx.ID().getSymbol(), filename));
		TermExpression te1 = new VariableTermExpression(var);
		TermExpression te2 = new ComponentTermExpression(new ComponentDefinition(sign, set));
		predicates.put(ctx, new Relation(RelationSymbol.EQ, Arrays.asList(te1, te2), new Location(ctx.start, filename)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_membership(nl.cwi.reo.
	 * interpret.ReoParser.Formula_membershipContext)
	 */
	@Override
	public void exitFormula_membership(Formula_membershipContext ctx) {
		Membership p = new Membership(new Identifier(ctx.ID().getText()), lists.get(ctx.list()));
		predicates.put(ctx, p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_variable(nl.cwi.reo.
	 * interpret.ReoParser.Formula_variableContext)
	 */
	@Override
	public void exitFormula_variable(Formula_variableContext ctx) {
		PredicateVariable p = new PredicateVariable(new VariableTermExpression(variables.get(ctx.var())));
		predicates.put(ctx, p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_binaryrelation(nl.cwi.
	 * reo.interpret.ReoParser.Formula_binaryrelationContext)
	 */
	@Override
	public void exitFormula_binaryrelation(Formula_binaryrelationContext ctx) {
		List<TermExpression> l = Arrays.asList(terms.get(ctx.term(0)), terms.get(ctx.term(1)));
		switch (ctx.op.getType()) {
		case ReoParser.LEQ:
			predicates.put(ctx, new Relation(RelationSymbol.LEQ, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.LT:
			predicates.put(ctx, new Relation(RelationSymbol.LT, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.GEQ:
			predicates.put(ctx, new Relation(RelationSymbol.GEQ, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.GT:
			predicates.put(ctx, new Relation(RelationSymbol.GT, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.EQ:
			predicates.put(ctx, new Relation(RelationSymbol.EQ, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.NEQ:
			predicates.put(ctx, new Relation(RelationSymbol.NEQ, l, new Location(ctx.start, filename)));
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_negation(nl.cwi.reo.
	 * interpret.ReoParser.Formula_negationContext)
	 */
	@Override
	public void exitFormula_negation(Formula_negationContext ctx) {
		predicates.put(ctx, new Negation(predicates.get(ctx.formula())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_existential(nl.cwi.reo.
	 * interpret.ReoParser.Formula_existentialContext)
	 */
	@Override
	public void exitFormula_existential(Formula_existentialContext ctx) {
		Membership m = new Membership(new Identifier(ctx.ID().getText()), lists.get(ctx.list()));
		predicates.put(ctx, new Existential(m, predicates.get(ctx.formula())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_universal(nl.cwi.reo.
	 * interpret.ReoParser.Formula_universalContext)
	 */
	@Override
	public void exitFormula_universal(Formula_universalContext ctx) {
		Membership m = new Membership(new Identifier(ctx.ID().getText()), lists.get(ctx.list()));
		predicates.put(ctx, new Universal(m, predicates.get(ctx.formula())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_conjunction(nl.cwi.reo.
	 * interpret.ReoParser.Formula_conjunctionContext)
	 */
	@Override
	public void exitFormula_conjunction(Formula_conjunctionContext ctx) {
		List<PredicateExpression> l = Arrays.asList(predicates.get(ctx.formula(0)), predicates.get(ctx.formula(1)));
		predicates.put(ctx, new Conjunction(l));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_disjunction(nl.cwi.reo.
	 * interpret.ReoParser.Formula_disjunctionContext)
	 */
	@Override
	public void exitFormula_disjunction(Formula_disjunctionContext ctx) {
		List<PredicateExpression> l = Arrays.asList(predicates.get(ctx.formula(0)), predicates.get(ctx.formula(1)));
		predicates.put(ctx, new Disjunction(l));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitFormula_implication(nl.cwi.reo.
	 * interpret.ReoParser.Formula_implicationContext)
	 */
	@Override
	public void exitFormula_implication(Formula_implicationContext ctx) {
		List<PredicateExpression> l = Arrays.asList(new Negation(predicates.get(ctx.formula(0))),
				predicates.get(ctx.formula(1)));
		predicates.put(ctx, new Disjunction(l));
	}

	/*
	 * Signatures
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitSign(nl.cwi.reo.interpret.
	 * ReoParser.SignContext)
	 */
	@Override
	public void exitSign(SignContext ctx) {

		List<NodeExpression> nodes = new ArrayList<>();
		for (NodeExpression n : nodelists.get(ctx.nodes()))
			nodes.add(n);
		List<ParameterExpression> parameters = new ArrayList<>();
		if (ctx.params() != null)
			for (ParameterExpression p : parameterlists.get(ctx.params()))
				parameters.add(p);

		signatureExpressions.put(ctx, new SignatureExpression(parameters, nodes, new Location(ctx.start, filename)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitParams(nl.cwi.reo.interpret.
	 * ReoParser.ParamsContext)
	 */
	@Override
	public void exitParams(ParamsContext ctx) {
		List<ParameterExpression> list = new ArrayList<ParameterExpression>();
		for (ParamContext param_ctx : ctx.param())
			list.add(parameters.get(param_ctx));
		parameterlists.put(ctx, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitParam(nl.cwi.reo.interpret.
	 * ReoParser.ParamContext)
	 */
	@Override
	public void exitParam(ParamContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		ParameterType type = null;
		TypeTag tag = typetags.get(ctx.type());
		if (tag != null) {
			type = tag;
		} else {
			SignatureExpression sign = signatureExpressions.get(ctx.sign());
			if (sign != null) {
				type = sign;
			} else {
				type = new TypeTag("");
			}
		}
		parameters.put(ctx, new ParameterExpression(var, type));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitNodes(nl.cwi.reo.interpret.
	 * ReoParser.NodesContext)
	 */
	@Override
	public void exitNodes(NodesContext ctx) {
		List<NodeExpression> list = new ArrayList<NodeExpression>();
		for (NodeContext node_ctx : ctx.node())
			list.add(nodes.get(node_ctx));
		nodelists.put(ctx, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitNode(nl.cwi.reo.interpret.
	 * ReoParser.NodeContext)
	 */
	@Override
	public void exitNode(NodeContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		if (var == null)
			var = new VariableExpression("", new ArrayList<TermExpression>(), new Location(ctx.start, filename));
		TypeTag tag = typetags.get(ctx.type());
		PortType type = PortType.NONE;
		if (ctx.io != null) {
			switch (ctx.io.getType()) {
			case ReoParser.IN:
				type = PortType.IN;
				break;
			case ReoParser.OUT:
				type = PortType.OUT;
				break;
			case ReoParser.MIX:
				type = PortType.NONE;
				break;
			default:
				break;
			}
		}
		nodes.put(ctx, new NodeExpression(var, type, tag));
	}

	/*
	 * Type tags for uninterpreted data.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitType(nl.cwi.reo.interpret.
	 * ReoParser.TypeContext)
	 */
	@Override
	public void exitType(TypeContext ctx) {
		if (!ctx.getText().trim().equals(""))
			typetags.put(ctx, new TypeTag(ctx.getText()));
	}

	/*
	 * Interface instantiation.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitPorts(nl.cwi.reo.interpret.
	 * ReoParser.PortsContext)
	 */
	@Override
	public void exitPorts(PortsContext ctx) {
		List<PortExpression> list = new ArrayList<PortExpression>();
		for (PortContext port : ctx.port())
			list.add(ports.get(port));
		portlists.put(ctx, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitPort(nl.cwi.reo.interpret.
	 * ReoParser.PortContext)
	 */
	@Override
	public void exitPort(PortContext ctx) {
		PrioType prio = PrioType.NONE;
		if (ctx.prio != null)
			prio = ctx.prio.getType() == ReoParser.AND ? PrioType.AMPERSANT : PrioType.PLUS;
		ports.put(ctx, new PortExpression(prio, variables.get(ctx.var())));
	}

	/*
	 * Variables.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitVar(nl.cwi.reo.interpret.
	 * ReoParser.VarContext)
	 */
	@Override
	public void exitVar(VarContext ctx) {
		String name = ctx.name().getText();
		String[] parts = name.split("\\.");
		for (String imprt : imports) {
			String[] impparts = imprt.split("\\.");
			if (impparts[impparts.length - 1].equals(parts[parts.length - 1])) {
				name = imprt;
				break;
			}
		}
		List<TermExpression> list = new ArrayList<TermExpression>();
		for (TermContext indices_ctx : ctx.term())
			list.add(terms.get(indices_ctx));
		variables.put(ctx, new VariableExpression(name, list, new Location(ctx.start, filename)));
	}

	/*
	 * Term expressions.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_natural(nl.cwi.reo.
	 * interpret.ReoParser.Term_naturalContext)
	 */
	@Override
	public void exitTerm_natural(Term_naturalContext ctx) {
		terms.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().getText())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_boolean(nl.cwi.reo.
	 * interpret.ReoParser.Term_booleanContext)
	 */
	@Override
	public void exitTerm_boolean(Term_booleanContext ctx) {
		terms.put(ctx, new BooleanValue(Boolean.parseBoolean(ctx.BOOL().getText())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitTerm_string(nl.cwi.reo.interpret
	 * .ReoParser.Term_stringContext)
	 */
	@Override
	public void exitTerm_string(Term_stringContext ctx) {
		terms.put(ctx, new StringValue((ctx.STRING().getText())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_decimal(nl.cwi.reo.
	 * interpret.ReoParser.Term_decimalContext)
	 */
	@Override
	public void exitTerm_decimal(Term_decimalContext ctx) {
		terms.put(ctx, new DecimalValue(Double.parseDouble(ctx.DEC().getText())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitTerm_componentdefn(nl.cwi.reo.
	 * interpret.ReoParser.Term_componentdefnContext)
	 */
	@Override
	public void exitTerm_componentdefn(Term_componentdefnContext ctx) {
		terms.put(ctx, new ComponentTermExpression(components.get(ctx.component())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_variable(nl.cwi.reo.
	 * interpret.ReoParser.Term_variableContext)
	 */
	@Override
	public void exitTerm_variable(Term_variableContext ctx) {
		terms.put(ctx, new VariableTermExpression(variables.get(ctx.var())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitTerm_list(nl.cwi.reo.interpret.
	 * ReoParser.Term_listContext)
	 */
	@Override
	public void exitTerm_list(Term_listContext ctx) {
		terms.put(ctx, lists.get(ctx.list()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_instance(nl.cwi.reo.
	 * interpret.ReoParser.Term_instanceContext)
	 */
	@Override
	public void exitTerm_instance(Term_instanceContext ctx) {
		terms.put(ctx, new InstanceTermExpression(instances.get(ctx.instance())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_operation(nl.cwi.reo.
	 * interpret.ReoParser.Term_operationContext)
	 */
	@Override
	public void exitTerm_operation(Term_operationContext ctx) {
		TermExpression e1 = terms.get(ctx.term(0));
		TermExpression e2 = terms.get(ctx.term(1));
		List<TermExpression> l = Arrays.asList(e1, e2);
		switch (ctx.op.getType()) {
		case ReoParser.DIV:
			terms.put(ctx, new FunctionExpression(FunctionSymbol.DIV, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.MUL:
			terms.put(ctx, new FunctionExpression(FunctionSymbol.MUL, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.MOD:
			terms.put(ctx, new FunctionExpression(FunctionSymbol.MOD, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.ADD:
			terms.put(ctx, new FunctionExpression(FunctionSymbol.ADD, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.MIN:
			terms.put(ctx, new FunctionExpression(FunctionSymbol.MIN, l, new Location(ctx.start, filename)));
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_exponent(nl.cwi.reo.
	 * interpret.ReoParser.Term_exponentContext)
	 */
	@Override
	public void exitTerm_exponent(Term_exponentContext ctx) {
		TermExpression e1 = terms.get(ctx.term(0));
		TermExpression e2 = terms.get(ctx.term(1));
		List<TermExpression> l = Arrays.asList(e1, e2);
		terms.put(ctx, new FunctionExpression(FunctionSymbol.POW, l, new Location(ctx.start, filename)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_unarymin(nl.cwi.reo.
	 * interpret.ReoParser.Term_unaryminContext)
	 */
	@Override
	public void exitTerm_unarymin(Term_unaryminContext ctx) {
		TermExpression e = terms.get(ctx.term());
		List<TermExpression> l = Arrays.asList(e);
		terms.put(ctx, new FunctionExpression(FunctionSymbol.MIN, l, new Location(ctx.start, filename)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitTerm_brackets(nl.cwi.reo.
	 * interpret.ReoParser.Term_bracketsContext)
	 */
	@Override
	public void exitTerm_brackets(Term_bracketsContext ctx) {
		terms.put(ctx, terms.get(ctx.term()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitTerm_range(nl.cwi.reo.interpret.
	 * ReoParser.Term_rangeContext)
	 */
	@Override
	public void exitTerm_range(Term_rangeContext ctx) {
		terms.put(ctx, new Range(terms.get(ctx.term(0)), terms.get(ctx.term(1))));
	}

	/*
	 * List of terms.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitList(nl.cwi.reo.interpret.
	 * ReoParser.ListContext)
	 */
	@Override
	public void exitList(ListContext ctx) {
		List<TermExpression> list = new ArrayList<TermExpression>();
		for (TermContext expr_ctx : ctx.term())
			list.add(terms.get(expr_ctx));
		lists.put(ctx, new ListExpression(list));
		// terms.put(ctx, new ListExpression(list));
	}
}