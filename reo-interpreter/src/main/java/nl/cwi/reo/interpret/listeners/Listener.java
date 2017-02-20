package nl.cwi.reo.interpret.listeners;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.components.ComponentDefinition;
import nl.cwi.reo.interpret.components.ComponentAtomic;
import nl.cwi.reo.interpret.components.ComponentComposite;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.components.ComponentVariable;
import nl.cwi.reo.interpret.connectors.Component;
import nl.cwi.reo.interpret.connectors.Connector;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.SourceCode;
import nl.cwi.reo.interpret.instances.InstancesExpression;
import nl.cwi.reo.interpret.instances.Set;
import nl.cwi.reo.interpret.nodes.NodeExpression;
import nl.cwi.reo.interpret.parameters.ParameterExpression;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortExpression;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.predicates.PredicateExpression;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.MessageType;
import nl.cwi.reo.interpret.ReoBaseListener;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.ReoParser.Component_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Component_compositeContext;
import nl.cwi.reo.interpret.ReoParser.Component_variableContext;
import nl.cwi.reo.interpret.ReoParser.FileContext;
import nl.cwi.reo.interpret.ReoParser.ImpsContext;
import nl.cwi.reo.interpret.ReoParser.Instance_atomicContext;
import nl.cwi.reo.interpret.ReoParser.MultisetContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_constraintContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_elseContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_setbuilderContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_withoutContext;
import nl.cwi.reo.interpret.ReoParser.PortContext;
import nl.cwi.reo.interpret.ReoParser.PortsContext;
import nl.cwi.reo.interpret.ReoParser.SecnContext;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.interpret.p}.
 * @param <O>
 */
public class Listener<T extends Semantics<T>> extends ReoBaseListener {
	
	// Symbol table
	private ParseTreeProperty<Map<String, String>> symbols = new ParseTreeProperty<Map<String, String>>();
	
	public boolean hasErrors = false;
	
	// File structure
	private ReoFile<T> program;
	private String section = "";	
	private List<String> imports = new ArrayList<String>();
	
	// Components
	private ParseTreeProperty<ComponentExpression<T>> systems = new ParseTreeProperty<ComponentExpression<T>>();
	
	// Blocks
	
	// Values	
	private ParseTreeProperty<List<TermsExpression>> lists = new ParseTreeProperty<List<TermsExpression>>();
	
	// Formula
	private ParseTreeProperty<PredicateExpression> formula = new ParseTreeProperty<PredicateExpression>();
	
	// Instance
	private ParseTreeProperty<InstancesExpression> instances = new ParseTreeProperty<InstancesExpression>();

	// Multiset
	private ParseTreeProperty<InstancesExpression> set = new ParseTreeProperty<InstancesExpression>();	
	
	// Boolean expressions
//	private ParseTreeProperty<BooleanExpression> bools = new ParseTreeProperty<BooleanExpression>();
	
	
	// String expressions
	private ParseTreeProperty<String> strgs = new ParseTreeProperty<String>();
	
	// Signatures
	private ParseTreeProperty<SignatureExpression> signatureExpressions = new ParseTreeProperty<SignatureExpression>();
	private ParseTreeProperty<List<ParameterExpression>> parameterlists = new ParseTreeProperty<List<ParameterExpression>>();
	private ParseTreeProperty<ParameterExpression> parameters = new ParseTreeProperty<ParameterExpression>();
//	private ParseTreeProperty<ParameterType> parametertypes = new ParseTreeProperty<ParameterType>();
	private ParseTreeProperty<List<NodeExpression>> nodelists = new ParseTreeProperty<List<NodeExpression>>();
	private ParseTreeProperty<NodeExpression> nodes = new ParseTreeProperty<NodeExpression>();
	
	// Type tags for uninterpreted data
	private ParseTreeProperty<TypeTag> typetags = new ParseTreeProperty<TypeTag>();

	// Interface instantiation
	private ParseTreeProperty<PortExpression> portList = new ParseTreeProperty<PortExpression>();
	private ParseTreeProperty<List<PortExpression>> interfaces = new ParseTreeProperty<List<PortExpression>>();
	       
	// Variables
	private ParseTreeProperty<VariableExpression> variables = new ParseTreeProperty<VariableExpression>();	
	private ParseTreeProperty<List<TermsExpression>> bounds = new ParseTreeProperty<List<TermsExpression>>();	
	private ParseTreeProperty<SourceCode> sourceCode = new ParseTreeProperty<SourceCode>();	
	
	// Term Expression
	private ParseTreeProperty<TermsExpression> terms = new ParseTreeProperty<TermsExpression>();

