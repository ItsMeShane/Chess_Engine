package pieces;

import main.Board;

public class Pawn extends Piece{

    public Pawn(Board board, boolean isWhite, int x, int y) {
        super(board);
        this.name = 'p';
        this.val = board.pawnVal;
        this.isWhite = isWhite;
        this.sprite = sprites[isWhite ? wPawn : bPawn];
        this.xPos = x * board.tileSize;
        this.yPos = y * board.tileSize;
        this.col = x;
        this.row = y;
        this.colorVal = this.isWhite ? -1 : 1;
        this.fenRepresentation = this.isWhite ? 'P' : 'p';

    }

    public boolean validMovement(int col, int row) {

        Piece pieceXY = board.getPiece(col, row);

        // push pawn 1
        if (this.row + colorVal == row && pieceXY == null && this.col == col) {
            return true;
        }

        // push pawn 2
        if ((this.isWhite ? this.row == 6 : this.row == 1) && this.isFirstMove && this.row + colorVal * 2 == row && pieceXY == null && this.col == col && board.getPiece(this.col, this.row + colorVal) == null) {
            return true;
        }

        // capture
        if (this.row + colorVal == row && pieceXY != null && pieceXY.isWhite != this.isWhite && Math.abs(this.col - col) == 1) {
            return true;
        }

        // en passant
        if (
                board.enPassantCol == col && board.enPassantRow == row &&
                this.row + this.colorVal == row &&
                Math.abs(this.col - col) == 1 &&
//                board.enPassantCol == col &&
//                board.enPassantRow == row &&
                board.getPiece(col, row - colorVal) != null &&
                board.getPiece(col, row - colorVal).isWhite != this.isWhite) {
//            System.out.println("EN PASSANTED!");
//            System.out.println(board.location(board.enPassantCol, board.enPassantRow));
//            System.out.println();
            return true;
        }


//        System.out.println(board.enPassantCol == col && board.enPassantRow == row);
//        System.out.println(this.row + this.colorVal == row);
//        System.out.println(Math.abs(this.col - col) == 1);
//        System.out.println(board.getPiece(col, row - colorVal) != null);
//        System.out.println(board.getPiece(col, row - colorVal).isWhite != this.isWhite);
//        System.out.println();



        return false;
    }

}
