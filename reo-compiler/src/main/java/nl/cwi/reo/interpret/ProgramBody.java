package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashMap;
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
		this.stmts = stmts;
	}
	
	/**
	 * Evaluates this body for a particular parameter assignment.
	 * @param params			parameter assignment
	 * @return Concrete instance of this body.
	 * @throws Exception if not all required parameters are provided.
	 */
	public ProgramExpression evaluate(DefinitionList params) throws Exception {	
		List<ProgramExpression> stmts_p = new ArrayList<ProgramExpression>();
		boolean instancesAreValue = true;
		List<InstanceExpression> insts = new ArrayList<InstanceExpression>();
		List<InstanceValue> insts_val = new ArrayList<InstanceValue>();
		DefinitionList definitions = new DefinitionList(params);
		for (ProgramExpression e : stmts) {
			ProgramExpression pe = e.evaluate(params);
			stmts_p.add(pe);
			if (e instanceof ProgramValue) {
				ProgramValue B = (ProgramValue)pe;
				InstanceExpression inst = B.getInstances();
				insts.add(inst);
				if (inst instanceof InstanceValue) {
					insts_val.add((InstanceValue)inst);
				} else {
					instancesAreValue = false;
				}
				definitions.putAll(B.getDefinitions());
			}
		}
		if (instancesAreValue) {			
			return new ProgramValue(new InstanceValue().compose(insts_val), definitions);			
		}
		return new ProgramBody(stmts_p);
	}
}
