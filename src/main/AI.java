package main;

import pieces.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

import static main.Main.ai_verses_ai;

public class AI {

    Board board;
    public final int positiveInfinity = 9999999;
    public final int negativeInfinity = -positiveInfinity;

    public final int depth = 3;

    public AI(Board board) {
        this.board = board;
    }


    public void aiMove() {
        if (true)
            if (ai_verses_ai || !board.w2m) {
                if (ai_verses_ai) {
                    board.repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                makeMove();

            }
    }


    public void makeMove() {


        ArrayList<Move> validMoves = findValidMoves();

        if (validMoves.size() < 1) {
            board.repaint();
            System.out.println(board.w2m ? "Black" : "White" + " wins!");
            JOptionPane.showMessageDialog(null, board.w2m ? "Black" : "White" + " wins!", "Winner!", JOptionPane.INFORMATION_MESSAGE);

            return;
        }


        Random rng = new Random();
        Move chosenMove = validMoves.get(rng.nextInt(validMoves.size()));


        int low = Integer.MAX_VALUE;
        for (Move move : validMoves) {
            board.makeMove(move);
            int search = search(depth, negativeInfinity, positiveInfinity);
            if (search < low) {
                chosenMove = move;
                low = search;
            }
            board.unMakeMove(move);
        }


        board.makeMove(chosenMove);

        String gameState = board.getGameState(board.w2m);

        if (!gameState.equals("Continue")) {
            board.gameState = gameState;
        }
        board.repaint();
        aiMove();
    }



    int search(int depth, int alpha, int beta) {
        if (depth == 0) {
            return evaluate();
        }

        ArrayList<Move> moves = findValidMoves();
        if (moves.size() == 0) {
            Piece king = board.findKing(board.w2m);
            if (king.inCheck(king.col, king.row)) {
                return Integer.MIN_VALUE;
            }
            return 0;
        }

        for (Move move : moves) {

            board.makeMove(move);
            int evaluation = -search(depth - 1, -beta, -alpha);
            board.unMakeMove(move);
            if (evaluation >= beta) {
                return beta;
            }

            alpha = Math.max(alpha, evaluation);

        }

        return alpha;

    }






    public int evaluate() {
        int whiteVal = countMaterial(true);
        int blackVal = countMaterial(false);

        int eval = whiteVal - blackVal;

        int perspective = board.w2m ? 1 : -1;
//        System.out.println("eval = " + eval * perspective);
        return eval * perspective;
    }
    public int countMaterial(boolean isWhite) {
        int material = 0;

        material += board.pieceCount(isWhite, 'p') * board.pawnVal;
        material += board.pieceCount(isWhite, 'n') * board.knightVal;
        material += board.pieceCount(isWhite, 'b') * board.bishopVal;
        material += board.pieceCount(isWhite, 'r') * board.rookVal;
        material += board.pieceCount(isWhite, 'q') * board.queenVal;

        return material;
    }



    public void orderMoves(ArrayList<Move> moves) {

        for (Move move : moves){
            Piece piece = move.piece;

            int moveScoreGuess = 0;
            char pieceName = piece.name;
            char pieceCapName = 'e';
            if (move.capture != null)
                pieceCapName = move.capture.name;

            if (pieceCapName != 'e') {
                moveScoreGuess = 10 * move.capture.val - piece.val;
            }
            if (pieceName == 'p' && (piece.isWhite ? piece.row == 0 : piece.row == 7)) {
                moveScoreGuess += move.promo.val;
            }



        }


    }













    ArrayList<Move> findValidMoves() {

        ArrayList<Move> moves = new ArrayList<>();

        Piece king = board.findKing(board.w2m);

        if (king != null) {

            for (Piece piece : board.pieceList) {
                board.selectedPiece = piece;

                if (board.sameTeam(piece, king)) {

                    for (int row = 0; row < board.rows; row++) {
                        for (int col = 0; col < board.cols; col++) {
                            Piece pieceXY = board.getPiece(col, row);
                            if (piece.canMakeMove(pieceXY, col, row)) {

                                // handle pawns differently
                                if (piece.name == 'p' && row == (piece.isWhite ? 0 : 7)) {

                                    moves.add(new Move(board.getSpareQueen(piece.isWhite), col, row, pieceXY, piece));
                                    moves.add(new Move(board.getSpareRook(piece.isWhite), col, row, pieceXY, piece));
                                    moves.add(new Move(board.getSpareBishop(piece.isWhite), col, row, pieceXY, piece));
                                    moves.add(new Move(board.getSpareKnight(piece.isWhite), col, row, pieceXY, piece));

                                } else {

                                    moves.add(new Move(piece, col, row, pieceXY, null));

                                }

                            }
                        }
                    }

                }
            }
        }




        board.selectedPiece = null;
        return moves;
    }


    public int moveGenerationTest(int depth) {
        if (depth == 0) {
            return 1;
        }

        ArrayList<Move> moves = findValidMoves();

        int totalPositions = 0;

        for (Move move : moves) {

//            if (move.description.equals("w pawn b6")) pause(move);

            board.makeMove(move);
            pause(move);

            totalPositions += moveGenerationTest(depth - 1);

            board.unMakeMove(move);
            pause(move);

        }

        return totalPositions;
    }

    void pause(Move move) {

//        if (move.description.equals("w pawn b6") && board.enPassantCol == 1) {
        if (false) {

            try {
//                Thread.sleep(10);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            board.repaint();
        }

    }

}





