package nl.cwi.pr.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public abstract class IdObjectFactory<Obj extends IdObjectFactory<Obj, S, Sp>.IdObject, S extends IdObjectFactory<Obj, S, Sp>.IdObjectSet, Sp extends IdObjectSpec> {
	private final Map<Sp, Obj> specs = new HashMap<Sp, Obj>();
	private final Map<Integer, Obj> ids = new HashMap<Integer, Obj>();
	private int nextId = 1;

	//
	// METHODS - PUBLIC
	//

	public boolean constructed(Sp spec) {
		if (spec == null)
			throw new NullPointerException();

		return specs.containsKey(spec);
	}

	public void dispose(Obj object) {
		if (object == null)
			throw new NullPointerException();
		if (!specs.containsValue(object))
			throw new IllegalStateException();

		specs.remove(object.getSpec());
		ids.remove(object.getId());
	}

	public Obj get(Sp spec) {
		if (spec == null)
			throw new NullPointerException();
		if (!constructed(spec))
			throw new IllegalStateException();

		return specs.get(spec);
	}

	public Obj newOrGet(Sp spec) {
		if (spec == null)
			throw new NullPointerException();

		Obj object = specs.get(spec);
		if (object == null) {
			int id = nextId++;
			object = newObject(id, spec);
			specs.put(spec, object);
			ids.put(id, object);
		}

		return object;
	}

	public abstract S newSet();

	public S newSet(S set) {
		if (set == null)
			throw new NullPointerException();

		S newSet = newSet();
		newSet.addAll(set);
		return newSet;
	}

	public S newSet(Collection<Obj> objects) {
		if (objects == null)
			throw new NullPointerException();
		if (objects.contains(null))
			throw new NullPointerException();
		for (Obj obj : objects)
			if (obj.getFactory() != this)
				throw new IllegalArgumentException();

		S newSet = newSet();
		for (Obj obj : objects)
			newSet.add(obj);

		return newSet;
	}

	public S newSet(Obj[] objects) {
		if (objects == null)
			throw new NullPointerException();
		if (Arrays.asList(objects).contains(null))
			throw new NullPointerException();
		for (Obj obj : objects)
			if (obj.getFactory() != this)
				throw new IllegalArgumentException();

		S newSet = newSet();
		for (Obj obj : objects)
			newSet.add(obj);

		return newSet;
	}

	public S takeComplement(S set1, S set2) {
		if (set1 == null)
			throw new NullPointerException();
		if (set2 == null)
			throw new NullPointerException();
		if (set1.getFactory() != this)
			throw new IllegalStateException();
		if (set2.getFactory() != this)
			throw new IllegalStateException();

		S complement = newSet();
		complement.addAll(set1);
		complement.removeAll(set2);
		return complement;
	}

	public S takeComplement(S set, Obj object) {
		if (set == null)
			throw new NullPointerException();
		if (set.getFactory() != this)
			throw new IllegalStateException();
		if (object.getFactory() != this)
			throw new IllegalStateException();

		S difference = newSet();
		difference.addAll(set);
		difference.remove(object);
		return difference;
	}

	public S takeDifference(S set1, S set2) {
		if (set1 == null)
			throw new NullPointerException();
		if (set2 == null)
			throw new NullPointerException();
		if (set1.getFactory() != this)
			throw new IllegalStateException();
		if (set2.getFactory() != this)
			throw new IllegalStateException();

		S difference = takeUnion(set1, set2);
		difference.removeAll(takeIntersection(set1, set2));
		return difference;
	}

	public S takeIntersection(S set1, S set2) {
		if (set1 == null)
			throw new NullPointerException();
		if (set2 == null)
			throw new NullPointerException();
		if (set1.getFactory() != this)
			throw new IllegalStateException();
		if (set2.getFactory() != this)
			throw new IllegalStateException();

		S intersection = newSet();
		intersection.addAll(set1);
		intersection.retainAll(set2);
		return intersection;
	}

	public S takeUnion(S set1, S set2) {
		if (set1 == null)
			throw new NullPointerException();
		if (set2 == null)
			throw new NullPointerException();
		if (set1.getFactory() != this)
			throw new IllegalStateException();
		if (set2.getFactory() != this)
			throw new IllegalStateException();

		S union = newSet();
		union.addAll(set1);
		union.addAll(set2);
		return union;
	}

	//
	// METHODS - PROTECTED
	//

	protected abstract Obj newObject(int id, Sp spec);

	//
	// CLASSES - PUBLIC
	//

	public class IdObject implements Comparable<Obj> {
		private final int id;
		private final Sp spec;

		//
		// CONSTRUCTORS
		//

		protected IdObject(int id, Sp spec) {
			if (spec == null)
				throw new NullPointerException();

			this.id = id;
			this.spec = spec;
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public int compareTo(Obj o) {
			return id - o.getId();
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof IdObjectFactory<?, ?, ?>.IdObject
					&& equals((IdObjectFactory<?, ?, ?>.IdObject) obj);
		}

		public boolean equals(IdObjectFactory<?, ?, ?>.IdObject object) {
			return object != null
					&& (this == object || spec.equals(object.getSpec()));
		}

		public IdObjectFactory<Obj, S, Sp> getFactory() {
			return IdObjectFactory.this;
		}

		public int getId() {
			return id;
		}

		@Override
		public int hashCode() {
			return spec.hashCode();
		}

		@Override
		public String toString() {
			return spec.toString();
		}

		//
		// METHODS - PROTECTED
		//

		protected Sp getSpec() {
			return spec;
		}
	}

	public class IdObjectSet implements Comparable<S>, Iterable<Obj> {
		private final BitSet bits = new BitSet();
		private boolean isModified = true;
		private List<Obj> sortedObjects;

		//
		// CONSTRUCTORS
		//

		protected IdObjectSet() {
		}

		//
		// METHODS - PUBLIC
		//

		public void add(Obj object) {
			if (object == null)
				throw new NullPointerException();
			if (object.getFactory() != getFactory())
				throw new IllegalStateException();

			bits.set(object.getId());
			isModified = true;
		}

		public void addAll(Collection<Obj> objects) {
			if (objects == null)
				throw new NullPointerException();
			if (objects.contains(null))
				throw new NullPointerException();

			for (Obj obj : objects)
				if (obj.getFactory() != getFactory())
					throw new IllegalStateException();

			for (Obj o : objects)
				add(o);
		}

		public void addAll(S objects) {
			if (objects == null)
				throw new NullPointerException();
			if (objects.getFactory() != getFactory())
				throw new IllegalStateException();

			bits.or(objects.getBits());
			isModified = true;
		}

		public void clear() {
			bits.clear();
			isModified = true;
		}

		@Override
		public int compareTo(S o) {
			// TODO: Check if this method satisfies transitivity :o

			if (o == null)
				throw new NullPointerException();
			if (o.getFactory() != getFactory())
				throw new IllegalStateException();

			int j = 0;
			for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
				if (!ids.containsKey(i)) {
					bits.clear(i);
					continue;
				}

				while (!ids.containsKey(o.getBits().nextSetBit(j)))
					o.getBits().clear(o.getBits().nextSetBit(j));

				if (i != o.getBits().nextSetBit(j))
					return j - i;
				else
					j = i;
			}

			return j;
		}

		public boolean contains(Obj object) {
			if (object == null)
				throw new NullPointerException();
			if (object.getFactory() != getFactory())
				throw new IllegalStateException();

			return bits.get(object.getId());
		}

		public boolean containsAll(S objects) {
			if (objects == null)
				throw new NullPointerException();
			if (objects.getFactory() != getFactory())
				throw new IllegalStateException();

			BitSet set = (BitSet) bits.clone();
			set.and(objects.getBits());
			return set.equals(objects.getBits());
		}

		public boolean containsNone(S objects) {
			if (objects == null)
				throw new NullPointerException();
			if (objects.getFactory() != getFactory())
				throw new IllegalStateException();

			final BitSet set = (BitSet) bits.clone();
			set.and(objects.getBits());
			return set.cardinality() == 0;
		}

		public int count() {
			return bits.cardinality();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				throw new NullPointerException();

			return obj instanceof IdObjectFactory.IdObjectSet
					&& equals((IdObjectFactory<?, ?, ?>.IdObjectSet) obj);
		}

		public boolean equals(IdObjectFactory<?, ?, ?>.IdObjectSet objects) {
			if (objects == null)
				throw new NullPointerException();
			if (objects.getFactory() != getFactory())
				throw new IllegalStateException();

			return bits.equals(objects.bits);
		}

		public IdObjectFactory<Obj, S, Sp> getFactory() {
			return IdObjectFactory.this;
		}

		public List<Integer> getIds() {
			List<Integer> ids = new ArrayList<>();

			for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
				if (!IdObjectFactory.this.ids.containsKey(i)) {
					bits.clear(i);
					continue;
				}

				ids.add(i);
			}

			return ids;
		}

		public List<Obj> getSorted() {
			if (!isModified)
				return sortedObjects;

			sortedObjects = getUnsorted();
			Collections.sort(sortedObjects);
			isModified = false;

			return sortedObjects;
		}

		public List<Obj> getUnsorted() {
			List<Obj> objects = new ArrayList<>();

			for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
				if (!ids.containsKey(i)) {
					bits.clear(i);
					continue;
				}

				objects.add(ids.get(i));
				objects.contains(null); // FIXME: Why is this necessary!? :o
			}

			return objects;
		}

		@Override
		public int hashCode() {
			return bits.hashCode();
		}

		public boolean isEmpty() {
			return count() == 0;
		}

		@Override
		public Iterator<Obj> iterator() {
			return getUnsorted().iterator();
		}

		public void remove(Obj object) {
			if (object == null)
				throw new NullPointerException();
			if (object.getFactory() != getFactory())
				throw new IllegalStateException();

			bits.clear(object.getId());
			isModified = true;
		}

		public void removeAll(S objects) {
			if (objects == null)
				throw new NullPointerException();
			if (objects.getFactory() != getFactory())
				throw new IllegalStateException();

			bits.andNot(objects.getBits());
			isModified = true;
		}

		public void retainAll(S objects) {
			if (objects == null)
				throw new NullPointerException();
			if (objects.getFactory() != getFactory())
				throw new IllegalStateException();

			bits.and(objects.getBits());
			isModified = true;
		}

		//
		// METHODS - PROTECTED
		//

		protected final BitSet getBits() {
			return bits;
		}
	}

	//
	// STATIC - CLASSES
	//

	private static class BitSet implements Cloneable {

		private final TreeMap<Integer, Integer> map = new TreeMap<>();

		public void and(BitSet set) {
			for (Entry<Integer, Integer> entr : set.map.entrySet())
				if (map.containsKey(entr.getKey()))
					map.put(entr.getKey(),
							map.get(entr.getKey()) & entr.getValue());

			Iterator<Integer> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				Integer integer = iterator.next();
				if (map.get(integer) == 0 || !set.map.containsKey(integer))
					iterator.remove();
			}
		}

		public void andNot(BitSet set) {
			for (Entry<Integer, Integer> entr : set.map.entrySet())
				if (map.containsKey(entr.getKey()))
					map.put(entr.getKey(),
							map.get(entr.getKey()) & ~entr.getValue());

			Iterator<Integer> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				Integer integer = iterator.next();
				if (map.get(integer) == 0)
					iterator.remove();
			}
		}

		public int cardinality() {
			int cardinality = 0;
			for (Integer integ : map.values())
				cardinality += Integer.bitCount(integ);

			return cardinality;
		}

		@Override
		protected BitSet clone() {
			BitSet set = new BitSet();
			set.map.putAll(map);
			return set;
		}

		public void clear() {
			map.clear();
		}

		public void clear(int index) {
			int major = getMajor(index);
			if (!map.containsKey(major))
				return;

			map.put(major, map.get(major) & ~getMask(getMinor(index)));

			if (map.get(major) == 0)
				map.remove(major);
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof BitSet && equals((BitSet) obj);
		}

		public boolean equals(BitSet set) {
			return map.equals(set.map);
		}

		public boolean get(int index) {
			int major = getMajor(index);
			if (!map.containsKey(major))
				return false;

			int shift = 31 - getMinor(index);
			return ((map.get(major) << shift) & 0b10000000_00000000_00000000_00000000) == 0b10000000_00000000_00000000_00000000;
		}

		@Override
		public int hashCode() {
			return map.hashCode();
		}

		public int nextSetBit(int index) {
			int major = getMajor(index);

			Iterator<Integer> iterator = map.keySet().iterator();
			int key = -1;
			while (iterator.hasNext()) {
				key = iterator.next();
				if (key >= major)
					break;
			}

			if (key < major)
				return -1;

			int value = map.get(key);
			int shift = 31 - getMinor(index);

			while (true) {
				while (shift >= 0) {
					if (((value << shift) & 0b10000000_00000000_00000000_00000000) == 0b10000000_00000000_00000000_00000000)
						return 32 * key + (31 - shift);

					shift--;
				}

				if (!iterator.hasNext())
					return -1;

				key = iterator.next();
				value = map.get(key);
				shift = 31;
			}
		}

		public void or(BitSet set) {
			for (Entry<Integer, Integer> entr : set.map.entrySet())
				if (map.containsKey(entr.getKey()))
					map.put(entr.getKey(),
							map.get(entr.getKey()) | entr.getValue());
				else
					map.put(entr.getKey(), entr.getValue());
		}

		public void set(int index) {
			int major = getMajor(index);
			int minor = getMinor(index);

			if (map.containsKey(major))
				map.put(major, map.get(major) | getMask(minor));
			else
				map.put(major, getMask(minor));
		}

		@Override
		public String toString() {
			String s = "";
			for (Entry<Integer, Integer> entr : map.entrySet())
				s += ", "
						+ entr.getKey()
						+ ":"
						+ String.format("%32s",
								Integer.toBinaryString(entr.getValue()))
								.replace(' ', '0');

			return s.substring(2);
		}

		private int getMajor(int index) {
			return index / 32;
		}

		private int getMinor(int index) {
			return index % 32;
		}

		private int getMask(int minor) {
			switch (minor) {
			case 0:
				return 0b00000000_00000000_00000000_00000001;
			case 1:
				return 0b00000000_00000000_00000000_00000010;
			case 2:
				return 0b00000000_00000000_00000000_00000100;
			case 3:
				return 0b00000000_00000000_00000000_00001000;
			case 4:
				return 0b00000000_00000000_00000000_00010000;
			case 5:
				return 0b00000000_00000000_00000000_00100000;
			case 6:
				return 0b00000000_00000000_00000000_01000000;
			case 7:
				return 0b00000000_00000000_00000000_10000000;
			case 8:
				return 0b00000000_00000000_00000001_00000000;
			case 9:
				return 0b00000000_00000000_00000010_00000000;
			case 10:
				return 0b00000000_00000000_00000100_00000000;
			case 11:
				return 0b00000000_00000000_00001000_00000000;
			case 12:
				return 0b00000000_00000000_00010000_00000000;
			case 13:
				return 0b00000000_00000000_00100000_00000000;
			case 14:
				return 0b00000000_00000000_01000000_00000000;
			case 15:
				return 0b00000000_00000000_10000000_00000000;
			case 16:
				return 0b00000000_00000001_00000000_00000000;
			case 17:
				return 0b00000000_00000010_00000000_00000000;
			case 18:
				return 0b00000000_00000100_00000000_00000000;
			case 19:
				return 0b00000000_00001000_00000000_00000000;
			case 20:
				return 0b00000000_00010000_00000000_00000000;
			case 21:
				return 0b00000000_00100000_00000000_00000000;
			case 22:
				return 0b00000000_01000000_00000000_00000000;
			case 23:
				return 0b00000000_10000000_00000000_00000000;
			case 24:
				return 0b00000001_00000000_00000000_00000000;
			case 25:
				return 0b00000010_00000000_00000000_00000000;
			case 26:
				return 0b00000100_00000000_00000000_00000000;
			case 27:
				return 0b00001000_00000000_00000000_00000000;
			case 28:
				return 0b00010000_00000000_00000000_00000000;
			case 29:
				return 0b00100000_00000000_00000000_00000000;
			case 30:
				return 0b01000000_00000000_00000000_00000000;
			case 31:
				return 0b10000000_00000000_00000000_00000000;
			default:
				throw new Error();
			}
		}
	}
}
