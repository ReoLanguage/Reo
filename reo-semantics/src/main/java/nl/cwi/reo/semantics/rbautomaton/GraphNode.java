package nl.cwi.reo.semantics.rbautomaton;

import java.util.List;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.ports.Port;

@Deprecated
public class GraphNode {

	private List<GraphNode> childs;
	private List<GraphNode> parents;

	private Rule rule;
	/**
	 * True if the node has already been visited
	 */
	private boolean visited;

	public GraphNode(Rule r, List<GraphNode> childs, List<GraphNode> parents) {
		this.rule = r;
		this.childs = childs;
		this.parents = parents;
		visited = false;
	}

	public List<GraphNode> getChilds() {
		return childs;
	}

	public void addChilds(List<GraphNode> n) {
		childs.addAll(n);
	}

	public List<GraphNode> getParents() {
		return parents;
	}

	public void addParents(List<GraphNode> n) {
		parents.addAll(n);
	}

	public Rule getRule() {
		return rule;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setFlag(boolean flag) {
		this.visited = flag;
	}

	/**
	 * Return true if the current node has an edge with the other node
	 * 
	 * @param n
	 * @return
	 */
	public boolean hasEdge(GraphNode n) {
		Rule x = n.getRule();
		boolean hasEdge = false;
		for (Port p : rule.getSync().keySet()) {
			if (rule.getSync().get(p)) {
				if (x.getSync().get(p) == true) {
					hasEdge = true;
				} else { 
					return false;
				}
			}
		}
		return hasEdge;
	}
	
	@Override
	public String toString() {
		ST st = new ST("<rule> ( \n<childs:{c | \t<c.rule>}; separator=\"\n\">\n) (\n<parents:{c | \t<c.rule>}; separator=\"\n\">\n)");
		st.add("rule", rule);
		st.add("childs", childs);
		st.add("parents", parents);
		return st.render();
	}
}