	// Semantics
	protected ParseTreeProperty<T> atoms = new ParseTreeProperty<T>();
	
	/**
	 * Gets the program expression.
	 * @return program expression
	 */
	public ReoFile<T> getMain() {
		return program;
	}

	/**
	 * File structure
	 */
	
	@Override
	public void enterFile(FileContext ctx) {
		Map<String, String> s = new HashMap<String, String>();
		s.put(ctx.ID().getText(), "component");
		symbols.put(ctx.component(), s);
	}
	
	@Override
	public void exitFile(FileContext ctx) {
		program = new ReoFile<T>(section, imports, ctx.ID().getText(), systems.get(ctx.component()), ctx.ID().getSymbol());
	}

	@Override
	public void exitSecn(SecnContext ctx) {
		section = ctx.name().getText();
	}

	@Override
	public void exitImps(ImpsContext ctx) {
		imports.add(ctx.name().getText());
	}

	/**
	 * Components
	 * 
	 */
	@Override
	public void exitComponent_variable(Component_variableContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		systems.put(ctx, new ComponentVariable<T>(var));
	}

	@Override
	public void enterComponent_atomic(Component_atomicContext ctx) { }

	@Override
	public void exitComponent_atomic(Component_atomicContext ctx) {
		T atom = atoms.get(ctx.atom());
		SourceCode s;

		Connector<T> comps = null;
		if (atom == null) {
			comps = new Connector<T>();
			hasErrors = true;
			System.err.println(new Message(MessageType.ERROR, "Undefined semantics."));
		} else {
			if((ctx.source()!=null)){
				ctx.source().LANG().toString().toUpperCase();
				s=new SourceCode(ctx.source().STRING().toString(),ctx.source().LANG().toString().toUpperCase());
				}
		}
		systems.put(ctx, new ComponentAtomic<T>(signatureExpressions.get(ctx.sign()), atom, s));
	}

	@Override
	public void exitComponent_composite(Component_compositeContext ctx) {
		systems.put(ctx, new ComponentComposite(signatureExpressions.get(ctx.sign()), (Set) instances.get(ctx.multiset())));		
	}
	
	/**
	 * Multiset
	 */ 
	
	@Override
	public void exitMultiset_constraint(Multiset_constraintContext ctx) {
		InstancesExpression i = instances.get(ctx.instance());
		set.put(ctx, new Set(null, Arrays.asList(i), null));
	}
	
	@Override
	public void exitMultiset_setbuilder(Multiset_setbuilderContext ctx) {
		PredicateExpression p = formula.get(ctx.formula());
		
		List<InstancesExpression> stmtlist = new ArrayList<InstancesExpression>();
		for (MultisetContext stmt_ctx : ctx.multiset())
			stmtlist.add(set.get(stmt_ctx));

		set.put(ctx, new Set((Term) terms.get(ctx.term()),stmtlist,formula.get(ctx.formula())));
	}
	@Override
	public void exitMultiset_else(Multiset_elseContext ctx) {
		InstancesExpression m1 = set.get(ctx.multiset(0));
		InstancesExpression m2 = set.get(ctx.multiset(0));
		
		List<InstancesExpression> l = Arrays.asList(m1,m2);
		
		set.put(ctx, new Set(new StringValue("+"),l,null));
	}
	
	@Override
	public void exitMultiset_without(Multiset_withoutContext ctx) {
		InstancesExpression m1 = set.get(ctx.multiset(0));
		InstancesExpression m2 = set.get(ctx.multiset(0));
		
		List<InstancesExpression> l = Arrays.asList(m1,m2);
		
		set.put(ctx, new Set(new StringValue("-"),l,null));
	}

	/**
	 * Instance
	 * @param ctx
	 */
	@Override
	public void exitInstance_atomic(Instance_atomicContext ctx) {
		ComponentExpression<T> cexpr = systems.get(ctx.component());
		List<TermsExpression> list = lists.get(ctx.list());
		if (list == null) list = new ArrayList<TermsExpression>();
		InterfaceExpression iface = ifaces.get(ctx.ports());
		instances.put(ctx, new AtomicInstance<T>(cexpr, list, iface));
	}
	
	
	@Override
	public void exitStmt_equation(Stmt_equationContext ctx) {
		Expression x = exprs.get(ctx.expr(0));
		Expression y = exprs.get(ctx.expr(1));		
		if (x instanceof Variable) {
			statements.put(ctx, new Definition<T>((Variable)x, y));			
		} else if (x instanceof Variable) {
			statements.put(ctx, new Definition<T>((Variable)y, x));
		} else {
			statements.put(ctx, new Body<T>());
			System.err.println(new Message(MessageType.WARNING, ctx.start, "Ignoring assertion " + ctx.getText() + "."));
		}
	}

