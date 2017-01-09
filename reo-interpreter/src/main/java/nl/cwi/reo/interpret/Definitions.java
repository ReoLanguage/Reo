package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

public class Definitions extends HashMap<VariableName, Expression> implements Evaluable<Definitions> {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -6117381497904646504L;
	
	public Definitions() {}
	
	public Definitions(Map<? extends VariableName,? extends Expression> definitions) {
		super.putAll(definitions);
	}

	@Override
	public Definitions evaluate(Map<VariableName, Expression> params) throws Exception {
		Map<VariableName, Expression> defns_p = new HashMap<VariableName, Expression>();
		for (Map.Entry<VariableName, Expression> def : super.entrySet()) 
			defns_p.put(def.getKey(), def.getValue().evaluate(params));
		return new Definitions(defns_p);
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
	}
}
