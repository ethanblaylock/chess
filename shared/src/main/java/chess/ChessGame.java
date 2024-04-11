package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    private ChessBoard tempBoard;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board) && Objects.equals(tempBoard, chessGame.tempBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board, tempBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return teamTurn; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { teamTurn = team; }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        } else {
            ChessPiece piece = board.getPiece(startPosition);
            Collection<ChessMove> moves = new HashSet<>();
            for (ChessMove move : piece.pieceMoves(board, startPosition)) { /* For each move checks if it causes a check */
                tempBoard = board.copyBoard();
                board.addPiece(move.getEndPosition(), piece);
                board.deletePiece(move.getStartPosition());
                if (isInCheck(piece.getTeamColor())) {
                    board = tempBoard;
                } else {
                    board = tempBoard;
                    moves.add(move);
                }
            }
            return moves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (!validMoves(move.getStartPosition()).contains(move)) { /* Checks for invalid move */
            throw new InvalidMoveException("Not valid move");
        }
        else if (piece.getTeamColor() != teamTurn) { /* Checks for wrong turn */
            throw new InvalidMoveException("Not team's turn");
        } else {
            /* tempBoard is used to evaluate the validity of move by executing it on a copy of the board */
            tempBoard = board;
            ChessPiece.PieceType newPieceType = piece.getPieceType();
            if (move.getPromotionPiece() != null && move.getPromotionPiece() != ChessPiece.PieceType.KING) { /* Deals with pawn promotion */
                newPieceType = move.getPromotionPiece();
            }

            /* Determines whether a move is a castling move, so that it can execute castling operation */
            if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING && Math.abs(move.getStartPosition().getColumn() - move.getEndPosition().getColumn()) > 1) {
                boolean kingSide = move.getEndPosition().getColumn() >= 5;
                if (kingSide) {
                    board.addPiece(new ChessPosition(move.getStartPosition().getRow(), 6), new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.ROOK));
                    board.deletePiece(new ChessPosition(move.getStartPosition().getRow(), 8));
                    board.setNotMoved(new ChessPosition(move.getStartPosition().getRow(), 6));
                } else {
                    board.addPiece(new ChessPosition(move.getStartPosition().getRow(), 4), new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.ROOK));
                    board.deletePiece(new ChessPosition(move.getStartPosition().getRow(), 1));
                    board.setNotMoved(new ChessPosition(move.getStartPosition().getRow(), 4));
                }
            }

            /* Determines whether a move is an en passant move and executes it */
            if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(move.getEndPosition()) == null) {
                board.deletePiece(new ChessPosition(move.getStartPosition().getRow(), move.getEndPosition().getColumn()));
            }

            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), newPieceType));
            board.deletePiece(move.getStartPosition());
            board.setNotMoved(move.getEndPosition());
            board.resetJustDoubleMoved();

            if (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
                if (Math.abs(move.getStartPosition().getRow() - move.getEndPosition().getRow()) > 1) {
                    board.setJustDoubleMoved(move.getEndPosition());
                }
            }

            if (isInCheck(piece.getTeamColor())) { /* Checks if move moved into check*/
                board = tempBoard;
                throw new InvalidMoveException("Cannot move into check");
            } else { /* Executes the move on the main board and swaps turns */
                swapTurns();
            }
        }
    }

    /**
     * Swaps the team whose turn it is
     */
    public void swapTurns() {
        TeamColor currentTeam = getTeamTurn();
        switch(currentTeam) {
            case WHITE:
                setTeamTurn(TeamColor.BLACK);
                break;
            case BLACK:
                setTeamTurn(TeamColor.WHITE);
                break;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) != null) {
                    if (board.getPiece(position).getTeamColor() != teamColor) {
                        Collection<ChessMove> pieceMoves = board.getPiece(position).pieceMoves(board, position);
                        for (ChessMove move : pieceMoves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        Collection<ChessPosition> checkPositions = new HashSet<>();
        for (int i = 1; i <= 8; i++) { /* This loop builds the Hashset of pieces currently checking the king */
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) != null) {
                    if (board.getPiece(position).getTeamColor() != teamColor) {
                        Collection<ChessMove> pieceMoves = validMoves(position);
                        for (ChessMove move : pieceMoves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                checkPositions.add(move.getStartPosition());
                            }
                        }
                    }
                }
            }
        }
        for (int i = 1; i <= 8; i++) { /* This loop tries to eliminate pieces currently checking the king */
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) != null) {
                    if (board.getPiece(position).getTeamColor() == teamColor) {
                        Collection<ChessMove> pieceMoves = validMoves(position);
                        for (ChessMove move : pieceMoves) {
                            checkPositions.remove(move.getEndPosition());
                        }
                    }
                }
            }
        }
        return !checkPositions.isEmpty(); /* If there are still positions that check the king, it is checkmate */
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allMoves = new HashSet<>();
        for (int i = 1; i <= 8; i++) { /* Puts together a HashSet containing all the moves for a team */
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) != null) {
                    if (board.getPiece(position).getTeamColor() == teamColor) {
                        allMoves.addAll(validMoves(position));
                    }
                }
            }
        }
        return allMoves.isEmpty(); /* If there are no moves a team can make, then it is stalemate */
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { this.board = board; }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return board; }
}