	@Override
	public void exitStmt_compdefn(Stmt_compdefnContext ctx) {
		statements.put(ctx, new Definition<T>(variables.get(ctx.var()), systems.get(ctx.rsys())));
	}

	@Override
	public void exitStmt_instance(Stmt_instanceContext ctx) {	
		statements.put(ctx, statements.get(ctx.comp()));
	}

	@Override
	public void exitStmt_block(Stmt_blockContext ctx) {
		statements.put(ctx, statements.get(ctx.block()));
	}

	@Override
	public void exitStmt_iteration(Stmt_iterationContext ctx) {
		VariableName p = new VariableName(ctx.ID().getText(), ctx.start);
		IntegerExpression a = intrs.get(ctx.intr(0));
		IntegerExpression b = intrs.get(ctx.intr(1));
		Statement<T> B = statementlists.get(ctx.block());
		statements.put(ctx, new ForLoop<T>(p, a, b, B));
	}

	@Override
	public void exitStmt_condition(Stmt_conditionContext ctx) {
		List<BooleanExpression> guards = new ArrayList<BooleanExpression>();
		List<Statement<T>> branches = new ArrayList<Statement<T>>();
		for (BoolContext Bool_ctx : ctx.bool())
			guards.add(bools.get(Bool_ctx));
		for (BlockContext block_ctx : ctx.block())
			branches.add(statementlists.get(block_ctx));
		if (guards.size() == branches.size()) {
			guards.add(new BooleanValue(true));
			branches.add(new Body<T>());
		} else {
			guards.add(new BooleanValue(true));
		}
		statements.put(ctx, new IfThenElse<T>(guards, branches));
	}
	
<<<<<<< HEAD

=======
	@Override
	public void exitComp_instance(Comp_instanceContext ctx) {
		Component<T> cexpr = systems.get(ctx.rsys());
		ExpressionList list = lists.get(ctx.list());
		if (list == null) list = new ExpressionList();
		InterfaceExpression iface = ifaces.get(ctx.iface());
		statements.put(ctx, new Instance<T>(cexpr, list, iface));
	}
>>>>>>> 36ff5504231fec52be49f69a24da99dd885f83a4
	
	@Override
	public void exitComp_composition(Comp_compositionContext ctx) {
//		List<ReoBlock<T>> list = new ArrayList<ReoBlock<T>>();
//		list.add(new Definition<T>(new VariableName("*", ctx.mul.start), new StringValue("product")));
//		list.add(stmts.get(ctx.comp(0)));
//		list.add(stmts.get(ctx.comp(1)));
//		stmts.put(ctx, new Body<T>(list));
	}
	
	@Override
	public void exitComp_product(Comp_productContext ctx) {
//		List<ReoBlock<T>> list = new ArrayList<ReoBlock<T>>();
//		list.add(new Definition<T>(new VariableName("*", ctx.mul.start), new StringValue("product")));
//		list.add(stmts.get(ctx.comp(0)));
//		list.add(stmts.get(ctx.comp(1)));
//		stmts.put(ctx, new Body<T>(list));
	}
	
	@Override
	public void exitComp_sum(Comp_sumContext ctx) {
//		List<ReoBlock<T>> list = new ArrayList<ReoBlock<T>>();
//		list.add(new Definition<T>(new VariableName("+", ctx.add.start), new StringValue("sum")));
//		list.add(stmts.get(ctx.comp(0)));
//		list.add(stmts.get(ctx.comp(1)));
//		stmts.put(ctx, new Body<T>(list));
	}
	
	@Override
	public void exitComp_semicolon(Comp_semicolonContext ctx) {
//		List<ReoBlock<T>> list = new ArrayList<ReoBlock<T>>();
//		list.add(new Definition<T>(new VariableName("+", ctx.scl.start), new StringValue("semicolon")));
//		list.add(stmts.get(ctx.comp(0)));
//		list.add(stmts.get(ctx.comp(1)));
//		stmts.put(ctx, new Body<T>(list));
	}
		
	/**
	 * exprs	
	 */

	@Override
	public void exitExpr_variable(Expr_variableContext ctx) {
		exprs.put(ctx, variables.get(ctx.var()));
	}
	
	@Override
	public void exitExpr_list(Expr_listContext ctx) {
		exprs.put(ctx, lists.get(ctx.list()));
	}

