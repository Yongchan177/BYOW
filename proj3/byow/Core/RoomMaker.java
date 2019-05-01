package byow.Core;

import byow.KdTree.Point;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class RoomMaker {
    /** Class will create rooms based on specified width and heights. Referenced in Engine.java */

    /** Instance Variables */
    private int width;
    private int height;
    private int seed;
    private TETile[][] world;

    private int avatarX;
    private int avatarY;
    private int flowerX;
    private int flowerY;
    private int noOfMoves;
    private int maxMoxes;
    private int currScore;

    private boolean hardMode;


    private ArrayList<Point> roomPoints;
    private Random seedRandom;
    private Random seedRandomSeparate;



    /** Constructor: Initializes instance variables and instantiates 2D world Array. */
    public RoomMaker(int width, int height, int seed) {

        //Setting instance variables
        this.width = width; //number of columns
        this.height = height; //number of rows
        this.seed = seed;
        seedRandom = new Random(seed);
        seedRandomSeparate = new Random(seed);
        world = new TETile[width][height];
        roomPoints = new ArrayList<>();
        noOfMoves = 0;
        currScore = 0;
        hardMode = false;

        //Initializes all the tiles in the world
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //Create randomly generated rooms and hallways within the world
        makeRooms();
        makeHallways();
        makeAvatar();

        int[] flowerInitial = getPossibleXandY();
        flowerX = flowerInitial[0];
        flowerY = flowerInitial[1];
        genFlower();

        maxMoxes = RandomUtils.uniform(seedRandomSeparate, 10, 20);
    }

    /** getWorld: Returns world 2D Array */
    public TETile[][] getWorld() {
        if (hardMode) {
            return getDarkWorld();
        }
        return world;
    }

    /** Creates a copy of the world array where only what's
     * in the line of site can be seen
     */
    public TETile[][] getDarkWorld() {
        TETile[][] darkened = new TETile[world.length][world[0].length];
        //Initializes darkened array
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                darkened[x][y] = Tileset.NOTHING;
            }
        }
        System.arraycopy(world[avatarX], 0, darkened[avatarX], 0, world[avatarX].length);
        for (int i = 0; i < width; i++) {
            darkened[i][avatarY] = world[i][avatarY];
        }

        return darkened;
    }



    /** makeRooms: uses random seed to generate rooms in the 2D world
     * Notes:
     *      1. Unique seeds must return unique rooms
     *      2. Identical seeds must return the same rooms */
    private void makeRooms() {
        int noOfRooms = RandomUtils.uniform(seedRandom, 5, 15);
        int k = 0;

        while (k < noOfRooms) {
            //Get random starting points based on the seed

            //Finding a gap to the edges
            int offSetX = Math.floorDiv(width, 5);
            int offSetY = Math.floorDiv(height, 5);

            //Finding a random starting point for the rooms
            int startingRow = RandomUtils.uniform(seedRandom, offSetY, height - offSetY);
            int startingColumn = RandomUtils.uniform(seedRandom, offSetX, width - offSetX);

            //Creating a random size of the room
            int heightOfRoom = RandomUtils.uniform(seedRandom, 5, offSetY);
            int widthOfRoom = RandomUtils.uniform(seedRandom, 5, offSetX);
            int endingColumn = startingColumn + widthOfRoom;
            int endingRow = startingRow + heightOfRoom;

            boolean isEmpty = true;

            //Check if room already exists at the position
            for (int i = startingColumn; i < endingColumn; i++) {
                for (int j = startingRow; j < endingRow; j++) {
                    if (!world[i][j].equals(Tileset.NOTHING)) {
                        isEmpty = false;
                    }
                }
            }

            //Create the room
            if (isEmpty) {
                for (int i = startingColumn; i < endingColumn; i++) {
                    for (int j = startingRow; j < endingRow; j++) {
                        //Building Walls Around The Room
                        if (i == startingColumn || i == endingColumn - 1
                                || j == startingRow || j == endingRow - 1) {
                            world[i][j] = Tileset.WALL;
                        } else {
                            world[i][j] = Tileset.GRASS;
                        }
                    }
                }

                k++;

                int midX = startingColumn + Math.floorDiv((endingColumn - startingColumn), 2);
                int midY = startingRow + Math.floorDiv((endingRow - startingRow), 2);

                Point midPoint = new Point((double) midX, (double) midY);
                roomPoints.add(midPoint);
            }
        }
    }


    /** makeHallways: connects rooms together randomly using a KD tree's closest method */
    private void makeHallways() {

        for (Point roomCorner : roomPoints) {

            int noOfPaths = RandomUtils.uniform(seedRandom, 1, 5);

            for (int k = 0; k < noOfPaths; k++) {

                int random = RandomUtils.uniform(seedRandom, 0, roomPoints.size() - 1);
                Point randomPoint = roomPoints.get(random);

                int myX = (int) roomCorner.getX();
                int myY = (int) roomCorner.getY();
                int randomX = (int) randomPoint.getX();
                int randomY = (int) randomPoint.getY();

                int startingX = Math.min(myX, randomX);
                int endingX = Math.max(myX, randomX);
                int startingY = Math.min(myY, randomY);
                int endingY = Math.max(myY, randomY);

                boolean topRight = ((randomX > myX) && (randomY > myY))
                        || ((myX > randomX) && (myY > randomY));


                for (int i = startingX; i <= endingX; i++) {
                    changeTile(world, i, startingY, false);
                }

                for (int i = startingY; i <= endingY; i++) {
                    if (topRight) {
                        changeTile(world, endingX, i, true);
                    } else {
                        changeTile(world, startingX, i, true);
                    }

                }
            }
        }
    }

    /** Helper method for creating walls around the hallways as they're built*/
    private void changeTile(TETile[][] thisWorld, int x, int y, boolean up) {

        thisWorld[x][y] = Tileset.GRASS;

        if (up) {
            if (thisWorld[x - 1][y].equals(Tileset.NOTHING)) {
                thisWorld[x - 1][y] = Tileset.WALL;
            }
            if (thisWorld[x + 1][y].equals(Tileset.NOTHING)) {
                thisWorld[x + 1][y] = Tileset.WALL;
            }
        } else {
            if (thisWorld[x][y - 1].equals(Tileset.NOTHING)) {
                thisWorld[x][y - 1] = Tileset.WALL;
            }
            if (thisWorld[x][y + 1].equals(Tileset.NOTHING)) {
                thisWorld[x][y + 1] = Tileset.WALL;
            }
        }

    }

    /**Finds a random X and Y that's inside a room or hallway*/
    private int[] getPossibleXandY() {
        int x = RandomUtils.uniform(seedRandomSeparate, 0, width);
        int y = RandomUtils.uniform(seedRandomSeparate, 0, height);
        while (!world[x][y].equals(Tileset.GRASS)) {
            x = RandomUtils.uniform(seedRandomSeparate, 0, width);
            y = RandomUtils.uniform(seedRandomSeparate, 0, height);
        }
        int[] position = new int[2];
        position[0] = x;
        position[1] = y;
        return position;
    }

    /**Creates the avatar at the beginning of the game*/
    private void makeAvatar() {
        int[] avatarLocation = getPossibleXandY();
        avatarX = avatarLocation[0];
        avatarY = avatarLocation[1];
        world[avatarX][avatarY] = Tileset.AVATAR;
    }

    /**Generates a flower at a random possible location*/
    private void genFlower() {
        world[flowerX][flowerY] = Tileset.GRASS;
        int[] flowerLocation = getPossibleXandY();
        flowerX = flowerLocation[0];
        flowerY = flowerLocation[1];
        world[flowerX][flowerY] = Tileset.FLOWER;
    }


    /**Takes in the user's input and controls the avatar accordingly*/
    public void controlAvatar(char input) {
        switch (input) {
            case 'W':
                possibleSwitch(avatarX, avatarY + 1);
                break;
            case 'A':
                possibleSwitch(avatarX - 1, avatarY);
                break;
            case 'S':
                possibleSwitch(avatarX, avatarY - 1);
                break;
            case 'D':
                possibleSwitch(avatarX + 1, avatarY);
                break;
            case 'R':
                rotateWorld();
                break;
            case 'H':
                hardMode = !hardMode;
                break;
            default:
                break;
        }
        if (noOfMoves == maxMoxes) {
            genFlower();
            maxMoxes = RandomUtils.uniform(seedRandomSeparate, 10, 20);
            noOfMoves = 0;
        }

    }

    /**Helper method for moving the avatar around*/
    private void possibleSwitch(int xTo, int yTo) {
        if (world[xTo][yTo].equals(Tileset.FLOWER)) {
            genFlower();
            currScore++;
        }
        if (!world[xTo][yTo].equals(Tileset.WALL)) {
            world[xTo][yTo] = Tileset.AVATAR;
            world[avatarX][avatarY] = Tileset.GRASS;
            avatarX = xTo;
            avatarY = yTo;
            noOfMoves++;
        }
    }

    /**Creates a new world array that has all elements rotated*/
    private void rotateWorld() {
        TETile[][] tempArray = new TETile[world.length][world[0].length];
        for (int i = 0; i < world[0].length; i++) {
            for (int j = world.length - 1; j >= 0; j--) {
                tempArray[i][j] = world[j][i];
            }
        }
        world = tempArray;

        int temp = height;
        height  = width;
        width = temp;

        temp = avatarX;
        avatarX = avatarY;
        avatarY = temp;

        temp = flowerX;
        flowerX = flowerY;
        flowerY = temp;
    }

    /**Returns the Player's Score*/
    public int getScore() {
        return currScore;
    }
}
