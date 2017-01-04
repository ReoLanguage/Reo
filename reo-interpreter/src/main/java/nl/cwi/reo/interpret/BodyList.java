package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class BodyList implements BodyExpression {
	
	/**
	 * Program statements.
	 */
	public List<BodyExpression> stmts;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	list of definitions
	 */
	public BodyList(List<BodyExpression> stmts) {
		if (stmts == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.stmts = stmts;
	}
	
	/**
	 * Evaluates this body for a particular parameter assignment.
	 * @param params			parameter assignment
	 * @return Concrete instance of this body.
	 * @throws Exception if not all required parameters are provided.
	 */
	public BodyExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		boolean instancesAreValue = true;
		List<BodyExpression> stmts_p = new ArrayList<BodyExpression>();				
		BodyValue body = new BodyValue(params); 
		for (BodyExpression stmt : stmts) {
			BodyExpression expr = stmt.evaluate(body.getDefinitions());
			stmts_p.add(expr);
			if (expr instanceof BodyValue) {
				body = body.compose((BodyValue)expr);
			} else {
				instancesAreValue = false;
			}
		}
		if (instancesAreValue) return body;
		return new BodyList(stmts_p);
	}
	
	@Override
	public String toString() {
		String s = "";
		for (BodyExpression x : stmts)
			s += "" + x + "\n";
		return s;
	}
}
