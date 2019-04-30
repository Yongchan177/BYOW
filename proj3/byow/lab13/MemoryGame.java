package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.Stopwatch;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Font font;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */

        rand = new Random(seed);

        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //Generate random string of letters of length n

        String random = "";
        for (int i = 0; i < n; i++) {
            int randomIndex = RandomUtils.uniform(rand, CHARACTERS.length);
            random += CHARACTERS[randomIndex];
        }
        return random;
    }

    public void drawFrame(String s) {
        //Take the string and display it in the center of the screen
        //If game is not over, display relevant game information at the top of the screen

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();

    }

    public void flashSequence(String letters) {
        //Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            Stopwatch stopwatch = new Stopwatch();
            String toDisplay = letters.charAt(i) + "";
            drawFrame(toDisplay);
            while(stopwatch.elapsedTime() < 1) {

            }
            drawFrame("");
            while(stopwatch.elapsedTime() < 0.5) {

            }


        }

    }

    public String solicitNCharsInput(int n) {
        //Read n letters of player input
        String playerInput = "";
        while (n > 0 && StdDraw.hasNextKeyTyped()) {
            char typed = StdDraw.nextKeyTyped();
            playerInput += typed;
            drawFrame(typed+"");
        }
        return playerInput;
    }

    public void startGame() {
        //Set any relevant variables before the game starts

        gameOver = false;
        round = 1;

        //Establish Engine loop
        while (!gameOver) {
            drawFrame("Round: " + round);
            String toDisplay = generateRandomString(round);
            flashSequence(toDisplay);
            String typedIn = solicitNCharsInput(round);
            if (typedIn == toDisplay) {
                round++;
            } else {
                drawFrame("Game Over! You made it to round: " + round);
                gameOver = true;
            }
        }
    }

}
