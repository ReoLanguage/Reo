package nl.cwi.reo.pr.comp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.connectors.Language;

public class CompilerSettings implements Map<String, Object> {

	//
	// FIELDS
	//

	private final Language targetLanguage;

	private final Map<String, Object> settings = new HashMap<String, Object>();

	private final boolean kSemantic;
	
	private String sourceFileLocation;

	//
	// CONSTRUCTORS
	//

	public CompilerSettings(String sourceFileLocation, Language targetLanguage,boolean ksemantic) {
		if (sourceFileLocation == null)
			throw new NullPointerException();
		if (targetLanguage == null)
			throw new NullPointerException();

		this.sourceFileLocation = sourceFileLocation;
		this.targetLanguage = targetLanguage;
		this.kSemantic=ksemantic;
	}

	//
	// METHODS
	//

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

	public Language getTargetLanguage() {
		return targetLanguage;
	}

	public String getSourceFileLocation() {
		return sourceFileLocation;
	}

	public void setSourceFileLocation(String source) {
		this.sourceFileLocation=source;
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
	public boolean isKSemantic() {
		return this.kSemantic;
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
//		throw new UnsupportedOperationException();
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

	//
	// STATIC - FIELDS
	//

	public static final String FLAG_COMMANDIFY = "COMMANDIFY";

	public static final String FLAG_IGNORE_DATA = "IGNORE_DATA";

	public static final String FLAG_IGNORE_INPUT = "IGNORE_INPUT";

	public static final String FLAG_INFER_QUEUES = "INFER_QUEUES";

	public static final String FLAG_PARTITION = "PARTITION";

	public static final String FLAG_SUBTRACT_SYNTACTICALLY = "SUBTRACT_SYNTACTICALLY";
}
