package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Formula;

@Deprecated
public class Graph {

	private List<GraphNode> n;

	public Graph(Set<Rule> l) {
		List<GraphNode> list = new ArrayList<GraphNode>();
		for (Rule r : l) {
			list.add(new GraphNode(r, new ArrayList<GraphNode>(), new ArrayList<GraphNode>()));
		}
		int i = 0;
		for (GraphNode k : list) {
			i++;
			int j = 0;
			for (GraphNode r : list) {
				j++;
				if (i != j && k.hasEdge(r)) {
					k.addChilds(Arrays.asList(r));
					r.addParents(Arrays.asList(k));
				}
			}
		}
		this.n = list;
	}

	public Graph(List<GraphNode> n) {
		this.n = n;
	}

	public List<GraphNode> getNodes() {
		return n;
	}

	public void resetFlags() {
		for (GraphNode node : n) {
			node.setFlag(false);
		}
	}

	/**
	 * Compute new rules at each nodes
	 * 
	 * @return
	 */
	public Graph isolate() {
		List<GraphNode> newNodes = new ArrayList<GraphNode>();
		
		Set<Set<Rule>> newRuleSet = new HashSet<>();
		for (GraphNode node : n) {
			Set<Rule> newRule = new HashSet<>();
			
			Queue<GraphNode> queue = new LinkedList<GraphNode>(node.getChilds());
			GraphNode child = queue.poll();
			newRule.add(child.getRule());
			
			while (!queue.isEmpty() || (child != null && !child.isVisited())) {
				child.setFlag(true);
				for (GraphNode n : child.getChilds())
					if (!n.isVisited())
						queue.add(n);
				newRule.add(child.getRule());
				child = queue.poll();
			}
			
			newRuleSet.add(newRule);
			resetFlags();
		}
		
		for (Set<Rule> newRule : newRuleSet)
			newNodes.add(new GraphNode(merge(new ArrayList<>(newRule)), new ArrayList<>(), new ArrayList<>()));
		
//		for (GraphNode node : n) {
//			List<Rule> listRules = new ArrayList<Rule>();
//			Queue<GraphNode> queue = new LinkedList<GraphNode>(node.getChilds());
//			GraphNode child = queue.poll();
//			if (child != null) {
//				while (!queue.isEmpty() || (child != null && !child.isVisited())) {
//					child.setFlag(true);
//					for (GraphNode n : child.getChilds())
//						if (!n.isVisited())
//							queue.add(n);
//					listRules.add(child.getRule());
//					child = queue.poll();
//				}
//				newNodes.add(new GraphNode(merge(listRules), new ArrayList<GraphNode>(), new ArrayList<GraphNode>()));
//			} else {
//				newNodes.add(node);
//			}
//			resetFlags();
//		}
		return new Graph(newNodes);
	}

	public Rule merge(List<Rule> list) {
		List<Formula> clauses = new ArrayList<Formula>();
		Map<Port, Boolean> sync = new HashMap<>();

		for (Rule r : list) {
			sync.putAll(r.getSync());
			clauses.add(r.getFormula());
		}
		
		return new Rule(sync, Conjunction.conjunction(clauses));
	}

	public Rule getRule(GraphNode n) {
		return n.getRule();
	}

}
