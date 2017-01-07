package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ProgramBody implements ProgramExpression {
	
	/**
	 * Program statements.
	 */
	public List<ProgramExpression> stmts;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	list of definitions
	 */
	public ProgramBody(List<ProgramExpression> stmts) {
		if (stmts == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		for (ProgramExpression stmt : stmts)
			if (stmt == null)
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
		boolean isProgramValue = true;
		List<ProgramExpression> stmts_p = new ArrayList<ProgramExpression>();				
		ProgramValue body = new ProgramValue(params); 
		for (ProgramExpression stmt : stmts) {
			ProgramExpression expr = stmt.evaluate(body.getDefinitions());
			stmts_p.add(expr);
			if (expr instanceof ProgramValue) {
				body = body.compose((ProgramValue)expr);
			} else {
				isProgramValue = false;
			}
		}
		if (isProgramValue) return body;
		return new ProgramBody(stmts_p);
	}
	
	@Override
	public String toString() {
		String s = "";
		Iterator<ProgramExpression> stmt = stmts.iterator();
		while (stmt.hasNext()) 
			s += stmt.next() + (stmt.hasNext() ? " " : "");
		return s;
	}
}
