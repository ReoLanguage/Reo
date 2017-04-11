package nl.cwi.reo.semantics.rbautomaton;

import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class GraphNode {

	private List<GraphNode> childs; 
	private List<GraphNode> parents; 

	private Rule rule;
	/**
	 * True if the node has already been visited
	 */
	private boolean visited;
	
	public GraphNode(Rule r, List<GraphNode> childs, List<GraphNode> parents){
		this.rule=r;
		this.childs=childs;
		this.parents=parents;
		visited=false;
	}
	
	public List<GraphNode> getChilds(){
		return childs;
	}
	
	public void addChilds(List<GraphNode> n){
		childs.addAll(n);
	}
	
	public List<GraphNode> getParents(){
		return parents;
	}
	
	public void addParents(List<GraphNode> n){
		parents.addAll(n);
	}
	
	public Rule getRule(){
		return rule;
	}
	
	public boolean isVisited(){
		return visited;
	}
	
	public void setFlag(boolean flag){
		this.visited=flag;
	}
	
	/**
	 * Return true if the current node has an edge with the other node 
	 * @param n
	 * @return
	 */
	public boolean hasEdge(GraphNode n){
		Map<Port,Role> map = n.getRule().getSync();
//		boolean hasEdge = false;
		for(Port p : rule.getSync().keySet()){
			int r = rule.getSync().get(p).getValue();
			int l = map.get(p)!=null?map.get(p).getValue():-1;
			
			if((r==1 ||r==2) && l==0){
				return false;
			}
			if(r==0 && (l==1||l==2)){
				return false;
			}	
		}
		return true;
	}
}
