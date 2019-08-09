package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MS_Viewer extends JPanel {
	
	private final int TILE_DIMENSION;
	private final MS_Model model;
	private JFrame frame;
	private JPanel bombs_panel;
	private JLabel bombs_left;
	
	public MS_Viewer(MS_Model m, int tile_dim) {
		model = m;
		setPreferredSize(new Dimension(tile_dim * model.getWidth(), tile_dim * model.getHeight()));
		setBackground(Color.black);
		this.TILE_DIMENSION = tile_dim;
		frameSetup();
		setOpaque(true);
		setFont(new Font(getFont().getName(), getFont().getStyle(), 4*TILE_DIMENSION/5));
	}
	
	private void frameSetup() {
		frame = new JFrame();
		bombs_panel = new JPanel();
		bombs_panel.setPreferredSize(new Dimension(TILE_DIMENSION * model.getWidth(), 30));
		bombs_panel.setBackground(Color.BLACK);
		bombs_left = new JLabel();
		bombs_panel.add(bombs_left);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.NORTH);
		frame.add(bombs_panel, BorderLayout.SOUTH);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public int getTileDimension() {
		return TILE_DIMENSION;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		MS_Model.TileState[][] states = model.getTileStates();
		final int[][] tiles = model.getTiles();
		final int num_bombs_left = model.getNumUnflaggedBombs();
		final int total_bombs = model.getNumBombs();
		
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				g.setColor(Color.WHITE);
				if (states[i][j] == MS_Model.TileState.HIDDEN) {
					g.setColor(Color.LIGHT_GRAY);
				} else if (states[i][j] == MS_Model.TileState.FLAGGED) {
					g.setColor(Color.LIGHT_GRAY);
				}
				g.fillRect(i * TILE_DIMENSION, j * TILE_DIMENSION,
							TILE_DIMENSION - 1, TILE_DIMENSION - 1);
				if (model.isDead() && tiles[i][j] == -1) {
					g.setColor(Color.BLACK);
					g.fillOval(i * TILE_DIMENSION + TILE_DIMENSION / 5, j * TILE_DIMENSION + TILE_DIMENSION / 5, 3 * TILE_DIMENSION / 5 - 1, 3 * TILE_DIMENSION / 5 - 1);
				}
				if (states[i][j] == MS_Model.TileState.VISIBLE) {
					g.setColor(Color.BLACK);
					if (tiles[i][j] > 0)
						g.drawString(new String(Integer.toString(tiles[i][j])), i * TILE_DIMENSION + TILE_DIMENSION / 5, j * TILE_DIMENSION + 4 * TILE_DIMENSION / 5);
					continue;
				} else if (states[i][j] == MS_Model.TileState.FLAGGED) {
					g.setColor(Color.RED);
					g.drawString("!", i * TILE_DIMENSION + TILE_DIMENSION / 5, j * TILE_DIMENSION + 4 * TILE_DIMENSION / 5);
				}
			}
		}
		bombs_left.setForeground(num_bombs_left < 0 ? Color.RED : Color.GREEN);
		bombs_left.setText(new String("Bombs left: " + num_bombs_left + " / " + total_bombs));
	}
	
}
