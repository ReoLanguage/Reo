package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class Body implements ProgramExpression {
	
	/**
	 * Program statements.
	 */
	public List<ProgramExpression> stmts;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	list of definitions
	 */
	public Body(List<ProgramExpression> stmts) {
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
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		boolean instancesAreValue = true;
		List<ProgramExpression> stmts_p = new ArrayList<ProgramExpression>();				
		Program body = new Program(params); 
		for (ProgramExpression stmt : stmts) {
			ProgramExpression expr = stmt.evaluate(body.getDefinitions());
			stmts_p.add(expr);
			if (expr instanceof Program) {
				body = body.compose((Program)expr);
			} else {
				instancesAreValue = false;
			}
		}
		if (instancesAreValue) return body;
		return new Body(stmts_p);
	}
	
	@Override
	public String toString() {
		String s = "";
		for (ProgramExpression x : stmts)
			s += "" + x + "\n";
		return s;
	}
}
