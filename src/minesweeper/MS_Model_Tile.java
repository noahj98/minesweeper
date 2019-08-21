package minesweeper;

public class MS_Model_Tile {
	
	private enum TileState {
		HIDDEN, VISIBLE, FLAGGED
	}
	
	private enum TileType {
		EMPTY, BOMB,
		ONE, TWO,
		THREE, FOUR,
		FIVE, SIX,
		SEVEN, EIGHT
	}
	
	private TileState tile_state;
	private TileType tile_type;
	
	public MS_Model_Tile() {
		reset();
	}
	
	public void reset() {
		tile_state = TileState.HIDDEN;
		tile_type = TileType.EMPTY;
	}
	
	public void makeBomb() {
		tile_type = TileType.BOMB;
	}
	
	public boolean isBomb() {
		return tile_type == TileType.BOMB;
	}
	
	public boolean isEmpty() {
		return tile_type == TileType.EMPTY;
	}
	
	public void setNumberSurroundingBombs(int n) {
		if (tile_type == TileType.BOMB) return; 
		switch (n) {
		case 1:		tile_type = TileType.ONE;
					break;
		case 2:		tile_type = TileType.TWO;
					break;
		case 3:		tile_type = TileType.THREE;
					break;
		case 4:		tile_type = TileType.FOUR;
					break;
		case 5:		tile_type = TileType.FIVE;
					break;
		case 6:		tile_type = TileType.SIX;
					break;
		case 7:		tile_type = TileType.SEVEN;
					break;
		case 8:		tile_type = TileType.EIGHT;
		default:
		}
	}
	
	public String getNumberSurroundingBombs() {
		switch (tile_type) {
		case ONE:		return "1";
		case TWO:		return "2";
		case THREE:		return "3";
		case FOUR:		return "4";
		case FIVE:		return "5";
		case SIX:		return "6";
		case SEVEN:		return "7";
		case EIGHT:		return "8";
		default:		return null;
		}
	}
	
	public boolean isRevealed() {
		return tile_state == TileState.VISIBLE;
	}
	
	public void reveal() {
		tile_state = TileState.VISIBLE;
	}
	
	public void flag() {
		tile_state = TileState.FLAGGED;
	}
	
	public boolean isFlagged() {
		return tile_state == TileState.FLAGGED;
	}
	
	public void unFlag() {
		tile_state = TileState.HIDDEN;
	}

}
