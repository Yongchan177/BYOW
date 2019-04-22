package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import javax.swing.text.Position;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void addHexagon(TETile[][] world, int s, Position p, TETile t) {

        TERenderer ter = new TERenderer();
        ter.initialize(30, 30);

        for(int i =0; i<world.length; i++) {
            for(int j=0; j<world[0].length; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        for(int i = 1; i <= s*2; i++) {
            drawRow(i, s, world, t);
        }

        ter.renderFrame(world);
    }


    public static void drawRow(int row, int size, TETile[][] world, TETile t) {
        if(row > size) {
            row = (2*size) - row;
        }
        int noOfElementsInRow = size + ((row-1)*2);

        int offset = getOffset(row, size);

        for(int i = 0; i < offset; i++) {
            world[row][i] = Tileset.NOTHING;
        }
        for(int i = offset; i < noOfElementsInRow; i++) {
            world[row][i] = t;
        }
    }

    public static int getOffset(int row, int size) {
        int maxNoOfElements = 3 * size - 2;
        int elementsInRow = size + ((row-1)*2);
        int offset = maxNoOfElements-elementsInRow;
        return offset/2;
    }

    public static void main(String[] args) {
        TETile[][] world = new TETile[30][30];
        addHexagon(world, 4, null, Tileset.FLOWER);
    }






}
