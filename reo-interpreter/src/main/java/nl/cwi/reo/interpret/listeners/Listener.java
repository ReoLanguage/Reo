package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.Message;
import nl.cwi.reo.errors.MessageType;
import nl.cwi.reo.interpret.ReoBaseListener;
import nl.cwi.reo.interpret.ReoFile;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.ReoParser.BoolContext;
import nl.cwi.reo.interpret.ReoParser.Bool_booleanContext;
import nl.cwi.reo.interpret.ReoParser.Bool_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Bool_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Bool_disjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Bool_negationContext;
import nl.cwi.reo.interpret.ReoParser.Bool_relationContext;
import nl.cwi.reo.interpret.ReoParser.Bool_variableContext;
import nl.cwi.reo.interpret.ReoParser.Comp_compositionContext;
import nl.cwi.reo.interpret.ReoParser.BlockContext;
import nl.cwi.reo.interpret.ReoParser.Rsys_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Rsys_compositeContext;
import nl.cwi.reo.interpret.ReoParser.Rsys_variableContext;
import nl.cwi.reo.interpret.ReoParser.Comp_instanceContext;
import nl.cwi.reo.interpret.ReoParser.Comp_productContext;
import nl.cwi.reo.interpret.ReoParser.Comp_semicolonContext;
import nl.cwi.reo.interpret.ReoParser.Comp_sumContext;
import nl.cwi.reo.interpret.ReoParser.Expr_stringContext;
import nl.cwi.reo.interpret.ReoParser.Expr_booleanContext;
import nl.cwi.reo.interpret.ReoParser.Expr_integerContext;
import nl.cwi.reo.interpret.ReoParser.Expr_componentContext;
import nl.cwi.reo.interpret.ReoParser.FileContext;
import nl.cwi.reo.interpret.ReoParser.IntrContext;
import nl.cwi.reo.interpret.ReoParser.Intr_addsubContext;
import nl.cwi.reo.interpret.ReoParser.Intr_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Intr_exponentContext;
import nl.cwi.reo.interpret.ReoParser.Intr_multdivremContext;
import nl.cwi.reo.interpret.ReoParser.Intr_naturalContext;
import nl.cwi.reo.interpret.ReoParser.Intr_unaryminContext;
import nl.cwi.reo.interpret.ReoParser.Intr_variableContext;
import nl.cwi.reo.interpret.ReoParser.IfaceContext;
import nl.cwi.reo.interpret.ReoParser.ImpsContext;
import nl.cwi.reo.interpret.ReoParser.IndicesContext;
import nl.cwi.reo.interpret.ReoParser.ListContext;
import nl.cwi.reo.interpret.ReoParser.NodeContext;
import nl.cwi.reo.interpret.ReoParser.NodesContext;
import nl.cwi.reo.interpret.ReoParser.ParamContext;
import nl.cwi.reo.interpret.ReoParser.ParamsContext;
import nl.cwi.reo.interpret.ReoParser.Ptype_signatureContext;
import nl.cwi.reo.interpret.ReoParser.Ptype_typetagContext;
import nl.cwi.reo.interpret.ReoParser.RangeContext;
import nl.cwi.reo.interpret.ReoParser.Range_exprContext;
import nl.cwi.reo.interpret.ReoParser.Range_listContext;
import nl.cwi.reo.interpret.ReoParser.Range_variableContext;
import nl.cwi.reo.interpret.ReoParser.RnodeContext;
import nl.cwi.reo.interpret.ReoParser.SignContext;
import nl.cwi.reo.interpret.ReoParser.StmtContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_blockContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_conditionContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_instanceContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_equationContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_compdefnContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_iterationContext;
import nl.cwi.reo.interpret.ReoParser.Strg_stringContext;
import nl.cwi.reo.interpret.ReoParser.Strg_variableContext;
import nl.cwi.reo.interpret.ReoParser.TypeContext;
import nl.cwi.reo.interpret.ReoParser.SecnContext;
import nl.cwi.reo.interpret.ReoParser.VarContext;
import nl.cwi.reo.interpret.blocks.Body;
import nl.cwi.reo.interpret.blocks.Assembly;
import nl.cwi.reo.interpret.blocks.ReoBlock;
import nl.cwi.reo.interpret.blocks.Definition;
import nl.cwi.reo.interpret.blocks.ForLoop;
import nl.cwi.reo.interpret.blocks.IfThenElse;
import nl.cwi.reo.interpret.blocks.InstanceReference;
import nl.cwi.reo.interpret.booleans.BooleanConjunction;
import nl.cwi.reo.interpret.booleans.BooleanDisequality;
import nl.cwi.reo.interpret.booleans.BooleanDisjunction;
import nl.cwi.reo.interpret.booleans.BooleanEquality;
import nl.cwi.reo.interpret.booleans.BooleanExpression;
import nl.cwi.reo.interpret.booleans.BooleanGreaterOrEqual;
import nl.cwi.reo.interpret.booleans.BooleanGreaterThan;
import nl.cwi.reo.interpret.booleans.BooleanLessOrEqual;
import nl.cwi.reo.interpret.booleans.BooleanLessThan;
import nl.cwi.reo.interpret.booleans.BooleanValue;
import nl.cwi.reo.interpret.booleans.BooleanVariable;
import nl.cwi.reo.interpret.integers.IntegerAddition;
import nl.cwi.reo.interpret.integers.IntegerDivision;
import nl.cwi.reo.interpret.integers.IntegerExponentiation;
import nl.cwi.reo.interpret.integers.IntegerExpression;
import nl.cwi.reo.interpret.integers.IntegerMultiplication;
import nl.cwi.reo.interpret.integers.IntegerRemainder;
import nl.cwi.reo.interpret.integers.IntegerSubstraction;
import nl.cwi.reo.interpret.integers.IntegerUnaryMinus;
import nl.cwi.reo.interpret.integers.IntegerValue;
import nl.cwi.reo.interpret.integers.IntegerVariable;
import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.ranges.RangeList;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.ComponentList;
import nl.cwi.reo.interpret.signatures.InterfaceExpression;
import nl.cwi.reo.interpret.signatures.InterfaceNode;
import nl.cwi.reo.interpret.signatures.Node;
import nl.cwi.reo.interpret.signatures.NodeList;
import nl.cwi.reo.interpret.signatures.NodeType;
import nl.cwi.reo.interpret.signatures.Parameter;
import nl.cwi.reo.interpret.signatures.ParameterList;
import nl.cwi.reo.interpret.signatures.ParameterType;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.signatures.TypeTag;
import nl.cwi.reo.interpret.strings.StringExpression;
import nl.cwi.reo.interpret.strings.StringValue;
import nl.cwi.reo.interpret.strings.StringVariable;
import nl.cwi.reo.interpret.systems.ReoSystemComposite;
import nl.cwi.reo.interpret.systems.ReoSystem;
import nl.cwi.reo.interpret.systems.ReoSystemValue;
import nl.cwi.reo.interpret.systems.ReoSystemVariable;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableRange;
import nl.cwi.reo.semantics.api.PrioType;
import nl.cwi.reo.semantics.api.Semantics;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.interpret.p}.
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
	private ParseTreeProperty<ReoSystem<T>> systems = new ParseTreeProperty<ReoSystem<T>>();
	
	// Blocks
	private ParseTreeProperty<Body<T>> bodies = new ParseTreeProperty<Body<T>>();
	private ParseTreeProperty<ReoBlock<T>> blocks = new ParseTreeProperty<ReoBlock<T>>();
	
	// Values	
	private ParseTreeProperty<Range> ranges = new ParseTreeProperty<Range>();	
	private ParseTreeProperty<Expression> exprs = new ParseTreeProperty<Expression>();
	private ParseTreeProperty<RangeList> lists = new ParseTreeProperty<RangeList>();
	
	// Boolean expressions
	private ParseTreeProperty<BooleanExpression> bools = new ParseTreeProperty<BooleanExpression>();
	
	// String expressions
	private ParseTreeProperty<StringExpression> strgs = new ParseTreeProperty<StringExpression>();
	
	// Signatures
	private ParseTreeProperty<SignatureExpression> signatureExpressions = new ParseTreeProperty<SignatureExpression>();
	private ParseTreeProperty<ParameterList> parameterlists = new ParseTreeProperty<ParameterList>();
	private ParseTreeProperty<Parameter> parameters = new ParseTreeProperty<Parameter>();
	private ParseTreeProperty<ParameterType> parametertypes = new ParseTreeProperty<ParameterType>();
	private ParseTreeProperty<NodeList> nodelists = new ParseTreeProperty<NodeList>();
	private ParseTreeProperty<Node> nodes = new ParseTreeProperty<Node>();
	
	// Type tags for uninterpreted data
	private ParseTreeProperty<TypeTag> typetags = new ParseTreeProperty<TypeTag>();

	// Interface instantiation
	private ParseTreeProperty<InterfaceExpression> ifaces = new ParseTreeProperty<InterfaceExpression>();
	private ParseTreeProperty<InterfaceNode> rnodes = new ParseTreeProperty<InterfaceNode>();
	       
	// Variables
	private ParseTreeProperty<Variable> variables = new ParseTreeProperty<Variable>();	
	private ParseTreeProperty<List<IntegerExpression>> bounds = new ParseTreeProperty<List<IntegerExpression>>();	

	// Integer expressions
	private ParseTreeProperty<IntegerExpression> intrs = new ParseTreeProperty<IntegerExpression>();

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
		symbols.put(ctx.rsys(), s);
	}
	
	@Override
	public void exitFile(FileContext ctx) {
		program = new ReoFile<T>(section, imports, ctx.ID().getText(), systems.get(ctx.rsys()));
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
	 * Reo Systems.
	 */

	@Override
	public void enterRsys_variable(Rsys_variableContext ctx) { }

	@Override
	public void exitRsys_variable(Rsys_variableContext ctx) {
		Variable var = variables.get(ctx.var());
		systems.put(ctx, new ReoSystemVariable<T>(var));
	}

	@Override
	public void enterRsys_atomic(Rsys_atomicContext ctx) { }

	@Override
	public void exitRsys_atomic(Rsys_atomicContext ctx) {
		T atom = atoms.get(ctx.atom());
		ComponentList<T> comps = null;
		if (atom == null) {
			comps = new ComponentList<T>();
			hasErrors = true;
			System.err.println(new Message(MessageType.ERROR, ctx.start, "Undefined semantics."));
		} else {
			comps = new ComponentList<T>(atom);
		}
		Assembly<T> prog = new Assembly<T>(new Definitions(), comps);
		systems.put(ctx, new ReoSystemValue<T>(signatureExpressions.get(ctx.sign()), prog));
	}

	@Override
	public void enterRsys_composite(Rsys_compositeContext ctx) { }

	@Override
	public void exitRsys_composite(Rsys_compositeContext ctx) {
		systems.put(ctx, new ReoSystemComposite<T>(signatureExpressions.get(ctx.sign()), bodies.get(ctx.block())));		
	}
	
	/**
	 * Blocks
	 */
	
	@Override
	public void exitBlock(BlockContext ctx) {
		List<ReoBlock<T>> stmtlist = new ArrayList<ReoBlock<T>>();
		for (StmtContext stmt_ctx : ctx.stmt())
			stmtlist.add(blocks.get(stmt_ctx));
		bodies.put(ctx, new Body<T>(stmtlist));
	}

	@Override
	public void exitStmt_equation(Stmt_equationContext ctx) {
		Range x = ranges.get(ctx.range(0));
		Range y = ranges.get(ctx.range(1));		
		if (x instanceof Variable) {
			blocks.put(ctx, new Definition<T>((Variable)x, y));			
		} else if (x instanceof Variable) {
			blocks.put(ctx, new Definition<T>((Variable)y, x));
		} else {
			blocks.put(ctx, new Assembly<T>());
			System.err.println(new Message(MessageType.WARNING, ctx.start, "Ignoring assertion " + ctx.getText() + "."));
		}
	}

	@Override
	public void exitStmt_compdefn(Stmt_compdefnContext ctx) {
		blocks.put(ctx, new Definition<T>(variables.get(ctx.var()), systems.get(ctx.rsys())));
	}

	@Override
	public void exitStmt_instance(Stmt_instanceContext ctx) {	
		blocks.put(ctx, blocks.get(ctx.comp()));
	}

	@Override
	public void exitStmt_block(Stmt_blockContext ctx) {
		blocks.put(ctx, blocks.get(ctx.block()));
	}

	@Override
	public void exitStmt_iteration(Stmt_iterationContext ctx) {
		VariableName p = new VariableName(ctx.ID().getText(), ctx.start);
		IntegerExpression a = intrs.get(ctx.intr(0));
		IntegerExpression b = intrs.get(ctx.intr(1));
		ReoBlock<T> B = bodies.get(ctx.block());
		blocks.put(ctx, new ForLoop<T>(p, a, b, B));
	}

	@Override
	public void exitStmt_condition(Stmt_conditionContext ctx) {
		List<BooleanExpression> guards = new ArrayList<BooleanExpression>();
		List<ReoBlock<T>> branches = new ArrayList<ReoBlock<T>>();
		for (BoolContext Bool_ctx : ctx.bool())
			guards.add(bools.get(Bool_ctx));
		for (BlockContext block_ctx : ctx.block())
			branches.add(bodies.get(block_ctx));
		if (guards.size() == branches.size()) {
			guards.add(new BooleanValue(true));
			branches.add(new Assembly<T>());
		} else {
			guards.add(new BooleanValue(true));
		}
		blocks.put(ctx, new IfThenElse<T>(guards, branches));
	}
	
	@Override
	public void exitComp_instance(Comp_instanceContext ctx) {
		ReoSystem<T> cexpr = systems.get(ctx.rsys());
		RangeList list = lists.get(ctx.list());
		if (list == null) list = new RangeList();
		InterfaceExpression iface = ifaces.get(ctx.iface());
		blocks.put(ctx, new InstanceReference<T>(cexpr, list, iface));
	}
	
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
	 * Ranges	
	 */

	@Override
	public void exitRange_variable(Range_variableContext ctx) {
		ranges.put(ctx, variables.get(ctx.var()));
	}
	
	@Override
	public void exitRange_expr(Range_exprContext ctx) {
		ranges.put(ctx, exprs.get(ctx.expr()));
	}
	
	@Override
	public void exitRange_list(Range_listContext ctx) {
		ranges.put(ctx, lists.get(ctx.list()));
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
		List<Range> list = new ArrayList<Range>();
		for (RangeContext expr_ctx : ctx.range())
			list.add(ranges.get(expr_ctx));
		lists.put(ctx, new RangeList(list));
	}
	
	/**
	 * Boolean expressions
	 */

	@Override
	public void exitBool_relation(Bool_relationContext ctx) {
		
		IntegerExpression e1 = intrs.get(ctx.intr(0));
		IntegerExpression e2 = intrs.get(ctx.intr(1));
		
		switch (ctx.op.getType()) {
		case ReoParser.LEQ:
			bools.put(ctx, new BooleanLessOrEqual(e1, e2));
			break;
		case ReoParser.LT:
			bools.put(ctx, new BooleanLessThan(e1, e2));
			break;
		case ReoParser.GEQ:
			bools.put(ctx, new BooleanGreaterOrEqual(e1, e2));
			break;
		case ReoParser.GT:
			bools.put(ctx, new BooleanGreaterThan(e1, e2));
			break;
		case ReoParser.EQ:
			bools.put(ctx, new BooleanEquality(e1, e2));
			break;
		case ReoParser.NEQ:
			bools.put(ctx, new BooleanDisequality(e1, e2));
			break;
		default:
			break;
		}
	}

	@Override
	public void exitBool_boolean(Bool_booleanContext ctx) {
		bools.put(ctx, new BooleanValue(Boolean.parseBoolean(ctx.BOOL().getText())));
	}

	@Override
	public void exitBool_disjunction(Bool_disjunctionContext ctx) {
		BooleanExpression e1 = bools.get(ctx.bool(0));
		BooleanExpression e2 = bools.get(ctx.bool(1));
		bools.put(ctx, new BooleanDisjunction(e1, e2));
	}

	@Override
	public void exitBool_negation(Bool_negationContext ctx) {
		bools.put(ctx, bools.get(ctx.bool()));
	}

	@Override
	public void exitBool_conjunction(Bool_conjunctionContext ctx) {
		BooleanExpression e1 = bools.get(ctx.bool(0));
		BooleanExpression e2 = bools.get(ctx.bool(1));
		bools.put(ctx, new BooleanConjunction(e1, e2));
	}

	@Override
	public void exitBool_brackets(Bool_bracketsContext ctx) {
		bools.put(ctx, bools.get(ctx.bool()));
	}

	@Override
	public void exitBool_variable(Bool_variableContext ctx) {
		bools.put(ctx, new BooleanVariable(variables.get(ctx.var())));		
	}

	/**
	 * String Expressions
	 */
	
	@Override
	public void exitStrg_string(Strg_stringContext ctx) {
		exprs.put(ctx, new StringValue(ctx.STRING().getText()));	
	}

	@Override
	public void exitStrg_variable(Strg_variableContext ctx) {
		strgs.put(ctx, new StringVariable(variables.get(ctx.var())));		
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
		ParameterType type = parametertypes.get(ctx.ptype());
		if (type == null) type = new TypeTag("");
		parameters.put(ctx, new Parameter(var, type));
	}

	@Override
	public void exitPtype_typetag(Ptype_typetagContext ctx) {
		parametertypes.put(ctx, new TypeTag(ctx.type().getText()));
	}


	@Override
	public void exitPtype_signature(Ptype_signatureContext ctx) {
		parametertypes.put(ctx, signatureExpressions.get(ctx.sign()));
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
	public void exitIface(IfaceContext ctx) {
		List<InterfaceNode> list = new ArrayList<InterfaceNode>();
		for (RnodeContext node_ctx : ctx.rnode())
			list.add(rnodes.get(node_ctx));
		ifaces.put(ctx, new InterfaceExpression(list, ctx.start));
	}

	@Override
	public void exitRnode(RnodeContext ctx) {
		PrioType prio = PrioType.NONE;
		if (ctx.prio != null)
			prio = ctx.prio.getType() == ReoParser.AMP ? PrioType.AMPERSANT : PrioType.PLUS ;
		rnodes.put(ctx, new InterfaceNode(variables.get(ctx.var()), prio));
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
		List<List<IntegerExpression>> indices = new ArrayList<List<IntegerExpression>>();		
		for (IndicesContext indices_ctx : ctx.indices())
			indices.add(bounds.get(indices_ctx));
		if (indices.isEmpty()) {
			variables.put(ctx, new VariableName(name, ctx.start));
		} else { 
			variables.put(ctx, new VariableRange(name, indices, ctx.start));
		}
	}

	@Override
	public void exitIndices(IndicesContext ctx) {
		List<IntegerExpression> list = new ArrayList<IntegerExpression>();
		for (IntrContext Intr_ctx : ctx.intr())
			list.add(intrs.get(Intr_ctx));
		bounds.put(ctx, list);
	}

	/**
	 * Integer expressions
	 */

	@Override
	public void exitIntr_variable(Intr_variableContext ctx) {
		intrs.put(ctx, new IntegerVariable(variables.get(ctx.var())));
	}

	@Override
	public void exitIntr_multdivrem(Intr_multdivremContext ctx) {
		IntegerExpression e1 = intrs.get(ctx.intr(0));
		IntegerExpression e2 = intrs.get(ctx.intr(1));		
		switch (ctx.op.getType()) {
		case ReoParser.MUL:
			intrs.put(ctx, new IntegerMultiplication(e1, e2));
			break;
		case ReoParser.DIV:
			intrs.put(ctx, new IntegerDivision(e1, e2, ctx.DIV().getSymbol()));
			break;
		case ReoParser.MOD:
			intrs.put(ctx, new IntegerRemainder(e1, e2, ctx.MOD().getSymbol()));
			break;
		default:
			break;
		}
	}

	@Override
	public void exitIntr_exponent(Intr_exponentContext ctx) {
		IntegerExpression e1 = intrs.get(ctx.intr(0));
		IntegerExpression e2 = intrs.get(ctx.intr(1));
		intrs.put(ctx, new IntegerExponentiation(e1, e2));
	}

	@Override
	public void exitIntr_addsub(Intr_addsubContext ctx) {
		IntegerExpression e1 = intrs.get(ctx.intr(0));
		IntegerExpression e2 = intrs.get(ctx.intr(1));		
		switch (ctx.op.getType()) {
		case ReoParser.ADD:
			intrs.put(ctx, new IntegerAddition(e1, e2));
			break;
		case ReoParser.MIN:
			intrs.put(ctx, new IntegerSubstraction(e1, e2));
			break;
		default:
			break;
		}
	}

	@Override
	public void exitIntr_unarymin(Intr_unaryminContext ctx) {
		IntegerExpression e = intrs.get(ctx.intr());
		intrs.put(ctx, new IntegerUnaryMinus(e));
	}

	@Override
	public void exitIntr_natural(Intr_naturalContext ctx) {
		intrs.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().getText())));
	}

	@Override
	public void exitIntr_brackets(Intr_bracketsContext ctx) {
		intrs.put(ctx, intrs.get(ctx.intr()));
	}
	
}