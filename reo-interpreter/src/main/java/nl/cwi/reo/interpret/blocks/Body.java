package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.booleans.BooleanValue;
import nl.cwi.reo.interpret.integers.IntegerValue;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.strings.StringValue;
import nl.cwi.reo.interpret.systems.ReoSystemValue;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public class Body<T extends Semantics<T>> implements ReoBlock<T> {
	
	/**
	 * List of statements.
	 */
	public List<ReoBlock<T>> stmts;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	list of definitions
	 */
	public Body(List<ReoBlock<T>> stmts) {
		if (stmts == null)
			throw new NullPointerException();
		for (ReoBlock<T> stmt : stmts)
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
	public ReoBlock<T> evaluate(Map<String, Expression> params) throws CompilationException {	
		
//		System.out.println("[info] Evaluating " + this);

		Definitions definitions = new Definitions(params);
		
		List<ReoBlock<T>> stmts = new ArrayList<ReoBlock<T>>();
		List<ReoBlock<T>> stmts_p = new ArrayList<ReoBlock<T>>(this.stmts);	
		List<Assembly<T>> progs = new ArrayList<Assembly<T>>(); 
		boolean isProgramValue = true;
		boolean loop = true;
		
		while (loop) {
			
			stmts = new ArrayList<ReoBlock<T>>(stmts_p);
			stmts_p = new ArrayList<ReoBlock<T>>();			
			progs = new ArrayList<Assembly<T>>();
			isProgramValue = true;
			loop = false;
			
			for (ReoBlock<T> s : stmts) {			
				
				ReoBlock<T> s_p = s.evaluate(definitions);
				stmts_p.add(s_p);
				
				if (s_p instanceof Assembly) {
					progs.add((Assembly<T>)s_p);
					Map<String, Expression> progDefs = ((Assembly<T>)s_p).getDefinitions();
					for (Map.Entry<String, Expression> def : progDefs.entrySet()) {
						if (!definitions.containsKey(def.getKey())) {
							definitions.put(def.getKey(), def.getValue());
							if (def.getValue() instanceof BooleanValue) loop = true;
							if (def.getValue() instanceof IntegerValue) loop = true;
							if (def.getValue() instanceof StringValue) loop = true;
							if (def.getValue() instanceof ReoSystemValue) loop = true;
						} else {
//							if (def.getValue().equals(definitions.get(def.getKey())))
//								throw new CompilationException(null, null);
							// TODO If redefined evaluates to false: throw an error.
						}
					}
				} else {
					isProgramValue = false;
				}
			}
			
		}
		
		ReoBlock<T> prog = null;
		
		if (isProgramValue) {
			prog = new Assembly<T>().compose(progs);
		} else {
			prog = new Body<T>(stmts_p);
		}
		
		System.out.println("[info] " + this + " evaluates to ");
		System.out.println("       " + prog + " using " + params.keySet());
		return prog;
	}
	
	@Override
	public String toString() {
		String s = "{";
		Iterator<ReoBlock<T>> stmt = stmts.iterator();
		while (stmt.hasNext()) 
			s += stmt.next() + (stmt.hasNext() ? ", " : "");
		return s + "}";
	}
}
