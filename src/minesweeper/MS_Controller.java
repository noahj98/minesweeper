package minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MS_Controller implements MouseListener {
	
	/*
	 * MS_Controller
	 * 
	 * controls the model and viewer
	 */
	
	private MS_Viewer viewer;
	private MS_Model model;
	private long time;
	
	public MS_Controller(int tile_dimension, int num_horz_tiles, int num_vert_tiles, int num_bombs) {
		model = new MS_Model(num_horz_tiles, num_vert_tiles, num_bombs);
		viewer = new MS_Viewer(model, tile_dimension);
		viewer.getViewer().addMouseListener(this);
		time = -1L;
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	
	/*
	 * mouseReleased(MouseEvent)
	 * 
	 * when the viewer is clicked, this method is called
	 * 
	 * it communicates the click to the model
	 * if the game has been won, it displays the time elasped
	 * it updates the viewer
	 */
	
	public void mouseReleased(MouseEvent e) {
		int x = e.getX()/viewer.getTileDimension();
		int y = e.getY()/viewer.getTileDimension();
		
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:	model.leftClick(x, y); break;
		case MouseEvent.BUTTON3:	model.rightClick(x, y);
		}
		
		switch (model.getGameState()) {
		case WON:		double total_time = (System.nanoTime() - time) / 100000000.0;
						System.out.println("WINNER WINNER " + (time == -1L ? 0 : ((int)(total_time)) / 10.0));
		case DEAD:		time = -1L;
						break;
		case ALIVE:		if (time == -1L) time = System.nanoTime();
		default:
		}
		
		viewer.updateViewer();
	}
}
