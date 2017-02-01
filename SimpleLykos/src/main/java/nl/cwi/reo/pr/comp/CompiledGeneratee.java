package nl.cwi.reo.pr.comp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public class CompiledGeneratee {
	private final Map<String, String> files;

	//
	// CONSTRUCTORS
	//

	public CompiledGeneratee(Map<String, String> files) {
		if (files == null)
			throw new NullPointerException();
		if (files.containsKey(null))
			throw new NullPointerException();
		if (files.containsValue(null))
			throw new NullPointerException();

		this.files = Collections.unmodifiableMap(files);
	}

	//
	// METHODS - PUBLIC
	//

	public Map<String, String> getFiles() {
		return files;
	}

	public void writeFiles(final String targetGenerateesDirectoryLocation)
			throws IOException {

		if (targetGenerateesDirectoryLocation == null)
			throw new NullPointerException();

		for (Entry<String, String> entr : files.entrySet()) {
			Path parentDirectory = Paths.get(targetGenerateesDirectoryLocation,
					entr.getKey()).getParent();

			if (parentDirectory != null)
				Files.createDirectories(parentDirectory);

			Files.write(
					Paths.get(targetGenerateesDirectoryLocation, entr.getKey()),
					Arrays.asList(entr.getValue()), Charset.defaultCharset());
		}
	}
}
