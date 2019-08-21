package minesweeper;

import java.util.Random;

public class MS_Model_OLD {
	
	/*
	 * MS_Model
	 * 
	 * holds the logic for minesweeper
	 */
	
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
	
	public MS_Model_OLD(int w, int h, int bombs) {
		WIDTH = w;
		HEIGHT = h;
		BOMBS = bombs;
		tiles = new int[w][h];
		states = new TileState[w][h];
		r = new Random();
		prepareBoard();
	}
	
	/*
	 * getNumUnflaggedBombs()
	 * 
	 * returns the total number of bombs less the number of flagged tiles
	 */
	
	public int getNumUnflaggedBombs() {
		return BOMBS - num_flagged;
	}
	
	/*
	 * getNumBombs()
	 * 
	 * returns total number of bombs
	 */
	
	public int getNumBombs() {
		return BOMBS;
	}
	
	/*
	 * prepareBoard()
	 * 
	 * wrapper for resetBoard()
	 * simulates a click off-screen
	 * 
	 * used to make all tiles hidden again, but the game hasn't truly started
	 */
	
	public void prepareBoard() {
		resetBoard(-10, -10);
		gamestate = GameState.NOT_INITIALIZED;
	}

	/*
	 * getGameState()
	 * 
	 * returns gamestate - see GameState enum above
	 */
	
	public GameState getGameState() {
		return gamestate;
	}
	
	/*
	 * isDead()
	 * 
	 * returns true if a bomb has been hit
	 */
	
	public boolean isDead() {
		return gamestate == GameState.DEAD;
	}
	
	/*
	 * hasWon()
	 * 
	 * returns true if the game has been won
	 */
	
	public boolean hasWon() {
		return gamestate == GameState.WON;
	}
	
	/*
	 * isAlive()
	 * 
	 * returns true it no bombs have been hit yet
	 */
	
	public boolean isAlive() {
		return gamestate == GameState.ALIVE;
	}
	
	/*
	 * getTiles()
	 * 
	 * returns the array of tiles
	 */
	
	public int[][] getTiles() {
		return tiles;
	}

	/*
	 * getTileState()
	 * 
	 * returns the array of states - see TileState enum above
	 */
	
	public TileState[][] getTileStates() {
		return states;
	}

	/*
	 * resetBoard(int, int)
	 * 
	 * used to start the game
	 * when the user initially clicks,
	 * this sets up the board and ensures
	 * a 3x3 around the tile the user clicked
	 * has no bombs
	 */
	
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
	
	/*
	 * getBombsAround(int, int)
	 * 
	 * returns the number of bombs around a tile
	 * helper function for resetBoard()
	 */
	
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
	
	/*
	 * leftClick(int, int)
	 * 
	 * left click logic
	 * 
	 * may return prematurely depending on if the
	 * user has hit a bomb or if the board is not
	 * set up yet
	 * 
	 * handles clicks on:
	 *  - previously revealed tiles
	 *  - hidden tiles that:
	 *  	- are not bombs
	 *  	- are bombs
	 */
	
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
			states[x][y] = TileState.VISIBLE;
			return;
		}
		if (tiles[x][y] == 0) {
			makeSectionVisible(x, y);
		} else
			states[x][y] = TileState.VISIBLE;
		gamestate = checkOnlyBombsHidden() ? GameState.WON : gamestate;
	}
	
	/*
	 * checkOnlyBombsHidden()
	 * 
	 * leftClick(int, int) helper function
	 * 
	 * returns true if and only if all tiles
	 * have been revealed besides the bombs
	 *  - in this case, the game is won
	 */
	
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

	/*
	 * rightClick(int, int)
	 * 
	 * right click logic
	 * 
	 * this method:
	 *  - flags tiles
	 *  - unflags tiles
	 *  - keeps track of number of flagged tiles
	 */
	
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

	/*
	 * makeSectionVisible(int, int)
	 * 
	 * this is a recursive function
	 * 
	 * it reveals the coordinate it receives
	 * 
	 * when this function receives
	 * a coordinate for a tile
	 * with no adjacent bombs,
	 * it recursively calls itself
	 * for the 8 tiles surrounding
	 * it
	 * 
	 * net effect: all nearby tiles
	 * with no adjacent bombs, and all
	 * tiles adjacent to these tiles,
	 * are revealed
	 * 
	 * leftClick(int, int) helper function
	 */
	
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
	
	/*
	 * getWidth()
	 * 
	 * returns the width
	 */
	
	public int getWidth() {
		return WIDTH;
	}
	
	/*
	 * getHeight()
	 * 
	 * return the height
	 */
	
	public int getHeight() {
		return HEIGHT;
	}

}
