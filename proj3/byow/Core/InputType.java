package byow.Core;

public class InputType {

    private String seed;

    public InputType(String inputString) {
        seed = inputString;
    }

    public int getSeed() {

        String seedString = "";

       if (seed.charAt(0) == 'N' || seed.charAt(0) == 'n') {
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
