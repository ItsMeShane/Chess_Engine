package main;

import pieces.Piece;

public class Move {
    Piece piece;
    int col;
    int row;

    int oldCol;
    int oldRow;

    String location;
    String description;

    Piece capture;
    Piece promo;

    public Move(Piece piece, int col, int row, Piece capture, Piece promo) {
        this.piece = piece;
        this.promo = promo;
        this.col = col;
        this.row = row;
        this.location = piece.board.location(col, row);
        this.description = (piece.isWhite ? "w " : "b ") + piece.name + " " + location;
        this.capture = capture;

        this.oldCol = piece.col;
        this.oldRow = piece.row;

    }


}







