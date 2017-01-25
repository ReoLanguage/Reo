package nl.cwi.pr.runtime.examples.furelise;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Musicians {

	public static void Alice(InputPort play, OutputPort done) {
		Musician(play, done, "Alice",
				"src/nl/cwi/pr/runtime/examples/furelise/1.wav");
	}

	public static void Bob(InputPort play, OutputPort done) {
		Musician(play, done, "Bob",
				"src/nl/cwi/pr/runtime/examples/furelise/2,6.wav");
	}

	public static void Carol(InputPort play, OutputPort done) {
		Musician(play, done, "Carol",
				"src/nl/cwi/pr/runtime/examples/furelise/3,7.wav");
	}

	public static void Dave(InputPort play, OutputPort done) {
		Musician(play, done, "Dave",
				"src/nl/cwi/pr/runtime/examples/furelise/4.wav");
	}

	public static void Erin(InputPort play, OutputPort done) {
		Musician(play, done, "Erin",
				"src/nl/cwi/pr/runtime/examples/furelise/5.wav");
	}

	public static void Frank(InputPort play, OutputPort done) {
		Musician(play, done, "Frank",
				"src/nl/cwi/pr/runtime/examples/furelise/8.wav");
	}

	public static void Orchestrator(final OutputPort port) {
		if (port == null)
			throw new NullPointerException();

		JButton button = new JButton("Play");
		button.setFont(new Font("Palatino", Font.PLAIN, 40));
		button.setPreferredSize(new Dimension(200, 150));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						port.putUninterruptibly(0);
					}
				}).start();
			}
		});

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		JFrame frame = new JFrame("Orchestrator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocation(dim.width / 2 - 100, dim.height / 2 - 75);

		frame.add(button);
		frame.pack();

		button.requestFocus();
	}

	public static void Musician(InputPort play, OutputPort done, String name,
			String wavLocation) {

		if (done == null)
			throw new NullPointerException();
		if (play == null)
			throw new NullPointerException();
		if (name == null)
			throw new NullPointerException();
		if (wavLocation == null)
			throw new NullPointerException();

		System.out
				.println("No output? First enable JavaFX (by explicitly adding jfxr.jar to the classpath), then uncomment *both* the body of method nl.cwi.pr.runtime.examples.furelise.Musicians.Musican *and* class nl.cwi.pr.runtime.examples.furelise.Musician.\n");

		// Musician musician = new Musician(name, wavLocation);
		// musician.startConcert();
		// while (true) {
		// Object token = play.getUninterruptibly();
		// musician.play();
		// done.putUninterruptibly(token);
		// }
	}
}
