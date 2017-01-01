package nl.cwi.reo.interpret;

public final class ProgramInstance implements ProgramExpression {

	public final ComponentExpression cexpr;

	public final ExpressionList plist;
	
	private final VariableList iface;

	public ProgramInstance(ComponentExpression cexpr, ExpressionList plist, 
			VariableList iface) {
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}
	
	@Override
	public ProgramExpression evaluate(DefinitionList params) throws Exception {
		
		ExpressionList parameters_p = plist.evaluate(params); 
		VariableList intface_p = iface.evaluate(params); 
		
		ComponentExpression compexpr_p = cexpr.instantiate(parameters_p, intface_p); 
		
		if (compexpr_p instanceof ComponentValue)
			return new ProgramValue(((ComponentValue)compexpr_p).getInstance(), 
					new DefinitionList());
		
		return new ProgramInstance(cexpr, parameters_p, intface_p);
	}
	
}