package nl.cwi.reo.interpret;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Signature implements Evaluable<SortedMap<String,String>> {

	private List<Variables> intface;
	
	public Signature(List<Variables> intface) {
		this.intface = intface;
	}
	
	public List<Variables> getInterface() {
		return this.intface;
	}

	@Override
	public SortedMap<String, String> evaluate(Map<String, Value> p)
			throws Exception {
		SortedMap<String,String> P = new TreeMap<String,String>();
		for (Variables var : this.intface)
			P.putAll(var.evaluate(p));
		return P;
	}
}
