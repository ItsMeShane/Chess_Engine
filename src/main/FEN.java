package main;

import pieces.*;

import java.util.ArrayList;

public class FEN {

    Board board;

//    public final String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public final String fenStartingPosition = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR w KQkq - 0 1";
//public final String fenStartingPosition = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";

//    public final String fenStartingPosition = "3r4/pR2N3/2pkb3/5p2/8/2B5/qP3PPP/4R1K1 w - - 1 0";
//    public final String fenStartingPosition = "8/8/3K4/8/8/4k3/8/q7 w - - 0 1";
//    public final String fenStartingPosition = "8/3K4/7k/8/8/8/pppppppp/8 w - - 0 1";

    public String fenString = fenStartingPosition;
    int halfMoves = 0;

    public FEN(Board board) {
        this.board = board;
    }

    public void loadFEN(String fenString) {
        board.pieceList = new ArrayList<>();
        String pieces = fenString.split(" ")[0];

        int x = 0;
        int y = 0;

        for (String row : pieces.split("/")) {
            for (String col : row.split("")) {

                boolean isPieceWhite = Character.isUpperCase(col.charAt(0));
                if (Character.isDigit(col.charAt(0))) x += Integer.parseInt(col) - 1;

                if (col.equalsIgnoreCase("p")) board.pieceList.add(new Pawn(board, isPieceWhite, x, y));
                if (col.equalsIgnoreCase("n")) board.pieceList.add(new Knight(board, isPieceWhite, x, y));
                if (col.equalsIgnoreCase("b")) board.pieceList.add(new Bishop(board, isPieceWhite, x, y));
                if (col.equalsIgnoreCase("r")) board.pieceList.add(new Rook(board, isPieceWhite, x, y));
                if (col.equalsIgnoreCase("q")) board.pieceList.add(new Queen(board, isPieceWhite, x, y));
                if (col.equalsIgnoreCase("k")) board.pieceList.add(new King(board, isPieceWhite, x, y));
                x++;
            }
            y++;
            x = 0;
        }

        board.w2m = fenString.split(" ")[1].equalsIgnoreCase("w");
    }

    public void generateFEN() {
        fenString = "";

        // find fen pieces
        int nullTileCount = 0;
        for (int r = 0; r < 8; r++) {
            for (int file = 0; file < 8; file++) {
                Piece pieceXY = board.getPiece(file, r);
                if (pieceXY != null && board.getPiece(file - 1, r) == null) {
                    if (nullTileCount != 0) {
                        fenString += nullTileCount;
                    }
                    nullTileCount = 0;
                }
                if (pieceXY != null) {
                    fenString += board.getPiece(file, r).fenRepresentation;
                } else {
                    nullTileCount++;
                }
            }
            if (nullTileCount != 0) {
                fenString += nullTileCount;
            }
            if (r < 7)
                fenString += "/";
            else
                fenString += " ";
            nullTileCount = 0;
        }

        fenString += board.w2m ? "w " : "b ";

        String castles = "";

        Piece wKing = board.findKing(true);
        if (wKing != null) {
            if (wKing.isFirstMove) {
                Piece kRook = board.getPiece(7, 7);
                if (kRook != null && kRook.name == 'r' && wKing.col == 4 && kRook.isFirstMove) {
                    castles += "K";
                }
                Piece qRook = board.getPiece(0, 7);
                if (qRook != null && qRook.name == 'r' && wKing.col == 4 && qRook.isFirstMove) {
                    castles += "Q";
                }
            }
        }
        Piece bKing = board.findKing(false);
        if (bKing != null) {
            if (bKing.isFirstMove) {
                Piece kRook = board.getPiece(7, 0);
                if (kRook != null && kRook.name == 'r' && bKing.col == 4 && kRook.isFirstMove) {
                    castles += "k";
                }
                Piece qRook = board.getPiece(0, 0);
                if (qRook != null && qRook.name == 'r' && bKing.col == 4 && qRook.isFirstMove) {
                    castles += "q";
                }
            }
        }
        if (castles.equals("")) {
            castles = "-";
        }
        fenString += castles + " ";

        String enPassant = "";
        if (board.enPassantCol != -1) {
            enPassant += board.location(board.enPassantCol, board.enPassantRow);
        } else {
            enPassant += "-";
        }
        fenString += enPassant + " ";

        board.fenStack.push(fenString);

    }

}













