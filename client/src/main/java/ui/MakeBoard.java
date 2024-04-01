package ui;

import chess.ChessBoard;
import chess.ChessPosition;

public class MakeBoard {
    public static void makeChessBoard(ChessBoard board) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
        int flipper = 0;
        for (int i = 0; i < 10; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int i = 8; i >= 1; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            for (int j = 1; j <= 8; j++) {
                if (flipper == 1) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                }
                flipper = flipper ^ 1;
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            flipper = flipper ^ 1;
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }

        System.out.println(EscapeSequences.RESET_BG_COLOR);
        System.out.println(EscapeSequences.RESET_BG_COLOR);

        for (int i = 9; i >= 0; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            for (int j = 8; j >= 1; j--) {
                if (flipper == 1) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                }
                flipper = flipper ^ 1;
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            flipper = flipper ^ 1;
        }
        for (int i = 9; i >= 0; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }

        System.out.println(EscapeSequences.RESET_BG_COLOR);
        System.out.println(EscapeSequences.RESET_TEXT_COLOR);
    }

}
