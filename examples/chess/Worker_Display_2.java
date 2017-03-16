import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Worker_Display_2 extends Thread {
	private final InputPort _16;
	private final InputPort _17;

	public Worker_Display_2(
		InputPort _16,
		InputPort _17
	) {
		this._16 = _16;
		this._17 = _17;
	}

	@Override
	public void run() {
		List<Throwable> exceptions = new ArrayList<>();

		/* 
		 * Treat "Workers.Display" as a class of type Runnable
		 */

		Runnable runnable = null;
		try {
			Class<?> workerClass = Class.forName("Workers.Display");
			Object workerInstance = null;
			for (Constructor<?> constr : workerClass.getConstructors())
				try {
					constr.setAccessible(true);
					workerInstance = constr.newInstance(
						_17,
						_16
					);
				} catch (Throwable throwable) {
					exceptions.add(throwable);
				}

			if (workerInstance == null)
				throw new Error(
						"Class \"Workers.Display\" has no constructor for the provided arguments");

			if (!(workerInstance instanceof Runnable))
				throw new Error(
						"Class \"Workers.Display\" does not implement interface Runnable");

			runnable = (Runnable) workerInstance;
		}

		catch (Throwable throwable) {
			exceptions.add(throwable);
		}

		if (runnable != null) {
			runnable.run();
			return;
		}

		/*
		 * Treat "Workers.Display" as a static method
		 */

		int lastDotIndex = "Workers.Display".lastIndexOf('.');
		if ("Workers.Display".startsWith(".") || "Workers.Display".endsWith("."))
			throw new Error("Failed to load worker \"Workers.Display\"");
		if (lastDotIndex == -1)
			throw new Error("Failed to load worker \"Workers.Display\"");

		String className = "Workers.Display".substring(0, lastDotIndex);
		String methodName = "Workers.Display".substring(lastDotIndex + 1);

		boolean hasMethod = false;

		try {
			Class<?> workerClass = Class.forName(className);
			for (Method meth : workerClass.getMethods())
				if (meth.getName().equals(methodName)) {
					meth.setAccessible(true);
					meth.invoke(
						null, 
						_17,
						_16
					);
					hasMethod = true;
				}
		}

		catch (Throwable throwable) {
			exceptions.add(throwable);
		}

		if (!hasMethod) {
			for (Throwable thr : exceptions)
				thr.printStackTrace();

			throw new Error("Failed to load worker \"Workers.Display\" because of the following exceptions: " + exceptions.toString());
		}
	}
}
