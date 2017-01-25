package nl.cwi.reo.interpret.blocks;

import java.util.Map;

import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.systems.ReoSystem;
import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.api.Semantics;

public final class InstanceReference<T extends Semantics<T>> implements ReoBlock<T> {

	public final ReoSystem<T> cexpr;

	public final Range plist;
	
	private final Range iface;

	public InstanceReference(ReoSystem<T> cexpr, Range plist, Range iface) {
		if (cexpr == null || plist == null || iface == null)
			throw new NullPointerException();		
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}
	
	@Override
	public ReoBlock<T> evaluate(Map<String, Expression> params) throws CompilationException {
		ReoSystem<T> cexpr_p = cexpr.evaluate(params);
		Range plist_p = plist.evaluate(params); 
		Range iface_p = iface.evaluate(params); 
		if (plist_p instanceof ExpressionList && iface_p instanceof VariableNameList) {
			ExpressionList values = (ExpressionList)plist_p;
			VariableNameList nodes = (VariableNameList)iface_p;
			ReoBlock<T> e = cexpr_p.instantiate(values, nodes);
			if (e != null) 
				return e.evaluate(params);
		}
		return new InstanceReference<T>(cexpr_p, plist_p, iface_p);
	}
	
	@Override
	public String toString() {
		return "" + cexpr + plist + iface;
	}
}