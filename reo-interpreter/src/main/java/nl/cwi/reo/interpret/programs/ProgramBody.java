package nl.cwi.reo.interpret.programs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.booleans.BooleanValue;
import nl.cwi.reo.interpret.components.ComponentValue;
import nl.cwi.reo.interpret.integers.IntegerValue;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.strings.StringValue;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Semantics;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ProgramBody<T extends Semantics<T>> implements ProgramExpression<T> {
	
	/**
	 * Program statements.
	 */
	public List<ProgramExpression<T>> stmts;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	list of definitions
	 */
	public ProgramBody(List<ProgramExpression<T>> stmts) {
		if (stmts == null)
			throw new NullPointerException();
		for (ProgramExpression<T> stmt : stmts)
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
	public ProgramExpression<T> evaluate(Map<VariableName, Expression> params) throws Exception {	
		
//		System.out.println("[info] Evaluating " + this + " using " + params.keySet());

		Definitions definitions = new Definitions(params);
		
		List<ProgramExpression<T>> stmts = new ArrayList<ProgramExpression<T>>();
		List<ProgramExpression<T>> stmts_p = new ArrayList<ProgramExpression<T>>(this.stmts);	
		List<ProgramValue<T>> progs = new ArrayList<ProgramValue<T>>(); 
		boolean isProgramValue = true;
		boolean loop = true;
		
		while (loop) {
			
			stmts = new ArrayList<ProgramExpression<T>>(stmts_p);
			stmts_p = new ArrayList<ProgramExpression<T>>();			
			progs = new ArrayList<ProgramValue<T>>();
			isProgramValue = true;
			loop = false;
			
			for (ProgramExpression<T> s : stmts) {			
				
				ProgramExpression<T> s_p = s.evaluate(definitions);
				stmts_p.add(s_p);
				
				if (s_p instanceof ProgramValue) {
					progs.add((ProgramValue<T>)s_p);
					Map<VariableName, Expression> progDefs = ((ProgramValue<T>)s_p).getDefinitions();
					for (Map.Entry<VariableName, Expression> def : progDefs.entrySet()) {
						if (!definitions.containsKey(def.getKey())) {
							definitions.put(def.getKey(), def.getValue());
							if (def.getValue() instanceof BooleanValue) loop = true;
							if (def.getValue() instanceof IntegerValue) loop = true;
							if (def.getValue() instanceof StringValue) loop = true;
							if (def.getValue() instanceof ComponentValue) loop = true;
						}	
					}
				} else {
					isProgramValue = false;
				}
			}
			
		}
		
		ProgramExpression<T> prog = null;
		
		if (isProgramValue) 
			prog = new ProgramValue<T>().compose(progs);
		else
			prog = new ProgramBody<T>(stmts_p);

//		System.out.println("[info] " + this + " evaluates to " + prog + " using " + params.keySet());
		return prog;
	}
	
	@Override
	public String toString() {
		String s = "{";
		Iterator<ProgramExpression<T>> stmt = stmts.iterator();
		while (stmt.hasNext()) 
			s += stmt.next() + (stmt.hasNext() ? ", " : "");
		return s + "}";
	}
}
