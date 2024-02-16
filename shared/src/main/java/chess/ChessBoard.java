package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] chessBoard = new ChessPiece[8][8];    /* Creates an 8 by 8 array of pieces */

    public ChessBoard() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.chessBoard[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.chessBoard[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Deletes a chess piece from the chessboard
     *
     * @param position where to delete the piece
     */
    public void deletePiece(ChessPosition position) {
        this.chessBoard[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Creates and returns a cop of the board
     */
    public ChessBoard copyBoard() {
        ChessBoard copy = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy.addPiece(new ChessPosition(i + 1, j + 1), chessBoard[i][j]);
            }
        }
        return copy;
    }

    /**
     * @param position
     * Sets the piece corresponding to that position to moved
     */
    public void setNotMoved(ChessPosition position) {
        chessBoard[position.getRow() - 1][ position.getColumn() - 1].setNotMoved();
    }

    /**
     * @param position
     * @return If the piece at position has been moved or not
     */
    public boolean getNotMoved(ChessPosition position) {
        return chessBoard[position.getRow() - 1][ position.getColumn() - 1].getNotMoved();
    }

    /**
     * @param position
     * Sets the piece corresponding to that position to just double moved
     */
    public void setJustDoubleMoved(ChessPosition position) {
        resetJustDoubleMoved();
        chessBoard[position.getRow() - 1][ position.getColumn() - 1].setJustDoubleMoved(true);
    }

    /**
     * Sets the entire board to have not just double moved
     */
    public void resetJustDoubleMoved() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoard[i][j] != null) {
                    chessBoard[i][j].setJustDoubleMoved(false);
                }
            }
        }
    }

    /**
     * @param position
     * @return If the piece at position has just been double moved or not
     */
    public boolean getJustDoubleMoved(ChessPosition position) {
        return chessBoard[position.getRow() - 1][ position.getColumn() - 1].getJustDoubleMoved();
    }

    /**
     * @return the king's position given a certain team
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoard[i][j] != null) {
                    if (chessBoard[i][j].getPieceType() == ChessPiece.PieceType.KING && chessBoard[i][j].getTeamColor() == teamColor) {
                        return new ChessPosition(i + 1, j + 1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard[i][j] = null; /* Clears all the pieces from the board */
            }
        }

        /* Manually adds the proper pieces to the game board */
        chessBoard[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        chessBoard[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        chessBoard[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        chessBoard[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        chessBoard[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        chessBoard[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        chessBoard[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        chessBoard[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        for (int i = 0; i < 8; i++) {
            chessBoard[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        for (int i = 0; i < 8; i++) {
            chessBoard[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }

        chessBoard[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        chessBoard[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        chessBoard[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        chessBoard[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        chessBoard[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        chessBoard[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        chessBoard[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        chessBoard[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    }
}
