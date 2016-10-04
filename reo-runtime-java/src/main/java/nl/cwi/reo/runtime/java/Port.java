package nl.cwi.reo.runtime.java;

public interface Port<T> extends Input<T>, Output<T> {
	public void setProducer(Component p);
	public void setConsumer(Component c);
	public void setPut(T datum);
	public void setGet();
	public boolean hasPut();
	public boolean hasGet();
	public T take();
	public void activateProducer();
	public void activateConsumer();
}