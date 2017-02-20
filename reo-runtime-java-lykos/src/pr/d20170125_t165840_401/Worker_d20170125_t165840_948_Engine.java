package pr.d20170125_t165840_401;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Worker_d20170125_t165840_948_Engine extends Thread {
	private final OutputPort WhiteReturn$1$2;
	private final InputPort WhiteCall$1$2;

	public Worker_d20170125_t165840_948_Engine(
		OutputPort WhiteReturn$1$2,
		InputPort WhiteCall$1$2
	) {
		this.WhiteReturn$1$2 = WhiteReturn$1$2;
		this.WhiteCall$1$2 = WhiteCall$1$2;
	}

	@Override
	public void run() {
		List<Throwable> exceptions = new ArrayList<>();

		/* 
		 * Treat "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine" as a class of type Runnable
		 */

		Runnable runnable = null;
		try {
			Class<?> workerClass = Class.forName("nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine");
			Object workerInstance = null;
			for (Constructor<?> constr : workerClass.getConstructors())
				try {
					constr.setAccessible(true);
					workerInstance = constr.newInstance(
						WhiteCall$1$2,
						WhiteReturn$1$2
					);
				} catch (Throwable throwable) {
					exceptions.add(throwable);
				}

			if (workerInstance == null)
				throw new Error(
						"Class \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine\" has no constructor for the provided arguments");

			if (!(workerInstance instanceof Runnable))
				throw new Error(
						"Class \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine\" does not implement interface Runnable");

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
		 * Treat "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine" as a static method
		 */

		int lastDotIndex = "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine".lastIndexOf('.');
		if ("nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine".startsWith(".") || "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine".endsWith("."))
			throw new Error("Failed to load worker \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine\"");
		if (lastDotIndex == -1)
			throw new Error("Failed to load worker \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine\"");

		String className = "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine".substring(0, lastDotIndex);
		String methodName = "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine".substring(lastDotIndex + 1);

		boolean hasMethod = false;

		try {
			Class<?> workerClass = Class.forName(className);
			for (Method meth : workerClass.getMethods())
				if (meth.getName().equals(methodName)) {
					meth.setAccessible(true);
					meth.invoke(
						null, 
						WhiteCall$1$2,
						WhiteReturn$1$2
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

			throw new Error("Failed to load worker \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Engine\" because of the following exceptions: " + exceptions.toString());
		}
	}
}
