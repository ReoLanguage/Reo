package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public interface Formula {

	/**
	 * Computes the condition on input ports by existentially quantifying over
	 * output ports.
	 * 
	 * @return existential quantification of this constraint over all output
	 *         ports.
	 */
	public Formula getGuard();

	/**
	 * Computes an assignment of data terms to output ports that satisfies this
	 * data constraint.
	 * 
	 * @return Assignment of data terms to output ports.
	 */
	public Map<Port, Term> getAssignment();
	
	public @Nullable Formula evaluate(Scope s, Monitor m);

	public Set<Port> getInterface();
	
	public Formula rename(Map<Port, Port> links);
	
	public Formula DNF();

}
