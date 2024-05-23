/**
 *  JSoko - A Java implementation of the game of Sokoban
 *  Copyright (c) 2012 by Matthias Meger, Germany
 *
 *  This file is part of JSoko.
 *
 *	JSoko is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.sokoban_online.jsoko.boardpositions;

import de.sokoban_online.jsoko.board.Board;
import de.sokoban_online.jsoko.solver.SolverGoalRoom;


/**
 * Instances of this class store board configurations during the backwards search
 * within a goal room.
 * <p>
 * This is used only by {@link SolverGoalRoom}.
 * Since during that search also inactive boxes have to be considered,
 * we derived from {@link RelativeBoardPosition}.
 */
public final class RelativeBoardPositionGoalAreaSolver extends RelativeBoardPosition {

	/**
	 * The highest bit of the box number (9 bits) marks an inactive box.
	 */
	protected static final short INACTIVE_BOX = 1 << 9;
	// FFS: implicit limit: document? check?

	/**
	 * @param board  the board of the current level
	 * @param boxNo		  number of the pushed box
	 * @param direction   direction into which the box was pushed
	 * @param precedingBoardPosition reference to the preceding board configuration
	 */
	public RelativeBoardPositionGoalAreaSolver(Board board, int boxNo, int direction, IBoardPosition precedingBoardPosition) {
		super(board, boxNo, direction, precedingBoardPosition);
	}

	/**
	 * Returns the box positions and the player position stored in this board position.
	 * Inactive boxes have a position of 0.
	 *
	 * @return int-array containing all box positions and the player position
	 */
	@Override
	public int[] getPositions() {

		// Ermittelt, welche Stellung in diesem Objekt abgelegt wurde, in dem aus der
		// relativen Stellung wieder eine absolute Stellung errechnet wird
		short[] boxesDifferences = new short[boxCount];

		// Zeigt an, ob eine Kiste deaktiv ist
		boolean[] isBoxInactive = new boolean[boxCount];

		// Nehmen die Kistennr der verschobenen Kiste und die Richtung, in die sie geschoben wurde auf.
		int boxNo;
		int direction;

		IBoardPosition currentBoardPosition = this;

		// Die aktuelle relative Stellung bis zur ersten Stellung durchgehen und
		// alle Kistenverschiebungen "aufaddieren". Die erste Stellung wird daran erkannt,
		// dass sie vom Typ "AbsoluteStellung" ist und nicht vom Typ "RelativeStellung".
		while (currentBoardPosition instanceof RelativeBoardPosition) {
			boxNo = currentBoardPosition.getBoxNo();
			direction = currentBoardPosition.getDirection();

			// Die Kiste wurde in eine bestimmte Richtung geschoben, um die aktuelle
			// Stellung zu erreichen.
			// Die Bewegungen der Kiste werden in Kistendifferenz summiert.
			if ((((RelativeBoardPositionGoalAreaSolver) currentBoardPosition).isBoxInactive())) {
				isBoxInactive[boxNo] = true;
			} else {
				boxesDifferences[boxNo] += board.offset[direction];
			}

			currentBoardPosition = currentBoardPosition.getPrecedingBoardPosition();
		}

		// Die aktuelle Stellung ist eine absolute Stellung. Diese absolute Stellung wird als
		// Ausgangspunkt genommen und alle Verschiebungen der Kisten werden zu diesen Anfangs-
		// positionen "hinzuaddiert".
		int[] boxPositions = currentBoardPosition.getPositions().clone();

		// Für alle Kisten die Position errechnen und speichern.
		for (boxNo = 0; boxNo < boxCount; boxNo++) {

			// Deaktive Kisten bekommen eine Position von 0.
			if (isBoxInactive[boxNo]) {
				boxPositions[boxNo] = 0;
			} else {
				boxPositions[boxNo] += boxesDifferences[boxNo];
			}
		}

		// Die in diesem Objekt gespeicherte Spielerposition mit in das Positionenarray aufnehmen.
		// Diese Position entspricht ja der aktuellen Spielerposition.
		boxPositions[boxCount] = playerPosition;

		return boxPositions;

	}

	/**
	 * Gibt die Kistepositionen zusammen mit der Spielerposition der aktuellen Stellung zurück.
	 * Deaktive Kisten werden mit ihrer tatsächlichen Position zurückgegeben und nicht mit 0!
	 *
	 * @return int-array containing all box positions and the player position
	 */
	public int[] getRealPositions() {

		// Ermittelt, welche Stellung in diesem Objekt abgelegt wurde, in dem aus der
		// relativen Stellung wieder eine absolute Stellung errechnet wird
		short[] boxesDifferences = new short[boxCount];

		// Nehmen die Kistennr der verschobenen Kiste und die Richtung, in die sie geschoben wurde auf.
		int boxNo;
		int direction;

		IBoardPosition currentBoardPosition = this;

		// Die aktuelle relative Stellung bis zur ersten Stellung durchgehen und
		// alle Kistenverschiebungen "aufaddieren". Die erste Stellung wird daran erkannt,
		// dass sie vom Typ "AbsoluteStellung" ist und nicht vom Typ "RelativeStellung".
		while (currentBoardPosition instanceof RelativeBoardPosition) {
			boxNo = currentBoardPosition.getBoxNo();
			direction = currentBoardPosition.getDirection();

			// Die Kiste wurde in eine bestimmte Richtung geschoben, um die aktuelle
			// Stellung zu erreichen.
			// Die Bewegungen der Kiste werden in Kistendifferenz summiert.
			boxesDifferences[boxNo] += board.offset[direction];

			currentBoardPosition = currentBoardPosition.getPrecedingBoardPosition();
		}

		// Die aktuelle Stellung ist eine absolute Stellung. Diese absolute Stellung wird als
		// Ausgangspunkt genommen und alle Verschiebungen der Kisten werden zu diesen Anfangs-
		// positionen "hinzuaddiert".
		int[] boxPositions = currentBoardPosition.getPositions().clone();

		// Für alle Kisten die Position errechnen und speichern.
		for (boxNo = 0; boxNo < boxCount; boxNo++) {
			boxPositions[boxNo] += boxesDifferences[boxNo];
		}

		// Die in diesem Objekt gespeicherte Spielerposition mit in das Positionenarray aufnehmen.
		// Diese Position entspricht ja der aktuellen Spielerposition.
		boxPositions[boxCount] = playerPosition;

		return boxPositions;
	}

	/**
	 *  Kennzeichnet die verschobene Kiste als deaktiv. Bei allen Kisten, die deaktiv sind,
	 *  wird bei der Ermittlung der Positionen aller Kisten eine Position = 0 gesetzt.
	 */
	public void setBoxInactive() {
		positionData |= INACTIVE_BOX;
	}

	/* (non-Javadoc)
	 * @see de.sokoban_online.jsoko.boardpositions.RelativeBoardPosition#getBoxNo()
	 */
	@Override
    public int getBoxNo() {
		return positionData & ((INACTIVE_BOX) - 1);
	}

	/**
	 *  Returns whether the pushed box has been inactive.
	 *
	 * @return <code>true</code> the box is inactive
	 * 			<code>false</code> the box is active
	 */
	public boolean isBoxInactive() {
		return (positionData & INACTIVE_BOX) > 0;
	}
}