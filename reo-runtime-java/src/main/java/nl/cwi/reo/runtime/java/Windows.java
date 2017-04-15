package nl.cwi.reo.runtime.java;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nl.cwi.reo.runtime.java.Port;

public class Windows {
	private static final int HEIGHT = 400;
	private static final int OFFSET_INCREMENT = 25;
	private static final int WIDTH = 300;

	public static void producer(String name, Port<String> inputPorts) {

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

		Point leftCenter = new Point(center.x / 2, center.y);
		int leftOffset = 0;

		JFrame window = openAndGetOutput(inputPorts, name);

		int x = leftOffset + leftCenter.x - WIDTH / 2;
		int y = leftOffset + leftCenter.y - HEIGHT / 2;
		leftOffset += OFFSET_INCREMENT;

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		window.setLocation(x, y);
		window.setSize(WIDTH, HEIGHT);
		window.setVisible(true);
	}

	public static JFrame openAndGetOutput(final Port<String> port, String portName) {
		JFrame window = new JFrame("Port: " + portName);

		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("This is a live interaction window for port:\n\n    " + portName
				+ "\n\nUsage: insert a datum-to-put in the text field on the bottom of this window, and press <RETURN>.\n");

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("courier", Font.PLAIN, 12));

		final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

		new Thread() {
			public void run() {
				while (true) {
					String datum = null;
					while (true)
						try {
							datum = queue.take();
							break;
						} catch (InterruptedException exception) {
						}

					port.put(datum);
					synchronized (textArea) {
						textArea.append("\n! Completed put(\"" + datum + "\")");
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
				}
			}
		}.start();

		final JTextField textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				if (event.getKeyChar() == '\n') {
					String datum = textField.getText();
					textField.setText("");

					synchronized (textArea) {
						textArea.append("\n? Performing put(\"" + datum + "\")");
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}

					if (datum != null)
						while (true)
							try {
								queue.put(datum);
								break;
							} catch (InterruptedException exception) {
							}
				}
			}
		});

		window.add(new JScrollPane(textArea));
		window.add(textField, BorderLayout.SOUTH);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				textField.requestFocus();
			}
		});

		window.pack();
		return window;
	}

	public static void consumer(String name, Port<String> a) {
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

		Point rightCenter = new Point(center.x + center.x / 2, center.y);
		int rightOffset = 0;

		JFrame window = openAndGetInput(a, name);

		int x = rightOffset + rightCenter.x - WIDTH / 2;
		int y = rightOffset + rightCenter.y - HEIGHT / 2;
		rightOffset += OFFSET_INCREMENT;

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		window.setLocation(x, y);
		window.setSize(WIDTH, HEIGHT);
		window.setVisible(true);
	}

	public static JFrame openAndGetInput(final Port<String> port, String portName) {
		JFrame window = new JFrame("Port: " + portName);

		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("This is a live interaction window for port:\n\n    " + portName
				+ "\n\nUsage: click the button on the bottom of this window.\n");

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("courier", Font.PLAIN, 12));

		final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		new Thread() {
			public void run() {
				while (true) {
					while (true)
						try {
							queue.take();
							break;
						} catch (InterruptedException exception) {
						}
					String datum = port.get();
					synchronized (textArea) {
						textArea.append("\n! Completed get(), received " + datum);
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
				}
			}
		}.start();

		final JButton button = new JButton("get()");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (textArea) {
					textArea.append("\n? Performing get()");
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}

				while (true)
					try {
						queue.put("request");
						break;
					} catch (InterruptedException exception) {
					}
			}
		});

		window.add(new JScrollPane(textArea));
		window.add(button, BorderLayout.SOUTH);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				button.requestFocus();
			}
		});

		window.pack();
		return window;
	}

}