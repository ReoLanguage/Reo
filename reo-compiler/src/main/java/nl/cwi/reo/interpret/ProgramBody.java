package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ProgramBody implements Program {
	
	/**
	 * Program statements.
	 */
	public List<Program> stmts;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	list of definitions
	 */
	public ProgramBody(List<Program> stmts) {
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
	public Program evaluate(DefinitionList params) throws Exception {	
		
		// TODO Evaluate the subcomponents in this body is the correct order, such that
		// local definitions are resolved. (Hence, recognize possible recursive definitions)
		
		List<Program> stmts_p = new ArrayList<Program>();
		boolean instancesAreValue = true;
		List<Instance> insts = new ArrayList<Instance>();
		DefinitionList definitions = new DefinitionList(params);
		for (Program e : stmts) {
			Program pe = e.evaluate(params);
			stmts_p.add(pe);
			if (e instanceof ProgramValue) {
				ProgramValue B = (ProgramValue)pe;
				Instance inst = B.getInstances();
				insts.add(inst);
				if (inst instanceof Instance) {
					insts.add((Instance)inst);
				} else {
					instancesAreValue = false;
				}
				definitions.putAll(B.getDefinitions());
			}
		}
		if (instancesAreValue) {			
			return new ProgramValue(new Instance().compose(insts), definitions);			
		}
		return new ProgramBody(stmts_p);
	}
}
