package nl.cwi.reo.runtime;

public interface Port<T> extends Input<T>, Output<T> {
	public void setProducer(Component p);
	public void setConsumer(Component c);
	public void setPut(T datum);
	public void setGet();
	public T hasPut();
	public T peek();
	public boolean hasGet();
	public T take();
	public void activateProducer();
	public void activateConsumer();
}