package unfixedSwitch;

public class Envelope {
	public String inport;
	public String ipdst;
	public String ipsrc;
	public String action;
	public String priority;

	public String getIn_port() {
		return inport;
	}

	public void setInport(String inport) {
		this.inport = inport;
	}

	public String getIpdst() {
		return ipdst;
	}

	public void setIpdst(String ipdst) {
		this.ipdst = ipdst;
	}

	public String getIpsrc() {
		return ipsrc;
	}

	public void setIpsrc(String ipsrc) {
		this.ipsrc = ipsrc;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
