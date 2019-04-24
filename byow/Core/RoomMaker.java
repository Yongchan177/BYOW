package byow.Core;

import byow.KdTree.KDTree;
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
    private ArrayList<Point> roomCorners;
    private KDTree closestFinder;
    private Random seedRandom;


    /** Constructor: Initializes instance variables and instantiates 2D world Array. */
    public RoomMaker(int width, int height, int seed) {

        //Setting instance variables
        this.width = width; //number of columns
        this.height = height; //number of rows
        this.seed = seed;
        seedRandom = new Random(seed);
        world = new TETile[width][height];
        roomCorners = new ArrayList<>();

        //Initializes all the tiles in the world
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //Create randomly generated rooms and hallways within the world
        makeRooms();
        makeHallways();
    }

    /** getWorld: Returns world 2D Array */
    public TETile[][] getWorld() {
        return world;
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
                        world[i][j] = Tileset.WATER;

                        //Building Walls Around The Room
                        if (i == startingColumn || i == endingColumn - 1
                                || j == startingRow || j == endingRow - 1) {
                            world[i][j] = Tileset.WALL;
                        }
                    }
                }

                k++;
                Point topRight = new Point((double) endingColumn - 1, (double) endingRow - 1);
                roomCorners.add(topRight);
            }
        }
    }


    /** makeHallways: connects rooms together randomly using a KD tree's closest method */
    private void makeHallways() {

        for (Point roomCorner : roomCorners) {

            int random = RandomUtils.uniform(seedRandom, 0, roomCorners.size() - 1);
            Point randomPoint = roomCorners.get(random);

            int myX = (int) roomCorner.getX();
            int myY = (int) roomCorner.getY();

            int randomX = (int) randomPoint.getX();
            int randomY = (int) randomPoint.getY();

            int startingX = Math.min(myX, randomX);
            int endingX = Math.max(myX, randomX);
            int startingY = Math.min(myY, randomY);
            int endingY = Math.max(myY, randomY);

            for (int i = startingX; i < endingX; i++) {
                for (int j = startingY; j < endingY; j++) {
                    if(world[i][j].equals(Tileset.NOTHING)) {
                            world[i][j] = Tileset.TREE;
                    }
                    if(world[i][j].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.TREE;
                        break;
                    }
                }
            }

        }
    }








}