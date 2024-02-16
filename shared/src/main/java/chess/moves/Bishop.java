package chess.moves;

import chess.*;
import java.util.*;

public class Bishop {

    /**
     * @return A collection of chess moves possible for
     * the bishop given the board and current position.
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        possibleMoves.addAll(Queen.lineMovement(board, position, 1, 1));
        possibleMoves.addAll(Queen.lineMovement(board, position, -1, 1));
        possibleMoves.addAll(Queen.lineMovement(board, position, -1, -1));
        possibleMoves.addAll(Queen.lineMovement(board, position, 1, -1));
        int j =  0;
        return possibleMoves;
    }

}
