package nl.cwi.reo.interpret;

import java.util.List;

public class Value {
	
	/**
	 * Type.
	 */
	private final ValueType type;

	/**
	 * String value.
	 */
	private final String vnam;

	/**
	 * String value.
	 */
	private final String vtyp;
	
	/**
	 * String value.
	 */
	private final String strg;
	
	/**
	 * Integer value
	 */
	private final Integer intg;
	
	/**
	 * Boolean value
	 */
	private final Boolean bool;
	
	/**
	 * Component value
	 */
	private final Component prog;
	
	/**
	 * List value
	 */
	private final List<Value> list;
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public Value(String vnam, String vtyp) {
		this.type = ValueType.VARIABLE;
		this.vnam = vnam;
		this.vtyp = vtyp;
		this.strg = null;
		this.intg = null;
		this.bool = null;
		this.prog = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public Value(String strg) {
		this.type = ValueType.STRING;
		this.vnam = null;
		this.vtyp = null;
		this.strg = strg;
		this.intg = null;
		this.bool = null;
		this.prog = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public Value(Integer intg) {
		this.type = ValueType.INTEGER;
		this.vnam = null;
		this.vtyp = null;
		this.strg = null;
		this.intg = intg;
		this.bool = null;
		this.prog = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public Value(Boolean bool) {
		this.type = ValueType.BOOLEAN;
		this.vnam = null;
		this.vtyp = null;
		this.strg = null;
		this.intg = null;
		this.bool = bool;
		this.prog = null;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public Value(Component prog) {
		this.type = ValueType.COMPONENT;
		this.vnam = null;
		this.vtyp = null;
		this.strg = null;
		this.intg = null;
		this.bool = null;
		this.prog = prog;
		this.list = null;
	}
	
	/**
	 * Constructs a string value.
	 * @param string	string value.
	 */
	public Value(List<Value> list) {
		this.type = ValueType.LIST;
		this.vnam = null;
		this.vtyp = null;
		this.strg = null;
		this.intg = null;
		this.bool = null;
		this.prog = null;
		this.list = list;
	}
	
	/**
	 * Gets the value type.
	 * @return value type.
	 */
	public ValueType getType() {
		return this.type;
	}
	
	/**
	 * Gets the variable name.
	 * @return variable name.
	 */
	public String getVariableName() {
		return this.vnam;
	}
	
	/**
	 * Gets the variable type.
	 * @return variable type.
	 */
	public String getVariableType() {
		return this.vtyp;
	}
	
	/**
	 * Gets the string value.
	 * @return string value.
	 */
	public String getString() {
		return this.strg;
	}

	/**
	 * Gets the integer value.
	 * @return integer value.
	 */
	public Integer getInteger() {
		return this.intg;
	}

	/**
	 * Gets the boolean value.
	 * @return boolean value.
	 */
	public Boolean getBoolean() {
		return this.bool;
	}

	/**
	 * Gets the component value.
	 * @return component value.
	 */
	public Component getComponent() {
		return this.prog;
	}

	/**
	 * Gets the list value.
	 * @return list value.
	 */
	public List<Value> getList() {
		return this.list;
	}


}
