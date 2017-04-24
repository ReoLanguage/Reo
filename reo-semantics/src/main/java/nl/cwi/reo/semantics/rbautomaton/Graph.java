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
	
	public Set<Rule> getRules(){
		Set<Rule> s = new HashSet<Rule>();
		for(GraphNode node : n){
			s.add(node.getRule());
		}
		return s;
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
			List<Set<GraphNode>> listNode = Arrays.asList(new HashSet<GraphNode>());
			listNode.get(0).add(child);
			
			while (!queue.isEmpty() || (child != null && !child.isVisited())) {
				child.setFlag(true);
				for (GraphNode n : child.getChilds())
					if (!n.isVisited()){
						queue.add(n);
					}
				for (GraphNode n : child.getParents())
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
	
	public Graph isolate1(){
		List<GraphNode> newNodes = new ArrayList<GraphNode>();
		
		Set<Set<Rule>> newRuleSet = new HashSet<>();
		int connectedParts = 0;
		List<Set<GraphNode>> listLeaves = new ArrayList<Set<GraphNode>>();
		listLeaves.add(new HashSet<GraphNode>());
		int j = 0;
		
		//Go over all nodes in the graph, find connected parts and all leaf nodes.
		for (GraphNode node : n) {
			Queue<GraphNode> queue = new LinkedList<GraphNode>();
			GraphNode child = null;

			//If the node has one child, it's a leaf
			if(node.getChilds().size()<1 && !node.isVisited()){
				listLeaves.get(j).add(node);
			}
			if(!node.isVisited()){
				queue.addAll(node.getChilds());
				child = queue.poll();
			}

			node.setFlag(true);
	
			//Go through all child of the node, until there is no child, save the node if it is a leaf, and count the nodes of connected parts.
			while (!queue.isEmpty() || (child != null && !child.isVisited())) {
				connectedParts++;
				child.setFlag(true);
				int i=0;
				for (GraphNode n : child.getChilds()){
					i++;
					if (!n.isVisited() && !queue.contains(n)){
						queue.add(n);
					}
				}
				
				//It's a leaf, add it to the listLeave
				if(i==1){
					listLeaves.get(j).add(child);
				}
				for (GraphNode n : child.getParents())
					if (!n.isVisited() && !queue.contains(n))
						queue.add(n);
				child = queue.poll();
			}
			if(connectedParts != 0){
				listLeaves.add(new HashSet<GraphNode>());
				j++;
			}
			connectedParts=0;
		}
		
		resetFlags();
		newRuleSet = new HashSet<>();
		
		//Go through all leaves, and build the new list of rules
		for(Set<GraphNode> setNode : listLeaves){
			for(GraphNode node : setNode){
				Set<Rule> newRule = new HashSet<>();
				
				Queue<GraphNode> queue = new LinkedList<GraphNode>();
				GraphNode child = null;
				if(!(node.isVisited())){
					queue.addAll(node.getChilds());
					child = queue.poll();
					newRule.add(node.getRule());
				}
				
				node.setFlag(true);
				if(child != null)
					newRule.add(child.getRule());
				
				//A rule is added to the list if the node is a child, and if its rule compose with the new big rule
				while (!queue.isEmpty() || (child != null && !child.isVisited())) {
					connectedParts++;
					child.setFlag(true);
					for (GraphNode n : child.getChilds()){
						if (!n.isVisited() && !queue.contains(n) && n.getRule().canSync(merge(new ArrayList<>(newRule)))){
							newRule.add(n.getRule());
							queue.add(n);
						}
					}
					child = queue.poll();
				}
				resetFlags();
				newRuleSet.add(newRule);
			}
		}
		
		for (Set<Rule> newRule : newRuleSet)
			newNodes.add(new GraphNode(merge(new ArrayList<>(newRule)), new ArrayList<>(), new ArrayList<>()));

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
