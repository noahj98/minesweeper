package minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class MS_Controller implements MouseListener {
	
	private MS_Viewer viewer;
	private MS_Model model;
	private long time;
	
	public MS_Controller(int tile_dimension, int num_horz_tiles, int num_vert_tiles, int num_bombs) {
		model = new MS_Model(num_horz_tiles, num_vert_tiles, num_bombs);
		viewer = new MS_Viewer(model, tile_dimension);
		viewer.addMouseListener(this);
		time = -1L;
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
		int x = e.getX()/viewer.getTileDimension();
		int y = e.getY()/viewer.getTileDimension();
		
		if (e.getButton() == MouseEvent.BUTTON1)
			model.leftClick(x, y);
		else if (e.getButton() == MouseEvent.BUTTON3)
			model.rightClick(x, y);
		
		switch (model.getGameState()) {
		case WON:		double total_time = (System.nanoTime() - time) / 100000000.0;
						System.out.println("WINNER WINNER " + (time == -1L ? 0 : ((int)(total_time)) / 10.0));
		case DEAD:		time = -1L;
						break;
		case ALIVE:		if (time == -1L) time = System.nanoTime();
		}
						
		viewer.updateUI();
	}

}
