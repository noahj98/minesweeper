package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MS_Viewer extends JPanel {
	
	private final int TILE_DIMENSION;
	private final MS_Model model;
	private JFrame frame;
	
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
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
	
	public int getTileDimension() {
		return TILE_DIMENSION;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		MS_Model.TileState[][] states = model.getTileStates();
		int[][] tiles = model.getTiles();
		
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				g.setColor(Color.WHITE);
				if (states[i][j] == MS_Model.TileState.HIDDEN) {
					g.setColor(Color.GRAY);
				} else if (states[i][j] == MS_Model.TileState.FLAGGED) {
					g.setColor(Color.GRAY);
				}
				g.fillRect(i * TILE_DIMENSION, j * TILE_DIMENSION,
							TILE_DIMENSION - 1, TILE_DIMENSION - 1);
				if (states[i][j] == MS_Model.TileState.VISIBLE) {
					g.setColor(Color.BLACK);
					if (tiles[i][j] > 0)
						g.drawString(new String(Integer.toString(tiles[i][j])), i * TILE_DIMENSION + TILE_DIMENSION / 5, j * TILE_DIMENSION + 4 * TILE_DIMENSION / 5);
					continue;
				} else if (states[i][j] == MS_Model.TileState.FLAGGED) {
					g.setColor(Color.RED);
					int[] x_pts = {i * TILE_DIMENSION, i * TILE_DIMENSION, (i + 1) * TILE_DIMENSION - TILE_DIMENSION / 2};
					int[] y_pts = {j * TILE_DIMENSION, (j + 1) * TILE_DIMENSION - TILE_DIMENSION / 2, j * TILE_DIMENSION + TILE_DIMENSION / 4};
					g.fillPolygon(x_pts, y_pts, 3);
				}
			}
		}
	}
	
}
