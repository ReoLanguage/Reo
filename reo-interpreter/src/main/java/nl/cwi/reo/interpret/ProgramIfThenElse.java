package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ProgramIfThenElse implements ProgramExpression {
	
	/**
	 * Conditions for each branch. If there are more conditions than branches, 
	 * then the additional conditions are ignored.
	 */
	public List<BooleanExpression> conditions;
	
	/**
	 * Branches of subprograms.
	 */
	public List<ProgramExpression> branches;

	/**
	 * Constructs a parameterized if statement. 
	 * @param conditions		guards of each branch
	 * @param branches			subcomponent and definitions
	 */
	public ProgramIfThenElse(List<BooleanExpression> conditions, List<ProgramExpression> branches) {
		if (conditions == null || branches == null)
			throw new NullPointerException();
		this.conditions = conditions;
		this.branches = branches;
	}
	
	/**
	 * Gets a {link nl.cwi.reo.ProgramInstance} for a particular parameter assignment.
	 * @param parameters		parameter assignment
	 * @return Program instance {link nl.cwi.reo.ProgramInstance} for this parameterized component
	 * @throws Exception if the provided parameters do not match the signature of this program.
	 */
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		List<BooleanExpression> conditions_p = new ArrayList<BooleanExpression>();
		List<ProgramExpression> branches_p = new ArrayList<ProgramExpression>();
		Iterator<BooleanExpression> condition = conditions.iterator();
		Iterator<ProgramExpression> branch =  branches.iterator();
		while (condition.hasNext() && branch.hasNext()) {
			BooleanExpression e = condition.next().evaluate(params);
			ProgramExpression b = branch.next().evaluate(params);
			conditions_p.add(e);
			if (e instanceof BooleanValue && ((BooleanValue)e).toBoolean() == true)
				return b;
		}
		return new ProgramIfThenElse(conditions_p, branches_p);
	}
	
	@Override
	public String toString() {
		String s = "";
		boolean first = true;
		Iterator<BooleanExpression> condition = conditions.iterator();
		Iterator<ProgramExpression> branch =  branches.iterator();
		while (condition.hasNext() && branch.hasNext()) {
			s += (first ? "if " : " else " ) + condition.next() + " " + branch.next();
			first = false;
		}
		return s;
	}
}
