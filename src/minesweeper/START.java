package minesweeper;

import javax.swing.JOptionPane;

public class START {
	public static void main(String[] args) {
		
		String str_bombs;
		int NUM_BOMBS = -1;
		
		do {
			str_bombs = JOptionPane.showInputDialog("Enter number of bombs (10-100): ");
			if (str_bombs == null) System.exit(1);
			try {
				NUM_BOMBS = Integer.parseInt(str_bombs);
			} catch (Exception e) {}
		} while (NUM_BOMBS < 10 || NUM_BOMBS > 100);
				
		final int TILE_PIX = 30; //orig = 30
		final int W_TILE = 24;
		final int H_TILE = 16;
		//very easy	40
		//easy		50
		//medium	60
		//hard		70
		//very hard	80
		//near-bout impossible 90
		//good luck	100
		
		MS_Controller controller = new MS_Controller(TILE_PIX, W_TILE, H_TILE, NUM_BOMBS);
		
	}

}
