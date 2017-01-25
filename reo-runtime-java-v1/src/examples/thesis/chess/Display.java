package nl.cwi.pr.runtime.examples.thesis.chess;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import nl.cwi.pr.runtime.api.InputPort;

public class Display {
	private final Board board;
	private final Map<String, Piece> position;
	private final InputPort blackPort;
	private final InputPort whitePort;

	public Display(InputPort whitePort, InputPort blackPort) {
		this.blackPort = blackPort;
		this.whitePort = whitePort;

		this.position = new HashMap<>();
		this.position.put("a1", Piece.WHITE_ROOK);
		this.position.put("b1", Piece.WHITE_KNIGHT);
		this.position.put("c1", Piece.WHITE_BISHOP);
		this.position.put("d1", Piece.WHITE_QUEEN);
		this.position.put("e1", Piece.WHITE_KING);
		this.position.put("f1", Piece.WHITE_BISHOP);
		this.position.put("g1", Piece.WHITE_KNIGHT);
		this.position.put("h1", Piece.WHITE_ROOK);
		this.position.put("a2", Piece.WHITE_PAWN);
		this.position.put("b2", Piece.WHITE_PAWN);
		this.position.put("c2", Piece.WHITE_PAWN);
		this.position.put("d2", Piece.WHITE_PAWN);
		this.position.put("e2", Piece.WHITE_PAWN);
		this.position.put("f2", Piece.WHITE_PAWN);
		this.position.put("g2", Piece.WHITE_PAWN);
		this.position.put("h2", Piece.WHITE_PAWN);
		this.position.put("a7", Piece.BLACK_PAWN);
		this.position.put("b7", Piece.BLACK_PAWN);
		this.position.put("c7", Piece.BLACK_PAWN);
		this.position.put("d7", Piece.BLACK_PAWN);
		this.position.put("e7", Piece.BLACK_PAWN);
		this.position.put("f7", Piece.BLACK_PAWN);
		this.position.put("g7", Piece.BLACK_PAWN);
		this.position.put("h7", Piece.BLACK_PAWN);
		this.position.put("a8", Piece.BLACK_ROOK);
		this.position.put("b8", Piece.BLACK_KNIGHT);
		this.position.put("c8", Piece.BLACK_BISHOP);
		this.position.put("d8", Piece.BLACK_QUEEN);
		this.position.put("e8", Piece.BLACK_KING);
		this.position.put("f8", Piece.BLACK_BISHOP);
		this.position.put("g8", Piece.BLACK_KNIGHT);
		this.position.put("h8", Piece.BLACK_ROOK);

		this.board = new Board();
		this.board.update(position);
	}

	public void run() {
		String move = null;
		while (true) {
			move = (String) whitePort.getUninterruptibly();
			move(move);
			board.update(position);
			System.out.println("white: " + move);
			sleep();

			move = (String) blackPort.getUninterruptibly();
			move(move);
			board.update(position);
			System.out.println("black: " + move);
			sleep();
		}
	}

	private void move(String move) {
		String from, to, promotion;
		Piece piece;

		switch (move.length()) {
		case 4:
			from = move.substring(0, 2).toLowerCase();
			to = move.substring(2, 4).toLowerCase();
			if (!position.containsKey(from))
				throw new Error("invalid move");

			piece = position.remove(from);
			position.remove(to);
			position.put(to, piece);

			if (piece == Piece.WHITE_KING && move.equals("e1g1"))
				move("h1f1");
			if (piece == Piece.WHITE_KING && move.equals("e1c1"))
				move("a1d1");
			if (piece == Piece.BLACK_KING && move.equals("e8g8"))
				move("h8f8");
			if (piece == Piece.BLACK_KING && move.equals("e7c8"))
				move("a8d8");

			return;
		case 5:
			from = move.substring(0, 2).toLowerCase();
			to = move.substring(2, 4).toLowerCase();
			promotion = move.substring(4, 5).toLowerCase();

			if (!position.containsKey(from))
				throw new Error("invalid move");
			if (!Arrays.asList("q", "r", "b", "n").contains(promotion))
				throw new Error("unsupported promotion");

			piece = position.remove(from);
			position.remove(to);
			position.put(to, Piece.promote(piece, promotion));
			return;
		default:
			throw new Error();
		}

	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}

enum Piece {
	WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_BISHOP, WHITE_KNIGHT, WHITE_PAWN, BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_BISHOP, BLACK_KNIGHT, BLACK_PAWN;

