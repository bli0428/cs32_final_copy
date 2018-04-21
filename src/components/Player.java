package components;

import java.util.Map;
import java.util.Set;

import positions.Position;

public interface Player {
	
	/**
	 * Iterates over this player's pieces and bank and return all possible moves.
	 *
	 * @return
	 */
	Map<Piece, Position> moves();
	
	/**
	 * Return a set containing all pieces in this player's bank.
	 *
	 * @return
	 */
	Set<Piece> bank();
}
