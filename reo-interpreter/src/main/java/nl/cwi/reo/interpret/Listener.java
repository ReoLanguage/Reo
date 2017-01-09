package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.errors.ErrorLog;
import nl.cwi.reo.interpret.TreoParser.Atom_constraintautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_portautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_seepageautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_sourcecodeContext;
import nl.cwi.reo.interpret.TreoParser.Atom_workautomataContext;
import nl.cwi.reo.interpret.TreoParser.BexprContext;
import nl.cwi.reo.interpret.TreoParser.Bexpr_booleanContext;
import nl.cwi.reo.interpret.TreoParser.Bexpr_bracketsContext;
import nl.cwi.reo.interpret.TreoParser.Bexpr_conjunctionContext;
import nl.cwi.reo.interpret.TreoParser.Bexpr_disjunctionContext;
import nl.cwi.reo.interpret.TreoParser.Bexpr_negationContext;
import nl.cwi.reo.interpret.TreoParser.Bexpr_relationContext;
import nl.cwi.reo.interpret.TreoParser.Bexpr_variableContext;
import nl.cwi.reo.interpret.TreoParser.BodyContext;
import nl.cwi.reo.interpret.TreoParser.CamContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_addsubContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_andContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_existentialContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_exponentContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_ineqContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_multdivremContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_neqContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_orContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_termContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_universalContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_bracketsContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_dataContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_functionContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_nextContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_notContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_unaryMinContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_variableContext;
import nl.cwi.reo.interpret.TreoParser.Cam_trContext;
import nl.cwi.reo.interpret.TreoParser.Cexpr_atomicContext;
import nl.cwi.reo.interpret.TreoParser.Cexpr_compositeContext;
import nl.cwi.reo.interpret.TreoParser.Cexpr_variableContext;
import nl.cwi.reo.interpret.TreoParser.ExprContext;
import nl.cwi.reo.interpret.TreoParser.Expr_variableContext;
import nl.cwi.reo.interpret.TreoParser.Expr_stringContext;
import nl.cwi.reo.interpret.TreoParser.Expr_booleanContext;
import nl.cwi.reo.interpret.TreoParser.Expr_integerContext;
import nl.cwi.reo.interpret.TreoParser.Expr_componentContext;
import nl.cwi.reo.interpret.TreoParser.FileContext;
import nl.cwi.reo.interpret.TreoParser.GplContext;
import nl.cwi.reo.interpret.TreoParser.IdsetContext;
import nl.cwi.reo.interpret.TreoParser.IexprContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_addsubContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_bracketsContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_exponentContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_multdivremContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_naturalContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_unaryminContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_variableContext;
import nl.cwi.reo.interpret.TreoParser.IfaceContext;
import nl.cwi.reo.interpret.TreoParser.ImpsContext;
import nl.cwi.reo.interpret.TreoParser.IndicesContext;
import nl.cwi.reo.interpret.TreoParser.ListContext;
import nl.cwi.reo.interpret.TreoParser.NameContext;
import nl.cwi.reo.interpret.TreoParser.NodeContext;
import nl.cwi.reo.interpret.TreoParser.NodesContext;
import nl.cwi.reo.interpret.TreoParser.PaContext;
import nl.cwi.reo.interpret.TreoParser.Pa_trContext;
import nl.cwi.reo.interpret.TreoParser.ParamContext;
import nl.cwi.reo.interpret.TreoParser.ParamsContext;
import nl.cwi.reo.interpret.TreoParser.Ptype_signatureContext;
import nl.cwi.reo.interpret.TreoParser.Ptype_typetagContext;
import nl.cwi.reo.interpret.TreoParser.SaContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_andContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_boolContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_orContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_variableContext;
import nl.cwi.reo.interpret.TreoParser.Sa_seepagefunctionContext;
import nl.cwi.reo.interpret.TreoParser.Sa_transitionContext;
import nl.cwi.reo.interpret.TreoParser.SignContext;
import nl.cwi.reo.interpret.TreoParser.StmtContext;
import nl.cwi.reo.interpret.TreoParser.Stmt_conditionContext;
import nl.cwi.reo.interpret.TreoParser.Stmt_instanceContext;
import nl.cwi.reo.interpret.TreoParser.Stmt_equationContext;
import nl.cwi.reo.interpret.TreoParser.Stmt_compdefnContext;
import nl.cwi.reo.interpret.TreoParser.Stmt_iterationContext;
import nl.cwi.reo.interpret.TreoParser.TypeContext;
import nl.cwi.reo.interpret.TreoParser.SecnContext;
import nl.cwi.reo.interpret.TreoParser.Value_exprContext;
import nl.cwi.reo.interpret.TreoParser.Value_listContext;
import nl.cwi.reo.interpret.TreoParser.VarContext;
import nl.cwi.reo.interpret.TreoParser.WaContext;
import nl.cwi.reo.interpret.TreoParser.Wa_exprContext;
import nl.cwi.reo.interpret.TreoParser.Wa_invariantContext;
import nl.cwi.reo.interpret.TreoParser.Wa_jc_andContext;
import nl.cwi.reo.interpret.TreoParser.Wa_jc_boolContext;
import nl.cwi.reo.interpret.TreoParser.Wa_jc_bracketsContext;
import nl.cwi.reo.interpret.TreoParser.Wa_jc_eqContext;
import nl.cwi.reo.interpret.TreoParser.Wa_jc_geqContext;
import nl.cwi.reo.interpret.TreoParser.Wa_jc_leqContext;
import nl.cwi.reo.interpret.TreoParser.Wa_jc_orContext;
import nl.cwi.reo.interpret.TreoParser.Wa_transitionContext;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.workautomata.JobConstraint;
import nl.cwi.reo.workautomata.Transition;
import nl.cwi.reo.workautomata.WorkAutomaton;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.interpret.p}.
 */
