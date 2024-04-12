package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MakeBoard {
    public static void makeChessBoard(ChessBoard board, ChessGame.TeamColor playerColor) {
        int flipper = 0;
        if (playerColor == ChessGame.TeamColor.WHITE || playerColor == null) {
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);

            makeOtherLetters();
            for (int i = 8; i >= 1; i--) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                for (int j = 1; j <= 8; j++) {
                    flipper = getFlipper(board, flipper, i, j);
                }
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                System.out.println(EscapeSequences.RESET_BG_COLOR);
                flipper = flipper ^ 1;
            }
            makeOtherLetters();
            System.out.println(EscapeSequences.RESET_TEXT_COLOR);
        } else {
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            System.out.println(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
            makeLetters();
            for (int i = 1; i <= 8; i++) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                for (int j = 8; j >= 1; j--) {
                    flipper = getFlipper(board, flipper, i, j);
                }
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                System.out.println(EscapeSequences.RESET_BG_COLOR);
                flipper = flipper ^ 1;
            }
            makeLetters();
            System.out.println(EscapeSequences.RESET_TEXT_COLOR);
        }
    }

    private static void makeOtherLetters() {
        for (int i = 0; i < 10; i++) {
            letters(i);
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }

    private static void letters(int i) {
        System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
        if (i > 0 && i < 9) {
            char c = (char) (i + 96);
            System.out.print(" " + c + " ");
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
    }

    private static void makeLetters() {
        for (int i = 9; i >= 0; i--) {
            letters(i);
        }

        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }

    private static int getFlipper(ChessBoard board, int flipper, int i, int j) {
        if (flipper == 1) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);

        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        }
        if (board.getPiece(new ChessPosition(i, j)) != null) {
            System.out.print(board.getPiece(new ChessPosition(i, j)));
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
        flipper = flipper ^ 1;
        return flipper;
    }

    public static void highlightMove(ChessBoard board, ChessGame.TeamColor playerColor, Collection<ChessMove> validMoves) {
        Collection<ChessPosition> endPositions = new HashSet<>();
        ChessPosition startingPos = null;
        for (ChessMove move : validMoves) {
            endPositions.add(move.getEndPosition());
            startingPos = move.getStartPosition();
        }
        int flipper = 0;
        if (playerColor == ChessGame.TeamColor.WHITE || playerColor == null) {
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);

            makeOtherLetters();
            for (int i = 8; i >= 1; i--) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                for (int j = 1; j <= 8; j++) {
                    if (flipper == 1) {
                        if (endPositions.contains(new ChessPosition(i, j))) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                        }

                    } else {
                        if (endPositions.contains(new ChessPosition(i, j))) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                    }
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (Objects.equals(startingPos, new ChessPosition(i, j))) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                        }
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                    flipper = flipper ^ 1;
                }
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                System.out.println(EscapeSequences.RESET_BG_COLOR);
                flipper = flipper ^ 1;
            }
            makeOtherLetters();
            System.out.println(EscapeSequences.RESET_TEXT_COLOR);
        } else {
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            System.out.println(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
            makeLetters();
            for (int i = 1; i <= 8; i++) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                for (int j = 8; j >= 1; j--) {
                    if (flipper == 1) {
                        if (endPositions.contains(new ChessPosition(i, j))) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                        }

                    } else {
                        if (endPositions.contains(new ChessPosition(i, j))) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                    }
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (Objects.equals(startingPos, new ChessPosition(i, j))) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                        }
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                    flipper = flipper ^ 1;
                }
                System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
                System.out.print(" " + i + " ");
                System.out.println(EscapeSequences.RESET_BG_COLOR);
                flipper = flipper ^ 1;
            }
            makeLetters();
            System.out.println(EscapeSequences.RESET_TEXT_COLOR);
        }
    }
}
