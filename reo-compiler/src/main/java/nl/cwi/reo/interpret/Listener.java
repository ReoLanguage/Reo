package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.interpret.TreoParser.Atom_constraintautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_portautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_seepageautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_sourcecodeContext;
import nl.cwi.reo.interpret.TreoParser.Atom_workautomataContext;
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
import nl.cwi.reo.interpret.TreoParser.Comp_atomicContext;
import nl.cwi.reo.interpret.TreoParser.Comp_compositeContext;
import nl.cwi.reo.interpret.TreoParser.Comp_variableContext;
import nl.cwi.reo.interpret.TreoParser.CtermContext;
import nl.cwi.reo.interpret.TreoParser.DefnContext;
import nl.cwi.reo.interpret.TreoParser.Defn_definitionContext;
import nl.cwi.reo.interpret.TreoParser.Defn_equationContext;
import nl.cwi.reo.interpret.TreoParser.FileContext;
import nl.cwi.reo.interpret.TreoParser.GplContext;
import nl.cwi.reo.interpret.TreoParser.IdsetContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_addsubContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_bracketsContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_exponentContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_multdivremContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_naturalContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_unaryminContext;
import nl.cwi.reo.interpret.TreoParser.Iexpr_variableContext;
import nl.cwi.reo.interpret.TreoParser.IfaceContext;
import nl.cwi.reo.interpret.TreoParser.IndicesContext;
import nl.cwi.reo.interpret.TreoParser.Inst_conditionContext;
import nl.cwi.reo.interpret.TreoParser.Inst_iterationContext;
import nl.cwi.reo.interpret.TreoParser.Inst_referenceContext;
import nl.cwi.reo.interpret.TreoParser.ListContext;
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
import nl.cwi.reo.interpret.TreoParser.TypeContext;
import nl.cwi.reo.interpret.TreoParser.ValueContext;
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
import nl.cwi.reo.workautomata.JobConstraint;
import nl.cwi.reo.workautomata.Transition;
import nl.cwi.reo.workautomata.WorkAutomaton;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.semantics.Program}.
 */
public class Listener implements TreoListener {
	
	public Component main;
	
	private ParseTreeProperty<Map<String, Object>> localdefinitions = 
			new ParseTreeProperty<Map<String, Object>>();
	private ParseTreeProperty<Map<String, SignatureExpression>> localsignatures = 
			new ParseTreeProperty<Map<String, SignatureExpression>>();
	private ParseTreeProperty<Object> values = 
			new ParseTreeProperty<Object>();
	private ParseTreeProperty<ComponentExpression> componentdefinitions =
			new ParseTreeProperty<ComponentExpression>();
	private ParseTreeProperty<Expression<Component>> components =
			new ParseTreeProperty<Expression<Component>>();
	private ParseTreeProperty<ComponentBody> bodies =
			new ParseTreeProperty<ComponentBody>();
	private ParseTreeProperty<Definition> definitions = 
			new ParseTreeProperty<Definition>();
	private ParseTreeProperty<BooleanExpression> booleanexpressions = 
			new ParseTreeProperty<BooleanExpression>();
	private ParseTreeProperty<SignatureExpression> signatures = 
			new ParseTreeProperty<SignatureExpression>();
	private ParseTreeProperty<ParameterListExpression> parameterlists = 
			new ParseTreeProperty<ParameterListExpression>();
	private ParseTreeProperty<ParameterType> parametertypes = 
			new ParseTreeProperty<ParameterType>();
	private ParseTreeProperty<Variables> variables = 
			new ParseTreeProperty<Variables>();	
	private ParseTreeProperty<TypeTag> typetags = 
			new ParseTreeProperty<TypeTag>();
	private ParseTreeProperty<List<List<IntegerExpression>>> indices = 
			new ParseTreeProperty<List<List<IntegerExpression>>>();	
	private ParseTreeProperty<IntegerExpression> integerexpressions = 
			new ParseTreeProperty<IntegerExpression>();
	private ParseTreeProperty<Atom> atoms = 
			new ParseTreeProperty<Atom>();
	
