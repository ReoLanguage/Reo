package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

/**
 * An atomic component with a signature.
 */
public class ZComponentValue implements Component {

	/**
	 * Signature expression.
	 */
	private final Signature sign;
	
	/**
	 * Component instance.
	 */
	private final InstanceList inst;
	
	public ZComponentValue() {
		this.sign = new Signature();
		this.inst = new InstanceList();
	}
	
	/**
	 * Constructor.
	 * @param sign			signature expression
	 * @param inst			component instance
	 */
	public ZComponentValue(Signature sign, Semantics<?> atom) {
		if (sign == null || atom == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = sign;
		
		Map<String, Port> links = new HashMap<String, Port>();
		for (String a : atom.getInterface()) 
			links.put(a, new Port(a));
		
		this.inst = new InstanceList(new Instance(atom, links));
	}
	
	/**
	 * Constructor.
	 * @param sign			signature expression
	 * @param inst			component instance
	 */
	public ZComponentValue(Signature sign, InstanceList inst) {
		this.inst = inst;
		this.sign = sign;
	}
	
	public InstanceList getInstanceList() {
		return inst;
	}

	@Override
	public Component evaluate(Map<VariableName, Expression> params) throws Exception {
		return new ZComponentValue(sign, inst.evaluate(params));
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
	}

	@Override
	public Component instantiate(ExpressionList values, 
			Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		InstanceList inst_p = inst.evaluate(v.getDefinitions()).restrictAndRename(v.getLinks());
		return new ZComponentValue(sign, inst_p);	
	}
	
	@Override
	public String toString() {
		return sign + "{" + inst + "}";
	}
}
