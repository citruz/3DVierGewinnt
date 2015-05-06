package com.company;

/**
 * Executes the Minimax-algorithm on a three dimensional connect 4 board.
 */
public class MiniMax {

    /**
     * Global game board
     */
    private Board board;

    /**
     * Maximum depth in the search three
     */
    private int maxDepth;

    /**
     * Player for which the score should be maximized
     */
    private char maxPlayer;

    /**
     * Saves the current best move during algorithm execution
     */
    private int[] savedMove;

    /**
     * Saves the score of the current best move during algorithm execution
     */
    private int bestScore;

    /**
     * Executes the minimax algorithm.
     * @param b game board
     * @param maxDepth max depth of the search tree
     * @param maxPlayer player for which the score should be maximized (X or O)
     */
    public MiniMax(Board b, int maxDepth, char maxPlayer) {
        this.board = b;
        this.maxDepth = maxDepth;
        this.maxPlayer = maxPlayer;

        System.out.println("Starting minimax with depth "+maxDepth+" and max player "+maxPlayer+".");
        long startTime = System.currentTimeMillis();
        //start minimax with alpha = -inf and beta = +inf
        this.minimax(0, maxPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);

        long stopTime = System.currentTimeMillis();
        double elapsedTime = (stopTime - startTime) / 1000.0;

        System.out.println("Recommended move is at {"+savedMove[0]+","+savedMove[1]+"} with score: "+bestScore);
        System.out.println("Calculated in " + elapsedTime + " seconds.");
    }

    /**
     * Executes the actual algorithm.
     * @param depth current depth
     * @param currentPlayer current player (X or O)
     * @param alpha alpha value
     * @param beta beta value
     * @return best possible score for this part of the tree
     */
    private int minimax (int depth, char currentPlayer, int alpha, int beta) {
        //Return if max depth is reached, board is full or game has ended
        if (depth == maxDepth || !board.movePossible()) {
            return board.evaluate(maxPlayer);
        }
        int gameEnd = board.evaluate(maxPlayer);
        if (gameEnd == Board.WIN || gameEnd == Board.LOOSE) {
            return gameEnd;
        }

        //Saves the current best value for the children of the node
        int bestValue = (currentPlayer == maxPlayer) ? alpha : beta;

        //Generate all possible moves
        outerloop:
        for (int x = 0; x < board.getLength(); x++) {
            for (int y = 0; y < board.getWidth(); y++) {

                if(board.placeToken(currentPlayer, x, y)) {
                    //Move was possible
                    char otherPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                    if (currentPlayer == maxPlayer) {
                        //MAX
                        int score = minimax(depth+1, otherPlayer, bestValue, beta);
                        board.removeToken(x,y);

                        if (score > bestValue) {
                            //New best score
                            bestValue = score;
                            if (bestValue >= beta)
                                break outerloop; //alpha-beta pruning -> stop iterating

                            if (depth == 0) {
                                savedMove = new int[]{x, y};
                                bestScore = score;
                            }
                        }
                    } else {
                        //MIN
                        int score = minimax(depth+1, otherPlayer, alpha, bestValue);
                        board.removeToken(x,y);
                        if (score < bestValue) {
                            bestValue = score;
                            if (bestValue <= alpha)
                                break outerloop; //alpha-beta pruning -> stop iterating
                        }
                    }

                }
            }
        }

        return bestValue;
    }
}
