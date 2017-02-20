package nl.cwi.reo.semantics.workautomata;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A job constraint that is used in {@link nl.cwi.reo.semantics.workautomata.WorkAutomaton}.
 */
public final class JobConstraint implements Comparable<JobConstraint> {
	
	/**
	 * Satisfiability.
	 */
	private final boolean sat;
	
	/**
	 * Available work.
	 */
	private final SortedMap<String, Integer> w;

	/**
	 * Required jobs.
	 */
	private final Set<String> R;

	/**
	 * Constructs a trivial job constraint.
	 * @param sat 		satisfiability of the job constraint
	 */
	public JobConstraint(boolean sat) {
		this.sat = sat;
		this.w = new TreeMap<String, Integer>();
		this.R = new HashSet<String>();
	}

	/**
	 * Constructs an atomic job constraint.
	 * @param job		job name x
	 * @param bound		available work n
	 * @param required	requiredness: true iff x==n
	 */
	public JobConstraint(String job, int bound, boolean required) {
		this.sat = true;
		SortedMap<String, Integer> work = new TreeMap<String, Integer>();
		work.put(job, bound);
		this.w = work;
		Set<String> req = new HashSet<String>();
		if (required) req.add(job);
		this.R = req;
	}

	/**
	 * Constructs a new satisfiable job constraint
	 * @param w			maximal progress allowed by job constraint
	 * @param R			set of jobs that are required to finish
	 */
	public JobConstraint(SortedMap<String, Integer> w, Set<String> R) {
		this.sat = true;
		this.w = w;
		this.R = R;
	}
	
	/**
	 * Computes the conjunction of two job constraints
	 * @param jc1	job constraint
	 * @param jc2	job constraint
	 * @return conjunction of jc1 and jc2.
	 */
	public static JobConstraint conjunction(JobConstraint jc1, JobConstraint jc2) {
		TreeMap<String, Integer> w = new TreeMap<String, Integer>(jc1.w);
		Set<String> R = new HashSet<String>(jc1.R);
		for (Entry<String, Integer> entry : jc2.w.entrySet()) {
			if (w.containsKey(entry.getKey())) {
				if (R.contains(entry.getKey())) {
					if (entry.getValue() != w.get(entry.getKey()))
						return new JobConstraint(false);
				} else {
					if (entry.getValue() > w.get(entry.getKey())) {
						return new JobConstraint(false);
					} else {
						w.put(entry.getKey(), entry.getValue());
					}
				}
			} else {
				w.put(entry.getKey(), entry.getValue());
			}
		}
		return new JobConstraint(w, R);
	}
	
	/**
	 * Checks if the job constraint is satisfiable.
	 * @return true if job constraint is satisfiable
	 */
	public boolean isSatisfiable() {
		return this.sat;
	}
	
	/**
	 * Get the available work.
	 * @return map assigning the available work to each job
	 */
	public Map<String, Integer> getW() {
		return this.w;
	}
	
	/**
	 * Get the required work.
	 * @return set of all required jobs
	 */
	public Set<String> getR() {
		return this.R;
	}
	
	/**
	 * Gets the string representation of this job constraint. Occurrence of jobs in the string representation
	 * happen in lexicographical order, and each job occurs exactly once. 
	 */
	@Override 
	public String toString() { 
		String jc = "";
		if (w.isEmpty()) {
			jc = "true";
		} else {
			for (Entry<String, Integer> entry : w.entrySet()) {
				jc += (jc == "" ? entry.getKey() : " & " + entry.getKey());
				jc += R.contains(entry.getKey()) ?  "==" : "<=";
				jc += entry.getValue();
			}
		}
		return jc;
	}
	
	/**
	 * Returns the hash code of string representation of this job constraint.
	 */
	@Override 
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	/**
	 * Compares this job constraint with the specified job constraint for order.
	 * @param other 	the job constraint to be compared.
	 * @return a negative integer, zero, or a positive integer as this job constraint is less than, 
	 * equal to, or greater than the specified job constraint.
	 */
	public int compareTo(JobConstraint other) {
		return this.toString().compareTo(other.toString());
	}
	
	/**
	 * Compares this job constraint to the specified object. The result is true if and only if the argument 
	 * is not null and is a JobConstraint object that represents the same job constraint as this object.
	 * @param other 	the job constraint to be compared.
	 * @return true if the given object represents a JobConstraint equivalent to this job constraint, false otherwise.
	 */
	@Override 
	public boolean equals(Object other) {
		if (!(other instanceof JobConstraint)) return false;
		JobConstraint t = (JobConstraint)other;
		return t.toString().equals(this.toString());
	}
}
