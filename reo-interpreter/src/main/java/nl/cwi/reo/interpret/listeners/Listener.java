package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.errors.ErrorLog;
import nl.cwi.reo.interpret.ReoBaseListener;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.ReoParser.BexprContext;
import nl.cwi.reo.interpret.ReoParser.Bexpr_booleanContext;
import nl.cwi.reo.interpret.ReoParser.Bexpr_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Bexpr_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Bexpr_disjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Bexpr_negationContext;
import nl.cwi.reo.interpret.ReoParser.Bexpr_relationContext;
import nl.cwi.reo.interpret.ReoParser.Bexpr_variableContext;
import nl.cwi.reo.interpret.ReoParser.BodyContext;
import nl.cwi.reo.interpret.ReoParser.Cexpr_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Cexpr_compositeContext;
import nl.cwi.reo.interpret.ReoParser.Cexpr_variableContext;
import nl.cwi.reo.interpret.ReoParser.Expr_stringContext;
import nl.cwi.reo.interpret.ReoParser.Expr_booleanContext;
import nl.cwi.reo.interpret.ReoParser.Expr_integerContext;
import nl.cwi.reo.interpret.ReoParser.Expr_componentContext;
import nl.cwi.reo.interpret.ReoParser.FileContext;
import nl.cwi.reo.interpret.ReoParser.IexprContext;
import nl.cwi.reo.interpret.ReoParser.Iexpr_addsubContext;
import nl.cwi.reo.interpret.ReoParser.Iexpr_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Iexpr_exponentContext;
import nl.cwi.reo.interpret.ReoParser.Iexpr_multdivremContext;
import nl.cwi.reo.interpret.ReoParser.Iexpr_naturalContext;
import nl.cwi.reo.interpret.ReoParser.Iexpr_unaryminContext;
import nl.cwi.reo.interpret.ReoParser.Iexpr_variableContext;
import nl.cwi.reo.interpret.ReoParser.IfaceContext;
import nl.cwi.reo.interpret.ReoParser.ImpsContext;
import nl.cwi.reo.interpret.ReoParser.IndicesContext;
import nl.cwi.reo.interpret.ReoParser.ListContext;
import nl.cwi.reo.interpret.ReoParser.NameContext;
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
import nl.cwi.reo.interpret.ReoParser.SignContext;
import nl.cwi.reo.interpret.ReoParser.StmtContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_conditionContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_instanceContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_equationContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_compdefnContext;
import nl.cwi.reo.interpret.ReoParser.Stmt_iterationContext;
import nl.cwi.reo.interpret.ReoParser.TypeContext;
import nl.cwi.reo.interpret.ReoParser.SecnContext;
import nl.cwi.reo.interpret.ReoParser.VarContext;
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
import nl.cwi.reo.interpret.components.ComponentComposite;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.components.ComponentValue;
import nl.cwi.reo.interpret.components.ComponentVariable;
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
import nl.cwi.reo.interpret.programs.ProgramBody;
import nl.cwi.reo.interpret.programs.ProgramEquation;
import nl.cwi.reo.interpret.programs.ProgramExpression;
import nl.cwi.reo.interpret.programs.ProgramFile;
import nl.cwi.reo.interpret.programs.ProgramForLoop;
import nl.cwi.reo.interpret.programs.ProgramIfThenElse;
import nl.cwi.reo.interpret.programs.ProgramInstance;
import nl.cwi.reo.interpret.programs.ProgramValue;
import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.ranges.RangeList;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.signatures.InterfaceExpression;
import nl.cwi.reo.interpret.signatures.Node;
import nl.cwi.reo.interpret.signatures.NodeList;
import nl.cwi.reo.interpret.signatures.NodeType;
import nl.cwi.reo.interpret.signatures.Parameter;
import nl.cwi.reo.interpret.signatures.ParameterList;
import nl.cwi.reo.interpret.signatures.ParameterType;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.signatures.TypeTag;
import nl.cwi.reo.interpret.strings.StringValue;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableRange;
import nl.cwi.reo.semantics.Semantics;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.interpret.p}.
 */
public class Listener<T extends Semantics<T>> extends ReoBaseListener {
	
	// Error log
	private ErrorLog log;
	
	// Symbol table
//	private Map<String, ValueType> symbols = new HashMap<String, ValueType>();
	
