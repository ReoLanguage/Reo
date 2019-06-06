package nl.cwi.reo.components;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import nl.cwi.reo.runtime.Port;

public class Visual {
	private final TBoard board;
	private final List<TPiece> position;
	private final Port<String> input;

	public Visual(Port<String> cells) {
		
		this.input = cells;

		this.position = new ArrayList<>();


		this.board = new TBoard();
		this.board.initialize();
		move(" : : : : : : : : ");
		this.board.update(position);
	}

	public void run() {
		String move = null;
		while (true) {
			move = (String) input.get();
			move(move);
			board.update(position);
			System.out.println("state: " + move);
			sleep();
		}
	}

	private void move(String move) {
		String [] state = move.split(":");
		switch (state.length) {
		case 9:
			for(int i = 0; i<=8; i++) {
				if(state[i].contentEquals(" "))
					position.add(i,TPiece.EMPTY);
				if(state[i].contentEquals("X"))
					position.add(i,TPiece.CROSS);
				if(state[i].contentEquals("O"))
					position.add(i,TPiece.CIRCLE);
			}
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

enum TPiece {
	CROSS, CIRCLE, EMPTY;

	private final String cross = "\u2613";
	private final String circle = "\u26AA";
	private final String empty = " ";

	public TLabel getLabel() {
		switch (this) {
		case CROSS:
			return new TPieceLabel(cross);
		case CIRCLE:
			return new TPieceLabel(circle);
		case EMPTY:
			return new TPieceLabel(empty);
		default:
			throw new Error("unsupported piece");
		}
	}
}

class TLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	TLabel(String s) {
		super(s);
	}

	void set(int idx, int row) {
		setFont(new Font("Ariel", Font.PLAIN, 60));
		setOpaque(true);
		setBackground((idx + row) % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY);
		setHorizontalAlignment(SwingConstants.CENTER);
	}
}

class TPieceLabel extends TLabel {
	private static final long serialVersionUID = 1L;

	TPieceLabel(String s) {
		super(s);
	}

	void set(int idx, int row) {
		super.set(idx, row);
		setForeground(Color.WHITE);
	}
}

class TBoard extends JFrame {
	private static final long serialVersionUID = 1L;

	private TLabel[] labels = new TLabel[9];

	public void initialize() {
		setTitle("Tic-Tac-Toe");
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
		contentPane.setLayout(new GridLayout(3, 3));
	}

	void update(List<TPiece> position) {
		
		int k = 8;

		for (int j = 0; j <= k; j++) {
				labels[j] = position.get(j).getLabel();
			}

		Container contentPane = getContentPane();
		contentPane.removeAll();

		int row = 0;
		for (int i = 0; i < labels.length; i++) {
			labels[i].set(i, row);
			contentPane.add(labels[i]);
		}

		invalidate();
		validate();
		repaint();

	}
}
