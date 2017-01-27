package nl.cwi.pr.targ.java.autom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import nl.cwi.pr.autom.TransitionFactory;
import nl.cwi.pr.autom.TransitionSpec;
import nl.cwi.pr.targ.java.JavaClass;
import nl.cwi.pr.targ.java.JavaNames;
import nl.cwi.pr.targ.java.JavaVariable;

public class JavaTransitionFactory extends TransitionFactory {
	private final JavaNames javaNames;

	//
	// CONSTRUCTORS
	//

	public JavaTransitionFactory(JavaNames javaNames) {
		if (javaNames == null)
			throw new NullPointerException();

		this.javaNames = javaNames;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Transition newObject(int id, TransitionSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new JavaTransition(id, spec);
	}

	//
	// CLASSES - PUBLIC
	//

	public class JavaTransition extends Transition implements JavaClass,
			JavaVariable {

		private Cache cache;
		private String className;
		private String variableName;

		//
		// CONSTRUCTORS
		//

		protected JavaTransition(int id, TransitionSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public void disableCache() {
			super.disableCache();
			cache = null;
		}

		@Override
		public void enableCache() {
			cache = new Cache();
			super.enableCache();
		}

		@Override
		public String getClassName() {
			if (className == null)
				className = javaNames.getFreshName("Transition" + getId());

			return className;
		}

		public List<Map<String, Object>> getMasks() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getMasksResult != null)
				return cache.getMasksResult;

			/*
			 * Get
			 */

			Map<Integer, Long> map = new TreeMap<>();
			for (Integer integ : getIndexPerPublicPort().values()) {
				Integer wordIndex = integ / 32;
				map.put(wordIndex,
						(long) Math.pow(2, integ % 32)
								| (map.containsKey(wordIndex) ? map
										.get(wordIndex) : 0));
			}

			List<Map<String, Object>> masks = new ArrayList<>();
			for (Entry<Integer, Long> entr : map.entrySet()) {
				Map<String, Object> mask = new HashMap<>();
				mask.put("wordIndex", entr.getKey());
				mask.put(
						"mask",
						"0b"
								+ String.format("%32s",
										Long.toBinaryString(entr.getValue()))
										.replace(' ', '0'));
				masks.add(mask);
			}

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getMasksResult = masks;

			return masks;
		}

		@Override
		public String getVariableName() {
			if (variableName == null)
				variableName = javaNames.getFreshName("transition" + getId());

			return variableName;
		}

		//
		// CLASSES - PRIVATE
		//

		private class Cache {
			List<Map<String, Object>> getMasksResult;
		}
	}
}