public class Listener extends TreoBaseListener {
	
	// Error log
	private ErrorLog log;
	
	// Symbol table
//	private Map<String, ValueType> symbols = new HashMap<String, ValueType>();
	
	// File structure
	private ProgramFile program;
	private String section = "";	
	private List<String> imports = new ArrayList<String>();
	
	// Components
	private ParseTreeProperty<ComponentExpression> cexprs = new ParseTreeProperty<ComponentExpression>();
	
	// Bodies
	private ParseTreeProperty<ProgramExpression> progs = new ParseTreeProperty<ProgramExpression>();
	
	// Values	
	private ParseTreeProperty<Value> values = new ParseTreeProperty<Value>();	
	private ParseTreeProperty<Expression> exprs = new ParseTreeProperty<Expression>();
	private ParseTreeProperty<ExpressionList> lists = new ParseTreeProperty<ExpressionList>();
	
	// Boolean expressions
	private ParseTreeProperty<BooleanExpression> bexprs = new ParseTreeProperty<BooleanExpression>();
	
	// Signatures
	private ParseTreeProperty<Signature> signatures = new ParseTreeProperty<Signature>();
	private ParseTreeProperty<ParameterList> parameterlists = new ParseTreeProperty<ParameterList>();
	private ParseTreeProperty<Parameter> parameters = new ParseTreeProperty<Parameter>();
	private ParseTreeProperty<ParameterType> parametertypes = new ParseTreeProperty<ParameterType>();
	private ParseTreeProperty<NodeList> nodelists = new ParseTreeProperty<NodeList>();
	private ParseTreeProperty<Node> nodes = new ParseTreeProperty<Node>();
	
	// Type tags for uninterpreted data
	private ParseTreeProperty<TypeTag> typetags = new ParseTreeProperty<TypeTag>();

	// Interface instantiation
	private ParseTreeProperty<Interface> ifaces = new ParseTreeProperty<Interface>();
	       
	// Variables
	private ParseTreeProperty<Variable> variables = new ParseTreeProperty<Variable>();	
	private ParseTreeProperty<List<IntegerExpression>> bounds = new ParseTreeProperty<List<IntegerExpression>>();	

	// Integer expressions
	private ParseTreeProperty<IntegerExpression> iexprs = new ParseTreeProperty<IntegerExpression>();

	// Semantics
	private ParseTreeProperty<Semantics<?>> atoms = new ParseTreeProperty<Semantics<?>>();
	
	
	/**
	 * Work Automata.
	 */
	
