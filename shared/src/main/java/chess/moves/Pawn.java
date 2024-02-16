package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Pawn {

    /**
     * @return A collection of chess moves possible for
     * the pawn given the board and current position.
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();
        int movementDir = 1; /* movementDir is 1 for White and -1 for Black, meaning forward and backwards */
        if (teamColor == ChessGame.TeamColor.BLACK) {
            movementDir = -1;
        }

        /* left and right capture positions are the possible positions the pawn could move in order to capture */
        ChessPosition leftCapturePosition = new ChessPosition(row + movementDir, col - 1);
        ChessPosition rightCapturePosition = new ChessPosition(row + movementDir, col + 1);

        /* Adds the potential straight moves and captures moves for pawn */
        possibleMoves.addAll(addStraightMoves(board, position, movementDir));
        possibleMoves.addAll(addCaptureMoves(board, position, leftCapturePosition));
        possibleMoves.addAll(addCaptureMoves(board, position, rightCapturePosition));

        return possibleMoves;
    }

    /**
     * @return a boolean of whether the pawn is at
     * the end of board given the teamColor
     */
    public static boolean canPawnPromote(ChessGame.TeamColor teamColor, ChessPosition endPosition) {
        if (teamColor == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) {
            return true;
        }
        else if (teamColor == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1){
            return true;
        }
        return false;
    }

    /**
     * @return A collection of moves that include all
     * possible promotionPieces for a start and end
     * position
     */
    public static Collection<ChessMove> addPromotionMoves(ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        possibleMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        return possibleMoves;
    }

    /**
     * @return A collection of one move if the
     * requirements for a pawn capture are fulfilled
     */
    public static Collection<ChessMove> addCaptureMoves(ChessBoard board, ChessPosition position, ChessPosition endPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();
        if (!ChessMove.isInBounds(endPosition)) { /* If move not in bounds we stop considering this move*/
            return possibleMoves;
        }
        if (board.getPiece(endPosition) != null) { /* Checking if there is a piece on the position */
            if (board.getPiece(endPosition).getTeamColor() != teamColor) { /* If not on team we can capture */
                if (canPawnPromote(teamColor, endPosition)) { /* If we move into last row we include promotion moves */
                    possibleMoves.addAll(addPromotionMoves(position, endPosition));
                } else {
                    possibleMoves.add(new ChessMove(position, endPosition, null));
                }
            }
        }
        possibleMoves.addAll(addEnPassantMoves(board, position));
        return possibleMoves;
    }

    /**
     * @return A collection of one or two
     * moves for the straight movement of the pawn
     */
    public static Collection<ChessMove> addStraightMoves(ChessBoard board, ChessPosition position, int movementDir) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();
        /* Defines a position one space forward for pawn */
        ChessPosition forwardPosition = new ChessPosition(position.getRow() + movementDir, position.getColumn());
        if (board.getPiece(forwardPosition) == null) { /* If space is occupied we stop considering this move */
            if (canPawnPromote(teamColor, forwardPosition)) {
                possibleMoves.addAll(addPromotionMoves(position,  forwardPosition));
            } else {
                possibleMoves.add(new ChessMove(position, forwardPosition, null));
            }
        } else {
            return possibleMoves;
        }
        /* These check if pawn is on starting line, allowing it to move two spaces */
        if (teamColor == ChessGame.TeamColor.WHITE && position.getRow() == 2) {
            ChessPosition newDir2 = new ChessPosition(position.getRow() + 2*movementDir, position.getColumn());
            if (board.getPiece(newDir2) == null) {
                possibleMoves.add(new ChessMove(position, newDir2, null));
            }
        }
        else if (teamColor == ChessGame.TeamColor.BLACK && position.getRow() == 7) {
            ChessPosition newDir2 = new ChessPosition(position.getRow() + 2*movementDir, position.getColumn());
            if (board.getPiece(newDir2) == null) {
                possibleMoves.add(new ChessMove(position, newDir2, null));
            }
        }
        return possibleMoves;
    }

    public static Collection<ChessMove> addEnPassantMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();

        int requiredRow = switch (teamColor) { /* Determines needed row for move given the color */
            case WHITE -> 5;
            case BLACK -> 4;
        };

        ChessMove enPassantLeft = switch (teamColor) { /* Determines needed row for move given the color */
            case WHITE -> new ChessMove(position, new ChessPosition(6, position.getColumn() - 1), null);
            case BLACK -> new ChessMove(position, new ChessPosition(3, position.getColumn() - 1), null);
        };

        ChessMove enPassantRight = switch (teamColor) { /* Determines needed row for move given the color */
            case WHITE -> new ChessMove(position, new ChessPosition(6, position.getColumn() + 1), null);
            case BLACK -> new ChessMove(position, new ChessPosition(3, position.getColumn() + 1), null);
        };

        ChessPosition leftPosition = new ChessPosition(requiredRow, position.getColumn() - 1);
        ChessPosition rightPosition = new ChessPosition(requiredRow, position.getColumn() + 1);

        if (position.getRow() == requiredRow && leftPosition.getColumn() >= 1) {
            if (board.getPiece(leftPosition) != null) {
                if (board.getPiece(leftPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(leftPosition).getTeamColor() != teamColor) {
                    if (board.getJustDoubleMoved(leftPosition)) {
                        possibleMoves.add(enPassantLeft);
                    }
                }
            }
        }

        if (position.getRow() == requiredRow && rightPosition.getColumn() <= 8) {
            if (board.getPiece(rightPosition) != null) {
                if (board.getPiece(rightPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(rightPosition).getTeamColor() != teamColor) {
                    if (board.getJustDoubleMoved(rightPosition)) {
                        possibleMoves.add(enPassantRight);
                    }
                }
            }
        }

        return possibleMoves;
    }
}
