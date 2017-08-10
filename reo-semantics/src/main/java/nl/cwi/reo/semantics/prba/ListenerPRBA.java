package nl.cwi.reo.semantics.prba;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.ReoParser.Rba_distributionContext;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.rulebasedautomata.ListenerRBA;
import nl.cwi.reo.util.Monitor;

/**
 * Listener for probabilistic rule-based automata.
 */
public class ListenerPRBA extends ListenerRBA {

	/**
	 * Instantiates a new listener PRBA.
	 *
	 * @param m
	 *            the monitor
	 */
	public ListenerPRBA(Monitor m) {
		super(m);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exitRba_distribution(Rba_distributionContext ctx) {
		Map<Term, Term> distr = new HashMap<>();
		for (int i = 0; i < ctx.rba_term().size(); i += 2) {
			Term t1 = terms.get(ctx.rba_term(i));
			Term t2 = terms.get(ctx.rba_term(i+1));
			distr.put(t2, t1);
		}
		terms.put(ctx, new Distribution(distr));
	}

}
