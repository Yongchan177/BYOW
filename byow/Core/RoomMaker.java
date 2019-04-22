package byow.Core;

import byow.KdTree.KDTree;
import byow.KdTree.Point;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class RoomMaker {

    private int width;
    private int height;
    private int seed;
    private TETile[][] world;
    private ArrayList<Point> roomCorners;
    private KDTree closestFinder;


    public RoomMaker(int width, int height, int seed) {

        //Setting instance variables
        this.width = width;
        this.height = height;
        this.seed = seed;
        world = new TETile[height][width];
        roomCorners = new ArrayList<>();

        //Initializes all the tiles in the world
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                world[i][j] = Tileset.FLOWER;
            }
        }

        //Create randomly generated rooms within the world
        makeRooms();
    }


    public TETile[][] getWorld() {
        return world;
    }


    private void makeRooms() {

        Random seedRandom = new Random(seed);
        int noOfRooms = RandomUtils.uniform(seedRandom, 10, 20);
        int k = 0;

        while(k < noOfRooms) {

            //Get random starting points based on the seed
            int startingRow = RandomUtils.uniform(seedRandom, height);
            int startingColumn = RandomUtils.uniform(seedRandom, width);
            int heightOfRoom = RandomUtils.uniform(seedRandom, 2, height-startingRow);
            int widthOfRoom = RandomUtils.uniform(seedRandom, 2, width-startingColumn);


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
                Point topRight = new Point((double) startingRow, (double) startingColumn+widthOfRoom);
                roomCorners.add(topRight);
            }
        }
    }


    private void makeHallways() {
        closestFinder = new KDTree(roomCorners);
        for(Point roomCorner: roomCorners) {

            Point closest = closestFinder.nearest(roomCorner.getX(), roomCorner.getY());

        }
    }







}
