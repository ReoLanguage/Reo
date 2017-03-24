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
import nl.cwi.reo.interpret.sets.SetElse;
import nl.cwi.reo.interpret.sets.SetExpression;
import nl.cwi.reo.interpret.sets.SetWithout;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.statements.TruthValue;
import nl.cwi.reo.interpret.statements.Conjunction;
import nl.cwi.reo.interpret.statements.Disjunction;
import nl.cwi.reo.interpret.statements.Membership;
import nl.cwi.reo.interpret.statements.Negation;
import nl.cwi.reo.interpret.statements.PredicateExpression;
import nl.cwi.reo.interpret.statements.Relation;
import nl.cwi.reo.interpret.statements.RelationSymbol;
import nl.cwi.reo.interpret.statements.StatementVariable;
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
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;
import nl.cwi.reo.interpret.ReoBaseListener;
import nl.cwi.reo.interpret.ReoFile;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.ReoParser.Component_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Component_compositeContext;
import nl.cwi.reo.interpret.ReoParser.Component_variableContext;
import nl.cwi.reo.interpret.ReoParser.DefnContext;
import nl.cwi.reo.interpret.ReoParser.FileContext;
import nl.cwi.reo.interpret.ReoParser.Formula_binaryrelationContext;
import nl.cwi.reo.interpret.ReoParser.Formula_booleanContext;
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
import nl.cwi.reo.interpret.ReoParser.Multiset_elseContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_setbuilderContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_withoutContext;
import nl.cwi.reo.interpret.ReoParser.NodeContext;
import nl.cwi.reo.interpret.ReoParser.NodesContext;
import nl.cwi.reo.interpret.ReoParser.ParamContext;
import nl.cwi.reo.interpret.ReoParser.ParamsContext;
import nl.cwi.reo.interpret.ReoParser.PortContext;
import nl.cwi.reo.interpret.ReoParser.PortsContext;
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
 * {@link org.antlr.v4.runtime.tree.ParseTreeWalker}. Returns a
 * {@link nl.cwi.reo.interpret.p}.
 * 
 * @param <T>
 *            Reo semantics type
 */
public class Listener<T extends Semantics<T>> extends ReoBaseListener {

	// Symbol table
	// private ParseTreeProperty<Map<String, String>> symbols = new
	// ParseTreeProperty<Map<String, String>>();

	protected final Monitor m;

	private String filename = "";

	// File
	@Nullable
	private ReoFile<T> program;
	private Set<String> imports = new HashSet<String>();
	private ParseTreeProperty<Relation> definitions = new ParseTreeProperty<Relation>();

	// Section
	private ParseTreeProperty<String> section = new ParseTreeProperty<String>();

	// Components
	private ParseTreeProperty<ComponentExpression<T>> components = new ParseTreeProperty<ComponentExpression<T>>();

	// Formulas
	private ParseTreeProperty<PredicateExpression> formula = new ParseTreeProperty<PredicateExpression>();

	// Instances
	private ParseTreeProperty<InstanceExpression<T>> instances = new ParseTreeProperty<InstanceExpression<T>>();
	private ParseTreeProperty<SetExpression<T>> sets = new ParseTreeProperty<SetExpression<T>>();

	// Terms
	private ParseTreeProperty<TermExpression> terms = new ParseTreeProperty<TermExpression>();

	// Term lists
	private ParseTreeProperty<List<TermExpression>> termsList = new ParseTreeProperty<List<TermExpression>>();

	// Signatures
	private ParseTreeProperty<SignatureExpression> signatureExpressions = new ParseTreeProperty<SignatureExpression>();

	// Parameters
	private ParseTreeProperty<List<ParameterExpression>> parameterlists = new ParseTreeProperty<List<ParameterExpression>>();
	private ParseTreeProperty<ParameterExpression> parameters = new ParseTreeProperty<ParameterExpression>();

	// Nodes
	private ParseTreeProperty<List<NodeExpression>> nodelists = new ParseTreeProperty<List<NodeExpression>>();
	private ParseTreeProperty<NodeExpression> nodes = new ParseTreeProperty<NodeExpression>();

	// Type tags
	private ParseTreeProperty<TypeTag> typetags = new ParseTreeProperty<TypeTag>();

