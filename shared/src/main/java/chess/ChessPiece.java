package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import chess.moves.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType type;
    private boolean notMoved;
    private boolean justDoubleMoved;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        notMoved = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Indicates that a piece has been moved from its initial position
     */
    public void setNotMoved() {
        notMoved = false;
    }

    /**
     * @return Whether or not a piece has been moved from its initial position
     */
    public boolean getNotMoved() {
        return notMoved;
    }

    /**
     * Indicates that a piece has just been double moved from its initial position
     */
    public void setJustDoubleMoved(boolean bool) {
        justDoubleMoved = bool;
    }

    /**
     * @return Whether or not a piece has just been double moved from its initial position
     */
    public boolean getJustDoubleMoved() {
        return justDoubleMoved;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<>();

        switch (board.getPiece(myPosition).getPieceType()) {
            case BISHOP:
                possibleMoves.addAll(Bishop.pieceMoves(board, myPosition));
                break;
            case ROOK:
                possibleMoves.addAll(Rook.pieceMoves(board, myPosition));
                break;
            case KNIGHT:
                possibleMoves.addAll(Knight.pieceMoves(board, myPosition));
                break;
            case KING:
                possibleMoves.addAll(King.pieceMoves(board, myPosition));
                break;
            case QUEEN:
                possibleMoves.addAll(Queen.pieceMoves(board, myPosition));
                break;
            case PAWN:
                possibleMoves.addAll(Pawn.pieceMoves(board, myPosition));
                break;
        }
        return possibleMoves;
    }
}
