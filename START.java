package minesweeper;

public class START {
	public static void main(String[] args) {
		
		final int TILE_PIX = 30;
		final int W_TILE = 24;
		final int H_TILE = 16;
		final int NUM_BOMBS = 60;
		
		MS_Controller controller = new MS_Controller(TILE_PIX, W_TILE, H_TILE, NUM_BOMBS);
		controller.start();
		
	}

}
