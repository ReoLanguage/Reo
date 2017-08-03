package nl.cwi.reo.semantics.workautomata;

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
import nl.cwi.reo.interpret.ReoParser.Wa_jc_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_eqContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_geqContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_leqContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_orContext;
import nl.cwi.reo.interpret.ReoParser.Wa_setContext;
import nl.cwi.reo.interpret.ReoParser.Wa_transitionContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_falseContext;
import nl.cwi.reo.interpret.ReoParser.Wa_jc_trueContext;
import nl.cwi.reo.interpret.listeners.BaseListener;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

// TODO: Auto-generated Javadoc
/**
 * Listens to events triggered by a
 * {@link org.antlr.v4.runtime.tree.ParseTreeWalker}. 
 */
public class ListenerWA extends BaseListener {

	/** The workautomata. */
	private ParseTreeProperty<WorkAutomaton> workautomata = new ParseTreeProperty<WorkAutomaton>();

	/** The wa jobconstraints. */
	private ParseTreeProperty<JobConstraint> wa_jobconstraints = new ParseTreeProperty<JobConstraint>();

	/** The idsets. */
	private ParseTreeProperty<SortedSet<Port>> idsets = new ParseTreeProperty<SortedSet<Port>>();

	/** The wa transitions. */
	// private ParseTreeProperty<SortedSet<String>> wa_resets = new
	// ParseTreeProperty<SortedSet<String>>();
	private ParseTreeProperty<Transition> wa_transitions = new ParseTreeProperty<Transition>();

	/** The wa states. */
	private ParseTreeProperty<String> wa_states = new ParseTreeProperty<String>();

