package nl.cwi.pr.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Csp {

	//
	// FIELDS
	//

	private CspLiteral[] literals;
	private CspVariable[] variables;

	//
	// METHODS
	//

	public void setLiterals(final CspLiteral... literals) {
		this.literals = literals;
	}

	public void setVariables(final CspVariable... variables) {
		this.variables = variables;
	}

	//
	// STATIC - METHODS - PUBLIC
	//

	public static boolean solve(final Csp csp) {
		final List<CspVariable> variables = new ArrayList<CspVariable>();
		for (final CspVariable v : csp.variables)
			variables.add(v);

		return solve(variables, csp.literals);
	}

	public static boolean solve(final Csp[] csps) {
		final List<CspVariable> variables = new ArrayList<CspVariable>();
		for (final Csp csp : csps)
			for (final CspVariable v : csp.variables)
				variables.add(v);

		int nLiterals = 0;
		for (final Csp csp : csps)
			nLiterals += csp.literals.length;

		final CspLiteral[] literals = new CspLiteral[nLiterals];
		int index = 0;
		for (final Csp csp : csps) {
			int length = csp.literals.length;
			System.arraycopy(csp.literals, 0, literals, index, length);
			index += length;
		}

		return solve(variables, literals);
	}

	//
	// STATIC - METHODS - PRIVATE
	//

	private static boolean search(final Collection<Object> domain,
			final List<CspVariable> variables, final int i) {

		if (variables.size() == i)
			return true;

		final CspVariable variable = variables.get(i);
		if (variable.value != null)
			return variable.check() && search(domain, variables, i + 1);

		for (final Object o : domain)
			if (variable.setAndCheck(o) && search(domain, variables, i + 1))
				return true;

		variable.value = null;
		return false;
	}

	private static boolean solve(final List<CspVariable> variables,
			final CspLiteral[] literals) {

		for (final CspVariable v : variables)
			v.importValue();

		for (final CspLiteral l : literals)
			l.check = true;

		/*
		 * Close
		 */

		final List<CspLiteral> openLiterals = new ArrayList<>(
				Arrays.asList(literals));

		int nOpenLiterals = Integer.MAX_VALUE;
		while (!openLiterals.isEmpty() && openLiterals.size() < nOpenLiterals) {
			nOpenLiterals = openLiterals.size();

			Iterator<CspLiteral> iterator = openLiterals.iterator();
			while (iterator.hasNext())
				if (iterator.next().close())
					iterator.remove();
		}

		/*
		 * Solve
		 */

		boolean hasSolution;
		if (openLiterals.isEmpty()) {
			hasSolution = true;
			for (final CspLiteral l : literals)
				if (!l.isClosed() || !l.holds()) {
					hasSolution = false;
					break;
				}
		}

		else {
			final Set<Object> domain = new HashSet<Object>();
			for (final CspVariable v : variables)
				domain.add(v.value);

			domain.remove(null);
			hasSolution = search(domain, variables, 0);
		}

		/*
		 * Return
		 */

		for (final CspLiteral l : literals)
			l.check = false;

		if (!hasSolution)
			return false;

		for (final CspVariable v : variables)
			v.exportValue();

		return true;
	}
}