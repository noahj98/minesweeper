package minesweeper;

import java.util.Random;

public class MS_Model {

	enum GameState {
		SET_UP, STARTED, WON, DEAD
	}

	private final MS_Model_Tile tiles[][];
	private final int NUM_DESIRED_BOMBS;
	private GameState gamestate;
	private int num_actual_bombs;
	private int number_flagged_tiles;
	private final Random r;

	public MS_Model(final int w, final int h, final int num_desired_bombs) {
		if (w < 1 || h < 1 || num_desired_bombs < 0)
			throw new RuntimeException("illegal width, height, or number of bombs");
		tiles = new MS_Model_Tile[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				tiles[i][j] = new MS_Model_Tile();
			}
		}
		NUM_DESIRED_BOMBS = num_desired_bombs;
		r = new Random();
		makeBoardReadyForClick();
	}

	private void makeBoardReadyForClick() {
		initializeWithClick(-100, -100);
		gamestate = GameState.SET_UP;
	}

	private void initializeWithClick(final int PROTECTED_X, final int PROTECTED_Y) {
		// x and y may not have a bomb within a 3x3 area under any circumstances

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].reset();
			}
		}

		num_actual_bombs = 0;
		int attempts = 0;
		final int MAX_ALLOWED_ATTEMPTS = 25 * NUM_DESIRED_BOMBS;

		while (num_actual_bombs < NUM_DESIRED_BOMBS && attempts < MAX_ALLOWED_ATTEMPTS) {
			attempts++;
			int x = r.nextInt(tiles.length);
			int y = r.nextInt(tiles[0].length);
			if (Math.abs(PROTECTED_X - x) < 2 && Math.abs(PROTECTED_Y - y) < 2)
				continue;
			if (tiles[x][y].isBomb())
				continue;
			tiles[x][y].makeBomb();
			num_actual_bombs++;
		}
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (tiles[i][j].isBomb()) continue;
				tiles[i][j].setNumberSurroundingBombs(getNumSurroundingBombs(i,j));
			}
		}

		gamestate = GameState.STARTED;
		number_flagged_tiles = 0;
	}
	
	private int getNumSurroundingBombs(final int x, final int y) {
		int num = 0;
		for (int i = x-1; i <= x+1; i++) {
			for (int j = y-1; j <= y+1; j++) {
				if (coordinateIsValid(i, j)) num += tiles[i][j].isBomb() ? 1 : 0;
			}
		}
		return num;
	}

	public void leftClick(final int x, final int y) {
		if (!coordinateIsValid(x, y)) return;

		switch (gamestate) {
		case WON:
		case DEAD:		makeBoardReadyForClick();
						return;
		case SET_UP:	initializeWithClick(x, y);
		case STARTED:
		default:
		}
		
		if (tiles[x][y].isFlagged()) return;
		
		if (tiles[x][y].isBomb()) {
			tiles[x][y].reveal();
			gamestate = GameState.DEAD;
			return;
		}
		
		revealSection(x, y);
		checkIfWon();
	}
	
	private void checkIfWon() {
		for (int i = 0; i < tiles.length; i++)
			for (int j = 0; j < tiles[0].length; j++)
				if (!tiles[i][j].isRevealed())
					if (!tiles[i][j].isBomb())
						return;
		gamestate = GameState.WON;
	}
	
	private void revealSection(int x, int y) {
		if (!coordinateIsValid(x, y)) return;
		
		if (tiles[x][y].isRevealed()) return;
		if (tiles[x][y].isFlagged()) number_flagged_tiles--;
		tiles[x][y].reveal();
		
		if (!tiles[x][y].isEmpty()) return;
		
		for (int i = x-1; i <= x+1; i++) {
			for (int j = y-1; j <= y+1; j++) {
				if (x == i && y == j) continue;
				revealSection(i, j);
			}
		}
	}

	public void rightClick(final int x, final int y) {
		if (!coordinateIsValid(x, y)) return;

		switch (gamestate) {
		case WON:
		case DEAD:		makeBoardReadyForClick();
		case SET_UP:	return;
		case STARTED:
		default:
		}
		
		if (tiles[x][y].isRevealed())
			return;
		else if (tiles[x][y].isFlagged()) {
			tiles[x][y].unFlag();
			number_flagged_tiles--;
		} else {
			tiles[x][y].flag();
			number_flagged_tiles++;
		}
	}

	private boolean coordinateIsValid(int x, int y) {
		return (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length);
	}

	public int getNumUnflaggedBombs() {
		return num_actual_bombs - number_flagged_tiles;
	}
	
	public int getNumBombs() {
		return num_actual_bombs;
	}
	
	public MS_Model_Tile[][] getTiles() {
		return tiles;
	}
	
	public int getWidth() {
		return tiles.length;
	}
	
	public int getHeight() {
		return tiles[0].length;
	}
	
	public GameState getGameState() {
		return gamestate;
	}
	
	public boolean isDead() {
		return gamestate == GameState.DEAD;
	}
	
	public boolean isBlank() {
		return gamestate == GameState.SET_UP;
	}
	
}
