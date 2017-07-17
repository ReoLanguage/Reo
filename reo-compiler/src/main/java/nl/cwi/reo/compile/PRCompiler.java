package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;

// TODO: Auto-generated Javadoc
/**
 * The Class PRCompiler.
 */
public class PRCompiler {

	/**
	 * Generates FOCAML code from a Reo program.
	 * 
	 * @param program
	 *            Reo program
	 * @return FOCAML code equivalent to the Reo program
	 */
	public static String toPR(ReoProgram<PRAutomaton> program) {

		ReoConnector<PRAutomaton> rc = program.getConnector().flatten().insertNodes(true, true, new PRAutomaton())
				.integrate();

		Map<String, Object> c = new HashMap<String, Object>();
		c.put("name", program.getName());

		int i = 0;
		List<String> defs = new ArrayList<String>();
		List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> workers = new ArrayList<Map<String, Object>>();

		for (ReoConnectorAtom<PRAutomaton> atom : rc.getAtoms()) {

			List<String> inputs = new ArrayList<String>();
			List<String> outputs = new ArrayList<String>();
			for (Port p : atom.getSemantics().getInterface())
				if (p.isInput())
					inputs.add(p.getName());
				else
					outputs.add(p.getName());

			if (atom.getSemantics().getName().equals("identity")) {
				Map<String, Object> worker = new HashMap<String, Object>();
				defs.add("WORKER" + ++i + " = " + atom.getSourceCode().getCall().toString().replace("\"", ""));
				worker.put("ref", "WORKER" + i);
				worker.put("inputs", inputs);
				worker.put("outputs", outputs);
				workers.add(worker);
				continue;
			}

			Map<String, Object> I = new HashMap<String, Object>();
			I.put("name", atom.getSemantics().getName());
			I.put("inputs", inputs);
			I.put("outputs", outputs);

			Value pv = atom.getSemantics().getVariable();
			String param = null;
			if (pv != null)
				param = pv instanceof StringValue ? "\"" + pv.toString() + "\"" : pv.toString();
			if (param != null)
				I.put("params", Arrays.asList(param));

			instances.add(I);
		}

		c.put("defs", defs);
		c.put("instances", instances);
		c.put("workers", workers);

		STGroup group = new STGroupFile("PR.stg");
		ST temp = group.getInstanceOf("main");
		temp.add("c", c);

		return temp.render();
	}

}
