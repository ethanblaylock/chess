import chess.*;
import ui.EscapeSequences;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client. Type Help to get started. ♕ \n" );
        String currentState = "[LOGGED OUT] >>> ";
        Scanner scanner = new Scanner(System.in);
        boolean quitProgram = false;
        while (!quitProgram) {
            System.out.print(currentState);
            String command = scanner.nextLine();
            command = command.toLowerCase();
            switch(command) {
                case "help":
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                    System.out.print("  register ");
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                    System.out.println("- to create an account");
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                    System.out.print("  login ");
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                    System.out.println("- to play chess");
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                    System.out.print("  quit ");
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                    System.out.println("- playing chess");
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                    System.out.print("  help ");
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                    System.out.println("- with possible commands");
                    System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                    break;
                case "quit":
                    quitProgram = true;
                    System.out.print("  Thank you for playing!");
                    break;
                case "login":
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    System.out.println("asfdsa");
                    System.out.print(EscapeSequences.RESET_TEXT_COLOR);

                    break;
                case "register":
                    ChessBoard board = new ChessBoard();
                    board.resetBoard();
                    makeChessBoard(board);
                    break;
            }
        }
    }


    static void makeChessBoard(ChessBoard board) {
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