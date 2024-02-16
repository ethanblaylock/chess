package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class King {

    /**
     * @return A collection of chess moves possible for
     * the king given the board and current position.
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        int xDir;
        int yDir;
        int[][] kingMoves = {{1, 1} , {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};
        for (int i = 0; i < 8; i++) { /* Checks the different spaces relative to it that it can move */
            xDir = kingMoves[i][0];
            yDir = kingMoves[i][1];
            possibleMoves.addAll(Knight.hopMovement(board, position, xDir, yDir));
        }
        possibleMoves.addAll(castlingMoves(board, position));
        return possibleMoves;
    }

    /**
     *  Determines if the King can castle king or queen side, both, or neither
     * @param board the current board
     * @param position the kings position
     * @return A collection containing 0, 1, or 2 moves
     */
    public static Collection<ChessMove> castlingMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();
        int requiredRow = switch (teamColor) { /* Determines starting row for the given color */
            case WHITE -> 1;
            case BLACK -> 8;
        };
        ChessMove castleKingSide = new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + 2), null);
        if (board.getNotMoved(position) && position.getColumn() == 5 && position.getRow() == requiredRow) { /* Kings must not have been moved */
            ChessPosition rookPosition = new ChessPosition(requiredRow, 8);
            if (board.getPiece(rookPosition) != null) {
                if (board.getNotMoved(rookPosition)) { /* Rook must not have been moved */
                    if (board.getPiece(new ChessPosition(requiredRow, 6)) == null && board.getPiece(new ChessPosition(requiredRow, 7)) == null) { /* There must be no pieces between King and Rook */
                        if (!isInCheck(board, position, teamColor) && !isInCheck(board, new ChessPosition(requiredRow, 6), teamColor) && !isInCheck(board, new ChessPosition(requiredRow, 7), teamColor)) { /* King must not castle out of, through, or into check */
                            possibleMoves.add(castleKingSide);
                        }
                    }
                }

            }
        }

        ChessMove castleQueenSide = new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() - 2), null);
        if (board.getNotMoved(position) && position.getColumn() == 5 && position.getRow() == requiredRow) {
            ChessPosition rookPosition = new ChessPosition(requiredRow, 1);
            if (board.getPiece(rookPosition) != null) {
                if (board.getNotMoved(rookPosition)) {
                    if (board.getPiece(new ChessPosition(requiredRow, 4)) == null && board.getPiece(new ChessPosition(requiredRow, 3)) == null && board.getPiece(new ChessPosition(requiredRow, 2)) == null) {
                        if (!isInCheck(board, position, teamColor) && !isInCheck(board, new ChessPosition(requiredRow, 4), teamColor) && !isInCheck(board, new ChessPosition(requiredRow, 3), teamColor)) {
                            possibleMoves.add(castleQueenSide);
                        }
                    }
                }

            }
        }
        return possibleMoves;
    }

    /**
     * Determines whether a given position is in check
     * @param board the current board
     * @param position a position to be checked
     * @param teamColor the team for whom we are checking for check
     * @return A boolean indicating if the position is in check
     */
    public static boolean isInCheck(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != teamColor) {
                        Collection<ChessMove> pieceMoves = board.getPiece(newPosition).pieceMoves(board, newPosition);
                        for (ChessMove move : pieceMoves) {
                            if (move.getEndPosition().equals(position)) { /* If the opposing team can attack the position, then the position is in check */
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
