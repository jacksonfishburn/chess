package models;

import chess.ChessGame;

public record GameData( int gameID,
                        String whiteUserName,
                        String blackUserName,
                        String gameName,
                        ChessGame game )
{}
