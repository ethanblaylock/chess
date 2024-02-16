package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Queen {

    /**
     * @return A collection of chess moves possible for
     * the queen given the board and current position.
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        possibleMoves.addAll(lineMovement(board, position, 1, 1));
        possibleMoves.addAll(lineMovement(board, position, -1, 1));
        possibleMoves.addAll(lineMovement(board, position, -1, -1));
        possibleMoves.addAll(lineMovement(board, position, 1, -1));
        possibleMoves.addAll(lineMovement(board, position, 1, 0));
        possibleMoves.addAll(lineMovement(board, position, -1, 0));
        possibleMoves.addAll(lineMovement(board, position, 0, -1));
        possibleMoves.addAll(lineMovement(board, position, 0, 1));
        return possibleMoves;
    }


    /**
     * @param xDir represents a direction. 1 for right and -1 for left
     * @param yDir represents a direction. 1 for up and -1 for down
     * @return the collection of chess moves for that one direction
     */
    public static Collection<ChessMove> lineMovement(ChessBoard board, ChessPosition position, int xDir, int yDir) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        for (int i = 1; i < 8; i++) { /* Checks each position in a direction starting from 1 away */
            ChessPosition newDir = new ChessPosition(row + i*xDir, col + i*yDir);
            if (!ChessMove.isInBounds(newDir)) { /* If move not in bounds we stop considering that direction */
                break;
            } else if (board.getPiece(newDir) == null) { /* Null piece means we continue to check this direction */
                possibleMoves.add(new ChessMove(position, newDir, null));
            } else {
                if (board.getPiece(newDir).getTeamColor() != pieceColor) {
                    // If the piece is on opposite team we include this position but stop checking this direction
                    possibleMoves.add(new ChessMove(position, newDir, null));
                }
                break;
            }
        }
        return possibleMoves;
    }
}