	// File structure
	private ProgramFile<T> program;
	private String section = "";	
	private List<String> imports = new ArrayList<String>();
	
	// Components
	private ParseTreeProperty<ComponentExpression<T>> cexprs = new ParseTreeProperty<ComponentExpression<T>>();
	
	// Bodies
	private ParseTreeProperty<ProgramExpression<T>> progs = new ParseTreeProperty<ProgramExpression<T>>();
	
	// Values	
	private ParseTreeProperty<Range> arrays = new ParseTreeProperty<Range>();	
	private ParseTreeProperty<Expression> exprs = new ParseTreeProperty<Expression>();
	private ParseTreeProperty<RangeList> lists = new ParseTreeProperty<RangeList>();
	
	// Boolean expressions
	private ParseTreeProperty<BooleanExpression> bexprs = new ParseTreeProperty<BooleanExpression>();
	
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
	       
	// Variables
	private ParseTreeProperty<Variable> variables = new ParseTreeProperty<Variable>();	
	private ParseTreeProperty<List<IntegerExpression>> bounds = new ParseTreeProperty<List<IntegerExpression>>();	

	// Integer expressions
	private ParseTreeProperty<IntegerExpression> iexprs = new ParseTreeProperty<IntegerExpression>();

	// Semantics
	protected ParseTreeProperty<T> atoms = new ParseTreeProperty<T>();
	
	/**
	 * Gets the program expression.
	 * @return program expression
	 */
	public ProgramFile<T> getFile() {
		return program;
	}

	/**
	 * Gets the error log.
	 * @return error log
	 */
	public ErrorLog getErrorLog() {
		return log;
	}

	/**
	 * File structure
	 */

