package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by seele on 04.05.2015.
 */
public class Board {

    private char[][][] board = new char[4][6][7];

    public Board(String filename) {

        int x = 0,  y = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            for(String line; (line = br.readLine()) != null; ) {

                System.out.println(line);

                int z = 0;
                for (char c: line.toCharArray()) {
                    if (c == 'X' || c == 'O') {
                        board[x][y][z] = c;
                    } else {
                        board[x][y][z] = ' ';
                    }
                    z++;
                }
                x = (x + 1) % 4;
                if (x == 0) {
                    y++;
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
        for (int x = 0; x < board.length; x++) {
            System.out.println("Layer "+ (x+1));
            for (int y = 0; y < board[x].length; y++) {
                for (int z = 0; z < board[x][y].length; z++) {
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
