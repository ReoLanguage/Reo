import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Worker_Display_4 extends Thread {
	private final InputPort _13;
	private final InputPort _14;

	public Worker_Display_4(
		InputPort _13,
		InputPort _14
	) {
		this._13 = _13;
		this._14 = _14;
	}

	@Override
	public void run() {
		List<Throwable> exceptions = new ArrayList<>();

		/* 
		 * Treat "runtime.chess.Workers.Display" as a class of type Runnable
		 */

		Runnable runnable = null;
		try {
			Class<?> workerClass = Class.forName("runtime.chess.Workers.Display");
			Object workerInstance = null;
			for (Constructor<?> constr : workerClass.getConstructors())
				try {
					constr.setAccessible(true);
					workerInstance = constr.newInstance(
						_14,
						_13
					);
				} catch (Throwable throwable) {
					exceptions.add(throwable);
				}

			if (workerInstance == null)
				throw new Error(
						"Class \"runtime.chess.Workers.Display\" has no constructor for the provided arguments");

			if (!(workerInstance instanceof Runnable))
				throw new Error(
						"Class \"runtime.chess.Workers.Display\" does not implement interface Runnable");

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
		 * Treat "runtime.chess.Workers.Display" as a static method
		 */

		int lastDotIndex = "runtime.chess.Workers.Display".lastIndexOf('.');
		if ("runtime.chess.Workers.Display".startsWith(".") || "runtime.chess.Workers.Display".endsWith("."))
			throw new Error("Failed to load worker \"runtime.chess.Workers.Display\"");
		if (lastDotIndex == -1)
			throw new Error("Failed to load worker \"runtime.chess.Workers.Display\"");

		String className = "runtime.chess.Workers.Display".substring(0, lastDotIndex);
		String methodName = "runtime.chess.Workers.Display".substring(lastDotIndex + 1);

		boolean hasMethod = false;

		try {
			Class<?> workerClass = Class.forName(className);
			for (Method meth : workerClass.getMethods())
				if (meth.getName().equals(methodName)) {
					meth.setAccessible(true);
					meth.invoke(
						null, 
						_14,
						_13
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

			throw new Error("Failed to load worker \"runtime.chess.Workers.Display\" because of the following exceptions: " + exceptions.toString());
		}
	}
}
