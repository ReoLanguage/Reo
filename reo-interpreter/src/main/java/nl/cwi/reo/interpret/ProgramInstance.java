package nl.cwi.reo.interpret;

import java.util.Map;

public final class ProgramInstance implements ProgramExpression {

	public final ComponentExpression cexpr;

	public final ExpressionList plist;
	
	private final Interface iface;

	public ProgramInstance(ComponentExpression cexpr, ExpressionList plist, 
			Interface iface) {
		if (cexpr == null || plist == null || iface == null)
			throw new NullPointerException();		
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}
	
	@Override
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		ComponentExpression cexpr_p = cexpr.evaluate(params);
		ExpressionList plist_p = plist.evaluate(params); 
		Interface iface_p = iface.evaluate(params); 
		if (cexpr_p instanceof ComponentValue) {
			ProgramExpression prog = ((ComponentValue)cexpr_p).instantiate(plist_p, iface_p);
			System.out.println("Component " + String.format("%1$15s", cexpr) + String.format("%1$25s", " evaluated in " + plist_p + iface_p) + " gives " + prog);
			return prog; 
		}
		return new ProgramInstance(cexpr_p, plist_p, iface_p);
	}
	
	@Override
	public String toString() {
		return "" + cexpr + plist + iface;
	}
}