	private ParseTreeProperty<WorkAutomaton> workautomata = new ParseTreeProperty<WorkAutomaton>();
	private ParseTreeProperty<JobConstraint> wa_jobconstraints = new ParseTreeProperty<JobConstraint>();
	private ParseTreeProperty<SortedSet<String>> idsets = new ParseTreeProperty<SortedSet<String>>();
//	private ParseTreeProperty<SortedSet<String>> wa_resets = new ParseTreeProperty<SortedSet<String>>();	
	private ParseTreeProperty<Transition> wa_transitions = new ParseTreeProperty<Transition>();	
	private ParseTreeProperty<String> wa_states = new ParseTreeProperty<String>();
	
	/**
	 * Gets the program expression.
	 * @return program expression
	 */
	public ProgramFile getFile() {
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
		program = new ProgramFile(section, imports, ctx.ID().getText(), cexprs.get(ctx.cexpr()));
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
		List<ProgramExpression> stmtlist = new ArrayList<ProgramExpression>();
		for (StmtContext stmt_ctx : ctx.stmt())
			stmtlist.add(progs.get(stmt_ctx));
		progs.put(ctx, new ProgramBody(stmtlist));
	}
	
	@Override
	public void enterStmt_equation(Stmt_equationContext ctx) { }

	@Override
	public void exitStmt_equation(Stmt_equationContext ctx) {
		Value x = values.get(ctx.value(0));
		Value y = values.get(ctx.value(1));
		if (x instanceof Variable) {
			progs.put(ctx, new ProgramEquation((Variable)x, y));
		} else if (x instanceof Variable) {
			progs.put(ctx, new ProgramEquation((Variable)y, x));
		} else {
			progs.put(ctx, new ProgramValue());
			System.out.println("ERROR : Incorrect definition " + ctx.getText());
			//throw new Exception("Either the left-hand-side or the right-hand-side of an equation must be a variable.")
		}
	}
	
	@Override
	public void enterStmt_compdefn(Stmt_compdefnContext ctx) { }

	@Override
	public void exitStmt_compdefn(Stmt_compdefnContext ctx) {
		progs.put(ctx, new ProgramEquation(variables.get(ctx.var()), cexprs.get(ctx.cexpr())));
	}
	
	@Override
	public void enterStmt_instance(Stmt_instanceContext ctx) { }

	@Override
	public void exitStmt_instance(Stmt_instanceContext ctx) {	
		ComponentExpression cexpr = cexprs.get(ctx.cexpr());
		ExpressionList list = lists.get(ctx.list());
		if (list == null) list = new ExpressionList();
		Interface iface = ifaces.get(ctx.iface());
		progs.put(ctx, new ProgramInstance(cexpr, list, iface));
	}
	
	@Override
	public void enterStmt_iteration(Stmt_iterationContext ctx) { }

	@Override
	public void exitStmt_iteration(Stmt_iterationContext ctx) {
		VariableName p = new VariableName(ctx.ID().getText());
		IntegerExpression a = iexprs.get(ctx.iexpr(0));
		IntegerExpression b = iexprs.get(ctx.iexpr(1));
		ProgramExpression B = progs.get(ctx.body());
		progs.put(ctx, new ProgramForLoop(p, a, b, B));
	}

	@Override
	public void enterStmt_condition(Stmt_conditionContext ctx) { }

	@Override
	public void exitStmt_condition(Stmt_conditionContext ctx) {
		List<BooleanExpression> guards = new ArrayList<BooleanExpression>();
		for (BexprContext bexpr_ctx : ctx.bexpr())
			guards.add(bexprs.get(bexpr_ctx));
		List<ProgramExpression> branches = new ArrayList<ProgramExpression>();
		for (BodyContext body_ctx : ctx.body())
			branches.add(progs.get(body_ctx));
		progs.put(ctx, new ProgramIfThenElse(guards, branches));
	}
		
	/**
	 * Values	
	 */
	
	@Override
	public void exitValue_expr(Value_exprContext ctx) {
		values.put(ctx, exprs.get(ctx.expr()));
	}
	
	@Override
	public void exitValue_list(Value_listContext ctx) {
		values.put(ctx, lists.get(ctx.list()));
	}

	@Override
	public void exitExpr_variable(Expr_variableContext ctx) {
		exprs.put(ctx, variables.get(ctx.var()));
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
		List<Expression> list = new ArrayList<Expression>();
		for (ExprContext expr_ctx : ctx.expr())
			list.add(exprs.get(expr_ctx));
		lists.put(ctx, new ExpressionList(list));
	}

	/**
	 * Component expressions
	 */

	@Override
	public void enterCexpr_variable(Cexpr_variableContext ctx) { }

