package nl.cwi.reo.interpret;

import java.util.Map;

public final class StatementInstance implements Statement {

	public final Component cexpr;

	public final ExpressionList plist;
	
	private final Interface iface;

	public StatementInstance(Component cexpr, ExpressionList plist, 
			Interface iface) {
		if (cexpr == null || plist == null || iface == null)
			throw new IllegalArgumentException("Arguments cannot be null.");		
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}
	
	@Override
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Component cexpr_p = cexpr.evaluate(params);
		ExpressionList plist_p = plist.evaluate(params); 
		Interface iface_p = iface.evaluate(params); 
		Component cexpr_p_eval = cexpr_p.instantiate(plist_p, iface_p); 
		if (cexpr_p_eval instanceof ZComponentValue) {
			InstanceList inst = ((ZComponentValue)cexpr_p_eval).getInstanceList();
			return new Program(inst, new HashMap<Variable>());
		}
		return new StatementInstance(cexpr_p, plist_p, iface_p);
	}
	
}