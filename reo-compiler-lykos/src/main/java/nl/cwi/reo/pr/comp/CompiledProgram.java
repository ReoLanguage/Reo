package nl.cwi.reo.pr.comp;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;

public abstract class CompiledProgram {
	protected final List<CompiledGeneratee> generatees;
	private final String sourceRunTimeDirectoryLocation;

	//
	// CONSTRUCTORS
	//

	public CompiledProgram(String sourceRunTimeDirectoryLocation,
			List<CompiledGeneratee> generatees) {

		if (sourceRunTimeDirectoryLocation == null)
			throw new NullPointerException();
		if (generatees == null)
			throw new NullPointerException();
		if (generatees.contains(null))
			throw new NullPointerException();

		this.generatees = Collections.unmodifiableList(generatees);
		this.sourceRunTimeDirectoryLocation = sourceRunTimeDirectoryLocation;
	}

	//
	// METHODS - PUBLIC
	//

	public abstract String getMain();

	public void writeGeneratees(String targetGenerateesDirectoryLocation)
			throws IOException {

		if (targetGenerateesDirectoryLocation == null)
			throw new NullPointerException();

		for (CompiledGeneratee g : generatees)
			g.writeFiles(targetGenerateesDirectoryLocation);
	}

	public void writeRunTime(String targetRunTimeDirectoryLocation)
			throws IOException {

		if (targetRunTimeDirectoryLocation == null)
			throw new NullPointerException();

		final Path sourceDirectoryPath = Paths
				.get(sourceRunTimeDirectoryLocation);
		final Path targetDirectoryPath = Paths
				.get(targetRunTimeDirectoryLocation);

		Files.walkFileTree(sourceDirectoryPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {

				if (dir == null)
					throw new NullPointerException();
				if (attrs == null)
					throw new NullPointerException();

				Path directoryPath = targetDirectoryPath
						.resolve(sourceDirectoryPath.relativize(dir));
				try {
					Files.copy(dir, directoryPath);
				} catch (FileAlreadyExistsException exception) {
					if (!Files.isDirectory(directoryPath))
						throw exception;
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {

				if (file == null)
					throw new NullPointerException();
				if (attrs == null)
					throw new NullPointerException();

				Files.copy(file, targetDirectoryPath
						.resolve(sourceDirectoryPath.relativize(file)),
						StandardCopyOption.REPLACE_EXISTING);

				return FileVisitResult.CONTINUE;
			}
		});
	}
}
