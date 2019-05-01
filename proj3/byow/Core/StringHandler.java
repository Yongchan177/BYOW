package byow.Core;

public class StringHandler {

    private int seed;
    private String input;
    private int width;
    private int height;
    private RoomMaker myRoom;

    public StringHandler(String inputString, int width, int height) {
        input = inputString;
        this.width = width;
        this.height = height;
        processInput();
    }


    private void processInput() {
        input = input.toUpperCase();
        String seedString = "";
        int i = 1;
        if (input.charAt(0) == 'N') {
            while (input.charAt(i) != 'S') {
                seedString += input.charAt(i);
                i++;
            }
            seed = (int) Long.parseLong(seedString);
            i++;
        }
        myRoom = new RoomMaker(width, height, seed);
        for (int j = i; j < input.length(); j++) {
            myRoom.controlAvatar(input.charAt(j));
        }
    }

    public RoomMaker getRoom() {
        return myRoom;
    }

}
