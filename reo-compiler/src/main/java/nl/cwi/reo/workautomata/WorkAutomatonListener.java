package nl.cwi.reo.workautomata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.parse.ReoFileBaseListener;
import nl.cwi.reo.parse.ReoParser;
import nl.cwi.reo.workautomata.JobConstraint;
import nl.cwi.reo.workautomata.Transition;
import nl.cwi.reo.workautomata.WorkAutomaton;

/**
 * WorkAutomatonListener listens to events triggered by a ParseTreeWalker.
 *
 * @author	Kasper Dokter
 */
public class WorkAutomatonListener extends ReoFileBaseListener {
	
	/**
	 * Work automaton parse tree properties.
	 */
	private ParseTreeProperty<JobConstraint> wa_jobconstraints = new ParseTreeProperty<JobConstraint>();
	private ParseTreeProperty<SortedSet<String>> wa_syncconstraints = new ParseTreeProperty<SortedSet<String>>();	
	private ParseTreeProperty<Transition> wa_transitions = new ParseTreeProperty<Transition>();	
	private ParseTreeProperty<String> wa_states = new ParseTreeProperty<String>();

	/**
	 * This action provides {@link nl.cwi.reo.parse.ReoFileBaseListener} with work automata
	 * by annotating the parse tree with an {@link nl.cwi.reo.workautomata.WorkAutomaton}.
	 */
	@Override 
	public void exitAtomWA(@NotNull ReoParser.AtomWAContext ctx) { 

		// Initialize all work automaton fields.
		Set<String> Q = new HashSet<String>();
		Set<String> P = new HashSet<String>();
		Set<String> J = new HashSet<String>();
		Map<String, JobConstraint> I = new HashMap<String, JobConstraint>();
		Map<String, Set<Transition>> T = new HashMap<String, Set<Transition>>();
		String q0 = null;		
		
		// Iterate over all work automaton statements.
		for (ReoParser.Wa_stmtContext stmt_ctx : ctx.wa().wa_stmt()) {
			
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

		// Construct the work automaton, and 
		WorkAutomaton A = new WorkAutomaton(Q, P, J, I, T, q0);
		
		// Annotate the parse tree with this new work automaton. 
		setAtom(ctx, A);
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.workautomata.Transition} that models 
	 * a work automaton transition.
	 */
	@Override public void exitWa_stmtTrans(@NotNull ReoParser.Wa_stmtTransContext ctx) { 
		String q1 = ctx.ID(0).getText();
		String q2 = ctx.ID(1).getText();
		SortedSet<String> sc = wa_syncconstraints.get(ctx.sync_const());
		JobConstraint jc = wa_jobconstraints.get(ctx.wa_jc());
		wa_transitions.put(ctx, new Transition(q1, q2, sc, jc));
	}	

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.workautomata.JobConstraint} 
	 * that models a atomic job constraint equality.
	 */
	@Override public void exitWa_jcEql(@NotNull ReoParser.Wa_jcEqlContext ctx) {
		String job = ctx.ID().getText();
		int bound = 0;
		try {
			bound = Integer.parseInt(ctx.INT().getText());
		} catch (NumberFormatException e) { }
		wa_jobconstraints.put(ctx, new JobConstraint(job, bound, true));
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.workautomata.JobConstraint} 
	 * that models a conjunction of job constraints.
	 */
	@Override public void exitWa_jcAnd(@NotNull ReoParser.Wa_jcAndContext ctx) {
		JobConstraint jc1 = wa_jobconstraints.get(ctx.wa_jc(0));
		JobConstraint jc2 = wa_jobconstraints.get(ctx.wa_jc(1));
		wa_jobconstraints.put(ctx, JobConstraint.conjunction(jc1, jc2));
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.workautomata.JobConstraint} 
	 * that models an atomic job constraint inequality.
	 */
	@Override public void exitWa_jcLeq(@NotNull ReoParser.Wa_jcLeqContext ctx) { 
		String job = ctx.ID().getText();
		int bound = 0;
		try {
			bound = Integer.parseInt(ctx.INT().getText());
		} catch (NumberFormatException e) { }
		wa_jobconstraints.put(ctx, new JobConstraint(job, bound, false));
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.workautomata.JobConstraint} 
	 * that models an invariant condition.
	 */
	@Override public void exitWa_stmtInvar(@NotNull ReoParser.Wa_stmtInvarContext ctx) {
		JobConstraint jc = wa_jobconstraints.get(ctx.wa_jc());
		wa_jobconstraints.put(ctx, jc);
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.workautomata.JobConstraint} 
	 * that models a trivially true job constraint.
	 */
	@Override public void exitWa_jcTrue(@NotNull ReoParser.Wa_jcTrueContext ctx) {
		wa_jobconstraints.put(ctx, new JobConstraint(true));
	}

	/**
	 * Annotates the parse tree with an {@link java.util.Set}&lt;{@link java.lang.String}&gt; 
	 * that models a synchronization constraint.
	 */
	@Override public void exitSync_const(@NotNull ReoParser.Sync_constContext ctx) {
		SortedSet<String> sc = new TreeSet<String>();
		for (TerminalNode id : ctx.ID())
			sc.add(id.getText());
		wa_syncconstraints.put(ctx, sc);
	}
}
