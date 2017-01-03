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
		for (Program prog : stmts) {
			Program prog_p = prog.evaluate(params);
			stmts_p.add(prog_p);
			if (prog_p instanceof ProgramValue) {
				ProgramValue B = (ProgramValue)prog_p;
				Instance inst = B.getInstance();
				insts.add(inst);
				definitions.putAll(B.getDefinitions());
			} else {
				instancesAreValue = false;
			}
		}
		if (instancesAreValue) {
			Instance instances = new Instance();
			instances = instances.compose(insts);
			return new ProgramValue(instances, definitions);
		}
		return new ProgramBody(stmts_p);
	}
}
