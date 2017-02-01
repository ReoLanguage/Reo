package nl.cwi.reo.prautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.misc.MemberSignature;
import nl.cwi.reo.pr.misc.PortOrArray;
import nl.cwi.reo.pr.misc.PortSpec;
import nl.cwi.reo.pr.misc.TypedName;
import nl.cwi.reo.pr.misc.TypedName.Type;
import nl.cwi.reo.pr.targ.java.autom.JavaAutomatonFactory;
import nl.cwi.reo.pr.targ.java.autom.JavaPortFactory;
import nl.cwi.reo.pr.targ.java.autom.JavaPortFactory.JavaPort;
import nl.cwi.reo.pr.targ.java.autom.Member.Primitive;
import nl.cwi.reo.semantics.api.Port;
import nl.cwi.reo.semantics.api.Semantics;
import nl.cwi.reo.semantics.api.SemanticsType;

public class PRAutomaton implements Semantics<PRAutomaton> {
		
	private String name;
	private String variable;
	private Integer value; 
	private List<Port> port;
	private MemberSignature signature;
	

	public PRAutomaton(String name, String variable, Integer value, List<Port> port){
		this.name=name;
		this.value=value;
		this.variable=variable;
		this.port=port;
//		List<Port> inputPorts = new ArrayList<>();
//		List<Port> outputPorts = new ArrayList<>();

		Map<TypedName, Extralogical> extralogicals = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> inputPortsOrArrays = new LinkedHashMap<>();
		Map<TypedName, Integer> integers = new LinkedHashMap<>();
		Map<TypedName, PortOrArray> outputPortsOrArrays = new LinkedHashMap<>();
		
		JavaAutomatonFactory automatonFactory = new JavaAutomatonFactory();
		JavaPortFactory portFactory = (JavaPortFactory) automatonFactory.getPortFactory();
//		mainArgumentFactory = new JavaMainArgumentFactory(
//				((JavaAutomatonFactory) automatonFactory)
//						.getJavaNames());
		 
		PortSpec p = new PortSpec(port.get(0).getName()+"$"+"1");
		JavaPort jp = (JavaPort) portFactory.newOrGet(p);		
		inputPortsOrArrays.put(new TypedName("in",Type.PORT),jp);
		
		p = new PortSpec(port.get(1).getName()+"$"+"1");
		jp = (JavaPort) portFactory.newOrGet(p);		
		outputPortsOrArrays.put(new TypedName("out",Type.PORT),jp);
		
		TypedName typedName = new TypedName(name,Type.FAMILY);
		
		
		signature = new MemberSignature(typedName, integers,extralogicals, inputPortsOrArrays, outputPortsOrArrays, portFactory);
	}
	
//	public class JavaPort extends Port implements JavaVariable {
//		private String variableName;
//
		//
		// CONSTRUCTORS
		//

//		protected JavaPort(int id, PortSpec spec) {
//			new IdObjectFactory(id, spec);
//		}

		//
		// METHODS - PUBLIC
		//

//		@Override
//		public String getVariableName() {
//			if (variableName == null)
//				variableName = javaNames.getFreshName(getName()
//						.replaceAll("\\[", "\\$").replaceAll("\\]", "\\$")
//						.replaceAll("\\$\\$", "\\$"));
//
//			return variableName;
//		}
//	}
	
	public SemanticsType getType() {
		return SemanticsType.PA;
	}
	
	public Primitive getPrimitive(){
		Primitive pr = new Primitive("root_file","class/path");
		pr.setSignature(signature);
		return pr;
		
	}
		
	
/*
	public List<Member.Primitive> setPrimitive(){
		Member m = new Member();
		TypedName name = new TypedName(this.name,Type.FAMILY);
		Map<TypedName, Integer> integers = null;
		Map<TypedName, Extralogical> extralogicals = null;
		Map<TypedName, PortOrArray> inputPortsOrArrays = null;
		Map<TypedName, PortOrArray> outputPortsOrArrays = null;
		
		AutomatonFactory ja = new JavaAutomatonFactory();
		PortFactory portFactory = ja.getPortFactory();
		
		MemberSignature ms = new MemberSignature(name, integers, extralogicals, inputPortsOrArrays,
													outputPortsOrArrays, portFactory);
		m.setSignature(ms);
		
		return getPrimitives();
	}
*/
	
	public String getSource(){
		return port.get(0).toString();
	}
	public String getDest(){
		return port.get(1).toString();
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("main = " + name + "["+ variable + "]("+getSource()+","+getDest()+")");
	 
		return str.toString();
	}
	
	public SortedSet<Port> getInterface() {
		
		return new TreeSet<Port>(port);
	}

	public PRAutomaton getNode(SortedSet<Port> node) {
		
		List<Port> P = new ArrayList<Port>();

		SortedSet<Port> ins = new TreeSet<Port>();
		SortedSet<Port> outs = new TreeSet<Port>();
		for (Port p : node) {
			P.add(p);
			switch (p.getType()) {
			case IN:
				outs.add(p);
				break;
			case OUT: 
				ins.add(p);
				break;
			default:
				break;
			}
		}

		return new PRAutomaton(name,variable,value,P);
	}

	@Override
	public PRAutomaton rename(Map<Port, Port> links) {
		
		List<Port> P = new ArrayList<Port>();
		
		for (Port a : this.port) {
			Port b = links.get(a);
			if (b == null) b = a;
			P.add(b);//P.add(new Port(b.getName()));
		}
		
		return new PRAutomaton(name,variable,value,P);
	}

	@Override
	public PRAutomaton evaluate(Map<String, String> params) {
		String s = params.get(variable);
		Integer newValue=null;
		
		try {
			newValue =Integer.parseInt(s);
		}
		catch(NumberFormatException e){
			
		}
		
		return new PRAutomaton(name,variable,newValue,port);
	}

	@Override
	public PRAutomaton compose(List<PRAutomaton> automata) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PRAutomaton restrict(Collection<? extends Port> intface) {
		// TODO Auto-generated method stub
		return null;
	}

}
