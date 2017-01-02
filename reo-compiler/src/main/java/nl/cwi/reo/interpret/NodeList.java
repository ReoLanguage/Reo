package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

public class NodeList implements Evaluable<NodeList> {
	
	private final List<Node> list;
	
	public NodeList(List<Node> list) {
		this.list = list;
	}

	public List<Node> getList() {
		return list;
	}

	@Override
	public NodeList evaluate(DefinitionList params) throws Exception {
		List<Node> list_p = new ArrayList<Node>();
		for (Node x : list) {
			Node x_p = x.evaluate(params);
			List<Node> x_p_list = x_p.getList();
			if (x_p_list != null) {
				list_p.addAll(x_p_list);
			} else {
				list_p.add(x_p);
			}
		}
		return new NodeList(list_p);
	}
}
