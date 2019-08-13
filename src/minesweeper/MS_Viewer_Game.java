package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MS_Viewer_Game extends JPanel {
	
	/*
	 * MS_Viewer_Game
	 * 
	 * viewer for the MS_Model
	 */
	
	private final int TILE_DIMENSION;
	private final MS_Model model;
	
	public MS_Viewer_Game(MS_Model m, int tile_dim) {
		this.model = m;
		this.TILE_DIMENSION = tile_dim;
		setup();
	}
	
	/*
	 * setup()
	 * 
	 * sets up the JPanel (this)
	 */
	
	private void setup() {
		setPreferredSize(new Dimension(TILE_DIMENSION * model.getWidth(), TILE_DIMENSION * model.getHeight()));
		setBackground(Color.BLACK);
		setOpaque(true);
		setFont(new Font(getFont().getName(), getFont().getStyle(), 4 * TILE_DIMENSION / 5));
	}
	
	/*
	 * getTileDimension()
	 * 
	 * returns width/height of tiles
	 */
	
	public int getTileDimension() {
		return TILE_DIMENSION;
	}

	/*
	 * paintComponent(Graphics)
	 * 
	 * paints tiles, flags, numbers, and bombs
	 */
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		MS_Model.TileState[][] states = model.getTileStates();
		final int[][] tiles = model.getTiles();
		Color curr_color = Color.WHITE;
		MS_Model.TileState curr_state = MS_Model.TileState.VISIBLE;
		int curr_tile = 0;
		
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				/*
				 * 		PAINTS THE SQUARES WHITE OR GRAY
				 */
				curr_state = states[i][j];
				curr_tile = tiles[i][j];
				
				switch(curr_state) {
				case HIDDEN:
				case FLAGGED:	curr_color = Color.LIGHT_GRAY;
								break;
				default:		curr_color = Color.WHITE;
				}
				g.setColor(curr_color);
				g.fillRect(	i * TILE_DIMENSION,
							j * TILE_DIMENSION,
							TILE_DIMENSION - 1,
							TILE_DIMENSION - 1);
			}
		}
		
		if (model.isDead()) {
			g.setColor(Color.BLACK);
			for (int i = 0; i < states.length; i++) {
				for (int j = 0; j < states[0].length; j++) {
					/*
					 * 		IF DEAD, PAINT BOMBS
					 */
					curr_tile = tiles[i][j];
					if (curr_tile == -1)
						g.fillOval(	TILE_DIMENSION * i + TILE_DIMENSION / 5,
									TILE_DIMENSION * j + TILE_DIMENSION / 5,
									TILE_DIMENSION * 3 / 5 - 1,
									TILE_DIMENSION * 3 / 5 - 1);
				}
			}
		}
		
		String curr_string;
		
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				/*
				 * 		PAINTS FLAGS AND COUNTERS
				 */
				curr_state = states[i][j];
				curr_tile = tiles[i][j];
				curr_string = null;
								
				switch (curr_state) {
				case VISIBLE:	if (curr_tile <= 0) continue;
								curr_color = Color.BLACK;
								curr_string = Integer.toString(tiles[i][j]);
								break;
				case FLAGGED:	curr_color = Color.RED;
								curr_string = "!";
								break;
				default:		continue;
				}
				
				g.setColor(curr_color);
				g.drawString(	curr_string,
								i * TILE_DIMENSION + TILE_DIMENSION / 5,
								j * TILE_DIMENSION + 4 * TILE_DIMENSION / 5);
			}
		}
	}

}
