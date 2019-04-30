package byow.Core;

import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputHandler {


    private Font header;
    private Font subheader;
    private int width;
    private int height;
    private RoomMaker ourRoom;
    private int seed;
    TERenderer myRenderer;

    private String allCommands;


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

    public RoomMaker getRoom() {
        return ourRoom;
    }

    private void drawMenu() {

        StdDraw.setFont(header);
        StdDraw.text(width / 2, 4 * height / 5, "CS61B: The Endgame");
        StdDraw.setFont(subheader);
        StdDraw.text(width / 2, 6 * height / 10, "New Game (N)");
        StdDraw.text(width / 2, height / 2, "Load Game (L)");
        StdDraw.text(width / 2, 4 * height / 10, "Quit Game (Q)");

        StdDraw.show();

        InputType handler = new InputType(true, "");
        seed = handler.getSeed();

        if (seed == -1) {
            loadGame();
        } else if (seed == -2) {
            quitGame();
        } else if (seed == 0) {
            throw new IllegalArgumentException("Not a valid seed");
        } else {
            beginGame();
        }
    }


    private void loadGame() {
        myRenderer.initialize(width, height);

        renderGame();
    }

    private void quitGame() {
        System.exit(0);
    }


    private void beginGame() {
        ourRoom = new RoomMaker(width, height, seed);
        myRenderer.initialize(width, height);
        renderGame();
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
                saveFile(allCommands, "allcommands");
                saveFile(seed + "", "seed");
                quitGame();
            }
        }
    }

    private void saveFile(String toBeSaved, String fileName) {
        try {
            Files.write(Paths.get("/Desktop/cs61b/sp19-s574/sp19-proj3-s201-s574/proj3/"
                    + fileName + ".txt"), toBeSaved.getBytes());
        } catch (IOException e) {
            System.out.println("Me poo poo");
        }
    }

    private void renderGame() {
        myRenderer.renderFrame(ourRoom.getWorld());
        StdDraw.text(0, 0, "Tile: ");
        StdDraw.text(width, 0, "Score: ");
        StdDraw.show();

    }
}
