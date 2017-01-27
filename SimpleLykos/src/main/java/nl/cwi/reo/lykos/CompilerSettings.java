package nl.cwi.reo.lykos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompilerSettings implements Map<String, Object> {

	private final Map<String, Object> settings;

	public static final String FLAG_COMMANDIFY = "COMMANDIFY";

	public static final String FLAG_IGNORE_DATA = "IGNORE_DATA";

	public static final String FLAG_IGNORE_INPUT = "IGNORE_INPUT";

	public static final String FLAG_INFER_QUEUES = "INFER_QUEUES";

	public static final String FLAG_PARTITION = "PARTITION";

	public static final String FLAG_SUBTRACT_SYNTACTICALLY = "SUBTRACT_SYNTACTICALLY";

	public CompilerSettings() {
		this.settings = new HashMap<String, Object>();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean commandify() {
		return settings.containsKey(FLAG_COMMANDIFY)
				&& (boolean) settings.get(FLAG_COMMANDIFY);
	}

	public void commandify(boolean flag) {
		settings.put(FLAG_COMMANDIFY, flag);
	}

	@Override
	public boolean containsKey(Object key) {
		return settings.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return settings.containsValue(value);
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		return settings.entrySet();
	}

	@Override
	public Object get(Object key) {
		if (key == null)
			throw new NullPointerException();

		return settings.get(key);
	}
	
	public boolean ignoreData() {
		return settings.containsKey(FLAG_IGNORE_DATA)
				&& (boolean) settings.get(FLAG_IGNORE_DATA);
	}

	public void ignoreData(boolean flag) {
		settings.put(FLAG_IGNORE_DATA, flag);
	}

	public boolean ignoreInput() {
		return settings.containsKey(FLAG_IGNORE_INPUT)
				&& (boolean) settings.get(FLAG_IGNORE_INPUT);
	}

	public void ignoreInput(boolean flag) {
		settings.put(FLAG_IGNORE_INPUT, flag);
	}

	public boolean inferQueues() {
		return settings.containsKey(FLAG_INFER_QUEUES)
				&& (boolean) settings.get(FLAG_INFER_QUEUES);
	}

	public void inferQueues(boolean flag) {
		settings.put(FLAG_INFER_QUEUES, flag);
	}

	@Override
	public boolean isEmpty() {
		return settings.isEmpty();
	}

	public boolean partition() {
		return settings.containsKey(FLAG_PARTITION)
				&& (boolean) settings.get(FLAG_PARTITION);
	}

	public void partition(boolean flag) {
		settings.put(FLAG_PARTITION, flag);
	}

	@Override
	public Set<String> keySet() {
		return settings.keySet();
	}

	@Override
	public Object put(String key, Object value) {
		return settings.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return settings.size();
	}

	public boolean subtractSyntactically() {
		return settings.containsKey(FLAG_SUBTRACT_SYNTACTICALLY)
				&& (boolean) settings.get(FLAG_SUBTRACT_SYNTACTICALLY);
	}

	public void subtractSyntactically(boolean flag) {
		settings.put(FLAG_SUBTRACT_SYNTACTICALLY, flag);
	}

	@Override
	public Collection<Object> values() {
		return settings.values();
	}
}
