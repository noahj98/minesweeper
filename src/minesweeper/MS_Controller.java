package minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class MS_Controller implements MouseListener {
	
	private MS_Viewer viewer;
	private MS_Model model;
	
	public MS_Controller(int tile_dimension, int num_horz_tiles, int num_vert_tiles, int num_bombs) {
		model = new MS_Model(num_horz_tiles, num_vert_tiles, num_bombs);
		viewer = new MS_Viewer(model, tile_dimension);
		viewer.addMouseListener(this);
	}
	
	public void start() {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (!model.leftClick(e.getX()/viewer.getTileDimension(),
								e.getY()/viewer.getTileDimension())) {
				if (model.isAlive()) {
					viewer.updateUI();
					JOptionPane.showMessageDialog(null, null);
					model.initializeBoard();
				}
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			model.rightClick(e.getX()/viewer.getTileDimension(),
						e.getY()/viewer.getTileDimension());
		}
		viewer.updateUI();
	}

}
