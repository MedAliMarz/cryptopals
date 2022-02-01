package cryptopals;

public class Set1 {
    public static String[] base64Map = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9","+","/"};
    public static String padding = "=";
    public static Integer base16 = 16;
    public static Integer base2 = 2;


    public static String paddingLeft(String input, Integer padding) {
        StringBuilder ouput = new StringBuilder(input);
        while (padding != 0) {
            ouput.insert(0,'0');
            padding-=1;
        }
        return ouput.toString();
    }

    public static String hexToBase64(String hex) {
        int numberOfBytes = hex.length() / 2;
        int padding = (numberOfBytes * 8) % 6;
        boolean isPadding = padding != 0;
        StringBuilder binary = new StringBuilder();
        String convertedHex;
        int parsedInteger;
        String paddedBinary;
        for(int index =0; index < numberOfBytes; index++) {
            parsedInteger = Integer.parseInt(hex.substring(index*2, (index+1)*2), base16);
            convertedHex =  Integer.toBinaryString(parsedInteger);
            paddedBinary = paddingLeft(convertedHex, 8 - convertedHex.length());

            binary.append(paddedBinary);
        }

        if (isPadding) {
            for(int index = 0; index < 6-padding; index++) {
                binary.append("0");
            }
        }

        String binaryString = binary.toString();
        StringBuilder output = new StringBuilder();
        for(int index = 0; index < (numberOfBytes*8)/6; index++) {
            output.append(base64Map[Integer.parseInt(binaryString.substring(index*6, (index+1) *6 ), base2)]);
        }

        if (isPadding) {
            output.append(padding);
            if (padding == 4) {
                output.append(padding);
            }
        }
        return output.toString();
    }

    public static void solve() {

        String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        String output = hexToBase64(input);
        String expectedOutput = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
        System.out.println(expectedOutput.equals(output));
    }

}