	// Ports
	private ParseTreeProperty<List<PortExpression>> portlists = new ParseTreeProperty<List<PortExpression>>();
	private ParseTreeProperty<PortExpression> ports = new ParseTreeProperty<PortExpression>();

	// Variables
	private ParseTreeProperty<VariableExpression> variables = new ParseTreeProperty<VariableExpression>();

	// Semantics
	protected ParseTreeProperty<T> atoms = new ParseTreeProperty<T>();

	/**
	 * Constructs a new generic listener.
	 * 
	 * @param m
	 *            message container
	 */
	public Listener(Monitor m) {
		this.m = m;
	}

	/**
	 * Gets the program expression.
	 * 
	 * @return program expression
	 */
	@Nullable
	public ReoFile<T> getMain() {
		return program;
	}

	/**
	 * Sets the file name, and clears all parse tree properties.
	 */
	public void setFileName(String filename) {
		this.filename = filename;
		imports.clear();
		section = new ParseTreeProperty<String>();
		components = new ParseTreeProperty<ComponentExpression<T>>();
		formula = new ParseTreeProperty<PredicateExpression>();
		instances = new ParseTreeProperty<InstanceExpression<T>>();
		sets = new ParseTreeProperty<SetExpression<T>>();
		terms = new ParseTreeProperty<TermExpression>();
		termsList = new ParseTreeProperty<List<TermExpression>>();
		signatureExpressions = new ParseTreeProperty<SignatureExpression>();
		parameterlists = new ParseTreeProperty<List<ParameterExpression>>();
		parameters = new ParseTreeProperty<ParameterExpression>();
		nodelists = new ParseTreeProperty<List<NodeExpression>>();
		nodes = new ParseTreeProperty<NodeExpression>();
		typetags = new ParseTreeProperty<TypeTag>();
		portlists = new ParseTreeProperty<List<PortExpression>>();
		ports = new ParseTreeProperty<PortExpression>();
		variables = new ParseTreeProperty<VariableExpression>();
		atoms = new ParseTreeProperty<T>();
	}

	/**
	 * File structure
	 */

