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
	private final Signature sign;
	
	/**
	 * Component instance.
	 */
	private final Instance inst;
	
	public ComponentValue() {
		this.sign = new Signature();
		this.inst = new Instance();
	}
	
	/**
	 * Constructor.
	 * @param sign			signature expression
	 * @param inst			component instance
	 */
	public ComponentValue(Signature sign, Semantics<?> atom) {
		if (sign == null || atom == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = sign;
		
		Map<String, Port> links = new HashMap<String, Port>();
		for (String a : atom.getInterface()) 
			links.put(a, new Port(a));
		
		this.inst = new Instance(atom, links);
	}
	
	/**
	 * Constructor.
	 * @param sign			signature expression
	 * @param inst			component instance
	 */
	public ComponentValue(Signature sign, Instance inst) {
		this.inst = inst;
		this.sign = sign;
	}
	
	public Instance getInstance() {
		return inst;
	}

	@Override
	public ComponentExpression evaluate(DefinitionList params) throws Exception {
		return new ComponentValue(sign, inst.evaluate(params));
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values, 
			Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		Instance inst_p = inst.evaluate(v.getDefinitions()).restrictAndRename(v.getLinks());
		return new ComponentValue(sign, inst_p);	
	}	
}
