package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValueExpression implements Evaluable<Value> {
	
	/**
	 * Type.
	 */
	private final ValueType type;

	/**
	 * String value.
	 */
	private final Variables refc;

	/**
	 * String value.
	 */
	private final String strg;
	
	/**
	 * Integer value
	 */
	private final IntegerExpression iexp;
	
	/**
	 * Boolean value
	 */
	private final BooleanExpression bexp;
	
	/**
	 * Component value
	 */
	private final ComponentExpression comp;
	
	/**
	 * List value
	 */
	private final ListValue list;
	
	/**
	 * Constructs a reference value.
	 * @param refc		reference value.
	 */
	public ValueExpression(Variables refc) {
		this.type = ValueType.VARIABLE;
		this.refc = refc;
		this.strg = null;
		this.iexp = null;
		this.bexp = null;
		this.comp = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public ValueExpression(String strg) {
		this.type = ValueType.STRING;
		this.refc = null;
		this.strg = strg;
		this.iexp = null;
		this.bexp = null;
		this.comp = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public ValueExpression(IntegerExpression iexp) {
		this.type = ValueType.INTEGER;
		this.refc = null;
		this.strg = null;
		this.iexp = iexp;
		this.bexp = null;
		this.comp = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public ValueExpression(BooleanExpression bexp) {
		this.type = ValueType.BOOLEAN;
		this.refc = null;
		this.strg = null;
		this.iexp = null;
		this.bexp = bexp;
		this.comp = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public ValueExpression(ComponentExpression comp) {
		this.type = ValueType.COMPONENT;
		this.refc = null;
		this.strg = null;
		this.iexp = null;
		this.bexp = null;
		this.comp = comp;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public ValueExpression(ListValue list) {
		this.type = ValueType.LIST;
		this.refc = null;
		this.strg = null;
		this.iexp = null;
		this.bexp = null;
		this.comp = null;
		this.list = list;
	}
		
	@Override
	public Value evaluate(Map<String, Value> p) throws Exception {
		switch (this.type) {
		case VARIABLE:
			Map<String, String> vars = refc.evaluate(p); 
			List<Value> values = new ArrayList<Value>();
			for (Map.Entry<String, String> entry : vars.entrySet())
				values.add(p.get(entry.getKey()));
			return new Value(values);
		case STRING:
			return new Value(strg);
		case INTEGER:
			return new Value(iexp.evaluate(p));
		case BOOLEAN:
			return new Value(bexp.evaluate(p));
		case COMPONENT:
			return new Value(comp.evaluate(p));
		case LIST:
			return new Value(list.evaluate(p));
		default:
			return null;				
		}
	}

}