	/**
	 * Instantiates a new listener WA.
	 *
	 * @param m
	 *            the m
	 */
	public ListenerWA(Monitor m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitAtom(nl.cwi.reo.interpret.
	 * ReoParser.AtomContext)
	 */
	@Override
	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, workautomata.get(ctx.wa()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterWa(nl.cwi.reo.interpret.
	 * ReoParser.WaContext)
	 */
	@Override
	public void enterWa(WaContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitWa(nl.cwi.reo.interpret.
	 * ReoParser.WaContext)
	 */
	@Override
	public void exitWa(WaContext ctx) {
		// Initialize all work automaton fields.
		Set<String> Q = new HashSet<String>();
		SortedSet<Port> P = new TreeSet<Port>();
		Set<String> J = new HashSet<String>();
		Map<String, JobConstraint> I = new HashMap<String, JobConstraint>();
		Map<String, Set<Transition>> T = new HashMap<String, Set<Transition>>();
		String q0 = "";

		// Iterate over all work automaton statements.
		for (Wa_exprContext stmt_ctx : ctx.wa_expr()) {

			// If the statement is annotated with a state and invariant
			// condition, then add this to the work automaton.
			String q;
			if ((q = wa_states.get(stmt_ctx)) != null) {

				// Define the initial state, if necessary.
				if (q0 == "")
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
						// If the state has already an invariant, add the
						// current
						I.put(q, JobConstraint.conjunction(old_jc, jc));
					}
				}
			}

			// If the statement is annotated with a transition, then
			// add this transition to the work automaton.
			Transition t;
			if ((t = wa_transitions.get(stmt_ctx)) != null) {

				// Define the initial state, if necessary.
				if (q0 == "")
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
				Set<Transition> outs = T.get(t.getSource());
				if (outs == null)
					outs = new HashSet<Transition>();

				if (!outs.contains(t)) {

					// Append the interface with all ports in the
					// synchronization constraint.
					P.addAll(t.getSyncConstraint());

					// Append the job set with all jobs in the job constraint.
					J.addAll(t.getJobConstraint().getW().keySet());

					// Add the transition
					outs.add(t);
				}
			}
		}

		// Annotate the parse tree with this new work automaton.
		workautomata.put(ctx, new WorkAutomaton(Q, P, J, I, T, q0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_set(nl.cwi.reo.interpret.
	 * ReoParser.Wa_setContext)
	 */
	@Override
	public void enterWa_set(Wa_setContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_set(nl.cwi.reo.interpret.
	 * ReoParser.Wa_setContext)
	 */
	@Override
	public void exitWa_set(Wa_setContext ctx) {
		SortedSet<Port> sc = new TreeSet<Port>();
		for (TerminalNode id : ctx.ID())
			sc.add(new Port(id.getText()));
		idsets.put(ctx, sc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_brackets(nl.cwi.reo.
	 * interpret.ReoParser.Wa_jc_bracketsContext)
	 */
	@Override
	public void enterWa_jc_brackets(Wa_jc_bracketsContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_brackets(nl.cwi.reo.
	 * interpret.ReoParser.Wa_jc_bracketsContext)
	 */
	@Override
	public void exitWa_jc_brackets(Wa_jc_bracketsContext ctx) {
		wa_jobconstraints.put(ctx, wa_jobconstraints.get(ctx.jc()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_leq(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_leqContext)
	 */
	@Override
	public void enterWa_jc_leq(Wa_jc_leqContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_leq(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_leqContext)
	 */
	@Override
	public void exitWa_jc_leq(Wa_jc_leqContext ctx) {
		String job = ctx.ID().getText();
		int bound = 0;
		try {
			bound = Integer.parseInt(ctx.NAT().getText());
		} catch (NumberFormatException e) {
		}
		wa_jobconstraints.put(ctx, new JobConstraint(job, bound, false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_eq(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_eqContext)
	 */
	@Override
	public void enterWa_jc_eq(Wa_jc_eqContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_eq(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_eqContext)
	 */
	@Override
	public void exitWa_jc_eq(Wa_jc_eqContext ctx) {
		String job = ctx.ID().getText();
		int bound = 0;
		try {
			bound = Integer.parseInt(ctx.NAT().getText());
		} catch (NumberFormatException e) {
		}
		wa_jobconstraints.put(ctx, new JobConstraint(job, bound, true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterWa_transition(nl.cwi.reo.
	 * interpret.ReoParser.Wa_transitionContext)
	 */
	@Override
	public void enterWa_transition(Wa_transitionContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitWa_transition(nl.cwi.reo.
	 * interpret.ReoParser.Wa_transitionContext)
	 */
	@Override
	public void exitWa_transition(Wa_transitionContext ctx) {
		String q1 = ctx.ID(0).getText();
		String q2 = ctx.ID(1).getText();
		SortedSet<Port> sc = idsets.get(ctx.wa_set(0));
		JobConstraint jc = wa_jobconstraints.get(ctx.jc());
		// SortedSet<String> resets = wa_resets.get(ctx.idset(1));
		wa_transitions.put(ctx, new Transition(q1, q2, sc, jc));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterWa_invariant(nl.cwi.reo.
	 * interpret.ReoParser.Wa_invariantContext)
	 */
	@Override
	public void enterWa_invariant(Wa_invariantContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitWa_invariant(nl.cwi.reo.
	 * interpret.ReoParser.Wa_invariantContext)
	 */
	@Override
	public void exitWa_invariant(Wa_invariantContext ctx) {
		wa_jobconstraints.put(ctx, wa_jobconstraints.get(ctx.jc()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_geq(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_geqContext)
	 */
	@Override
	public void enterWa_jc_geq(Wa_jc_geqContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_geq(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_geqContext)
	 */
	@Override
	public void exitWa_jc_geq(Wa_jc_geqContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_true(nl.cwi.reo.interpret
	 * .ReoParser.Wa_jc_trueContext)
	 */
	@Override
	public void enterWa_jc_true(Wa_jc_trueContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_true(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_trueContext)
	 */
	@Override
	public void exitWa_jc_true(Wa_jc_trueContext ctx) {
		wa_jobconstraints.put(ctx, new JobConstraint(true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_false(nl.cwi.reo.interpret
	 * .ReoParser.Wa_jc_falseContext)
	 */
	@Override
	public void enterWa_jc_false(Wa_jc_falseContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_false(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_falseContext)
	 */
	@Override
	public void exitWa_jc_false(Wa_jc_falseContext ctx) {
		wa_jobconstraints.put(ctx, new JobConstraint(false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_and(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_andContext)
	 */
	@Override
	public void enterWa_jc_and(Wa_jc_andContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_and(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_andContext)
	 */
	@Override
	public void exitWa_jc_and(Wa_jc_andContext ctx) {
		JobConstraint jc1 = wa_jobconstraints.get(ctx.jc(0));
		JobConstraint jc2 = wa_jobconstraints.get(ctx.jc(1));
		wa_jobconstraints.put(ctx, JobConstraint.conjunction(jc1, jc2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterWa_jc_or(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_orContext)
	 */
	@Override
	public void enterWa_jc_or(Wa_jc_orContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitWa_jc_or(nl.cwi.reo.interpret.
	 * ReoParser.Wa_jc_orContext)
	 */
	@Override
	public void exitWa_jc_or(Wa_jc_orContext ctx) {
	}

}