package nl.cwi.reo.interpret.signatures;

import org.junit.Test;

public class SignatureExpressionTest {

   @Test
   public void evaluate_NoParametersAndExplicitNodes() {
//	   List<Parameter> params = new ArrayList<Parameter>();
//	   List<Port> nodes = new ArrayList<Port>();
//	   Token token = new CommonToken(0);
//	   nodes.add(new Node(new VariableName("a", token), NodeType.SOURCE, new TypeTag("int")));
//	   nodes.add(new Node(new VariableName("b", token), NodeType.SINK, new TypeTag("bool")));
//	   nodes.add(new Node(new VariableName("c", token), NodeType.MIXED, new TypeTag("string")));
//	   SignatureExpression e = new SignatureExpression(params, nodes, token);
//	   
//	   ValueList values = new ValueList();
//	   List<VariableName> newnodes = new ArrayList<VariableName>();
//	   newnodes.add(new VariableName("x", token));
//	   newnodes.add(new VariableName("y", token));
//	   newnodes.add(new VariableName("z", token));
//	   VariableNameList iface = new VariableNameList(newnodes, token);
//	   SignatureConcrete s = null;
//	   boolean hasException = false;
//	   
//	   try {
//		   s = e.evaluate(values, iface);
//		   
//		   assertTrue(s.getDefinitions().isEmpty());
//		   assertEquals(s.get(new Port("a")), new Port("x", PortType.IN, PrioType.NONE, "int", false));
//		   assertEquals(s.get(new Port("b")), new Port("y", PortType.OUT, PrioType.NONE, "bool", false));
//		   assertEquals(s.get(new Port("c")), new Port("z", PortType.NONE, PrioType.NONE, "string", false));
//	   } catch (CompilationException ce) {
//		   hasException = true;
//	   } finally {
//		   assertFalse(hasException);		   
//	   }
   }

   @Test
   public void evaluate_NoParametersAndNodeRanges() {
//	   ParameterList params = new ParameterList();
//	   NodeList nodes = new NodeList();
//	   Token token = new CommonToken(0);
//	   List<List<IntegerExpression>> indices = new ArrayList<List<IntegerExpression>>();
//	   List<IntegerExpression> rng = new ArrayList<IntegerExpression>();
//	   rng.add(new IntegerValue(1));
//	   rng.add(new IntegerVariable(new VariableName("k", token)));
//	   indices.add(rng);
//	   Variable var = new VariableRange("a", indices, token);
//	   nodes.add(new Node(var, NodeType.SOURCE, new TypeTag("int")));
//	   SignatureExpression e = new SignatureExpression(params, nodes, token);
//
//	   ValueList values = new ValueList();
//	   List<VariableName> newnodes = new ArrayList<VariableName>();
//	   newnodes.add(new VariableName("x", token));
//	   newnodes.add(new VariableName("y", token));
//	   newnodes.add(new VariableName("z", token));
//	   VariableNameList iface = new VariableNameList(newnodes, token);
//	   SignatureConcrete s = null;
//	   boolean hasException = false;
//	   
//	   try {
//		   s = e.evaluate(values, iface);
//
//		   assertEquals(s.getDefinitions().get("k"), new IntegerValue(3));
//		   assertNull(s.get(new Port("a[0]")));
//		   assertEquals(s.get(new Port("a[1]")), new Port("x", PortType.IN, PrioType.NONE, "int", false));
//		   assertEquals(s.get(new Port("a[2]")), new Port("y", PortType.IN, PrioType.NONE, "int", false));
//		   assertEquals(s.get(new Port("a[3]")), new Port("z", PortType.IN, PrioType.NONE, "int", false));
//		   assertNull(s.get(new Port("a[4]")));
//	   } catch (CompilationException ce) {
//		   ce.printStackTrace();
//		   hasException = true;
//	   } finally {
//		   assertFalse(hasException);
//	   }	  
   }   
}