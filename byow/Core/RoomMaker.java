package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class RoomMaker {

    private int width;
    private int height;
    private int seed;
    private TETile[][] world;

    public RoomMaker(int width, int height, int seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        world = new TETile[height][width];
        //Initializes all the tiles in the world
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                world[i][j] = Tileset.FLOWER;
            }
        }
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

            int startingRow = RandomUtils.uniform(seedRandom, height);
            int startingColumn = RandomUtils.uniform(seedRandom, width);

            int heightOfRoom = RandomUtils.uniform(seedRandom, 2, height - startingRow);
            int widthOfRoom = RandomUtils.uniform(seedRandom, 2, width - startingColumn);


            System.out.println(startingRow + ", " + startingColumn + ", " + heightOfRoom + ", " + widthOfRoom);


            boolean isEmpty = true;

            for (int i = startingRow; i < startingRow + height; i++) {
                for (int j = startingColumn; j < startingColumn + width; j++) {
                    if (!world[i][j].equals(Tileset.NOTHING)) {
                        isEmpty = false;
                    }
                }
            }

            if (isEmpty) {
                for (int i = startingRow; i < startingRow + height; i++) {
                    for (int j = startingColumn; j < startingColumn + width; j++) {
                        world[i][j] = Tileset.SAND;
                    }
                }
                k++;
            }
        }
    }







}
