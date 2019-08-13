package minesweeper;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MS_Viewer_Counter extends JPanel {
	
	/*
	 * MS_Viewer_Counter
	 * 
	 * used for viewing number of bombs counter
	 */

	private final MS_Model model;
	private final JLabel bomb_label;
	
	public MS_Viewer_Counter(MS_Model m, int tile_dim) {
		this.model = m;
		this.bomb_label = new JLabel();
		setup(tile_dim);
	}
	
	/*
	 * setup()
	 * 
	 * sets up JPanel (this)
	 */
	private void setup(int tile_dim) {
		setPreferredSize(new Dimension(tile_dim * model.getWidth(), 30));
		setBackground(Color.BLACK);
		add(bomb_label);
	}
	
	/*
	 * update()
	 * 
	 * updates bomb counter
	 */
	
	public void update() {
		bomb_label.setForeground(model.getNumUnflaggedBombs() < 0 ? Color.RED : Color.GREEN);
		bomb_label.setText(new String("Bombs remaining: " + model.getNumUnflaggedBombs() + " / " + model.getNumBombs()));
	}
	
}
