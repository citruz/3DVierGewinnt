package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by seele on 04.05.2015.
 */
public class Board {

    public static int WIN = Integer.MAX_VALUE-1;
    public static int LOOSE = Integer.MIN_VALUE+1;

    private char[][][] board = new char[4][7][6];

    /**
     * Loads the board from a file. The file must have 24 lines with 7 characters each.
     * The characters can only be spaces, X or O.
     * @param filename
     */
    public Board(String filename) {

        this.reset();

        int x = 0, z = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            for(String line; (line = br.readLine()) != null; ) {

                System.out.println(line);

                for (int y = 0; y < line.length() && y < board[x][y].length; y++) {
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
            System.err.println("Board could not be loaded.");
            e.printStackTrace();

            return;
        }

        System.out.println("Loaded board from file successfully:");
        this.print();

    }

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
     * @param player
     * @param x
     * @param y
     * @return true or false, whether the move was successful (i.e. the stack was not full)
     */
    public boolean placeToken(char player, int x, int y) {
        for (int z = 0; z < board[x][y].length; z++) {
            if (board[x][y][z] == ' ') {
                board[x][y][z] = player;
                return true;
            }
        }
        return false;
    }

    /**
     * Will remove the topmost token at the specified x and y position.
     * @param x
     * @param y
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
     * Returns wether moves are still possible, i.e. the board is not full.
     * @return
     */
    public boolean movePossible(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y][(board[x][y].length-1)] == ' ') {
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
     * Evaluates the favorability of the board as seen by the specified player.
     *
     * iterate over all fields : field {
     *     for each of the 26 possible directions : direction {
     *         if line of 4 is possible from field in direction [
     *              ecount = number of tokens from enemy
     *              scound = number of tokes from self
     *
     *              if ecount == 4 return -inf
     *              if scount == 4 return +inf
     *              if ecount > 0 return 0
     *              else return 2^scount
     *         }
     *     }
     * }
     *
     * @param player
     * @return
     */
    public int evaluate (char player) {
        int score = 0;
        int positions = 0;

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
                                    return LOOSE;
                                } else if (s_count == 4) {
                                    return WIN;
                                } else if (e_count == 0) {
                                    score += (int)Math.pow(5, s_count);
                                }
                                //Do not increment score if e_count != 0 -> enemy has occupied this opportunity

                                positions++;

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
     * @return
     */
    public int getLength() {
        return board.length;
    }

    /**
     * Returns the width of the board.
     * @return
     */
    public int getWidth() {
        return board[0].length;
    }
}
