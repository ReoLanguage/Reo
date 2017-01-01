package nl.cwi.reo.interpret;

import java.util.Objects;

public class NodeName implements Node {

	private final String name;

	private final TypeTag tag;
	
	private final NodeType type;
	
	private final boolean hidden;

	public NodeName(String name, TypeTag tag, NodeType type, boolean hidden) {
		this.name = name;
		this.tag = tag;
		this.type = type;
		this.hidden = hidden;
	}
	
	public String getName() {
		return name;
	}
	
	public TypeTag getTypeTag() {
		return tag;
	}
	
	public NodeType getNodeType() {
		return type;
	}
	
	public NodeName rename(String name) {
		return new NodeName(name, tag, type, hidden);
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public NodeName hide() {
		return new NodeName(name, tag, type, true);
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof NodeName)) return false;
	    NodeName p = (NodeName)other;
	   	return Objects.equals(this.name, p.name) && 
	   			Objects.equals(this.hidden, p.hidden);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.name, this.hidden);
	}

	@Override
	public Node evaluate(DefinitionList params) 
			throws Exception {
		return this;
	}
}
