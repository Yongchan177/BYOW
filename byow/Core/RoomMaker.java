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


    /** Constructor: Initializes instance variables and instantiates 2D world Array. */
    public RoomMaker(int width, int height, int seed) {

        //Setting instance variables
        this.width = width; //number of columns
        this.height = height; //number of rows
        this.seed = seed;
        world = new TETile[width][height];
        roomCorners = new ArrayList<>();

        //Initializes all the tiles in the world
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //Create randomly generated rooms within the world
       makeRooms();
    }

    /** getWorld: Returns world 2D Array */
    public TETile[][] getWorld() {
        return world;
    }

    /** makeRooms: uses random seed to generate rooms in the 2D world
     * Notes:
     *      1. Unique seeds must return unique rooms
     *      2. Identical seeds must return the same room */
    private void makeRooms() {
        Random seedRandom = new Random(seed);
        int noOfRooms = RandomUtils.uniform(seedRandom, 5, 10);
        int k = 0;

        while (k < noOfRooms) {
            //Get random starting points based on the seed
            int startingRow = RandomUtils.uniform(seedRandom, height);
            int startingColumn = RandomUtils.uniform(seedRandom, width);
            int heightOfRoom = RandomUtils.uniform(seedRandom, 2, height - startingRow);
            int widthOfRoom = RandomUtils.uniform(seedRandom, 2, width - startingColumn);

            boolean isEmpty = true;

            //Check if room already exists at the position
            for (int i = startingRow; i < startingRow + heightOfRoom; i++) {
                for (int j = startingColumn; j < startingColumn + widthOfRoom; j++) {
                    if(!world[i][j].equals(Tileset.NOTHING)) {
                        isEmpty = false;
                    }
                }
            }

            //Create the room
            if(isEmpty) {
                for (int i = startingRow; i < startingRow + heightOfRoom; i++) {
                    for (int j = startingColumn; j < startingColumn + widthOfRoom; j++) {
                        world[i][j] = Tileset.SAND;
                    }
                }
                k++;
                Point topRight = new Point((double) startingRow, (double) startingColumn + widthOfRoom);
                roomCorners.add(topRight);
            }
        }
    }


    /** makeHallways: connects rooms together randomly using a KD tree's closest method */
    private void makeHallways() {
        closestFinder = new KDTree(roomCorners);
        for (Point roomCorner: roomCorners) {
            Point closest = closestFinder.nearest(roomCorner.getX(), roomCorner.getY());
        }
    }








}