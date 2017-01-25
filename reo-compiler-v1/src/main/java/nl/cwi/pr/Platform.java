package nl.cwi.pr;

import java.io.File;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class Platform {

	//
	// STATIC - METHODS - PUBLIC
	//

	public static boolean hasBundle(String bundleName) {
		if (bundleName == null)
			throw new NullPointerException();

		try {
			tryGetBundle(bundleName);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean hasFileInBundle(String bundleName, String path) {
		if (bundleName == null)
			throw new NullPointerException();
		if (path == null)
			throw new NullPointerException();
		if (!hasBundle(bundleName))
			throw new IllegalStateException();

		try {
			tryGetFileInBundle(bundleName, path);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Bundle getBundle(String bundleName) {
		if (bundleName == null)
			throw new NullPointerException();
		if (!hasBundle(bundleName))
			throw new IllegalStateException();

		try {
			return tryGetBundle(bundleName);
		} catch (Exception e) {
			throw new Error();
		}
	}

	public static File getFileInBundle(String bundleName, String path) {
		if (bundleName == null)
			throw new NullPointerException();
		if (path == null)
			throw new NullPointerException();
		if (!hasBundle(bundleName))
			throw new IllegalStateException();
		if (!hasFileInBundle(bundleName, path))
			throw new IllegalStateException();

		try {
			return new File(FileLocator.toFileURL(
					FileLocator.find(getBundle(bundleName), new Path(path),
							null)).getPath());

		} catch (Exception e) {
			throw new Error();
		}
	}

	//
	// STATIC - METHODS - PRIVATE
	//

	private static Bundle tryGetBundle(String bundleName) throws Exception {
		if (bundleName == null)
			throw new NullPointerException();

		BundleContext bundleContext = FrameworkUtil.getBundle(
				new Platform().getClass()).getBundleContext();

		Bundle bundle = null;
		for (Bundle b : bundleContext.getBundles())
			if (b.getSymbolicName().equals(bundleName))
				if (bundle == null
						|| bundle.getVersion().compareTo(b.getVersion()) < 0)
					bundle = b;

		if (bundle == null)
			throw new Exception();

		return bundle;
	}

	private static File tryGetFileInBundle(String bundleName, String path)
			throws Exception {

		return new File(FileLocator.toFileURL(
				FileLocator.find(getBundle(bundleName), new Path(path), null))
				.getPath());
	}
}
