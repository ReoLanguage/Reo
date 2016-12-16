package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignatureExpression implements Expression<Signature> {

	private ParameterListExpression params;
	
	private ParameterListExpression intface;
	
	public SignatureExpression(ParameterListExpression params, 
			ParameterListExpression intface) {
		this.params = params;
		this.intface = intface;
	}

	@Override
	public Signature evaluate(Map<String, Value> p) throws Exception {
		return new Signature(params.evaluate(p), intface.evaluate(p));
	}

	@Override
	public List<String> variables() {
		List<String> vars = new ArrayList<String>(params.variables());
		vars.addAll(intface.variables());
		return vars;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof SignatureExpression)) return false;
	    SignatureExpression t = (SignatureExpression)other;
	   	return Objects.equals(this.params, t.params) && Objects.equals(this.intface, t.intface);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.params, this.intface);
	}
}
