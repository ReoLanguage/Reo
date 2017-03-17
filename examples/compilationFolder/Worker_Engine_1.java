import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Worker_Engine_1 extends Thread {
	private final OutputPort _18;
	private final InputPort _27;

	public Worker_Engine_1(
		OutputPort _18,
		InputPort _27
	) {
		this._18 = _18;
		this._27 = _27;
	}

	@Override
	public void run() {
		List<Throwable> exceptions = new ArrayList<>();

		/* 
		 * Treat "runtime.chess.Workers.Engine" as a class of type Runnable
		 */

		Runnable runnable = null;
		try {
			Class<?> workerClass = Class.forName("runtime.chess.Workers.Engine");
			Object workerInstance = null;
			for (Constructor<?> constr : workerClass.getConstructors())
				try {
					constr.setAccessible(true);
					workerInstance = constr.newInstance(
						_27,
						_18
					);
				} catch (Throwable throwable) {
					exceptions.add(throwable);
				}

			if (workerInstance == null)
				throw new Error(
						"Class \"runtime.chess.Workers.Engine\" has no constructor for the provided arguments");

			if (!(workerInstance instanceof Runnable))
				throw new Error(
						"Class \"runtime.chess.Workers.Engine\" does not implement interface Runnable");

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
		 * Treat "runtime.chess.Workers.Engine" as a static method
		 */

		int lastDotIndex = "runtime.chess.Workers.Engine".lastIndexOf('.');
		if ("runtime.chess.Workers.Engine".startsWith(".") || "runtime.chess.Workers.Engine".endsWith("."))
			throw new Error("Failed to load worker \"runtime.chess.Workers.Engine\"");
		if (lastDotIndex == -1)
			throw new Error("Failed to load worker \"runtime.chess.Workers.Engine\"");

		String className = "runtime.chess.Workers.Engine".substring(0, lastDotIndex);
		String methodName = "runtime.chess.Workers.Engine".substring(lastDotIndex + 1);

		boolean hasMethod = false;

		try {
			Class<?> workerClass = Class.forName(className);
			for (Method meth : workerClass.getMethods())
				if (meth.getName().equals(methodName)) {
					meth.setAccessible(true);
					meth.invoke(
						null, 
						_27,
						_18
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

			throw new Error("Failed to load worker \"runtime.chess.Workers.Engine\" because of the following exceptions: " + exceptions.toString());
		}
	}
}
