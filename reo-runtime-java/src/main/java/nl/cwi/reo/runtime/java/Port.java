package nl.cwi.reo.runtime.java;

public interface Port<T> extends Input<T>, Output<T> {
	public void setGet();
	public void setPut(T datum);
	public void setProducer(Component p);
	public void setConsumer(Component c);
	public boolean canPut();
	public boolean canGet();
	public void activateProducer();
	public void activateConsumer();
}