	@Override
	public void enterFile(FileContext ctx) {
		// Map<String, String> s = new HashMap<String, String>();
		// s.put(ctx.ID().getText(), "component");
		// symbols.put(ctx.component(), s);
	}

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
		program = new ReoFile<T>(sec, imports, filename, c, l);
	}

	@Override
	public void exitSecn(SecnContext ctx) {
		section.put(ctx, ctx.name().getText());
	}

	@Override
	public void exitImps(ImpsContext ctx) {
		imports.add(ctx.name().getText());
	}

	@Override
	public void exitDefn(DefnContext ctx) {
		VariableExpression e = new VariableExpression(ctx.ID().getText(), new ArrayList<TermExpression>(),
				new Location(ctx.ID().getSymbol(), filename));
		TermExpression t1 = new VariableTermExpression(e);
		ComponentTermExpression<T> t2 = new ComponentTermExpression<T>(components.get(ctx.component()));
		definitions.put(ctx, new Relation(RelationSymbol.EQ, Arrays.asList(t1, t2), new Location(ctx.start, filename)));
	}

	/**
	 * Components
	 */

	@Override
	public void exitComponent_variable(Component_variableContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		components.put(ctx, new ComponentVariable<T>(var));
	}

	@Override
	public void exitComponent_atomic(Component_atomicContext ctx) {
		T atom = atoms.get(ctx.atom());
		Reference s = new Reference();
		if (ctx.source() != null)
			s = new Reference(ctx.source().STRING().getText(), ctx.source().LANG().getText().toUpperCase());
		components.put(ctx, new ComponentDefinition<T>(signatureExpressions.get(ctx.sign()), new SetAtom<T>(atom, s)));
	}

	@Override
	public void exitComponent_composite(Component_compositeContext ctx) {
		components.put(ctx, new ComponentDefinition<T>(signatureExpressions.get(ctx.sign()),
				(SetComposite<T>) instances.get(ctx.multiset())));
	}

	/**
	 * Sets
	 */

	@Override
	public void exitMultiset_constraint(Multiset_constraintContext ctx) {
		InstanceExpression<T> i = instances.get(ctx.instance());
		instances.put(ctx, i);
	}

	@Override
	public void exitMultiset_setbuilder(Multiset_setbuilderContext ctx) {

		List<InstanceExpression<T>> stmtlist = new ArrayList<InstanceExpression<T>>();
		for (MultisetContext stmt_ctx : ctx.multiset())
			stmtlist.add(instances.get(stmt_ctx));
		TermExpression operator = terms.get(ctx.term());
		if (operator == null)
			operator = new StringValue("");
		PredicateExpression P = formula.get(ctx.formula());
		if (P == null)
			P = new TruthValue(true);
		instances.put(ctx, new SetComposite<T>(operator, stmtlist, P,
				new Location(ctx.start, filename)));
	}

	@Override
	public void exitMultiset_else(Multiset_elseContext ctx) {
		SetExpression<T> m1 = sets.get(ctx.multiset(0));
		SetExpression<T> m2 = sets.get(ctx.multiset(1));
		sets.put(ctx, new SetElse<T>(m1, m2));
	}

	@Override
	public void exitMultiset_without(Multiset_withoutContext ctx) {
		SetExpression<T> m1 = sets.get(ctx.multiset(0));
		SetExpression<T> m2 = sets.get(ctx.multiset(1));
		sets.put(ctx, new SetWithout<T>(m1, m2));
	}

	/**
	 * Instances
	 */

	@Override
	public void exitInstance_atomic(Instance_atomicContext ctx) {
		ComponentExpression<T> cexpr = components.get(ctx.component());
		List<TermExpression> list = termsList.get(ctx.list());
		if (list == null)
			list = new ArrayList<TermExpression>();

		List<PortExpression> v = new ArrayList<PortExpression>();

		for (PortExpression p : portlists.get(ctx.ports())) {
			v.add(p);
		}
		PortListExpression var = new PortListExpression(v);
		instances.put(ctx, new ComponentInstance<T>(cexpr, new ListExpression(list), var));
	}

	@Override
	public void exitInstance_product(Instance_productContext ctx) {
		InstanceExpression<T> i1 = instances.get(ctx.instance(0));
		InstanceExpression<T> i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue("*");
		instances.put(ctx, new ProductInstance<T>(s, i1, i2, new Location(ctx.start, filename)));
	}

	@Override
	public void exitInstance_sum(Instance_sumContext ctx) {
		InstanceExpression<T> i1 = instances.get(ctx.instance(0));
		InstanceExpression<T> i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue("+");
		instances.put(ctx, new ProductInstance<T>(s, i1, i2, new Location(ctx.start, filename)));
	}

	@Override
	public void exitInstance_semicolon(Instance_semicolonContext ctx) {
		InstanceExpression<T> i1 = instances.get(ctx.instance(0));
		InstanceExpression<T> i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue(";");
		instances.put(ctx, new ProductInstance<T>(s, i1, i2, new Location(ctx.start, filename)));
	}

	/**
	 * Predicates
	 */

	@Override
	public void exitFormula_boolean(Formula_booleanContext ctx) {
		TruthValue p = new TruthValue(Boolean.parseBoolean(ctx.BOOL().getText()));
		formula.put(ctx, p);
	}

	@Override
	public void exitFormula_componentdefn(Formula_componentdefnContext ctx) {
		List<TermExpression> arguments = Arrays.asList(new VariableTermExpression(variables.get(ctx.var())),
				new ComponentTermExpression<T>(components.get(ctx.component())));
		Relation p = new Relation(RelationSymbol.EQ, arguments, new Location(ctx.start, filename));
		formula.put(ctx, p);
	}

	@Override
	public void exitFormula_structdefn(Formula_structdefnContext ctx) {
		List<ParameterExpression> params = new ArrayList<ParameterExpression>();
		for (ParamContext p : ctx.param())
			params.add(parameters.get(p));
		SignatureExpression sign = new SignatureExpression(params, new ArrayList<NodeExpression>(),
				new Location(ctx.start, filename));
		SetExpression<T> set = new SetComposite<T>();
		VariableExpression var = new VariableExpression(ctx.ID().getText(), new ArrayList<TermExpression>(),
				new Location(ctx.ID().getSymbol(), filename));
		TermExpression te1 = new VariableTermExpression(var);
		TermExpression te2 = new ComponentTermExpression<T>(new ComponentDefinition<T>(sign, set));
		formula.put(ctx, new Relation(RelationSymbol.EQ, Arrays.asList(te1, te2), new Location(ctx.start, filename)));
	}

	@Override
	public void exitFormula_membership(Formula_membershipContext ctx) {
		Membership p = new Membership(new Identifier(ctx.ID().toString()),
				new ListExpression(termsList.get(ctx.list())));
		formula.put(ctx, p);
	}

	@Override
	public void exitFormula_variable(Formula_variableContext ctx) {
		StatementVariable p = new StatementVariable(new VariableTermExpression(variables.get(ctx.var())));
		formula.put(ctx, p);
	}

	@Override
	public void exitFormula_binaryrelation(Formula_binaryrelationContext ctx) {
		List<TermExpression> l = Arrays.asList(terms.get(ctx.term(0)), terms.get(ctx.term(1)));
		switch (ctx.op.getType()) {
		case ReoParser.LEQ:
			formula.put(ctx, new Relation(RelationSymbol.LEQ, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.LT:
			formula.put(ctx, new Relation(RelationSymbol.LT, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.GEQ:
			formula.put(ctx, new Relation(RelationSymbol.GEQ, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.GT:
			formula.put(ctx, new Relation(RelationSymbol.GT, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.EQ:
			formula.put(ctx, new Relation(RelationSymbol.EQ, l, new Location(ctx.start, filename)));
			break;
		case ReoParser.NEQ:
			formula.put(ctx, new Relation(RelationSymbol.NEQ, l, new Location(ctx.start, filename)));
			break;
		default:
			break;
		}
	}

	@Override
	public void exitFormula_negation(Formula_negationContext ctx) {
		formula.put(ctx, new Negation(formula.get(ctx.formula())));
	}

	@Override
	public void exitFormula_existential(Formula_existentialContext ctx) {
		// TODO
		// List<PredicateExpression> l =
		// Arrays.asList(formula.get(ctx.formula(0)),formula.get(ctx.formula(0)));
		// formula.put(ctx, new Conjunction(l));
	}

	@Override
	public void exitFormula_universal(Formula_universalContext ctx) {
		// TODO
		// formula.put(ctx, new Conjunction(formula.get(ctx.formula())));
	}

	@Override
	public void exitFormula_conjunction(Formula_conjunctionContext ctx) {
		List<PredicateExpression> l = Arrays.asList(formula.get(ctx.formula(0)), formula.get(ctx.formula(1)));
		formula.put(ctx, new Conjunction(l));
	}

	@Override
	public void exitFormula_disjunction(Formula_disjunctionContext ctx) {
		List<PredicateExpression> l = Arrays.asList(formula.get(ctx.formula(0)), formula.get(ctx.formula(1)));
		formula.put(ctx, new Disjunction(l));

	}

	@Override
	public void exitFormula_implication(Formula_implicationContext ctx) {
		// TODO interpret P -> Q as !P || Q.
		// List<PredicateExpression> l =
		// Arrays.asList(formula.get(ctx.formula(0)),formula.get(ctx.formula(0)));
		// formula.put(ctx, new Disjunction(l));

	}

	/**
	 * Signatures
	 */

	@Override
	public void exitSign(SignContext ctx) {

		List<NodeExpression> nodes = new ArrayList<NodeExpression>();
		for (NodeExpression n : nodelists.get(ctx.nodes()))
			nodes.add(n);
		List<ParameterExpression> parameters = new ArrayList<ParameterExpression>();
		if (ctx.params() != null)
			for (ParameterExpression p : parameterlists.get(ctx.params()))
				parameters.add(p);

		signatureExpressions.put(ctx, new SignatureExpression(parameters, nodes, new Location(ctx.start, filename)));
	}

	@Override
	public void exitParams(ParamsContext ctx) {
		List<ParameterExpression> list = new ArrayList<ParameterExpression>();
		for (ParamContext param_ctx : ctx.param())
			list.add(parameters.get(param_ctx));
		parameterlists.put(ctx, list);
	}

	@Override
	public void exitParam(ParamContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		TypeTag type = typetags.get(ctx.type());
		// if (type == null) type = new TypeTag(Arrays.asList(""));
		if (type == null)
			type = new TypeTag("");
		parameters.put(ctx, new ParameterExpression(var, type));

		// TODO : add signature option
	}

	@Override
	public void exitNodes(NodesContext ctx) {
		List<NodeExpression> list = new ArrayList<NodeExpression>();
		for (NodeContext node_ctx : ctx.node())
			list.add(nodes.get(node_ctx));
		nodelists.put(ctx, list);
	}

	@Override
	public void exitNode(NodeContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		if (var == null)
			var = new VariableExpression("", new ArrayList<TermExpression>(), new Location(ctx.start, filename));
		TypeTag tag = typetags.get(ctx.type());
		if (tag == null)
			tag = new TypeTag();
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

	/**
	 * Type tags for uninterpreted data
	 */

	@Override
	public void exitType(TypeContext ctx) {
		typetags.put(ctx, new TypeTag(ctx.getText()));
	}

	/**
	 * Interface instantiation
	 */

	@Override
	public void exitPorts(PortsContext ctx) {
		List<PortExpression> list = new ArrayList<PortExpression>();
		for (PortContext port : ctx.port())
			list.add(ports.get(port));
		portlists.put(ctx, list);
	}

	@Override
	public void exitPort(PortContext ctx) {
		PrioType prio = PrioType.NONE;
		if (ctx.prio != null)
			prio = ctx.prio.getType() == ReoParser.AND ? PrioType.AMPERSANT : PrioType.PLUS;
		ports.put(ctx, new PortExpression(prio, variables.get(ctx.var())));
	}

	/**
	 * Variables
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

	/**
	 * Term expressions
	 */

	@Override
	public void exitTerm_natural(Term_naturalContext ctx) {
		terms.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().getText())));
	}

	@Override
	public void exitTerm_boolean(Term_booleanContext ctx) {
		terms.put(ctx, new BooleanValue(Boolean.parseBoolean(ctx.BOOL().getText())));
	}

	@Override
	public void exitTerm_string(Term_stringContext ctx) {
		terms.put(ctx, new StringValue((ctx.STRING().getText())));
	}

	@Override
	public void exitTerm_decimal(Term_decimalContext ctx) {
		terms.put(ctx, new DecimalValue(Double.parseDouble(ctx.DEC().getText())));
	}

	@Override
	public void exitTerm_componentdefn(Term_componentdefnContext ctx) {
		terms.put(ctx, new ComponentTermExpression<T>(components.get(ctx.component())));
	}

	@Override
	public void exitTerm_variable(Term_variableContext ctx) {
		terms.put(ctx, new VariableTermExpression(variables.get(ctx.var())));
	}

	@Override
	public void exitTerm_list(Term_listContext ctx) {
		terms.put(ctx, new ListExpression(termsList.get(ctx.list())));
	}



	@Override
	public void exitTerm_instance(Term_instanceContext ctx) {
		terms.put(ctx, new InstanceTermExpression<T>(instances.get(ctx.instance())));
	}

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

	@Override
	public void exitTerm_exponent(Term_exponentContext ctx) {
		TermExpression e1 = terms.get(ctx.term(0));
		TermExpression e2 = terms.get(ctx.term(1));
		List<TermExpression> l = Arrays.asList(e1, e2);
		terms.put(ctx, new FunctionExpression(FunctionSymbol.POW, l, new Location(ctx.start, filename)));
	}

	@Override
	public void exitTerm_unarymin(Term_unaryminContext ctx) {
		TermExpression e = terms.get(ctx.term());
		List<TermExpression> l = Arrays.asList(e);
		terms.put(ctx, new FunctionExpression(FunctionSymbol.MIN, l, new Location(ctx.start, filename)));
	}

	@Override
	public void exitTerm_brackets(Term_bracketsContext ctx) {
		terms.put(ctx, terms.get(ctx.term()));
	}

	@Override
	public void exitTerm_range(Term_rangeContext ctx) {
		terms.put(ctx, new Range(terms.get(ctx.term(0)), terms.get(ctx.term(1))));
	}

	/**
	 * List of terms
	 */

	@Override
	public void exitList(ListContext ctx) {
		List<TermExpression> list = new ArrayList<TermExpression>();
		for (TermContext expr_ctx : ctx.term())
			list.add(terms.get(expr_ctx));
		termsList.put(ctx, list);
//		terms.put(ctx, new ListExpression(list));
	}
}