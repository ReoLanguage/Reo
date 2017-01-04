package nl.cwi.reo.automata;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A label is an object that decorates a transition of an automaton.
 * Although not strictly enforced, labels must be immutable.
 * We use null as the silent label.
 * @param <L> label type.
 */
public interface Label<L> {
	
	/**
	 * Constructs the composition of a list of labels. If a label is null, 
	 * then it is treated as a silent action label.
	 * @param lbls		list of labels
	 * @return the composition of the labels.
	 */
	public L compose(List<L> lbls);
	
	/**
	 * Constructs the restriction of a label to a given interface.
	 * @param intface		set of names in the interface.
	 * @return restricted label
	 */
	public L restrict(Set<String> intface);
	
	/**
	 * Renames entry.Key() to entry.Value() for every entry renaming map.
	 * @param links		renaming map
	 * @return renamed transition.
	 */
	public L rename(Map<String, String> links);
}
