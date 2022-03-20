package cryptopals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

// TODO: refactor functions and use bytes instead for string
public class Set1 {
    public static String[] base64Map = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/"};
    public static String padding = "=";
    public static Integer base16 = 16;
    public static Integer base2 = 2;


    public static String paddingLeft(String input, Integer padding) {
        StringBuilder ouput = new StringBuilder(input);
        while (padding > 0) {
            ouput.insert(0, '0');
            padding -= 1;
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
        for (int index = 0; index < numberOfBytes; index++) {
            parsedInteger = Integer.parseInt(hex.substring(index * 2, (index + 1) * 2), base16);
            convertedHex = Integer.toBinaryString(parsedInteger);
            paddedBinary = paddingLeft(convertedHex, 8 - convertedHex.length());

            binary.append(paddedBinary);
        }

        if (isPadding) {
            for (int index = 0; index < 6 - padding; index++) {
                binary.append("0");
            }
        }

        String binaryString = binary.toString();
        StringBuilder output = new StringBuilder();
        for (int index = 0; index < (numberOfBytes * 8) / 6; index++) {
            output.append(base64Map[Integer.parseInt(binaryString.substring(index * 6, (index + 1) * 6), base2)]);
        }

        if (isPadding) {
            output.append(padding);
            if (padding == 4) {
                output.append(padding);
            }
        }
        return output.toString();
    }


    public static String xor(String first, String second) throws Exception {
        if (first.length() != second.length()) {
            throw new Exception("Different buffers lengths");
        }

        StringBuilder result = new StringBuilder();
        for (int index = 0; index < first.length(); index += 2) {
            result.append(
                    Integer.toString(Integer.parseInt(first.substring(index, index + 2), base16) ^ Integer.parseInt(second.substring(index, index + 2), base16), base16));
        }

        return result.toString();
    }


    public static int getXorLettersScore(ArrayList<Integer> decoded, int key) {
        int charCode;
        int score = 0;
        for (int index = 0; index < decoded.size(); index++) {
            charCode = (decoded.get(index) ^ key);

            if (charCode == 32 || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122) || (charCode >= 48 && charCode <= 57)) {
                score += 1;
            }
            else {
                score -= 1;
            }
        }
        return score;
    }

    public static ArrayList<Integer> decodeHexString(String hexString) {
        ArrayList<Integer> decoded = new ArrayList<Integer>();
        for (int index = 0; index < hexString.length(); index += 2) {
            decoded.add(Integer.parseInt(hexString.substring(index, index + 2), base16));
        }
        return  decoded;
    }

    public static int decryptSingleXor(String encrypted) {
        // The idea is to brute force all the 2⁸ possibilities
        // and give for each one a score based on the number of english letter in the result (let say the sum)
        // the one with the greatest score should be the right key

        ArrayList<Integer> decoded = decodeHexString(encrypted);

        int max = 0;
        int key = 0;
        int score;
        int charCode;
        for (int candidate = 0; candidate < 257; candidate++) {
            score = getXorLettersScore(decoded, candidate);

            if (score > max) {
                max = score;
                key = candidate;
            }
        }
        return key;
    }

    public static void printXoredString(String str, int key) {
        ArrayList<Integer> decoded = decodeHexString(str);
        StringBuilder res = new StringBuilder();
        int charCode;
        for (int index = 0; index < decoded.size(); index++) {
            charCode = decoded.get(index) ^ key;
            res.append((char) charCode);
        }
        System.out.println(res.toString());
    }

    public static String detectedSingleCharXor() {
        String filename = "4.txt";
        String line;
        Integer score, key;
        Integer max = 0;
        String theLine = "";
        int lines = 0;
        try {
            FileReader fr = new FileReader(String.format("src/cryptopals/%s", filename));
            BufferedReader br = new BufferedReader(fr);
            while (br.ready() != false) {
                line = br.readLine();
                if (line.isEmpty()) {
                    continue;
                }
                lines += 1;
                key = decryptSingleXor(line);
                score = getXorLettersScore(decodeHexString(line), key);
                if (score > max) {
                    max = score;
                    theLine = line;
                }
            }
            printXoredString(theLine, decryptSingleXor(theLine));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(lines);
        return theLine;
    }


    public static String encodeHex(String str) {

        StringBuilder result = new StringBuilder();
        String hexValue;
        for (Byte b:  str.getBytes()) {
            hexValue = Integer.toHexString(b.intValue());
            result.append(paddingLeft(hexValue, 2- hexValue.length()));
        }
        return result.toString();
    }

    public static String EncryptXOrRepeating(String repeatingInput, String key ) {
        StringBuilder repeatedKey = new StringBuilder(key);
        while (repeatedKey.length() < repeatingInput.length()) {
            repeatedKey.append(key);
        }
        if (repeatedKey.length() > repeatingInput.length()) {
            int diff = repeatedKey.length() - repeatingInput.length();
            repeatedKey.delete(repeatedKey.length() - diff, repeatedKey.length());
        }

        try {
            return xor(encodeHex(repeatingInput), encodeHex(repeatedKey.toString()));
        }catch (Exception e) {

            System.out.println(e.toString());
            return null;
        }
    }


    public static int hummingDistance(String first, String second) {
        // convert to bits
        StringBuilder firstBits = new StringBuilder();
        String binary;
        for (char c : first.toCharArray()) {
            binary = Integer.toBinaryString(Character.getNumericValue(c));
            firstBits.append(paddingLeft(binary, 8 - binary.length()));
        }
        StringBuilder secondBits = new StringBuilder();
        for (char c : second.toCharArray()) {
            binary = Integer.toBinaryString(Character.getNumericValue(c));
            secondBits.append(paddingLeft(binary, 8 - binary.length()));
        }
        int diff = 0;

        for (int index = 0; index < firstBits.length(); index ++) {
            if (firstBits.charAt(index) != secondBits.charAt(index)) {
                diff ++;
            }
        }

        return diff;
        // cound the bits difference
    }

    public static void solve() {
        try {
            System.out.println("Challenge 1");
            String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
            String output = hexToBase64(input);
            String expectedOutput = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
            System.out.println(expectedOutput.equals(output));

            System.out.println("Challenge 2");
            String first = "1c0111001f010100061a024b53535009181c";
            String second = "686974207468652062756c6c277320657965";
            String expected = "746865206b696420646f6e277420706c6179";
            String result = xor(first, second);
            System.out.println(result);
            System.out.println(expected.equals(result));

            System.out.println("Challenge 3");
            String encrypted = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
            System.out.println(decryptSingleXor(encrypted));

            System.out.println("Challenge 4");
            String line = detectedSingleCharXor();

            System.out.println("Challenge 5");
            String repeatingInput = "Burning 'em, if you ain't quick and nimble\n" +
                    "I go crazy when I hear a cymbal";
            String repeatingencypted = EncryptXOrRepeating(repeatingInput, "ICE");
            String repeatedEncryptionResult = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
            System.out.println(repeatingencypted.toString());
            System.out.println(repeatedEncryptionResult);

            // TODO: finish step 6
            System.out.println("Challenge 6");
            System.out.println("Test humming distance");
            System.out.println(hummingDistance("this is a test", "wokka wokka!!!"));

        } catch (Exception e) {

        }
    }

}
