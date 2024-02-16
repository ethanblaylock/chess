package model;

import chess.ChessGame;

/**
 * A record containing information for a chess game
 * @param gameID the game's unique ID
 * @param whiteUsername the white player's username
 * @param blackUsername the black player's username
 * @param gameName the name pertaining to the game
 * @param game a ChessGame object containing the specific game info
 */
record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}
