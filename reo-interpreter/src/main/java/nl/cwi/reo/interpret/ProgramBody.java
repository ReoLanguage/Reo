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
			throw new NullPointerException();
		for (ProgramExpression stmt : stmts)
			if (stmt == null)
				throw new NullPointerException();
		this.stmts = stmts;
	}
	
	/**
	 * Evaluates this body for a particular parameter assignment.
	 * @param params			parameter assignment
	 * @return Concrete instance of this body.
	 * @throws Exception if not all required parameters are provided.
	 */
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		
		ProgramExpression prog = null;
		Definitions defs = new Definitions(params);
		Definitions newDefs = new Definitions();
		do {
			newDefs = findNewDefinitions(stmts, defs);
			defs.putAll(newDefs);
		} while (!newDefs.isEmpty());
		
		boolean isProgramValue = true;		
		List<ProgramExpression> stmts_p = new ArrayList<ProgramExpression>();	
		List<ProgramValue> progs = new ArrayList<ProgramValue>(); 
		for (ProgramExpression stmt : stmts) {
			ProgramExpression stmt_p = stmt.evaluate(defs);
			stmts_p.add(stmt_p);
			if (stmt_p instanceof ProgramValue) {
				progs.add((ProgramValue)stmt_p);
			} else {
				isProgramValue = false;
			}
		}		
		if (isProgramValue) {
			prog = new ProgramValue().compose(progs);
		} else {
			prog = new ProgramBody(stmts_p);
		}
		return prog;
	}
	
	private Definitions findNewDefinitions(List<ProgramExpression> stmts, 
			Definitions params) throws Exception {
		Definitions defs = new Definitions();
		for (ProgramExpression stmt : stmts) {			
			ProgramExpression stmt_p = stmt.evaluate(params);
			if (stmt_p instanceof ProgramValue) {
				ProgramValue prog = ((ProgramValue)stmt_p);
				for (Map.Entry<VariableName, Expression> def : prog.getDefinitions().entrySet()) {
					if (!params.containsKey(def.getKey())) {
						if (def.getValue() instanceof BooleanValue)
							defs.put(def.getKey(), (def.getValue()));

						if (def.getValue() instanceof IntegerValue)
							defs.put(def.getKey(), (def.getValue()));

						if (def.getValue() instanceof StringValue)
							defs.put(def.getKey(), (def.getValue()));

						if (def.getValue() instanceof ComponentValue)
							defs.put(def.getKey(), (def.getValue()));
					}	
				}
			}
		}
		return defs;
	}
	
	@Override
	public String toString() {
		String s = "{";
		Iterator<ProgramExpression> stmt = stmts.iterator();
		while (stmt.hasNext()) 
			s += stmt.next() + (stmt.hasNext() ? ", " : "");
		return s + "}";
	}
}
