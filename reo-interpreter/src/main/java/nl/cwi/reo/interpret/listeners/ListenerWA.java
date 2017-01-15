package nl.cwi.reo.interpret.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.WaContext;
import nl.cwi.reo.interpret.ReoParser.Wa_exprContext;
import nl.cwi.reo.interpret.ReoParser.Wa_invariantContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_andContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_boolContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_eqContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_geqContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_leqContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_orContext;
import nl.cwi.reo.interpret.ReoParser.Wa_setContext;
import nl.cwi.reo.interpret.ReoParser.Wa_transitionContext;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.workautomata.JobConstraint;
import nl.cwi.reo.workautomata.Transition;
import nl.cwi.reo.workautomata.WorkAutomaton;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.interpret.p}.
 */
public class ListenerWA extends Listener<WorkAutomaton> {	
		
	private ParseTreeProperty<WorkAutomaton> workautomata = new ParseTreeProperty<WorkAutomaton>();
	private ParseTreeProperty<JobConstraint> wa_jobconstraints = new ParseTreeProperty<JobConstraint>();
	private ParseTreeProperty<SortedSet<Port>> idsets = new ParseTreeProperty<SortedSet<Port>>();
//	private ParseTreeProperty<SortedSet<String>> wa_resets = new ParseTreeProperty<SortedSet<String>>();	
	private ParseTreeProperty<Transition> wa_transitions = new ParseTreeProperty<Transition>();	
	private ParseTreeProperty<String> wa_states = new ParseTreeProperty<String>();

	@Override
	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, workautomata.get(ctx.wa()));
	}
	
	@Override
	public void enterWa(WaContext ctx) {
	}

	@Override
	public void exitWa(WaContext ctx) {
		// Initialize all work automaton fields.
		Set<String> Q = new HashSet<String>();
		SortedSet<Port> P = new TreeSet<Port>();
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
	public void enterWa_set(Wa_setContext ctx) {
	}

	@Override
	public void exitWa_set(Wa_setContext ctx) {
		SortedSet<Port> sc = new TreeSet<Port>();
		for (TerminalNode id : ctx.ID())
			sc.add(new Port(id.getText()));
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
		SortedSet<Port> sc = idsets.get(ctx.wa_set(0));
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

}