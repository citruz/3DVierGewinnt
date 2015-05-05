package com.company;

import java.nio.file.Paths;

public class MiniMax {

    private Board board;
    private int maxDepth;
    private char maxPlayer;
    private int[] savedMove;
    private int bestScore;

    public static void main(String[] args) {
        MiniMax mm = new MiniMax(new Board("data.txt"), 3, 'X');
    }

    public MiniMax(Board b, int maxDepth, char maxPlayer) {
        this.board = b;
        this.maxDepth = maxDepth;
        this.maxPlayer = maxPlayer;

        this.minimax(0, maxPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);

        System.out.println("Recommended move is at {"+savedMove[0]+","+savedMove[1]+"} with score: "+bestScore);
    }

    private int minimax (int depth, char currentPlayer, int alpha, int beta) {
        //Return if max depth is reached, board is full or game has ended
        if (depth == maxDepth || !board.movePossible()) {
            return board.evaluate(maxPlayer);
        }
        int gameEnd = board.evaluate(maxPlayer);
        if (gameEnd == Board.WIN || gameEnd == Board.LOOSE) {
            return gameEnd;
        }

        int minOrMaxValue = (currentPlayer == maxPlayer) ? alpha : beta;

        //Generate all possible moves
        outerloop:
        for (int x = 0; x < board.getLength(); x++) {
            for (int y = 0; y < board.getWidth(); y++) {

                if(board.placeToken(currentPlayer, x, y)) {
                    //Move was possible
                    char otherPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                    if (currentPlayer == maxPlayer) {
                        //MAX
                        int score = minimax(depth+1, otherPlayer, minOrMaxValue, beta);
                        board.removeToken(x,y);

                        if (score > minOrMaxValue) {
                            minOrMaxValue = score;
                            if (minOrMaxValue >= beta)
                                break outerloop; //stop iterate moves
                            if (depth == 0) {
                                savedMove = new int[]{x, y};
                                bestScore = score;
                            }
                        }
                    } else {
                        //MIN
                        int score = minimax(depth+1, otherPlayer, alpha, minOrMaxValue);
                        board.removeToken(x,y);
                        if (score < minOrMaxValue) {
                            minOrMaxValue = score;
                            if (minOrMaxValue <= alpha)
                                break outerloop;
                        }
                    }

                }
            }
        }

        return minOrMaxValue;
    }
}
