package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

public class Signature implements ParameterType {
	
	private ParameterList params;
	
	private ParameterList intface;
	
	public Signature(ParameterList params, ParameterList intface) {
		this.params = params;
		this.intface = intface;
	}
	
	public ParameterList getParameters() {
		return this.params;
	}
	
	public List<String> getParameterNames() {
		List<String> names = new ArrayList<String>();
		for (Parameter p : params.params) 
			names.add(p.getName());
		return names;
	}
	
	public ParameterList getNodes() {
		return this.intface;
	}
	
	public List<String> getNodeNames() {
		List<String> names = new ArrayList<String>();
		for (Parameter p : intface.params) 
			names.add(p.getName());
		return names;
	}

}