	@Override
	public void exitCexpr_variable(Cexpr_variableContext ctx) {
		Variable var = variables.get(ctx.var());
		if (var instanceof VariableName) {
			for (String imprt : imports) {
				if (imprt.endsWith(((VariableName)var).getName())) {
					var = new VariableName(imprt);
					break;
				}
			}
		}
		cexprs.put(ctx, new ComponentVariable(var));
	}

	@Override
	public void enterCexpr_atomic(Cexpr_atomicContext ctx) { }

	@Override
	public void exitCexpr_atomic(Cexpr_atomicContext ctx) {
		
		ProgramValue prog = new ProgramValue(new Definitions(), new InstanceList(atoms.get(ctx.atom())));
		cexprs.put(ctx, new ComponentValue(signatures.get(ctx.sign()), prog));
	}

	@Override
	public void enterCexpr_composite(Cexpr_compositeContext ctx) { }

	@Override
	public void exitCexpr_composite(Cexpr_compositeContext ctx) {
		cexprs.put(ctx, new ComponentComposite(signatures.get(ctx.sign()), progs.get(ctx.body())));		
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
		case TreoParser.LEQ:
			bexprs.put(ctx, new BooleanLessOrEqual(e1, e2));
			break;
		case TreoParser.LT:
			bexprs.put(ctx, new BooleanLessThan(e1, e2));
			break;
		case TreoParser.GEQ:
			bexprs.put(ctx, new BooleanGreaterOrEqual(e1, e2));
			break;
		case TreoParser.GT:
			bexprs.put(ctx, new BooleanGreaterThan(e1, e2));
			break;
		case TreoParser.EQ:
			bexprs.put(ctx, new BooleanEquality(e1, e2));
			break;
		case TreoParser.NEQ:
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
		signatures.put(ctx, new Signature(params, nodes));
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
		parametertypes.put(ctx, signatures.get(ctx.sign()));
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
		NodeType type = NodeType.FREE;
		if (ctx.io != null) {
			switch (ctx.io.getType()) {
			case TreoParser.IN:
				type = NodeType.SOURCE;
				break;
			case TreoParser.OUT:
				type = NodeType.SINK;
				break;
			case TreoParser.MIX:
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
		ifaces.put(ctx, new Interface(list));
	}
     
	/**
	 * Variables	
	 */
	
	@Override
	public void enterVar(VarContext ctx) { }

	@Override
	public void exitVar(VarContext ctx) {		
		String name = ctx.name().getText();			
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
		case TreoParser.MUL:
			iexprs.put(ctx, new IntegerMultiplication(e1, e2));
			break;
		case TreoParser.DIV:
			iexprs.put(ctx, new IntegerDivision(e1, e2));
			break;
		case TreoParser.GEQ:
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
		case TreoParser.ADD:
			iexprs.put(ctx, new IntegerAddition(e1, e2));
			break;
		case TreoParser.MIN:
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
	
	/**
	 * Atoms
	 */

	@Override
	public void enterAtom_sourcecode(Atom_sourcecodeContext ctx) { }

	@Override
	public void exitAtom_sourcecode(Atom_sourcecodeContext ctx) {
	}
	
	@Override
	public void enterAtom_workautomata(Atom_workautomataContext ctx) { }

	@Override
	public void exitAtom_workautomata(Atom_workautomataContext ctx) {
		atoms.put(ctx, workautomata.get(ctx.wa()));
	}

	@Override
	public void enterAtom_constraintautomata(Atom_constraintautomataContext ctx) { }

	@Override
	public void exitAtom_constraintautomata(Atom_constraintautomataContext ctx) {
	}

	@Override
	public void enterAtom_seepageautomata(Atom_seepageautomataContext ctx) { }

	@Override
	public void exitAtom_seepageautomata(Atom_seepageautomataContext ctx) {
	}

	@Override
	public void enterAtom_portautomata(Atom_portautomataContext ctx) { }

	@Override
	public void exitAtom_portautomata(Atom_portautomataContext ctx) {
	}
	
	/**
	 * Semantics - GPL Source code
	 */

	@Override
	public void enterGpl(GplContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGpl(GplContext ctx) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Semantics - Work Automata
	 */
	
	@Override
	public void enterWa(WaContext ctx) {
	}

	@Override
	public void exitWa(WaContext ctx) {
		// Initialize all work automaton fields.
		Set<String> Q = new HashSet<String>();
		Set<String> P = new HashSet<String>();
		Set<String> J = new HashSet<String>();
		Map<String, JobConstraint> I = new HashMap<String, JobConstraint>();
		Map<String, Set<Transition>> T = new HashMap<String, Set<Transition>>();
		String q0 = null;		
		
		// Iterate over all work automaton statements.
		for (Wa_exprContext stmt_ctx : ctx.wa_expr()) {
			
			// If the statement is annotated with a state and invariant
			// condition, then add this to the work automaton.
			String q;
			if ((q = wa_states.get(stmt_ctx)) != null) {
				
				// Define the initial state, if necessary.
				if (q0 == null) 
					q0 = q;
				
				// Add the state, if not yet present
				if (!T.containsKey(q)) 
					T.put(q, new TreeSet<Transition>());
				Q.add(q);
				
				// Add invariant condition.
				JobConstraint jc;
				if ((jc = wa_jobconstraints.get(stmt_ctx)) != null) { 
					JobConstraint old_jc;
					if ((old_jc = I.get(q)) == null) {
						// If the state has no invariant yet, define it.
						I.put(q, jc); 
					} else {
						// If the state has already an invariant, add the current
						I.put(q, JobConstraint.conjunction(old_jc, jc));
					}
				}
			}
			
			// If the statement is annotated with a transition, then
			// add this transition to the work automaton.
			Transition t;
			if ((t = wa_transitions.get(stmt_ctx)) != null) {

				// Define the initial state, if necessary.
				if (q0 == null) 
					q0 = t.getSource();
				
				// Add the source state, if not yet present.
				if (!T.containsKey(t.getSource())) 
					T.put(t.getSource(), new TreeSet<Transition>());
				Q.add(t.getSource());
				
				// Add the target state, if not yet present.
				if (!T.containsKey(t.getTarget())) 
					T.put(t.getTarget(), new TreeSet<Transition>());
				Q.add(t.getTarget());
				
				// Add the transition, if not yet present
				if (!T.get(t.getSource()).contains(t)) {
					
					// Append the interface with all ports in the synchronization constraint.
					P.addAll(t.getSyncConstraint());
					
					// Append the job set with all jobs in the job constraint.
					J.addAll(t.getJobConstraint().getW().keySet());
					
					// Add the transition
					T.get(t.getSource()).add(t);
				}
			}
		}
		
		// Annotate the parse tree with this new work automaton. 
		workautomata.put(ctx, new WorkAutomaton(Q, P, J, I, T, q0));
	}

	@Override
	public void enterIdset(IdsetContext ctx) {
	}

	@Override
	public void exitIdset(IdsetContext ctx) {
		SortedSet<String> sc = new TreeSet<String>();
		for (TerminalNode id : ctx.ID())
			sc.add(id.getText());
		idsets.put(ctx, sc);
	}

	@Override
	public void enterWa_jc_brackets(Wa_jc_bracketsContext ctx) { }


	@Override
	public void exitWa_jc_brackets(Wa_jc_bracketsContext ctx) {
		wa_jobconstraints.put(ctx, wa_jobconstraints.get(ctx.jc()));
	}

	@Override
	public void enterWa_jc_leq(Wa_jc_leqContext ctx) {
	}

	@Override
	public void exitWa_jc_leq(Wa_jc_leqContext ctx) {
		String job = ctx.ID().getText();
		int bound = 0;
		try {
			bound = Integer.parseInt(ctx.NAT().getText());
		} catch (NumberFormatException e) { }
		wa_jobconstraints.put(ctx, new JobConstraint(job, bound, false));
	}

	@Override
	public void enterWa_jc_eq(Wa_jc_eqContext ctx) {
	}

	@Override
	public void exitWa_jc_eq(Wa_jc_eqContext ctx) {
		String job = ctx.ID().getText();
		int bound = 0;
		try {
			bound = Integer.parseInt(ctx.NAT().getText());
		} catch (NumberFormatException e) { }
		wa_jobconstraints.put(ctx, new JobConstraint(job, bound, true));
	}

	@Override
	public void enterWa_transition(Wa_transitionContext ctx) {
	}

	@Override
	public void exitWa_transition(Wa_transitionContext ctx) {
		String q1 = ctx.ID(0).getText();
		String q2 = ctx.ID(1).getText();
		SortedSet<String> sc = idsets.get(ctx.idset(0));
		JobConstraint jc = wa_jobconstraints.get(ctx.jc());
//		SortedSet<String> resets = wa_resets.get(ctx.idset(1));
		wa_transitions.put(ctx, new Transition(q1, q2, sc, jc));		
	}

	@Override
	public void enterWa_invariant(Wa_invariantContext ctx) {
	}

	@Override
	public void exitWa_invariant(Wa_invariantContext ctx) {
		wa_jobconstraints.put(ctx, wa_jobconstraints.get(ctx.jc()));
	}

	@Override
	public void enterWa_jc_geq(Wa_jc_geqContext ctx) {
	}

	@Override
	public void exitWa_jc_geq(Wa_jc_geqContext ctx) {
	}

	@Override
	public void enterWa_jc_bool(Wa_jc_boolContext ctx) {
	}

	@Override
	public void exitWa_jc_bool(Wa_jc_boolContext ctx) {
		wa_jobconstraints.put(ctx, new JobConstraint(Boolean.parseBoolean(ctx.getText())));
	}

	@Override
	public void enterWa_jc_and(Wa_jc_andContext ctx) {
	}

	@Override
	public void exitWa_jc_and(Wa_jc_andContext ctx) {
		JobConstraint jc1 = wa_jobconstraints.get(ctx.jc(0));
		JobConstraint jc2 = wa_jobconstraints.get(ctx.jc(1));
		wa_jobconstraints.put(ctx, JobConstraint.conjunction(jc1, jc2));
	}

	@Override
	public void enterWa_jc_or(Wa_jc_orContext ctx) {
	}

	@Override
	public void exitWa_jc_or(Wa_jc_orContext ctx) {
	}

	/**
	 * Semantics - Constraint automata with memory
	 */
	
	@Override
	public void enterCam_dc_exponent(Cam_dc_exponentContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_exponent(Cam_dc_exponentContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_next(Cam_dt_nextContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_next(Cam_dt_nextContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_tr(Cam_trContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_tr(Cam_trContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_multdivrem(Cam_dc_multdivremContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_multdivrem(Cam_dc_multdivremContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_ineq(Cam_dc_ineqContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_ineq(Cam_dc_ineqContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_neq(Cam_dc_neqContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_neq(Cam_dc_neqContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterCam_dt_brackets(Cam_dt_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_brackets(Cam_dt_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_existential(Cam_dc_existentialContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_existential(Cam_dc_existentialContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_or(Cam_dc_orContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_or(Cam_dc_orContext ctx) {
		// TODO Auto-generated method stub
	}

	@Override
	public void enterCam_dc_universal(Cam_dc_universalContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_universal(Cam_dc_universalContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_data(Cam_dt_dataContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_data(Cam_dt_dataContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_not(Cam_dt_notContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_not(Cam_dt_notContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam(CamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam(CamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_function(Cam_dt_functionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_function(Cam_dt_functionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_addsub(Cam_dc_addsubContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_addsub(Cam_dc_addsubContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterCam_dc_and(Cam_dc_andContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_and(Cam_dc_andContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_term(Cam_dc_termContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_term(Cam_dc_termContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_variable(Cam_dt_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitCam_dt_variable(Cam_dt_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_unaryMin(Cam_dt_unaryMinContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_unaryMin(Cam_dt_unaryMinContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Semantics - Seepage Automata
	 */

	@Override
	public void enterSa(SaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa(SaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterSa_pbe_bool(Sa_pbe_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_pbe_bool(Sa_pbe_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSa_transition(Sa_transitionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_transition(Sa_transitionContext ctx) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void enterSa_pbe_or(Sa_pbe_orContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_pbe_or(Sa_pbe_orContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterSa_pbe_variable(Sa_pbe_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_pbe_variable(Sa_pbe_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterSa_seepagefunction(Sa_seepagefunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_seepagefunction(Sa_seepagefunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSa_pbe_and(Sa_pbe_andContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitSa_pbe_and(Sa_pbe_andContext ctx) {
		// TODO Auto-generated method stub
		
	}	

	/**
	 * Semantics - Port Automata
	 */

	@Override
	public void enterPa(PaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPa(PaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPa_tr(Pa_trContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPa_tr(Pa_trContext ctx) {
		// TODO Auto-generated method stub
		
	}
}