package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

/**
 * An atomic component with a signature.
 */
public class ComponentValue implements ComponentExpression {

	/**
	 * Signature expression.
	 */
	private final SignatureExpression sign;
	
	/**
	 * Component instance.
	 */
	private final InstanceValue inst;
	
	/**
	 * Constructor.
	 * @param sign			signature expression
	 * @param inst			component instance
	 */
	public ComponentValue(SignatureExpression sign, Semantics<?> atom) {
		this.sign = sign;
		
		Map<String, NodeName> links = new HashMap<String, NodeName>();
		for (String a :  atom.getInterface()) 
			links.put(a, new NodeName(a, new TypeTag(""), NodeType.FREE, false));
		
		this.inst = new InstanceValue(atom, links);
	}
	
	/**
	 * Constructor.
	 * @param sign			signature expression
	 * @param inst			component instance
	 */
	public ComponentValue(SignatureExpression sign, InstanceValue inst) {
		this.inst = inst;
		this.sign = sign;
	}
	
	public InstanceValue getInstance() {
		return inst;
	}

	@Override
	public ComponentExpression evaluate(DefinitionList params) throws Exception {
		return this;
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values, 
			VariableList nodes) throws Exception {
		SignatureValue v = sign.evaluate(values, nodes);
		return new ComponentValue(sign, inst.evaluate(v.getParameters()));	
	}	
}