	/**
	 * Work Automata.
	 */
	private ParseTreeProperty<WorkAutomaton> workautomata = 
			new ParseTreeProperty<WorkAutomaton>();
	private ParseTreeProperty<JobConstraint> wa_jobconstraints = 
			new ParseTreeProperty<JobConstraint>();
	private ParseTreeProperty<SortedSet<String>> wa_syncconstraints = 
			new ParseTreeProperty<SortedSet<String>>();
	private ParseTreeProperty<SortedSet<String>> wa_resets = 
			new ParseTreeProperty<SortedSet<String>>();	
	private ParseTreeProperty<Transition> wa_transitions = 
			new ParseTreeProperty<Transition>();	
	private ParseTreeProperty<String> wa_states = 
			new ParseTreeProperty<String>();

	public Component getMain() {
		return main; // localdefinitions.get("main").instantiate(new HashMap<String,String>());
	}


	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterSign(SignContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitSign(SignContext ctx) {
		ParameterListExpression params = parameterlists.get(ctx.params());
		ParameterListExpression intface = parameterlists.get(ctx.params());
		signatures.put(ctx, new SignatureExpression(params, intface));
	}


	@Override
	public void enterBody(BodyContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBody(BodyContext ctx) {
		Set<ComponentExpression> comps = new HashSet<ComponentExpression>(); 
		for (CtermContext x : ctx.cterm())
			comps.add(componentdefinitions.get(x));
		List<Definition> defs = new ArrayList<Definition>();
		for (DefnContext x : ctx.defn())
			defs.add(definitions.get(x));
		bodies.put(ctx, new ComponentBody(comps, defs));
	}


	@Override
	public void enterType(TypeContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitType(TypeContext ctx) {
		typetags.put(ctx, new TypeTag(ctx.getText()));
	}


	@Override
	public void enterAtom_sourcecode(Atom_sourcecodeContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitAtom_sourcecode(Atom_sourcecodeContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterAtom_workautomata(Atom_workautomataContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitAtom_workautomata(Atom_workautomataContext ctx) {
		// Initialize all work automaton fields.
		Set<String> Q = new HashSet<String>();
		Set<String> P = new HashSet<String>();
		Set<String> J = new HashSet<String>();
		Map<String, JobConstraint> I = new HashMap<String, JobConstraint>();
		Map<String, Set<Transition>> T = new HashMap<String, Set<Transition>>();
		String q0 = null;		
		
		// Iterate over all work automaton statements.
		for (Wa_exprContext stmt_ctx : ctx.wa().wa_expr()) {
			
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
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIdset(IdsetContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterWa_jc_brackets(Wa_jc_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa_jc_brackets(Wa_jc_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}


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
	public void enterSa_pbe_bool(Sa_pbe_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitSa_pbe_bool(Sa_pbe_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterAtom_seepageautomata(Atom_seepageautomataContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitAtom_seepageautomata(Atom_seepageautomataContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterBexpr_relation(Bexpr_relationContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBexpr_relation(Bexpr_relationContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterBexpr_negation(Bexpr_negationContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBexpr_negation(Bexpr_negationContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterGpl(GplContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitGpl(GplContext ctx) {
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
	public void enterSa_transition(Sa_transitionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitSa_transition(Sa_transitionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterVar(VarContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitVar(VarContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterIexpr_variable(Iexpr_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIexpr_variable(Iexpr_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterComp_atomic(Comp_atomicContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitComp_atomic(Comp_atomicContext ctx) {
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
	public void enterList(ListContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitList(ListContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterParams(ParamsContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitParams(ParamsContext ctx) {
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
	public void enterComp_variable(Comp_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitComp_variable(Comp_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterIface(IfaceContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIface(IfaceContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterNode(NodeContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitNode(NodeContext ctx) {
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
	public void enterIexpr_multdivrem(Iexpr_multdivremContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIexpr_multdivrem(Iexpr_multdivremContext ctx) {
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
	public void enterWa_jc_leq(Wa_jc_leqContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa_jc_leq(Wa_jc_leqContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterWa_jc_eq(Wa_jc_eqContext ctx) {
		String job = ctx.ID().getText();
		int bound = 0;
		try {
			bound = Integer.parseInt(ctx.NAT().getText());
		} catch (NumberFormatException e) { }
		wa_jobconstraints.put(ctx, new JobConstraint(job, bound, true));
	}


	@Override
	public void exitWa_jc_eq(Wa_jc_eqContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterIexpr_exponent(Iexpr_exponentContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIexpr_exponent(Iexpr_exponentContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterBexpr_boolean(Bexpr_booleanContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBexpr_boolean(Bexpr_booleanContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterBexpr_disjunction(Bexpr_disjunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBexpr_disjunction(Bexpr_disjunctionContext ctx) {
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
	public void enterWa_transition(Wa_transitionContext ctx) {
		String q1 = ctx.ID(0).getText();
		String q2 = ctx.ID(1).getText();
		SortedSet<String> sc = wa_syncconstraints.get(ctx.idset(0));
		JobConstraint jc = wa_jobconstraints.get(ctx.jc());
		SortedSet<String> resets = wa_resets.get(ctx.idset(1));
		wa_transitions.put(ctx, new Transition(q1, q2, sc, jc));
	}


	@Override
	public void exitWa_transition(Wa_transitionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterDefn_equation(Defn_equationContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitDefn_equation(Defn_equationContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterAtom_constraintautomata(Atom_constraintautomataContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitAtom_constraintautomata(Atom_constraintautomataContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterPtype_typetag(Ptype_typetagContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitPtype_typetag(Ptype_typetagContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterWa(WaContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa(WaContext ctx) {
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
	public void enterSa_seepagefunction(Sa_seepagefunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitSa_seepagefunction(Sa_seepagefunctionContext ctx) {
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
	public void enterSa(SaContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitSa(SaContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterComp_composite(Comp_compositeContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitComp_composite(Comp_compositeContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterBexpr_conjunction(Bexpr_conjunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBexpr_conjunction(Bexpr_conjunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterFile(FileContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitFile(FileContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterAtom_portautomata(Atom_portautomataContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitAtom_portautomata(Atom_portautomataContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterParam(ParamContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitParam(ParamContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterBexpr_brackets(Bexpr_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBexpr_brackets(Bexpr_bracketsContext ctx) {
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


	@Override
	public void enterInst_iteration(Inst_iterationContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitInst_iteration(Inst_iterationContext ctx) {
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
	public void enterValue(ValueContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitValue(ValueContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterInst_reference(Inst_referenceContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitInst_reference(Inst_referenceContext ctx) {
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
	public void enterIexpr_addsub(Iexpr_addsubContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIexpr_addsub(Iexpr_addsubContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterWa_invariant(Wa_invariantContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa_invariant(Wa_invariantContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterDefn_definition(Defn_definitionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitDefn_definition(Defn_definitionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterBexpr_variable(Bexpr_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitBexpr_variable(Bexpr_variableContext ctx) {
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
	public void enterPa(PaContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitPa(PaContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterWa_jc_geq(Wa_jc_geqContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa_jc_geq(Wa_jc_geqContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterIndices(IndicesContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIndices(IndicesContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterNodes(NodesContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitNodes(NodesContext ctx) {
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


	@Override
	public void enterWa_jc_bool(Wa_jc_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa_jc_bool(Wa_jc_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterWa_jc_and(Wa_jc_andContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa_jc_and(Wa_jc_andContext ctx) {
		JobConstraint jc1 = wa_jobconstraints.get(ctx.wa_jc(0));
		JobConstraint jc2 = wa_jobconstraints.get(ctx.wa_jc(1));
		wa_jobconstraints.put(ctx, JobConstraint.conjunction(jc1, jc2));
	}


	@Override
	public void enterIexpr_unarymin(Iexpr_unaryminContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIexpr_unarymin(Iexpr_unaryminContext ctx) {
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


	@Override
	public void enterIexpr_natural(Iexpr_naturalContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIexpr_natural(Iexpr_naturalContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterIexpr_brackets(Iexpr_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitIexpr_brackets(Iexpr_bracketsContext ctx) {
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
	public void enterWa_jc_or(Wa_jc_orContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitWa_jc_or(Wa_jc_orContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterPtype_signature(Ptype_signatureContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitPtype_signature(Ptype_signatureContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enterInst_condition(Inst_conditionContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitInst_condition(Inst_conditionContext ctx) {
		// TODO Auto-generated method stub
		
	}
}