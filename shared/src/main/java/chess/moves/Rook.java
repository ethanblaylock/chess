package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Rook {

    /**
     * @return A collection of chess moves possible for
     * the rook given the board and current position.
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        possibleMoves.addAll(Queen.lineMovement(board, position, 1, 0));
        possibleMoves.addAll(Queen.lineMovement(board, position, -1, 0));
        possibleMoves.addAll(Queen.lineMovement(board, position, 0, -1));
        possibleMoves.addAll(Queen.lineMovement(board, position, 0, 1));
        return possibleMoves;
    }
}
