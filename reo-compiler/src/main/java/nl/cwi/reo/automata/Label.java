package nl.cwi.reo.automata;

import java.util.List;

/**
 * A label is an object that decorates a transition of an automaton.
 * @param <L> label type.
 */
public interface Label<L> {
	
	/**
	 * Composition of a list of labels. If a label is null, then it is
	 * treated as a silent action label.
	 * @param lbls		list of labels
	 * @return the composition of the labels.
	 */
	public L compose(List<L> lbls);
}
