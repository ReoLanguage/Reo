package nl.cwi.reo.interpret.programs;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Array;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.arrays.ExpressionRange;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.signatures.Interface;
import nl.cwi.reo.interpret.variables.VariableName;

public final class ProgramInstance implements ProgramExpression {

	public final ComponentExpression cexpr;

	public final Array plist;
	
	private final Interface iface;

	public ProgramInstance(ComponentExpression cexpr, Array plist, 
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
		Array plist_p = plist.evaluate(params); 
		Interface iface_p = iface.evaluate(params); 
		ProgramExpression prog = new ProgramInstance(cexpr_p, plist_p, iface_p);
		if (plist_p instanceof ExpressionRange) {
			ExpressionRange values = (ExpressionRange)plist_p;
			if (cexpr_p instanceof ComponentExpression) {
				ProgramExpression _prog = ((ComponentExpression)cexpr_p).instantiate(values, iface_p);
				if (_prog != null) prog = _prog;
			}
		}
//		System.out.println("[info] " + cexpr + plist + iface + " becomes " + prog + " using " + params);
		return prog;
	}
	
	@Override
	public String toString() {
		return "" + cexpr + plist + iface;
	}
}