package nl.cwi.reo.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nl.cwi.reo.runtime.java.Port;

@Deprecated
public class WindowP {
	private static final int HEIGHT = 400;
	private static final int OFFSET_INCREMENT = 25;
	private static final int WIDTH = 300;

	public static <T> void window(String name, Port<Object> inputPorts) {
		Map<String, Port<Object>> map = new HashMap<String, Port<Object>>();
		map.put(name, inputPorts);
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

		Point leftCenter = new Point(center.x / 2, center.y);
		int leftOffset = 0;

		final Semaphore semaphore = new Semaphore(0);

		for (Entry<String, Port<Object>> entr : map.entrySet()) {
			JFrame window = openAndGetOutput(entr.getValue(), entr.getKey());

			int x = leftOffset + leftCenter.x - WIDTH / 2;
			int y = leftOffset + leftCenter.y - HEIGHT / 2;
			leftOffset += OFFSET_INCREMENT;

			window.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
					// semaphore.release();
				}
			});

			window.setLocation(x, y);
			window.setSize(WIDTH, HEIGHT);
			window.setVisible(true);
		}

		semaphore.acquireUninterruptibly(2);
	}

	public static <T> JFrame openAndGetOutput(final Port<T> port, String portName) {
		JFrame window = new JFrame("Port: " + portName);

		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("This is a live interaction window for port:\n\n    " + portName
				+ "\n\nUsage: insert a datum-to-put in the text field on the bottom of this window, and press <RETURN>.\n");

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("courier", Font.PLAIN, 12));

		final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>();
		new Thread() {
			public void run() {
				while (true) {
					T datum = null;
					while (true)
						try {
							datum = queue.take();
							break;
						} catch (InterruptedException exception) {
						}

					port.put(datum);
					synchronized (textArea) {
						textArea.append("\n! Completed put(" + Datum.convertToString(datum) + ")");
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
					T datum = null;
					String datumText = textField.getText();
					if (Datum.canConvertToObject(datumText)) {
						datum = (T) Datum.convertToObject(datumText);
						textField.setText("");

						synchronized (textArea) {
							textArea.append("\n? Performing put(" + Datum.convertToString(datum) + ")");
							textArea.setCaretPosition(textArea.getDocument().getLength());
						}

					} else
						synchronized (textArea) {
							textArea.append("\n? Parse failure");
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

}