	private final String pawn = "\u265F";
	private final String rook = "\u265C";
	private final String knight = "\u265E";
	private final String bishop = "\u265D";
	private final String queen = "\u265B";
	private final String king = "\u265A";

	public ChessLabel getLabel() {
		switch (this) {
		case BLACK_BISHOP:
			return new ChessBlackLabel(bishop);
		case BLACK_KING:
			return new ChessBlackLabel(king);
		case BLACK_KNIGHT:
			return new ChessBlackLabel(knight);
		case BLACK_PAWN:
			return new ChessBlackLabel(pawn);
		case BLACK_QUEEN:
			return new ChessBlackLabel(queen);
		case BLACK_ROOK:
			return new ChessBlackLabel(rook);
		case WHITE_BISHOP:
			return new ChessWhiteLabel(bishop);
		case WHITE_KING:
			return new ChessWhiteLabel(king);
		case WHITE_KNIGHT:
			return new ChessWhiteLabel(knight);
		case WHITE_PAWN:
			return new ChessWhiteLabel(pawn);
		case WHITE_QUEEN:
			return new ChessWhiteLabel(queen);
		case WHITE_ROOK:
			return new ChessWhiteLabel(rook);
		default:
			throw new Error("unsupported piece");
		}
	}

	public static Piece promote(Piece piece, String string) {
		if (piece == WHITE_PAWN) {
			if ("q".equals(string))
				return WHITE_QUEEN;
			if ("r".equals(string))
				return WHITE_QUEEN;
			if ("b".equals(string))
				return WHITE_QUEEN;
			if ("n".equals(string))
				return WHITE_QUEEN;
		}

		if (piece == BLACK_PAWN) {
			if ("q".equals(string))
				return BLACK_QUEEN;
			if ("r".equals(string))
				return BLACK_QUEEN;
			if ("b".equals(string))
				return BLACK_QUEEN;
			if ("n".equals(string))
				return BLACK_QUEEN;
		}

		throw new Error("unsupported promotion");
	}
}

class ChessLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	ChessLabel(String s) {
		super(s);
	}

	void set(int idx, int row) {
		setFont(new Font("Ariel", Font.PLAIN, 60));
		setOpaque(true);
		setBackground((idx + row) % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY);
		setHorizontalAlignment(SwingConstants.CENTER);
	}
}

class ChessWhiteLabel extends ChessLabel {
	private static final long serialVersionUID = 1L;

	ChessWhiteLabel(String s) {
		super(s);
	}

	void set(int idx, int row) {
		super.set(idx, row);
		setForeground(Color.WHITE);
	}
}

class ChessBlackLabel extends ChessLabel {
	private static final long serialVersionUID = 1L;

	ChessBlackLabel(String s) {
		super(s);
	}

	void set(int idx, int row) {
		super.set(idx, row);
		setForeground(Color.BLACK);
	}
}

class Board extends JFrame {
	private static final long serialVersionUID = 1L;

	private ChessLabel[] labels = new ChessLabel[64];

	Board() {
		setTitle("Chess");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(640, 640);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(8, 8));
	}

	void update(Map<String, Piece> position) {
		String[] x = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
		String[] y = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };

		int k = 0;

		for (int j = y.length - 1; j >= 0; j--)
			for (int i = 0; i < x.length; i++) {

				String key = x[i] + y[j];
				if (position.containsKey(key))
					labels[k++] = position.get(key).getLabel();
				else
					labels[k++] = new ChessLabel(" ");
			}

		Container contentPane = getContentPane();
		contentPane.removeAll();

		int row = -1;
		for (int i = 0; i < labels.length; i++) {
			if (i % 8 == 0)
				row++;

			labels[i].set(i, row);
			contentPane.add(labels[i]);
		}

		invalidate();
		validate();
		repaint();

	}
}