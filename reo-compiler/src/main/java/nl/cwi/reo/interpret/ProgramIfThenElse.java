package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ProgramIfThenElse implements Program {
	
	/**
	 * Conditions for each branch. If there are more conditions than branches, 
	 * then the additional conditions are ignored.
	 */
	public List<BooleanExpression> conditions;
	
	/**
	 * Branches of subprograms.
	 */
	public List<Program> branches;

	/**
	 * Constructs a parameterized if statement. 
	 * @param conditions		guards of each branch
	 * @param branches			subcomponent and definitions
	 */
	public ProgramIfThenElse(List<BooleanExpression> conditions, List<Program> branches) {
		if (conditions == null || branches == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.conditions = conditions;
		this.branches = branches;
	}
	
	/**
	 * Gets a {link nl.cwi.reo.ProgramInstance} for a particular parameter assignment.
	 * @param parameters		parameter assignment
	 * @return Program instance {link nl.cwi.reo.ProgramInstance} for this parameterized component
	 * @throws Exception if the provided parameters do not match the signature of this program.
	 */
	public Program evaluate(DefinitionList params) throws Exception {
		
		List<BooleanExpression> conditions_p = new ArrayList<BooleanExpression>();
		List<Program> branches_p = new ArrayList<Program>();
		
		for (int i = 0; i < conditions.size(); ++i) {
			BooleanExpression e = conditions.get(i).evaluate(params);
			Program b = branches.get(i).evaluate(params);
			conditions_p.add(e);
			if (e instanceof BooleanValue && ((BooleanValue)e).toBoolean() == true)
				return b;
		}
		
		return new ProgramIfThenElse(conditions_p, branches_p);
	}
}
