package minesweeper;

import java.util.Random;

public class MS_Model {
	
	enum TileState {
		VISIBLE, HIDDEN, FLAGGED
	}
	
	private final int WIDTH;
	private final int HEIGHT;
	private final int BOMBS;
	private int num_flagged;
	private int[][] tiles;
	private TileState[][] states;
	private boolean alive;
	
	public MS_Model(int w, int h, int bombs) {
		WIDTH = w;
		HEIGHT = h;
		BOMBS = bombs;
		tiles = new int[w][h];
		states = new TileState[w][h];
		alive = false;
		initializeBoard();
	}
	
	public int getNumUnflaggedBombs() {
		return BOMBS - num_flagged;
	}
	
	public void initializeBoard() {
		resetBoard(-10, -10);
		alive = false;
	}

	public boolean isAlive() {
		return alive;
	}
	
	public int[][] getTiles() {
		return tiles;
	}

	public TileState[][] getTileStates() {
		return states;
	}

	public void resetBoard(int X_PROTECTED, int Y_PROTECTED) {
		alive = true;
		num_flagged = 0;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				tiles[i][j] = 0;
				states[i][j] = TileState.HIDDEN;
			}
		}
		Random r = new Random();
		for (int i = 0; i < BOMBS;) {
			int w = r.nextInt(WIDTH);
			int h = r.nextInt(HEIGHT);
			if (Math.abs(w-X_PROTECTED) < 2 && Math.abs(h-Y_PROTECTED) < 2) continue;
			if (tiles[w][h] == 0) {
				tiles[w][h] = -1;
				++i;
			}
		}
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				tiles[i][j] = getBombsAround(i, j);
			}
		}
	}
	
	private int getBombsAround(int i, int j) {
		if (tiles[i][j] == -1) return -1;
		int val = 0;
		
		for (int x = i - 1; x <= i + 1; x++) {
			for (int y = j - 1; y <= j + 1; y++) {
				if (x >= 0 && x < WIDTH &&
					y >= 0 && y < HEIGHT)
					val += tiles[x][y] == -1 ? 1 : 0;
			}
		}
		return val;
	}
	
	public boolean leftClick(int x, int y) {
		if (!isAlive()) resetBoard(x, y);
		if (states[x][y] == TileState.VISIBLE ||
			states[x][y] == TileState.FLAGGED) return true;
		if (tiles[x][y] == -1) {
			initializeBoard();
			return false;
		}
		if (tiles[x][y] == 0) {
			makeSectionVisible(x, y);
		} else
			states[x][y] = TileState.VISIBLE;
		return !checkOnlyBombsHidden();
	}
	
	private boolean checkOnlyBombsHidden() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (tiles[i][j] >= 0) {
					if (states[i][j] != TileState.VISIBLE)
						return false;
				}
			}
		}
		return true;
	}

	public void rightClick(int x, int y) {
		if (!isAlive()) return;
//		if (states[x][y] == TileState.VISIBLE) return;
//		states[x][y] = states[x][y] == TileState.HIDDEN ?
//						TileState.FLAGGED : TileState.HIDDEN;
//		if (states[x][y] == TileState.HIDDEN) {
//			states[x][y] = TileState.FLAGGED;
//			num_flagged++;
//		}
		switch (states[x][y]) {
		case HIDDEN:	states[x][y] = TileState.FLAGGED;
						num_flagged++;
						break;
		case FLAGGED:	states[x][y] = TileState.HIDDEN;
						num_flagged--;
		}
	}

	private void makeSectionVisible(int x,int y) {
		if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) return;
		if (states[x][y] == TileState.VISIBLE) return;
		if (states[x][y] == TileState.FLAGGED) num_flagged--; 
		states[x][y] = TileState.VISIBLE;
		if (tiles[x][y] != 0) return;
		
		for (int i = x-1; i <= x+1; i++) {
			for (int j = y-1; j <= y+1; j++) {
				makeSectionVisible(i,j);
			}
		}
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getNumberOfBombs() {
		return BOMBS;
	}

}