	@Override
	public void exitExpr_string(Expr_stringContext ctx) {
		exprs.put(ctx, strgs.get(ctx.strg()));
	}

	@Override
	public void exitExpr_boolean(Expr_booleanContext ctx) {
		exprs.put(ctx, bools.get(ctx.bool()));
	}

	@Override
	public void exitExpr_integer(Expr_integerContext ctx) {
		exprs.put(ctx, intrs.get(ctx.intr()));
	}

	@Override
	public void exitExpr_component(Expr_componentContext ctx) {
		exprs.put(ctx, systems.get(ctx.rsys()));
	}

	@Override
	public void exitList(ListContext ctx) {
		List<Expression> list = new ArrayList<Expression>();
		for (ExprContext expr_ctx : ctx.expr())
			list.add(exprs.get(expr_ctx));
		lists.put(ctx, new ExpressionList(list));
	}
	
	/**
	 * Formula :
	 */
	
	@Override
	public void exitFormula_binaryrelation(Formula_binaryrelationContext ctx) {
		List<Term> l = Arrays.asList(terms.get(ctx.term(0)),terms.get(ctx.term(0)));
		switch(ctx.op.getType()){
		case ReoParser.LEQ:
			formula.put(ctx, new Relation(RelationSymbol.LEQ,l,ctx.start));
			break;
		case ReoParser.LT:
			formula.put(ctx, new Relation(RelationSymbol.LT,l,ctx.start));
			break;
		case ReoParser.GEQ:
			formula.put(ctx, new Relation(RelationSymbol.GEQ,l,ctx.start));
			break;
		case ReoParser.GT:
			formula.put(ctx, new Relation(RelationSymbol.GT,l,ctx.start));
			break;
		case ReoParser.EQ:
			formula.put(ctx, new Relation(RelationSymbol.EQ,l,ctx.start));
			break;
		case ReoParser.NEQ:
			formula.put(ctx, new Relation(RelationSymbol.NEQ,l,ctx.start));
			break;
		default:
			break;
		}
	}
	@Override
	public void exitFormula_conjunction(Formula_conjunctionContext ctx) {
		List<Formula> l = Arrays.asList(formula.get(ctx.formula(0)),formula.get(ctx.formula(0)));
		formula.put(ctx, new Conjunction(l));
	}
	@Override
	public void exitFormula_disjunction(Formula_disjunctionContext ctx) {
		List<Formula> l = Arrays.asList(formula.get(ctx.formula(0)),formula.get(ctx.formula(0)));
		formula.put(ctx, new Disjunction(l));

	}
		
	/**
	 * List of terms	
	 */

	@Override
	public void exitList(ListContext ctx) {
		List<Term> list = new ArrayList<Term>();
		for (TermContext expr_ctx : ctx.term())
			list.add(terms.get(expr_ctx));
		lists.put(ctx, list);
	}
	
	/**
	 * Signatures
	 */

	@Override
	public void exitSign(SignContext ctx) {
		ParameterList params = parameterlists.get(ctx.params());
		if (params == null) params = new ParameterList();
		NodeList nodes = nodelists.get(ctx.nodes());
		signatureExpressions.put(ctx, new SignatureExpression(params, nodes, ctx.start));
	}

	@Override
	public void exitParams(ParamsContext ctx) {
		List<Parameter> list = new ArrayList<Parameter>();
		for (ParamContext param_ctx : ctx.param())
			list.add(parameters.get(param_ctx));
		parameterlists.put(ctx, new ParameterList(list));
	}

	@Override
	public void exitParam(ParamContext ctx) {
		Variable var = variables.get(ctx.var());
		ParameterType type = parametertypes.get(ctx.type());
		if (type == null) type = new TypeTag("");
		parameters.put(ctx, new Parameter(var, type));
		
		// TODO : add signature option
	}

	@Override
	public void exitNodes(NodesContext ctx) {
		List<Node> list = new ArrayList<Node>();
		for (NodeContext node_ctx : ctx.node())
			list.add(nodes.get(node_ctx));
		nodelists.put(ctx, new NodeList(list));
	}

	@Override
	public void exitNode(NodeContext ctx) {
		Variable var = variables.get(ctx.var());
		if (var == null) var = new VariableName("", ctx.start);
		TypeTag tag = typetags.get(ctx.type());
		if (tag == null) tag = new TypeTag();
		NodeType type = NodeType.MIXED;
		if (ctx.io != null) {
			switch (ctx.io.getType()) {
			case ReoParser.IN:
				type = NodeType.SOURCE;
				break;
			case ReoParser.OUT:
				type = NodeType.SINK;
				break;
			case ReoParser.MIX:
				type = NodeType.MIXED;
				break;
			default:
				break;
			}
		}
		nodes.put(ctx, new Node(var, type, tag));
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
			list.add(portList.get(port));
		interfaces.put(ctx,list);
	}

