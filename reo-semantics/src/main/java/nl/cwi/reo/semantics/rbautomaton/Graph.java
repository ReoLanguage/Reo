package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	
	public Graph(Set<Rule> l){
		List<GraphNode> list = new ArrayList<GraphNode>();
		for(Rule r : l){
			list.add(new GraphNode(r, new ArrayList<GraphNode>(), new ArrayList<GraphNode>()));
		}
		int i=0;
		for(GraphNode k : list){
			i++;
			int j=0;
			for(GraphNode r : list){
				j++;
				if(i!=j && k.hasEdge(r)){
					k.addChilds(Arrays.asList(r));
					r.addParents(Arrays.asList(k));
				}
			}
		}
		this.n=list;
	}
	public Graph(List<GraphNode> n){
		this.n=n;
	}
	
	public List<GraphNode> getNodes(){
		return n;
	}
	
	public void resetFlags(){
		for(GraphNode node : n){
			node.setFlag(false);
		}
	}
	
	/**
	 * Compute new rules at each nodes
	 * @return
	 */
	public Graph isolate(){
		List<GraphNode> newNodes = new ArrayList<GraphNode>();
		for(GraphNode node : n){
			List<Rule> listRules = new ArrayList<Rule>();
			Queue<GraphNode> queue = new LinkedList<GraphNode>(node.getChilds());
			GraphNode child = queue.poll();
			if(child!=null){
				while(!queue.isEmpty() || (child!=null && !child.isVisited())){
					child.setFlag(true);
					for(GraphNode n : child.getChilds())
						if(!n.isVisited())
							queue.add(n);
					listRules.add(child.getRule());
					child = queue.poll();
				}
				newNodes.add(new GraphNode(merge(listRules), new ArrayList<GraphNode>(),new ArrayList<GraphNode>()));
			}
			else{
				newNodes.add(node);
			}
			resetFlags();
		}
		return new Graph(newNodes);
	}
	
	public Rule merge(List<Rule> list){
		List<Formula> f = new ArrayList<Formula>();
		Map<Port,Role> map = new HashMap<Port,Role>();
		
		for(Rule r : list){
			f.add(r.getFormula());
			for(Port p : r.getSync().keySet()){
				if(map.containsKey(p)){
					if(map.get(p).getValue()==1 && r.getSync().get(p).getValue()==2){
						map.replace(p, r.getSync().get(p));
					}
				}
				else{
					map.put(p, r.getSync().get(p));
				}
			}
		}
		return new Rule(map,new Conjunction(f));

	}
	
	public Rule getRule(GraphNode n){
		return n.getRule();
	}
	
}
