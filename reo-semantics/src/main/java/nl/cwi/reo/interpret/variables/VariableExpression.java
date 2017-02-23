package nl.cwi.reo.interpret.variables;

import java.util.Collections;
import java.util.List;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.TermList;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a variable expression.
 * @param <I> type of identifier
 */
public class VariableExpression implements Expression<Variable> {
	
	/**
	 * Fully qualified name.
	 */
	protected final String name;
	
	/**
	 * Indices of this variable.
	 */
	protected final List<TermsExpression> indices;
	
	/**
	 * Location of this variable in Reo source file.
	 */
	protected final Location location;
	
	/**
	 * Constructs a variable list.
	 * @param name		name of the node
	 */
	public VariableExpression(String name, List<TermsExpression> indices, Location location) {
		if (name == null || indices == null)
			throw new NullPointerException();
		this.name = name;
		this.indices = Collections.unmodifiableList(indices);	
		this.location = location;
	}
	
	/**
	 * Gets the location of this variable expression in the Reo source file.
	 * @return location of this variable.
	 */
	public Location getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}
	
	public List<TermsExpression> getIndices() {
		return indices;
	}
	
	
	/**
	 * Finds values of all parameters in the indices, given that the size
	 * of the evaluated variable expression equals a given size.
	 * @param size		actual length of evaluated variable expression.
	 * @return a map that assigns each parameter name to an integer value, 
	 * if these values can be found, and null otherwise.
	 */
	public Scope findParamFromSize(int size) {		
//		Scope params = new Scope();
//		for (List<Term> bounds : indices) {
//			if (bounds.size() == 1) {
//				if (!(bounds.get(0) instanceof Value && ((Value)bounds.get(0)).getValue() instanceof Integer)) 
//					return null; 
//			} else if (bounds.size() == 2) {
//				if (bounds.get(0) instanceof Value && ((Value)bounds.get(0)).getValue() instanceof Integer) {
//					int lower = (Integer)((Value)bounds.get(0)).getValue();
//					
//					/*
//					 * TODO change IntegerVariable to Variable, and new IntegerValue(...) to new Datum(new Integer(...))
//					 */
//					
//					if (bounds.get(1) instanceof VariableName) {
//						VariableName k = (VariableName)bounds.get(1);					
//						if (k.getVariable() instanceof VariableName) {
//							VariableName name = (VariableName)k.getVariable();
//							params.put(name.getName(), new IntegerValue(lower + size - 1));
//						} else {
//							return null;
//						}
//					} else {
//						return null;
//					}
//				} else if (bounds.get(1) instanceof IntegerValue) {
//					int upper = ((IntegerValue)bounds.get(1)).toInteger();
//					if (bounds.get(0) instanceof IntegerVariable) {
//						IntegerVariable k = (IntegerVariable)bounds.get(1);						
//						if (k.getVariable() instanceof VariableName) {
//							VariableName name = (VariableName)k.getVariable();
//							params.put(name.getName(), new IntegerValue(upper - size + 1));
//						}
//					} else {
//						return null;
//					}
//				} else {
//					return null;
//				}
//			}
//		}
//		return params;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variable evaluate(Scope s, Monitor m) {
		
		return null;
	}
//		boolean boundsAreKnown = true;
//		List<List<Term>> indices_p = new ArrayList<List<Term>>();
//
//		for (List<Term> bounds : this.indices) {
//			List<Term> bounds_p = new ArrayList<Term>();
//			for (Term e : bounds) {
//				Term e_p = e.evaluate(params);
//				if (!(e_p instanceof Value && ((Value)e_p).getValue() instanceof Integer)) 
//					boundsAreKnown = false;
//				bounds_p.add(e_p);
//			}		
//			indices_p.add(bounds_p);
//		}
//		
//		if (boundsAreKnown) {
//			List<String> variables = new ArrayList<String>();
//			List<String> tempvars;
//			variables.add(name);
//			
//			for (List<Term> bounds_p : indices_p) {			
//				if (bounds_p.size() == 1) {
//					int i = (Integer)((Value)bounds_p.get(0)).getValue();
//					tempvars = new ArrayList<String>();
//					for (String var : variables)
//						tempvars.add(var + "[" + i + "]");
//					variables = tempvars;			
//				} else if (bounds_p.size() == 2) {
//					int a = (Integer)((Value)bounds_p.get(0)).getValue();
//					int b = (Integer)((Value)bounds_p.get(1)).getValue();
//					tempvars = new ArrayList<String>();
//					for (int i = a; i <= b; ++i)	
//						for (String var : variables)
//							tempvars.add(var + "[" + i + "]");
//					variables = tempvars;
//				}			
//			}
//			
//			List<VariableName> vars = new ArrayList<VariableName>();
//			for (String x : variables) 
//				vars.add(new VariableName(x, token));
//
//			if (isList)
//				return new VariableList(vars, token);
//			else
//				return vars.get(0);
//			
//		}
//		
//		List<List<Term>> indices_t = new ArrayList<List<Term>>();
//		
//		return new Variable(this.name, indices_t, token);
//		return null;
//	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = name;	
		for (TermsExpression bounds : this.indices) {			
			if (bounds instanceof TermList){
				TermList t = (TermList) bounds;
				if(t.getList().size() == 1) {
					s += "[" + t.getList().get(0) + "]";
				} else if (t.getList().size() == 2) {
	//				s += "[" + t.getList().get(0)  + ".." + bounds.get(1) + "]";
				}			
		}
		}	
		return s;
	}


}
