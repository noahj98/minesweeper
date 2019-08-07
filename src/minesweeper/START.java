package minesweeper;

public class START {
	public static void main(String[] args) {
		
		final int TILE_PIX = 30;
		final int W_TILE = 24;
		final int H_TILE = 16;
		final int NUM_BOMBS = 60;	//medium difficulty = 60
		//very easy	40
		//easy		50
		//medium	60
		//hard		70
		//very hard	80
		//near-bout impossible 90
		//good luck	100
		
		MS_Controller controller = new MS_Controller(TILE_PIX, W_TILE, H_TILE, NUM_BOMBS);
		controller.start();
		
	}

}
