package nl.cwi.reo.interpret.programs;

import java.util.Map;

import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;

public final class ProgramInstance<T extends Semantics<T>> implements ProgramExpression<T> {

	public final ComponentExpression<T> cexpr;

	public final Range plist;
	
	private final Range iface;

	public ProgramInstance(ComponentExpression<T> cexpr, Range plist, Range iface) {
		if (cexpr == null || plist == null || iface == null)
			throw new NullPointerException();		
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}
	
	@Override
	public ProgramExpression<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		ComponentExpression<T> cexpr_p = cexpr.evaluate(params);
		Range plist_p = plist.evaluate(params); 
		Range iface_p = iface.evaluate(params); 
		if (plist_p instanceof ExpressionList && iface_p instanceof VariableNameList) {
			ExpressionList values = (ExpressionList)plist_p;
			VariableNameList nodes = (VariableNameList)iface_p;
			ProgramExpression<T> e = cexpr_p.instantiate(values, nodes);
			if (e != null) 
				return e.evaluate(params);
		}
		return new ProgramInstance<T>(cexpr_p, plist_p, iface_p);
	}
	
	@Override
	public String toString() {
		return "" + cexpr + plist + iface;
	}
}