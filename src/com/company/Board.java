package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by seele on 04.05.2015.
 */
public class Board {

    private char[][][] board = new char[4][7][6];

    public Board(String filename) {

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

    public boolean placeToken(char player, int x, int y) {
        for (int z = 0; z < board[x][y].length; z++) {
            if (board[x][y][z] == ' ') {
                board[x][y][z] = player;
                return true;
            }
        }
        return false;
    }

    public void removeToken(int x, int y) {
        for (int z = board[x][y].length-1; z >= 0; z--) {
            if (board[x][y][z] != ' ') {
                board[x][y][z] = ' ';
                return;
            }
        }
    }

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

    public int evaluate (char player) {
        //Evaluate Board from player X or O view
        //Koten Sie hier!
        return 0;
    }

    public int getLength() {
        return board.length;
    }
    public int getWidth() {
        return board[0].length;
    }
}
