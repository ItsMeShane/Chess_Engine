package pieces;

import main.Board;

public class King extends Piece{

    public King(Board board, boolean isWhite, int x, int y) {
        super(board);
        this.name = 'k';
        this.isWhite = isWhite;
        this.sprite = sprites[isWhite ? wKing : bKing];
        this.xPos = x * board.tileSize;
        this.yPos = y * board.tileSize;
        this.col = x;
        this.row = y;
        this.fenRepresentation = this.isWhite ? 'K' : 'k';

    }

    public boolean validMovement(int col, int row) {
        return (((Math.abs(this.col - col) == 1 || Math.abs(this.col - col) == 0) && (Math.abs(this.row - row) == 1 || Math.abs(this.row - row) == 0)) || validCastleMovement(col, row));
    }
    public boolean validCastleMovement(int col, int row) {
        boolean canCastle;
        boolean kingSideCastle = this.col < col;

        if (kingSideCastle) {
            canCastle = this.isFirstMove &&
                    board.fen.fenString.split(" ")[2].contains(this.isWhite ? "K" : "k") &&
                    Math.abs(this.row - row) == 0 &&
                    Math.abs(this.col - col) == 2 &&
                    board.getPiece(5, this.row) == null &&
                    board.getPiece(6, this.row) == null &&
//                    board.findPiece(this.isWhite, "rook", 2) != null &&
//                    board.findPiece(this.isWhite, "rook", 2).isFirstMove &&
                    !inCheck(5, this.row) &&
                    !inCheck(6, this.row);
        } else {
            canCastle = this.isFirstMove &&
                    board.fen.fenString.split(" ")[2].contains(this.isWhite ? "Q" : "q") &&
                    Math.abs(this.row - row) == 0 &&
                    Math.abs(this.col - col) == 2 &&
                    board.getPiece(1, this.row) == null &&
                    board.getPiece(2, this.row) == null &&
                    board.getPiece(3, this.row) == null &&
//                    board.findPiece(this.isWhite, "rook", 1) != null &&
//                    board.findPiece(this.isWhite, "rook", 1).isFirstMove &&
                    !inCheck(2, this.row) &&
                    !inCheck(3, this.row);

        }


        return canCastle;
    }
}
