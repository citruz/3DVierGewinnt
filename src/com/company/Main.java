package com.company;

/**
 * Main class for launching the program.
 * Can be used with exactly three arguments: file to load board from, max depth and max player.
 * If not three parameters are specified, it will use the default values. <br>
 * Example Usage: java -jar 3DVierGewinnt.jar "data.txt" 3 X
 */
public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            MiniMax mm = new MiniMax(new Board("data.txt"), 3, 'X');
        } else {
            MiniMax mm = new MiniMax(new Board(args[0]), Integer.parseInt(args[1]), args[2].charAt(0));
        }
    }
}