	@Override
	public void exitFile(FileContext ctx) {
		program = new ProgramFile<T>(section, imports, ctx.ID().getText(), cexprs.get(ctx.cexpr()));
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
	 * Bodies
	 */
	
	@Override
	public void enterBody(BodyContext ctx) { }
	
	@Override
	public void exitBody(BodyContext ctx) {
		List<ProgramExpression<T>> stmtlist = new ArrayList<ProgramExpression<T>>();
		for (StmtContext stmt_ctx : ctx.stmt())
			stmtlist.add(progs.get(stmt_ctx));
		progs.put(ctx, new ProgramBody<T>(stmtlist));
	}
	
	@Override
	public void enterStmt_equation(Stmt_equationContext ctx) { }

	@Override
	public void exitStmt_equation(Stmt_equationContext ctx) {
		Range x = arrays.get(ctx.range(0));
		Range y = arrays.get(ctx.range(1));
		if (x instanceof Variable) {
			progs.put(ctx, new ProgramEquation<T>((Variable)x, y));
		} else if (x instanceof Variable) {
			progs.put(ctx, new ProgramEquation<T>((Variable)y, x));
		} else {
			progs.put(ctx, new ProgramValue<T>());
			System.out.println("ERROR : Incorrect definition " + ctx.getText());
			//throw new Exception("Either the left-hand-side or the right-hand-side of an equation must be a variable.")
		}
	}
	
	@Override
	public void enterStmt_compdefn(Stmt_compdefnContext ctx) { }

	@Override
	public void exitStmt_compdefn(Stmt_compdefnContext ctx) {
		progs.put(ctx, new ProgramEquation<T>(variables.get(ctx.var()), cexprs.get(ctx.cexpr())));
	}
	
	@Override
	public void enterStmt_instance(Stmt_instanceContext ctx) { }

	@Override
	public void exitStmt_instance(Stmt_instanceContext ctx) {	
		ComponentExpression<T> cexpr = cexprs.get(ctx.cexpr());
		RangeList list = lists.get(ctx.list());
		if (list == null) list = new RangeList();
		InterfaceExpression iface = ifaces.get(ctx.iface());
		progs.put(ctx, new ProgramInstance<T>(cexpr, list, iface));
	}
	
	@Override
	public void enterStmt_iteration(Stmt_iterationContext ctx) { }

	@Override
	public void exitStmt_iteration(Stmt_iterationContext ctx) {
		VariableName p = new VariableName(ctx.ID().getText());
		IntegerExpression a = iexprs.get(ctx.iexpr(0));
		IntegerExpression b = iexprs.get(ctx.iexpr(1));
		ProgramExpression<T> B = progs.get(ctx.body());
		progs.put(ctx, new ProgramForLoop<T>(p, a, b, B));
	}

	@Override
	public void enterStmt_condition(Stmt_conditionContext ctx) { }

	@Override
	public void exitStmt_condition(Stmt_conditionContext ctx) {
		List<BooleanExpression> guards = new ArrayList<BooleanExpression>();
		List<ProgramExpression<T>> branches = new ArrayList<ProgramExpression<T>>();
		for (BexprContext bexpr_ctx : ctx.bexpr())
			guards.add(bexprs.get(bexpr_ctx));
		for (BodyContext body_ctx : ctx.body())
			branches.add(progs.get(body_ctx));
		if (guards.size() == branches.size()) {
			guards.add(new BooleanValue(true));
			branches.add(new ProgramValue<T>());
		} else {
			guards.add(new BooleanValue(true));
		}
		progs.put(ctx, new ProgramIfThenElse<T>(guards, branches));
	}
		
	/**
	 * Values	
	 */

	@Override
	public void exitRange_variable(Range_variableContext ctx) {
		arrays.put(ctx, variables.get(ctx.var()));
	}
	
	@Override
	public void exitRange_expr(Range_exprContext ctx) {
		arrays.put(ctx, exprs.get(ctx.expr()));
	}
	
	@Override
	public void exitRange_list(Range_listContext ctx) {
		arrays.put(ctx, lists.get(ctx.list()));
	}

	@Override
	public void exitExpr_string(Expr_stringContext ctx) {
		exprs.put(ctx, new StringValue(ctx.STRING().getText()));
	}

	@Override
	public void exitExpr_boolean(Expr_booleanContext ctx) {
		exprs.put(ctx, bexprs.get(ctx.bexpr()));
	}

	@Override
	public void exitExpr_integer(Expr_integerContext ctx) {
		exprs.put(ctx, iexprs.get(ctx.iexpr()));
	}

	@Override
	public void exitExpr_component(Expr_componentContext ctx) {
		exprs.put(ctx, cexprs.get(ctx.cexpr()));
	}

	@Override
	public void exitList(ListContext ctx) {
		List<Range> list = new ArrayList<Range>();
		for (RangeContext expr_ctx : ctx.range())
			list.add(arrays.get(expr_ctx));
		lists.put(ctx, new RangeList(list));
	}

	/**
	 * Component expressions
	 */

	@Override
	public void enterCexpr_variable(Cexpr_variableContext ctx) { }

	@Override
	public void exitCexpr_variable(Cexpr_variableContext ctx) {
		Variable var = variables.get(ctx.var());
		cexprs.put(ctx, new ComponentVariable<T>(var));
	}

	@Override
	public void enterCexpr_atomic(Cexpr_atomicContext ctx) { }

	@Override
	public void exitCexpr_atomic(Cexpr_atomicContext ctx) {
		ProgramValue<T> prog = new ProgramValue<T>(new Definitions(), new InstanceList<T>(atoms.get(ctx.atom())));
		cexprs.put(ctx, new ComponentValue<T>(signatureExpressions.get(ctx.sign()), prog));
	}

	@Override
	public void enterCexpr_composite(Cexpr_compositeContext ctx) { }

	@Override
	public void exitCexpr_composite(Cexpr_compositeContext ctx) {
		cexprs.put(ctx, new ComponentComposite<T>(signatureExpressions.get(ctx.sign()), progs.get(ctx.body())));		
	}
	
	/**
	 * Boolean expressions
	 */

	@Override
	public void enterBexpr_relation(Bexpr_relationContext ctx) { }

	@Override
	public void exitBexpr_relation(Bexpr_relationContext ctx) {
		
		IntegerExpression e1 = iexprs.get(ctx.iexpr(0));
		IntegerExpression e2 = iexprs.get(ctx.iexpr(1));
		
		switch (ctx.op.getType()) {
		case ReoParser.LEQ:
			bexprs.put(ctx, new BooleanLessOrEqual(e1, e2));
			break;
		case ReoParser.LT:
			bexprs.put(ctx, new BooleanLessThan(e1, e2));
			break;
		case ReoParser.GEQ:
			bexprs.put(ctx, new BooleanGreaterOrEqual(e1, e2));
			break;
		case ReoParser.GT:
			bexprs.put(ctx, new BooleanGreaterThan(e1, e2));
			break;
		case ReoParser.EQ:
			bexprs.put(ctx, new BooleanEquality(e1, e2));
			break;
		case ReoParser.NEQ:
			bexprs.put(ctx, new BooleanDisequality(e1, e2));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void enterBexpr_boolean(Bexpr_booleanContext ctx) { }

	@Override
	public void exitBexpr_boolean(Bexpr_booleanContext ctx) {
		bexprs.put(ctx, new BooleanValue(Boolean.parseBoolean(ctx.BOOL().getText())));
	}

	@Override
	public void enterBexpr_disjunction(Bexpr_disjunctionContext ctx) { }

	@Override
	public void exitBexpr_disjunction(Bexpr_disjunctionContext ctx) {
		BooleanExpression e1 = bexprs.get(ctx.bexpr(0));
		BooleanExpression e2 = bexprs.get(ctx.bexpr(1));
		bexprs.put(ctx, new BooleanDisjunction(e1, e2));
	}

	@Override
	public void enterBexpr_negation(Bexpr_negationContext ctx) { }

	@Override
	public void exitBexpr_negation(Bexpr_negationContext ctx) {
		bexprs.put(ctx, bexprs.get(ctx.bexpr()));
	}

	@Override
	public void enterBexpr_conjunction(Bexpr_conjunctionContext ctx) { }

	@Override
	public void exitBexpr_conjunction(Bexpr_conjunctionContext ctx) {
		BooleanExpression e1 = bexprs.get(ctx.bexpr(0));
		BooleanExpression e2 = bexprs.get(ctx.bexpr(1));
		bexprs.put(ctx, new BooleanConjunction(e1, e2));
	}
	
	@Override
	public void enterBexpr_brackets(Bexpr_bracketsContext ctx) { }

	@Override
	public void exitBexpr_brackets(Bexpr_bracketsContext ctx) {
		bexprs.put(ctx, bexprs.get(ctx.bexpr()));
	}

	@Override
	public void enterBexpr_variable(Bexpr_variableContext ctx) { }

	@Override
	public void exitBexpr_variable(Bexpr_variableContext ctx) {
		bexprs.put(ctx, new BooleanVariable(variables.get(ctx.var())));		
	}

	/**
	 * Signatures
	 */

	@Override
	public void exitSign(SignContext ctx) {
		ParameterList params = parameterlists.get(ctx.params());
		if (params == null) params = new ParameterList();
		NodeList nodes = nodelists.get(ctx.nodes());
		signatureExpressions.put(ctx, new SignatureExpression(params, nodes));
	}
	
	@Override
	public void enterParams(ParamsContext ctx) { }

	@Override
	public void exitParams(ParamsContext ctx) {
		List<Parameter> list = new ArrayList<Parameter>();
		for (ParamContext param_ctx : ctx.param())
			list.add(parameters.get(param_ctx));
		parameterlists.put(ctx, new ParameterList(list));
	}
	
	@Override
	public void enterParam(ParamContext ctx) {
	}

	@Override
	public void exitParam(ParamContext ctx) {
		Variable var = variables.get(ctx.var());
		ParameterType type = parametertypes.get(ctx.ptype());
		if (type == null) type = new TypeTag("");
		parameters.put(ctx, new Parameter(var, type));
	}

	@Override
	public void enterPtype_typetag(Ptype_typetagContext ctx) { }

	@Override
	public void exitPtype_typetag(Ptype_typetagContext ctx) {
		parametertypes.put(ctx, new TypeTag(ctx.type().getText()));
	}

	@Override
	public void enterPtype_signature(Ptype_signatureContext ctx) { }


	@Override
	public void exitPtype_signature(Ptype_signatureContext ctx) {
		parametertypes.put(ctx, signatureExpressions.get(ctx.sign()));
	}
	
	@Override
	public void enterNodes(NodesContext ctx) { }

	@Override
	public void exitNodes(NodesContext ctx) {
		List<Node> list = new ArrayList<Node>();
		for (NodeContext node_ctx : ctx.node())
			list.add(nodes.get(node_ctx));
		nodelists.put(ctx, new NodeList(list));
	}
	
	@Override
	public void enterNode(NodeContext ctx) { }

	@Override
	public void exitNode(NodeContext ctx) {
		Variable var = variables.get(ctx.var());
		if (var == null) var = new VariableName();
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
	public void enterIface(IfaceContext ctx) { }

	@Override
	public void exitIface(IfaceContext ctx) {
		List<Variable> list = new ArrayList<Variable>();
		for (VarContext node_ctx : ctx.var())
			list.add(variables.get(node_ctx));
		ifaces.put(ctx, new InterfaceExpression(list));
	}
     
	/**
	 * Variables	
	 */
	
	@Override
	public void enterVar(VarContext ctx) { }

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
			variables.put(ctx, new VariableName(name));
		} else { 
			variables.put(ctx, new VariableRange(name, indices));
		}
	}

	@Override
	public void exitName(NameContext ctx) {
	}

	@Override
	public void enterIndices(IndicesContext ctx) { }

	@Override
	public void exitIndices(IndicesContext ctx) {
		List<IntegerExpression> list = new ArrayList<IntegerExpression>();
		for (IexprContext iexpr_ctx : ctx.iexpr())
			list.add(iexprs.get(iexpr_ctx));
		bounds.put(ctx, list);
	}

	/**
	 * Integer expressions
	 */

	@Override
	public void enterIexpr_variable(Iexpr_variableContext ctx) { }

	@Override
	public void exitIexpr_variable(Iexpr_variableContext ctx) {
		iexprs.put(ctx, new IntegerVariable(variables.get(ctx.var())));
	}

	@Override
	public void enterIexpr_multdivrem(Iexpr_multdivremContext ctx) { }

	@Override
	public void exitIexpr_multdivrem(Iexpr_multdivremContext ctx) {
		IntegerExpression e1 = iexprs.get(ctx.iexpr(0));
		IntegerExpression e2 = iexprs.get(ctx.iexpr(1));		
		switch (ctx.op.getType()) {
		case ReoParser.MUL:
			iexprs.put(ctx, new IntegerMultiplication(e1, e2));
			break;
		case ReoParser.DIV:
			iexprs.put(ctx, new IntegerDivision(e1, e2));
			break;
		case ReoParser.GEQ:
			iexprs.put(ctx, new IntegerRemainder(e1, e2));
			break;
		default:
			break;
		}
	}

	@Override
	public void enterIexpr_exponent(Iexpr_exponentContext ctx) { }

	@Override
	public void exitIexpr_exponent(Iexpr_exponentContext ctx) {
		IntegerExpression e1 = iexprs.get(ctx.iexpr(0));
		IntegerExpression e2 = iexprs.get(ctx.iexpr(1));
		iexprs.put(ctx, new IntegerExponentiation(e1, e2));
	}

	@Override
	public void enterIexpr_addsub(Iexpr_addsubContext ctx) {
	}

	@Override
	public void exitIexpr_addsub(Iexpr_addsubContext ctx) {
		IntegerExpression e1 = iexprs.get(ctx.iexpr(0));
		IntegerExpression e2 = iexprs.get(ctx.iexpr(1));		
		switch (ctx.op.getType()) {
		case ReoParser.ADD:
			iexprs.put(ctx, new IntegerAddition(e1, e2));
			break;
		case ReoParser.MIN:
			iexprs.put(ctx, new IntegerSubstraction(e1, e2));
			break;
		default:
			break;
		}
	}

	@Override
	public void enterIexpr_unarymin(Iexpr_unaryminContext ctx) { }

	@Override
	public void exitIexpr_unarymin(Iexpr_unaryminContext ctx) {
		IntegerExpression e = iexprs.get(ctx.iexpr());
		iexprs.put(ctx, new IntegerUnaryMinus(e));
	}

	@Override
	public void enterIexpr_natural(Iexpr_naturalContext ctx) {
	}

	@Override
	public void exitIexpr_natural(Iexpr_naturalContext ctx) {
		iexprs.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().getText())));
	}

	@Override
	public void enterIexpr_brackets(Iexpr_bracketsContext ctx) {
	}

	@Override
	public void exitIexpr_brackets(Iexpr_bracketsContext ctx) {
		iexprs.put(ctx, iexprs.get(ctx.iexpr()));
	}
	
}