	@Override
	public void exitPort(PortContext ctx) {
		PrioType prio = PrioType.NONE;
		if (ctx.prio != null)
			prio = ctx.prio.getType() == ReoParser.AND ? PrioType.AMPERSANT : PrioType.PLUS ;
		portList.put(ctx, new Port(variables.get(ctx.var()), prio));
	}
     
	/**
	 * Variables	
	 */

	@Override
	public void exitVar(VarContext ctx) {		
		String name = ctx.name().getText();	
		for (String imprt : imports) {
			if (imprt.endsWith(name)) {
				name = imprt;
				break;
			}
		}		
		List<List<Term>> indices = new ArrayList<List<Term>>();		
		for (IndicesContext indices_ctx : ctx.indices())
			indices.add(bounds.get(indices_ctx));
		if (indices.isEmpty()) {
			variables.put(ctx, new VariableName(name, ctx.start));
		} else { 
			// TODO : define the type of indices
			variables.put(ctx, new Variable(name, indices, ctx.start));
		}
	}

	@Override
	public void exitIndices(IndicesContext ctx) {
		List<Term> list = new ArrayList<Term>();
		for (TermContext Intr_ctx : ctx.term())
			list.add(terms.get(Intr_ctx));
		bounds.put(ctx, list);
	}

	
	/**
	 * Term expressions
	 */
	@Override
	public void exitTerm_natural(Term_naturalContext ctx) {
		terms.put(ctx, new Value(Integer.parseInt(ctx.NAT().getText())));		
	}
	

	@Override
	public void exitTerm_boolean(Term_booleanContext ctx) {
		terms.put(ctx, new Value(Boolean.parseBoolean(ctx.BOOL().getText())));			
	}
	
	@Override
	public void exitTerm_string(Term_stringContext ctx) {
		terms.put(ctx, new Value((ctx.STRING().getText())));			
	}
	
	@Override
	public void exitTerm_decimal(Term_decimalContext ctx) {
		terms.put(ctx, new Value(Double.parseDouble(ctx.DEC().getText())));			
	}
	
	@Override
	public void exitTerm_componentdefn(Term_componentdefnContext ctx) {
		terms.put(ctx, new Datum(systems.get(ctx.component())));			
	}
	
	@Override
	public void exitTerm_variable(Term_variableContext ctx) {
		terms.put(ctx, new Datum(variableExpressions.get(ctx.var())));
	}
	
	@Override
	public void exitTerm_instance(Term_instanceContext ctx) {
		terms.put(ctx, new Datum(statements.get(ctx.instance())));
	}

	@Override
	public void exitTerm_operation(Term_operationContext ctx) {
		Term e1 = terms.get(ctx.term(0));
		Term e2 = terms.get(ctx.term(1));		
		List<Term> l = Arrays.asList(e1,e2);
		switch(ctx.op.getType()){
		case ReoParser.DIV:
			terms.put(ctx, new Function(FunctionSymbol.DIV,l,ctx.start));
		case ReoParser.MUL:
			terms.put(ctx, new Function(FunctionSymbol.MUL,l,ctx.start));
		case ReoParser.MOD:
			terms.put(ctx, new Function(FunctionSymbol.MOD,l,ctx.op));
		case ReoParser.ADD:
			terms.put(ctx, new Function(FunctionSymbol.ADD,l,ctx.op));
		case ReoParser.MIN:
			terms.put(ctx, new Function(FunctionSymbol.MIN,l,ctx.op));
		} 
	}

	@Override
	public void exitTerm_exponent(Term_exponentContext ctx) {
		Term e1 = terms.get(ctx.term(0));
		Term e2 = terms.get(ctx.term(1));
		List<Term> l = Arrays.asList(e1,e2);
		terms.put(ctx, new Function(FunctionSymbol.POW,l,ctx.start));
	}

	@Override
	public void exitTerm_unarymin(Term_unaryminContext ctx) {
		Term e = terms.get(ctx.term());
		List<Term> l = Arrays.asList(e);
		terms.put(ctx, new Function(FunctionSymbol.MIN,l,ctx.start));
	}

	@Override
	public void exitTerm_brackets(Term_bracketsContext ctx) {
		terms.put(ctx, terms.get(ctx.term()));
	}
	
}