package byow.Core;

import byow.TileEngine.TERenderer;

public class StringHandler {

    private String input;
    private InputHandler handleInput;

    public StringHandler(String inputString, int width, int height, TERenderer myRenderer) {
        input = inputString;
        handleInput = new InputHandler(width, height, myRenderer, false);
        processInput();
    }

    private void processInput() {
        input = input.toUpperCase();
        handleInput.handleMenuOptions(input.charAt(0));
        int i = 1;
        if (input.charAt(0) == 'N') {
            while (input.charAt(i) != 'S') {
                handleInput.seedMenuHandler(input.charAt(i));
                i++;
            }
            handleInput.seedMenuHandler(input.charAt(i));
            i++;
        }
        for (int j = i; j < input.length(); j++) {
            handleInput.gameControlHandler(input.charAt(j));
        }
    }

    public RoomMaker getRoom() {
        return handleInput.getRoom();
    }
}
