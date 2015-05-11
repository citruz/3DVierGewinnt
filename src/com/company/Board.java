package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Represents the game board of a three-dimensional connect 4 game.
 */
public class Board {

    /**
     * Value that is returned by evaluate() if the player has won in the current situation.
     */
    public static int WIN = Integer.MAX_VALUE-1;

    /**
     * Value that is returned by evaluate() if the player has won in the current situation.
     */
    public static int LOOSE = Integer.MIN_VALUE+1;

    /**
     * Three dimensional array representing the fields. Values must only be spaces, X or O.
     */
    private char[][][] board = new char[4][7][6];

    /**
     * Loads the board from a file. The file must have 24 lines with 7 characters each.
     * The characters can only be spaces, X or O.
     * @param filename path to the file
     */
    public Board(String filename) {

        //Empty all fields of the board (space)
        this.reset();

        int x = 0, y = 0, z = 0;
        //Try to open file and read line by line
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            for(String line; ((line = br.readLine()) != null) && z < board[0][0].length;) {
                //Go to trough each character of the line.
                for (y = 0; y < line.length() && y < board[x].length; y++) {
                    if (line.charAt(y) == 'X' || line.charAt(y) == 'O') {
                        board[x][y][z] = line.charAt(y);
                    } else {
                        board[x][y][z] = ' ';
                    }
                }
                x = (x + 1) % 4;
                if (x == 0) {
                    z++;
                }
            }
        } catch (IOException e) {
            System.err.println("Board could not be loaded from "+filename+".");
            e.printStackTrace();

            return;
        }

        System.out.println("Loaded board from file "+filename+" successfully:");
        this.print();

    }

    /**
     * Reset all fields of the board to neutral state (space).
     */
    public void reset() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                for (int z = 0; z < board[x][y].length; z++) {
                    board[x][y][z] = ' ';
                }
            }
        }
    }

    /**
     * Places a token for the specified player at the specified x and y position on top of the stack.
     * @param player which token to place on the board
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true or false, whether the move was successful (i.e. the stack was not full)
     */
    public boolean placeToken(char player, int x, int y) {
        //Search for first empty position on the stack.
        for (int z = 0; z < board[x][y].length; z++) {
            if (board[x][y][z] == ' ') {
                //Place token and return.
                board[x][y][z] = player;
                return true;
            }
        }
        //No empty position was found -> move is not possible
        return false;
    }

    /**
     * Will remove the topmost token at the specified x and y position.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void removeToken(int x, int y) {
        for (int z = board[x][y].length-1; z >= 0; z--) {
            if (board[x][y][z] != ' ') {
                board[x][y][z] = ' ';
                return;
            }
        }
    }

    /**
     * Returns whether moves are still possible, i.e. the board is not full.
     * @return whether moves are still possible
     */
    public boolean movePossible(){
        //Go trough each stack and check if the topmost position is occupied
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y][(board[x][y].length-1)] == ' ') {
                    //Found empty position -> board is not full
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Prints the current board to commandline.
     */
    public void print() {
        //Go on the z-axis from bottom to top
        for (int z = 0; z < board[0][0].length; z++) {
            System.out.println("Layer "+ (z+1));
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[x].length; y++) {
                    System.out.print(board[x][y][z]);
                }
                System.out.print("\n");
            }
        }
    }

    /**
     * Evaluates the favorability of the board as seen by the specified player. Therefore all possible lines with a
     * length of 4 are evaluated and added to the final score (there are 456 positions on a 4x7x6 field). <br>
     * Possible lines that are not occupied by the enemy with at least one token receive a score according
     * to the number of tokens the player has placed on this line. The score is calculated as 5^(number of tokens).
     * This means that empty lines receive a score of 1, lines with one token of the player 5 and so on. <br><br>
     *
     * The pseudo-code for the algorithm can be found below. Because this methods visits all possible lines twice (once
     * with A as startfield and B as endfield, once the other way around), the final score must be divided by 2.<br>
     * This represents a small performance impact. We decided to use this algorithm anyway because:<br>
     * <ol>
     *  <li>The alternative would be to maintain a list of positions visited and check each time, if the current position
     *     is already on this list. The would probably be a much larger overhead and requires to maintain a
     *     data structure in memory.</li>
     *  <li>This adds only a constant factor of 2 to the complexity of the algorithm. Because of the exponential
     *     complexity of the Minimax-algorithm this can be neglected.</li>
     * </ol>
     * <pre>
     * score = 0
     * iterate over all fields : startfield {
     *     for each of the 26 possible directions : direction {
     *         if line of 4 is possible from startfield in direction {
     *              ecount = number of tokens from enemy in line
     *              scound = number of tokes from self in line
     *
     *              if ecount == 4 return -inf
     *              if scount == 4 return +inf
     *              if ecount == 0 score += 5^scount
     *         }
     *     }
     * }
     * return score/2
     * </pre>
     * @param player Player from whose view the board will be evaluated.
     * @return favorability of the board
     */
    public int evaluate (char player) {
        int score = 0;

        //Foreach field...
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                for (int z = 0; z < board[x][y].length; z++) {

                    //Foreach possible direction
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            for (int dz = -1; dz <= 1; dz++) {

                                if (dx == 0 && dy == 0 && dz == 0)
                                    continue;

                                //Calculate endpoints from line of 4
                                int endX = x + 3*dx;
                                int endY = y + 3*dy;
                                int endZ = z + 3*dz;
                                //Check if endpoint is still in bounds of board
                                if (endX < 0 || endX >= board.length) continue;
                                if (endY < 0 || endY >= board[x].length) continue;
                                if (endZ < 0 || endZ >= board[x][z].length) continue;

                                //Count tokens of each player
                                int e_count = 0, s_count = 0;
                                int tempX = x, tempY = y, tempZ = z;
                                for (int i = 0; i < 4; i++) {
                                    if (board[tempX][tempY][tempZ] == player) {
                                        s_count++;
                                    } else if (board[tempX][tempY][tempZ] != ' ') {
                                        e_count++;
                                    }

                                    tempX += dx;
                                    tempY += dy;
                                    tempZ += dz;
                                }

                                if (e_count == 4) {
                                    //Enemy has 4 in a row
                                    return LOOSE;
                                } else if (s_count == 4) {
                                    //I have 4 in a row
                                    return WIN;
                                } else if (e_count == 0) {
                                    //If enemy has no token in the line, calculate the score
                                    score += (int)Math.pow(5, s_count);
                                }
                                //Do not increment score if e_count != 0 -> enemy has occupied this opportunity
                            }
                        }
                    }

                }
            }
        }

        return score/2;
    }

    /**
     * Returns the length of the board.
     * @return length in x direction
     */
    public int getLength() {
        return board.length;
    }

    /**
     * Returns the width of the board.
     * @return width in y direction
     */
    public int getWidth() {
        return board[0].length;
    }
}
