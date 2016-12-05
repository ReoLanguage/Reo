package nl.cwi.reo.Treoparse;

import java.util.HashMap;
import java.util.Map;

/**
 * An integer linear combination of parameters and a constant term, such as, for example, <code>2k-6n+3</code>.
 * This class allows manipulation and evaluation of such expressions.
 */
public final class Expression {

	/**
	 * Coefficients of each parameter (and 1) in linear combination. For example, 
	 * <code>{"k"=2,"n"=-6,"1"=3}</code> represents <code>2k-6n+3</code>.
	 * Since parameters names cannot start with an integer,
	 * the auxiliary variable "1" for the constant term cannot be confused 
	 * with a parameter name.
	 */
	private final Map<String, Integer> expr;
	
	/**
	 * Constructor.
	 * @param expr		coefficients of each parameter (and 1) in linear combination.
	 */
	public Expression(Map<String, Integer> expr) {
		 this.expr = expr;
	}
	
	/**
	 * Constructor of a parameter.
	 * @param parameter		name of the parameter.
	 */
	public Expression(String parameter) {
		this.expr = new HashMap<String, Integer>();
		this.expr.put(parameter, 1);
	}
	
	/**
	 * Constructor of integer value.
	 * @param integer		integer value.
	 */
	public Expression(int integer) {
		this.expr = new HashMap<String, Integer>();
		this.expr.put("1", integer);
	}
	
	/**
	 * Adds two expressions together.
	 * @param e1		first expression
	 * @param e2		second expression
	 * @return sum e1 + e2
	 */
	public static Expression add(Expression e1, Expression e2) {
		Map<String, Integer> expr = new HashMap<String, Integer>(e1.expr);
		for (Map.Entry<String, Integer> entry : e2.expr.entrySet()) {
			if (e1.expr.containsKey(entry.getKey()))
				expr.put(entry.getKey(), e1.expr.get(entry.getKey()) + entry.getValue());
			else
				expr.put(entry.getKey(), entry.getValue());				
		}
		return new Expression(expr);
	}
	
	/**
	 * Multiplies this expression by an integer constant.
	 * @param c		integer constant
	 * @return scalar multiple c * e.
	 */
	public static Expression multiply(Expression e, int c) {
		Map<String, Integer> expr = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : e.expr.entrySet())
			expr.put(entry.getKey(), c * entry.getValue());
		return new Expression(expr);
	}
	
	/**
	 * Evaluates, if possible, this expression to an integer.
	 * @param parameters 		parameter assignment of integer values represented as a {@link java.util.String}.
	 * @return integer evaluation of the expression.
	 * @throws Exception if not all parameters are assigned
	 */
	public int eval(Map<String, String> parameters) throws Exception {
		int e = 0;
		for (Map.Entry<String, Integer> term : expr.entrySet()) {
			if (term.getKey().equals("1")) {
				e += term.getValue();
			} else {
				String parameter = term.getKey();
				String value = parameters.get(parameter);
				
				if (value == null) 
					throw new Exception("Undefined parameter: " + parameter + ".");
				
				e += term.getValue() * Integer.parseInt(value);
			}
		}		
		return e;
	}
	
	/**
	 * Returns the string representation of this expression.
	 */
	@Override
	public String toString() {
		String s = "";
		for (Map.Entry<String, Integer> term : expr.entrySet()) {
			if (term.getKey().equals("1") && term.getValue() < 0)
				s += term.getValue().toString();
			else if (term.getKey().equals("1") && term.getValue() > 0)
				s += (s.equals("") ? "" : "+") + term.getValue().toString();
			else if (term.getValue() < -1) 
				s += term.getValue() + term.getKey();
			else if (term.getValue() == -1) 
				s += "-" + term.getKey();		
			else if (term.getValue() == 1) 
				s += (s.equals("") ? "" : "+") + term.getKey();			
			else if (term.getValue() > 1) 
				s += (s.equals("") ? "" : "+") + term.getValue() + term.getKey();
		}
		return s.equals("") ? "0" : s;
	}
}
