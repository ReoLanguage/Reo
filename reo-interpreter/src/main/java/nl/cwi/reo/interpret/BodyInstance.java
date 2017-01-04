package nl.cwi.reo.interpret;

import java.util.Map;

public final class BodyInstance implements BodyExpression {

	public final ComponentExpression cexpr;

	public final ExpressionList plist;
	
	private final Interface iface;

	public BodyInstance(ComponentExpression cexpr, ExpressionList plist, 
			Interface iface) {
		if (cexpr == null || plist == null || iface == null)
			throw new IllegalArgumentException("Arguments cannot be null.");		
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}
	
	@Override
	public BodyExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		ComponentExpression cexpr_p = cexpr.evaluate(params);
		ExpressionList plist_p = plist.evaluate(params); 
		Interface iface_p = iface.evaluate(params); 
		ComponentExpression cexpr_p_eval = cexpr_p.instantiate(plist_p, iface_p); 
		if (cexpr_p_eval instanceof ComponentValue) {
			InstanceList inst = ((ComponentValue)cexpr_p_eval).getInstanceList();
			return new BodyValue(inst, new HashMap<Variable>());
		}
		return new BodyInstance(cexpr_p, plist_p, iface_p);
	}
	
}