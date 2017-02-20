//package nl.cwi.pr.runtime.examples.furelise;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.util.Observable;
//import java.util.Observer;
//import java.util.Random;
//import java.util.concurrent.Semaphore;
//
//import javafx.embed.swing.JFXPanel;
//import javafx.event.EventHandler;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaMarkerEvent;
//import javafx.scene.media.MediaPlayer;
//import javafx.util.Duration;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.SwingUtilities;
//
//public class Musician extends Observable {
//
//	//
//	// FIELDS
//	//
//
//	private final String name;
//	private final Media media;
//	private final MediaPlayer player;
//	private final Semaphore playerDone = new Semaphore(0);
//
//	private volatile JButton button;
//	private volatile boolean isPlaying = false;
//
//	//
//	// CONSTRUCTORS
//	//
//
//	public Musician(final String name, final String wavLocation) {
//		if (name == null)
//			throw new NullPointerException("name==null");
//		if (wavLocation == null)
//			throw new NullPointerException("wavLocation==null");
//
//		initializeJavaFx();
//
//		synchronized (Musician.class) {
//			this.name = name;
//			this.media = new Media(new File(wavLocation).toURI().toString());
//			this.player = new MediaPlayer(media);
//		}
//	}
//
//	//
//	// METHODS
//	//
//
//	public void play() {
//		isPlaying = true;
//		super.setChanged();
//		super.notifyObservers();
//
//		System.out.println("[" + name + "] Playing...");
//
//		player.seek(Duration.ZERO);
//		player.play();
//		playerDone.acquireUninterruptibly();
//
//		System.out.println("[" + name + "] Done.");
//
//		isPlaying = false;
//		super.setChanged();
//		super.notifyObservers();
//	}
//
//	public void startConcert() {
//
//		/*
//		 * Practice
//		 */
//
//		this.player.setMute(true);
//		this.player.setOnEndOfMedia(new Runnable() {
//			@Override
//			public void run() {
//				Musician.this.playerDone.release();
//			}
//		});
//
//		player.play();
//		playerDone.acquireUninterruptibly();
//
//		/*
//		 * Prepare
//		 */
//
//		this.player.setMute(false);
//		this.player.setOnEndOfMedia(null);
//		this.player.setOnMarker(new EventHandler<MediaMarkerEvent>() {
//			@Override
//			public void handle(final MediaMarkerEvent event) {
//				Musician.this.playerDone.release();
//			}
//		});
//
//		media.getMarkers().put("",
//				media.getDuration().subtract(Duration.millis(80)));
//
//		/*
//		 * Output
//		 */
//
//		int width = 200;
//		int height = 150;
//
//		JButton button = new JButton(name);
//		button.setFont(new Font("Palatino", Font.PLAIN, 40));
//		button.setForeground(Color.GRAY);
//		button.setPreferredSize(new Dimension(width, height));
//		button.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(final ActionEvent event) {
//				play();
//			}
//		});
//
//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//
//		JFrame frame = new JFrame(name);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//		frame.setResizable(false);
//
//		Random rng = new Random(System.nanoTime());
//		int x = rng.nextInt(dim.width - 1 * width);
//		int y = rng.nextInt(dim.height - 2 * height);
//
//		frame.setLocation(x, y);
//		frame.add(button);
//		frame.pack();
//
//		this.button = button;
//		this.addObserver(new MusicianObserver());
//
//		System.out.println("[" + name + "] Ready to go!");
//	}
//
//	public void stopConcert() {
//		this.player.stop();
//	}
//
//	//
//	// CLASS
//	//
//
//	public class MusicianObserver implements Observer {
//		public void update(Observable o, Object arg) {
//			if (isPlaying) {
//				button.setFont(new Font("Palatino", Font.BOLD, 40));
//				button.setForeground(Color.BLACK);
//			} else {
//				button.setFont(new Font("Palatino", Font.PLAIN, 40));
//				button.setForeground(Color.GRAY);
//			}
//		}
//	}
//
//	//
//	// STATIC - FIELDS
//	//
//
//	private static boolean hasInitializedJavaFx = false;
//
//	//
//	// STATIC - METHODS
//	//
//
//	public static synchronized void initializeJavaFx() {
//		if (!hasInitializedJavaFx) {
//			final Semaphore semaphore = new Semaphore(0);
//			SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
//					new JFXPanel();
//					semaphore.release();
//				}
//			});
//
//			semaphore.acquireUninterruptibly();
//			hasInitializedJavaFx = true;
//		}
//	}
// }