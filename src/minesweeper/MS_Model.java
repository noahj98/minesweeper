package minesweeper;

import java.util.Random;

public class MS_Model {
	
	enum GameState {
		ALIVE, DEAD, NOT_INITIALIZED, WON
	}
	
	enum TileState {
		VISIBLE, HIDDEN, FLAGGED
	}
	
	private final int WIDTH;
	private final int HEIGHT;
	private final int BOMBS;
	private int num_flagged;
	private final int[][] tiles;
	private final TileState[][] states;
	private GameState gamestate;
	private final Random r;
	
	public MS_Model(int w, int h, int bombs) {
		WIDTH = w;
		HEIGHT = h;
		BOMBS = bombs;
		tiles = new int[w][h];
		states = new TileState[w][h];
		r = new Random();
		prepareBoard();
	}
	
	public int getNumUnflaggedBombs() {
		return BOMBS - num_flagged;
	}
	
	public int getNumBombs() {
		return BOMBS;
	}
	
	public void prepareBoard() {
		resetBoard(-10, -10);
		gamestate = GameState.NOT_INITIALIZED;
	}

	public GameState getGameState() {
		return gamestate;
	}
	
	public boolean isDead() {
		return gamestate == GameState.DEAD;
	}
	
	public boolean hasWon() {
		return gamestate == GameState.WON;
	}
	
	public boolean isAlive() {
		return gamestate == GameState.ALIVE;
	}
	
	public int[][] getTiles() {
		return tiles;
	}

	public TileState[][] getTileStates() {
		return states;
	}

	public void resetBoard(int X_PROTECTED, int Y_PROTECTED) {
		gamestate = GameState.ALIVE;
		num_flagged = 0;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				tiles[i][j] = 0;
				states[i][j] = TileState.HIDDEN;
			}
		}
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
	
	public void leftClick(int x, int y) {
		switch (gamestate) {
		case WON:
		case DEAD:				prepareBoard();
								return;
		case NOT_INITIALIZED:	resetBoard(x, y);
		}
		if (states[x][y] == TileState.VISIBLE ||
			states[x][y] == TileState.FLAGGED) return;
		if (tiles[x][y] == -1) {
			gamestate = GameState.DEAD;
			return;
		}
		if (tiles[x][y] == 0) {
			makeSectionVisible(x, y);
		} else
			states[x][y] = TileState.VISIBLE;
		gamestate = checkOnlyBombsHidden() ? GameState.WON : gamestate;
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
		switch (gamestate) {
		case WON:
		case DEAD:				prepareBoard();
								return;
		case NOT_INITIALIZED:	return;
		}
		
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

}
