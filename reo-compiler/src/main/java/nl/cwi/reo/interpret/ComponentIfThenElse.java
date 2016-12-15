package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ComponentIfThenElse implements Expression<Component> {
	
	/**
	 * Conditions for each branch. If there are more conditions than branches, 
	 * then the additional conditions are ignored.
	 */
	public List<BooleanExpression> conditions;
	
	/**
	 * Branches of subprograms.
	 */
	public List<ComponentBody> branches;

	/**
	 * Constructs a parameterized if statement. 
	 * @param conditions		guards of each branch
	 * @param branches			subcomponent and definitions
	 */
	public ComponentIfThenElse(List<BooleanExpression> conditions, List<ComponentBody> branches) {
		this.conditions = conditions;
		this.branches = branches;
	}
	
	/**
	 * Gets a {link nl.cwi.reo.ProgramInstance} for a particular parameter assignment.
	 * @param parameters		parameter assignment
	 * @return Program instance {link nl.cwi.reo.ProgramInstance} for this parameterized component
	 * @throws Exception if the provided parameters do not match the signature of this program.
	 */
	public Component evaluate(Map<String,Value> p) throws Exception {
		for (int i = 0; i < conditions.size(); ++i) {
			if (conditions.get(i).evaluate(p) == true) {
				return branches.get(i).evaluate(p);
			}
		}
		return new Component();
	}
	
	/**
	 * Gets all variables in order of occurrence. 
	 * @return list of all variables in order of occurrence.
	 */
	public List<String> variables() {
		List<String> vars = new ArrayList<String>();
		for (int i = 0; i < branches.size(); ++i) {
			if (i < conditions.size())
				vars.addAll(conditions.get(i).variables());
			vars.addAll(branches.get(i).variables());
		}
		return vars;
	}
}
