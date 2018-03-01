
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import nl.cwi.reo.runtime.Port;

public class Engine {
	private final Port<String> inputPort;
	private final Port<String> outputPort;
	private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private final BufferedReader reader;
	private final BufferedWriter writer;
	private final int moveTime = 500;

	public Engine(Port<String> inputPort, Port<String> outputPort) {
		this.inputPort = inputPort;
		this.outputPort = outputPort;

		String os = System.getProperty("os.name").toLowerCase();
		final String stockfish;
		if (os.contains("win"))
			stockfish = "stockfish-6-win-32.exe";
		else if (os.contains("mac"))
			stockfish = "stockfish-6-osx-64";
		else
			stockfish = "stockfish-6-linux-64";

		String path = "./" + stockfish;

		Process process = null;
		try {
			process = Runtime.getRuntime().exec(path);
			System.out.println("Running " + path + "...");
		} catch (IOException e) {
			System.out.println("Failed to run " + path);
			e.printStackTrace();
			throw new Error();
		}

		this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		this.writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

		new Thread() {
			@Override
			public void run() {
				String line;
				StringBuilder builder = new StringBuilder();
				if (reader == null || queue == null)
					throw new NullPointerException();

				try {
					while ((line = reader.readLine()) != null) {
						builder.append(line + "\n");
						if (line.startsWith("uciok") || line.startsWith("bestmove")) {

							queue.put(builder.toString());
							builder = new StringBuilder();
						}
					}
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
					throw new Error();
				}
			}
		}.start();

		try {
			writer.write("uci\n");
			writer.flush();
			queue.take();
		}

		catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
	}

	public void run() {
		while (true) {
			String moves = (String) inputPort.get();
			System.out.println(moves);
			String result = "";

			try {
				writer.write("ucinewgame\n");
				writer.write("position startpos moves " + moves + "\n");
				writer.write("go movetime " + moveTime + "\n");
				writer.flush();
				result = queue.take();
			}

			catch (IOException | InterruptedException e) {
				e.printStackTrace();
				throw new Error();
			}

			outputPort.put(result);
		}

	}
}
