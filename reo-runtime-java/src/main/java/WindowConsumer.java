import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.cwi.reo.runtime.java.Component;
import nl.cwi.reo.runtime.java.Port;

public class WindowConsumer implements Component {
	private static final int HEIGHT = 400;
	private static final int OFFSET_INCREMENT = 25;
	private static final int WIDTH = 300;

	private volatile Map<String,Port<Integer>> map = new HashMap<String,Port<Integer>>();
	
	public WindowConsumer(String name, Port<Integer> a) {
		a.setConsumer(this);
		map.put(name,a);
	}
	
	@Override
	public void activate() { 
		synchronized (this) {
			notify();	
		} 
	}

	@Override
	public void run() {	
		openThenWait(map);
//		WindowComponent.consume(a); 
	}
	
	public static <T> void openThenWait(Map<String, Port<T>> outputPorts) {

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		Point rightCenter = new Point(center.x + center.x / 2, center.y);
		int rightOffset = 0;

		final Semaphore semaphore = new Semaphore(0);

		for (Entry<String, Port<T>> entr : outputPorts.entrySet()) {
			JFrame window = openAndGetInput(entr.getValue(), entr.getKey());

			int x = rightOffset + rightCenter.x - WIDTH / 2;
			int y = rightOffset + rightCenter.y - HEIGHT / 2;
			rightOffset += OFFSET_INCREMENT;

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

		semaphore
				.acquireUninterruptibly(2);
	}
	
	
	public static <T> JFrame openAndGetInput(final Port<T> port, String portName) {
		JFrame window = new JFrame("Port: " + portName);

		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("This is a live interaction window for port:\n\n    "
				+ portName
				+ "\n\nUsage: click the button on the bottom of this window.\n");

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("courier", Font.PLAIN, 12));

		final LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
		new Thread() {
			public void run() {
				while (true) {
					while (true)
						try {
							queue.take();
							break;
						} catch (InterruptedException exception) {
						}

					Object datum = port.get();
					synchronized (textArea) {
						textArea.append("\n! Completed get(), received "
								+ Datum.convertToString(datum));
						textArea.setCaretPosition(textArea.getDocument()
								.getLength());
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
					textArea.setCaretPosition(textArea.getDocument()
							.getLength());
				}

				while (true)
					try {
						queue.put(new Object());
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