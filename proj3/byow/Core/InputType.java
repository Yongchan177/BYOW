package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class InputType {

    private boolean keyboardUsed;
    private String inputString;

    public InputType(boolean keyboardUsed, String inputString) {
        this.keyboardUsed = keyboardUsed;
        this.inputString = inputString;
    }

    public int getSeed() {

        String seed = "";

        if (keyboardUsed) {
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char curr = StdDraw.nextKeyTyped();
                    seed += curr;
                    if (curr == 'S' || curr == 's' || curr == 'Q'
                            || curr == 'q' || curr == 'L' || curr == 'l') {
                        break;
                    }
                }
            }
        } else {
            seed = inputString;
        }

        String seedString = "";

        if (seed.charAt(0) == 'L' || seed.charAt(0) == 'l') {
            return -1;
        } else if (seed.charAt(0) == 'Q' || seed.charAt(0) == 'q') {
            return -2;
        } else if (seed.charAt(0) == 'N' || seed.charAt(0) == 'n') {
            for (int i = 1; i < seed.length(); i++) {
                if (seed.charAt(i) == 'S' || seed.charAt(i) == 's') {
                    break;
                }
                seedString += seed.charAt(i);
            }
            int seedNumber = (int) Long.parseLong(seedString);
            return seedNumber;
        }
        return 0;
    }



}
