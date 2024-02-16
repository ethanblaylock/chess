package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Knight {

    /**
     * @return A collection of chess moves possible for
     * the knight given the board and current position.
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        int xDir;
        int yDir;
        int[][] knightMoves = {{2, 1} , {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        for (int i = 0; i < 8; i++) { /* Checks the different spaces relative to it that it can move */
            xDir = knightMoves[i][0];
            yDir = knightMoves[i][1];
            possibleMoves.addAll(hopMovement(board, position, xDir, yDir));
        }
        return possibleMoves;
    }

    /**
     * @param xDir represents number of spaces in a direction.
     * @param yDir represents number of spaces in a direction.
     * @return the collection with the chess move for that one hop
     */
    public static Collection<ChessMove> hopMovement(ChessBoard board, ChessPosition position, int xDir, int yDir) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        ChessPosition newDir = new ChessPosition(row + xDir, col + yDir);
        if (!ChessMove.isInBounds(newDir)) { /* If move not in bounds we stop considering that hop */
                return possibleMoves;
        } else if (board.getPiece(newDir) == null) { /* Null piece means we add this hop */
            possibleMoves.add(new ChessMove(position, newDir, null));
        } else {
            if (board.getPiece(newDir).getTeamColor() != pieceColor) {
                // If the piece is on opposite team we still include this hop
                possibleMoves.add(new ChessMove(position, newDir, null));
            }

        }

        return possibleMoves;
    }
}
