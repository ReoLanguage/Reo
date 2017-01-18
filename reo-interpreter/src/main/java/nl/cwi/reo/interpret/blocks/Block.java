package nl.cwi.reo.interpret.blocks;

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

public class Block<T extends Semantics<T>> implements Statement<T> {
	
	/**
	 * List of statements.
	 */
	public List<Statement<T>> stmts;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	list of definitions
	 */
	public Block(List<Statement<T>> stmts) {
		if (stmts == null)
			throw new NullPointerException();
		for (Statement<T> stmt : stmts)
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
	public Statement<T> evaluate(Map<VariableName, Expression> params) throws Exception {	
		
//		System.out.println("[info] Evaluating " + this);

		Definitions definitions = new Definitions(params);
		
		List<Statement<T>> stmts = new ArrayList<Statement<T>>();
		List<Statement<T>> stmts_p = new ArrayList<Statement<T>>(this.stmts);	
		List<Program<T>> progs = new ArrayList<Program<T>>(); 
		boolean isProgramValue = true;
		boolean loop = true;
		
		while (loop) {
			
			stmts = new ArrayList<Statement<T>>(stmts_p);
			stmts_p = new ArrayList<Statement<T>>();			
			progs = new ArrayList<Program<T>>();
			isProgramValue = true;
			loop = false;
			
			for (Statement<T> s : stmts) {			
				
				Statement<T> s_p = s.evaluate(definitions);
				stmts_p.add(s_p);
				
				if (s_p instanceof Program) {
					progs.add((Program<T>)s_p);
					Map<VariableName, Expression> progDefs = ((Program<T>)s_p).getDefinitions();
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
		
		Statement<T> prog = null;
		
		if (isProgramValue) {
			prog = new Program<T>().compose(progs);
		} else {
			prog = new Block<T>(stmts_p);
		}
		
//		System.out.println("[info] " + this + " evaluates to ");
//		System.out.println("       " + prog + " using " + params.keySet());
		return prog;
	}
	
	@Override
	public String toString() {
		String s = "{";
		Iterator<Statement<T>> stmt = stmts.iterator();
		while (stmt.hasNext()) 
			s += stmt.next() + (stmt.hasNext() ? ", " : "");
		return s + "}";
	}
}
