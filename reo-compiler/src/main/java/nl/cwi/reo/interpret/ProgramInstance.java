package nl.cwi.reo.interpret;

public final class ProgramInstance implements Program {

	public final ComponentExpression cexpr;

	public final ExpressionList plist;
	
	private final Interface iface;

	public ProgramInstance(ComponentExpression cexpr, ExpressionList plist, 
			Interface iface) {
		if (cexpr == null || plist == null || iface == null)
			throw new IllegalArgumentException("Arguments cannot be null.");		
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}
	
	@Override
	public Program evaluate(DefinitionList params) throws Exception {
		ComponentExpression cexpr_p = cexpr.evaluate(params);
		ExpressionList plist_p = plist.evaluate(params); 
		Interface iface_p = iface.evaluate(params); 
		ComponentExpression cexpr_p_eval = cexpr_p.instantiate(plist_p, iface_p); 
		if (cexpr_p_eval instanceof ComponentValue) {
			Instance inst = ((ComponentValue)cexpr_p_eval).getInstance();
			return new ProgramValue(inst, new DefinitionList());
		}
		return new ProgramInstance(cexpr_p, plist_p, iface_p);
	}
	
}