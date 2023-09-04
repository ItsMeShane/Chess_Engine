package main;

import pieces.Piece;

public class CheckScanner {

    Board board;

    public CheckScanner (Board board) {
        this.board = board;
    }

    public boolean isKingChecked(int col, int row, boolean isWhite) {
        Piece king = board.findKing(isWhite);
        assert king != null;

        // sets kingCol/Row to kings location on board
        int kingCol = king.col;
        int kingRow = king.row;
        if (board.selectedPiece != null && board.selectedPiece.name == 'k') {
            kingCol = col;
            kingRow = row;
        }

        return ScanStraight(col, row, king, kingCol, kingRow, 0, +1) ||    // up
                ScanStraight(col, row, king, kingCol, kingRow, +1, 0) ||   // right
                ScanStraight(col, row, king, kingCol, kingRow, 0, -1) ||   // down
                ScanStraight(col, row, king, kingCol, kingRow, -1, 0) ||   // left

                scanDiagonally(col, row, king, kingCol, kingRow, -1, -1) ||   // up left
                scanDiagonally(col, row, king, kingCol, kingRow, +1, -1) ||   // up right
                scanDiagonally(col, row, king, kingCol, kingRow, +1, +1) ||   // down right
                scanDiagonally(col, row, king, kingCol, kingRow, -1, +1) ||     // down left

                scanForKnight(col, row, king, kingCol, kingRow) ||
                scanForKing(king, kingCol, kingRow) ||
                scanForPawn(col, row, king, kingCol, kingRow);
    }


    public boolean ScanStraight(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        for (int offset = 1; offset < board.boardSize; offset++) {
            // if tile to search point == new location point
            if (kingCol + (offset * colVal) == col && kingRow + (offset * rowVal) == row) {
                break;
            }
            Piece p = board.getPiece(kingCol + (offset * colVal), kingRow + (offset * rowVal));
            if (p != null && p != board.selectedPiece) {
                if (!board.sameTeam(p, king) && (p.name == 'r' || p.name == 'q')) {
                    return true;
                }
                break;
            }
        }
        return false;
    }


    public boolean scanDiagonally(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        for (int offset = 1; offset < board.boardSize; offset++) {
            // if tile to search point == new location point
            if (kingCol - (offset * colVal) == col && kingRow - (offset * rowVal) == row) {
                break;
            }
            Piece p = board.getPiece(kingCol - (offset * colVal), kingRow - (offset * rowVal));
            if (p != null && p != board.selectedPiece) {
                if (!board.sameTeam(p, king) && (p.name == 'b' || p.name == 'q')) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public boolean scanForKnight(int col, int row, Piece king, int kingCol, int kingRow) {
        // different knight patterns
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }
    private boolean checkKnight(Piece p, Piece king, int col, int row) {
        return p != null && !board.sameTeam(p, king) && p.name == 'n' && !(p.col == col && p.row == row);
    }


    public boolean scanForKing(Piece king, int kingCol, int kingRow) {
        // different king patterns
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }
    private boolean checkKing(Piece p, Piece king) {
        return p != null && !board.sameTeam(p, king) && p.name == 'k';
    }

    public boolean scanForPawn(int col, int row, Piece king, int kingCol, int kingRow) {
        int colorVal = king.isWhite ? -1 : 1;
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, col, row);
    }
    private boolean checkPawn(Piece p, Piece king, int col, int row) {
        return p != null && !board.sameTeam(p, king) && p.name == 'p' && !(p.col == col && p.row == row);
    }

    /**
     * <li>find total valid moves in position</li>
     * <li>if 0 valid moves then checkmate</li>
     */

    public boolean noValidMoves(boolean isWhite) {
        Piece king = board.findKing(isWhite);
        if (king != null) {
            for (Piece piece : board.pieceList) {
                board.selectedPiece = piece == king ? king : null;
                if (board.sameTeam(piece, king)) {
                    for (int row = 0; row < board.rows; row++) {
                        for (int col = 0; col < board.cols; col++) {
                            Piece pieceXY = board.getPiece(col, row);
                            if (piece.canMakeMove(pieceXY, col, row)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


}










