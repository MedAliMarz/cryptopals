package cryptopals;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

// TODO: refactor all functions to use bytes instead of string
public class Set2 {


    public String pkcs7(String input, int blockSize) {

        int byteSize = input.length();
        StringBuilder paddedString = new StringBuilder(input);
        int missing = byteSize > blockSize ? byteSize % blockSize: blockSize % byteSize;
        for(int index = 0; index < missing; index ++) {
            paddedString.append(missing);
        }
        return paddedString.toString();
    }

    public String cbcEncrypt(String toBeCBCEncrypted, String key) throws NotImplementedException{
        final String IV = "fake 0th ciphertext block";

        throw new NotImplementedException();
    }
    public static void main(String[] args) {
        Set2 set = new Set2();
        System.out.println("Challenge 9");
        String toBePadded = "YELLOW SUBMARINE";
        String padded = set.pkcs7(toBePadded, 20);
        System.out.println(padded);


        System.out.println("Challenge 10");
        String toBeCBCEncrypted = "";
        // set.cbcEncrypt(toBeCBCEncrypted, key);
    }
}
