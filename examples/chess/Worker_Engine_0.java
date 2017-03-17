import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Worker_Engine_0 extends Thread {
	private final OutputPort _19;
	private final InputPort _13;

	public Worker_Engine_0(
		OutputPort _19,
		InputPort _13
	) {
		this._19 = _19;
		this._13 = _13;
	}

	@Override
	public void run() {
		List<Throwable> exceptions = new ArrayList<>();

		/* 
		 * Treat "Workers.Engine" as a class of type Runnable
		 */

		Runnable runnable = null;
		try {
			Class<?> workerClass = Class.forName("Workers.Engine");
			Object workerInstance = null;
			for (Constructor<?> constr : workerClass.getConstructors())
				try {
					constr.setAccessible(true);
					workerInstance = constr.newInstance(
						_13,
						_19
					);
				} catch (Throwable throwable) {
					exceptions.add(throwable);
				}

			if (workerInstance == null)
				throw new Error(
						"Class \"Workers.Engine\" has no constructor for the provided arguments");

			if (!(workerInstance instanceof Runnable))
				throw new Error(
						"Class \"Workers.Engine\" does not implement interface Runnable");

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
		 * Treat "Workers.Engine" as a static method
		 */

		int lastDotIndex = "Workers.Engine".lastIndexOf('.');
		if ("Workers.Engine".startsWith(".") || "Workers.Engine".endsWith("."))
			throw new Error("Failed to load worker \"Workers.Engine\"");
		if (lastDotIndex == -1)
			throw new Error("Failed to load worker \"Workers.Engine\"");

		String className = "Workers.Engine".substring(0, lastDotIndex);
		String methodName = "Workers.Engine".substring(lastDotIndex + 1);

		boolean hasMethod = false;

		try {
			Class<?> workerClass = Class.forName(className);
			for (Method meth : workerClass.getMethods())
				if (meth.getName().equals(methodName)) {
					meth.setAccessible(true);
					meth.invoke(
						null, 
						_13,
						_19
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

			throw new Error("Failed to load worker \"Workers.Engine\" because of the following exceptions: " + exceptions.toString());
		}
	}
}
