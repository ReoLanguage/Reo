package nl.cwi.reo.pr.autom;

public class DefaultMemoryCellFactory extends MemoryCellFactory {

	@Override
	protected MemoryCell newObject(int id, MemoryCellSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new MemoryCell(id, spec);
	}
}
