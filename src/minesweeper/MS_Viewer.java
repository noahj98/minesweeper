package minesweeper;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class MS_Viewer extends JFrame {
	
	/*
	 * MS_Viewer
	 * 
	 * controls the actual game viewer and bomb counter viewer
	 */
	
	private final MS_Viewer_Game game_viewer;
	private final MS_Viewer_Counter game_counter;
	
	public MS_Viewer(MS_Model m, int tile_dim) {
		this.game_viewer = new MS_Viewer_Game(m, tile_dim);
		this.game_counter = new MS_Viewer_Counter(m, tile_dim);
		setup();
	}
	
	/*
	 * setup()
	 * 
	 * sets up JFrame (this)
	 */
	
	private void setup() {
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(game_viewer, BorderLayout.NORTH);
		add(game_counter, BorderLayout.SOUTH);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	/*
	 * getViewer()
	 * 
	 * returns game viewer
	 *  - for MS_Controller to listen to for clicks
	 */
	
	public MS_Viewer_Game getViewer() {
		return game_viewer;
	}
	
	/*
	 * getTileDimension()
	 * 
	 * returns width/height of tiles
	 */
	
	public int getTileDimension() {
		return game_viewer.getTileDimension();
	}
	
	/*
	 * updateViewer()
	 * 
	 * updates game viewer and bomb counter viewer
	 */
	
	public void updateViewer() {
		game_viewer.updateUI();
		game_counter.update();
	}
	
}
