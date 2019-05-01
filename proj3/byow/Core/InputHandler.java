package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class InputHandler {

    /**Instance Variables for Room Dimensions */
    //Font
    private Font header;
    private Font subheader;

    //Dimensions of the Room
    private int width;
    private int height;
    private RoomMaker ourRoom;
    private int seed;

    //Renderer Object to Display and Update Room
    TERenderer myRenderer;

    //Keep track of user input
    private String allCommands;


    /** Constructor: Intializes Room Dimensions and Renderer Object */
    public InputHandler(int width, int height, TERenderer myRenderer) {
        this.width = width;
        this.height = height;
        this.myRenderer = myRenderer;
        allCommands = "";

        header = new Font("Monaco", Font.BOLD, 30);
        subheader = new Font("Monaco", Font.BOLD, 15);

        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);

        drawMenu();
    }

    /** Menu: Creates the Menu with Three Options and Notes
     * 1. New Game: Sends user to enter seed menu
     * 2. Load Game: Saves File and will be reinstated upon relaunch
     * 3. Quit Game: Exits and closes the console
     * 4. Notes: Explains two features:
     *      a. Rotate world
     *      b. Enter Dark/Hard Mode */
    private void drawMenu() {

        //Draw the menu with all options
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(header);
        StdDraw.text(width / 2, 4 * height / 5, "CS61B: The Endgame");
        StdDraw.setFont(subheader);
        StdDraw.text(width / 2, 6 * height / 10, "New Game (N)");
        StdDraw.text(width / 2, height / 2, "Load Game (L)");
        StdDraw.text(width / 2, 4 * height / 10, "Quit Game (Q)");
        StdDraw.text(width / 2, 2 * height / 10, "Catch the Flower. "
                + "Press R while playing for a surprise");
        StdDraw.text(width / 2, 1 * height / 10, "Too Easy? Press H while playing");

        StdDraw.show();

        //Get their first input letter
        char input;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                input = java.lang.Character.toUpperCase(StdDraw.nextKeyTyped());
                break;
            }
        }
        switch (input) {
            case 'Q':
                quitGame();
                break;
            case 'L':
                loadGame();
                break;
            case 'N':
                seedMenu();
                break;
            default:
                invalidSeed();
                break;
        }
    }


    /**Seed Menu Maker: Displays the seed as users input it and checks for an ending S"*/
    private void seedMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(subheader);
        StdDraw.text(width / 2, 2 * height / 3, "Enter Seed and Press S:");
        StdDraw.text(width / 2, 1 * height / 3, "Press B to Go Back");
        StdDraw.show();

        char input;
        String seedString = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                input = java.lang.Character.toUpperCase(StdDraw.nextKeyTyped());

                switch (input) {
                    case 'S':
                        if (seedString.equals("")) {
                            invalidSeed();
                        }
                        seed = (int) Long.parseLong(seedString);
                        beginGame();
                        break;
                    case 'B':
                        drawMenu();
                        break;
                }

                try {
                    Integer.parseInt(input + "");
                } catch (java.lang.NumberFormatException e) {
                    invalidSeed();
                }

                seedString += input;
                StdDraw.clear(Color.BLACK);
                StdDraw.text(width / 2, 2 * height / 3, "Enter Seed and Press S:");
                StdDraw.text(width / 2, 1 * height / 3, "Press B to Go Back");
                StdDraw.text(width / 2, height / 2, seedString);
                StdDraw.show();

            }
        }
    }

    /**Validates Seeds: Checks for cases where a letter is inputted instead of a number*/
    private void invalidSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(subheader);
        StdDraw.text(width / 2, height / 2, "Invalid Input, Please Try Again");
        StdDraw.show();
        StdDraw.pause(2000);
        drawMenu();
    }

    /**Keeps checking for user input and communicates with RoomMaker to move the avatar
     *  Also checks for a quit command in which case the progress would be saved
     */
    private void beginGame() {
        ourRoom = new RoomMaker(width, height, seed);
        myRenderer.initialize(width, height);
        renderGame();
        gameControl();
    }

    private void loadGame() {
        String retrievedSeed = retrieveGame("seed");
        String retrievedCommands = retrieveGame("allcommands");
        seed = (int) Long.parseLong(retrievedSeed);
        allCommands += retrievedCommands;

        ourRoom = new RoomMaker(width, height, seed);
        myRenderer.initialize(width, height);
        renderGame();
        for (int i = 0; i < retrievedCommands.length(); i++) {
            ourRoom.controlAvatar(retrievedCommands.charAt(i));
        }
        renderGame();
        gameControl();
    }

    /**Quits the window*/
    private void quitGame() {
        System.exit(0);
    }


    private void gameControl() {
        String quitGame = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = java.lang.Character.toUpperCase(StdDraw.nextKeyTyped());
                if (input == ':') {
                    quitGame += input;
                } else if (input == 'Q') {
                    quitGame += input;
                } else {
                    allCommands += input;
                }
                ourRoom.controlAvatar(input);
            }
            renderGame();

            if (quitGame.equals(":Q")) {
                try {
                    saveFile(allCommands, "allcommands");
                    saveFile(seed + "", "seed");
                    quitGame();
                } catch (IOException e) {
                    System.out.println("me fail");
                }

            }
        }
    }


    /** Saves the seed and user commands to a text file*/
    private void saveFile(String toBeSaved, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"));
        writer.write(toBeSaved);
        writer.close();
    }

    private String retrieveGame(String fileName) {
        StringBuilder buildWorld = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(fileName + ".txt"), StandardCharsets.UTF_8)) {
            stream.forEach(s -> buildWorld.append(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buildWorld.toString();
    }


    /** Uses the renderer object to display the world and the HUD*/
    private void renderGame() {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 10, 19 * height / 20, "Tile: " + tileUnderMouse());
        StdDraw.text(width - width / 10, 19 * height / 20, "Score: " + ourRoom.getScore());
        StdDraw.show();
        myRenderer.renderFrame(ourRoom.getWorld());
    }

    /** Returns the description of the tile the user is pointing to*/
    private String tileUnderMouse() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        TETile curr = ourRoom.getWorld()[mouseX][mouseY];
        return curr.description();
    }
}
