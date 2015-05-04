package com.company;

import java.nio.file.Paths;

public class MiniMax {

    private Board board;
    private int maxDepth;
    private char maxPlayer;
    private int[] savedMove;

    public static void main(String[] args) {
        MiniMax mm = new MiniMax(new Board("data.txt"), 3, 'X');
    }

    public MiniMax(Board b, int maxDepth, char maxPlayer) {
        this.board = b;
        this.maxDepth = maxDepth;
        this.maxPlayer = maxPlayer;

        this.minimax(0, maxPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);

        System.out.println("Recommended move is at {"+savedMove[0]+","+savedMove[1]+"}");
    }

    private int minimax (int depth, char currentPlayer, int alpha, int beta) {
        //Return if max depth is reached or the board is full
        if (depth == maxDepth || !board.movePossible()) {
            return board.evaluate(currentPlayer);
        }
        int minOrMaxValue = (currentPlayer == maxPlayer) ? alpha : beta;

        //Generate all possible moves
        for (int x = 0; x < board.getLength(); x++) {
            for (int y = 0; y < board.getWidth(); y++) {

                if(board.placeToken(currentPlayer, x, y)) {
                    //Move was possible
                    char otherPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                    if (currentPlayer == maxPlayer) {
                        //MAX
                        int value = minimax(depth+1,otherPlayer, minOrMaxValue, beta);
                        board.removeToken(x,y);

                        if (value > minOrMaxValue) {
                            minOrMaxValue = value;
                            if (minOrMaxValue >= beta)
                                break;
                            if (depth == 0)
                                savedMove = new int[]{x, y};
                        }
                    } else {
                        //MIN
                        int value = minimax(depth+1, otherPlayer, alpha, minOrMaxValue);
                        board.removeToken(x,y);
                        if (value < minOrMaxValue) {
                            minOrMaxValue = value;
                            if (minOrMaxValue <= alpha)
                                break;
                        }
                    }

                }
            }
        }

        return minOrMaxValue;
    }
}
