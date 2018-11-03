import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Vernam {

    public static void main(String[] args) {
        Vernam vernam = new Vernam();

        System.out.println("Text: \n" + vernam.text + "\n");

        byte[] encryptedArray = vernam.encryptBytes();
        System.out.println("Encrypted Message: \n" + vernam.createTextFromBitsArray(encryptedArray) + "\n");

        byte[] decryptedArray = vernam.decryptMessage(encryptedArray);
        System.out.println("Decrypted Message: \n" + vernam.createTextFromBitsArray(decryptedArray));
    }

    private String fileName;
    private String text;
    private BigInteger q, p, n, s;
    private BigInteger rest, divider;
    private byte[] messagesBits;
    private byte[] keyBits;

    public Vernam() {
        this("vernam.txt");
    }

    public Vernam(String filePath) {
        this.fileName = filePath;
        this.text = "";
        this.rest = new BigInteger("3");
        this.divider = new BigInteger("4");

        this.mainFunction();
    }

    public void mainFunction() {

        if(!loadFile()) {
            return;
        }

        q = createPorQValue();
        p = createPorQValue();
        n = p.multiply(q);
        s = createSValue();
        messagesBits = createBitArray();
        keyBits = generateKey();
    }

    private boolean loadFile() {
        BufferedReader br = null;
        String tmpText;

        try {
            br = new BufferedReader(new FileReader(fileName));

            while ((tmpText = br.readLine()) != null) {
                text += tmpText;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {

            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private BigInteger createRandomBigInteger () {
        SecureRandom random = new SecureRandom();
        BigInteger number = new BigInteger(512, random);

        return number;
    }

    private BigInteger createPorQValue() {
        BigInteger number = createRandomBigInteger();

        if (number.mod(divider).intValue() != rest.intValue()) {
            return createPorQValue();
        }

        return number;
    }

    private BigInteger createSValue () {
        BigInteger s = createRandomBigInteger();

        if (s.gcd(n).intValue() != 1) {
            return createSValue();
        }

        return s;
    }

    private byte[] generateKey() {
        byte[] key = new byte[messagesBits.length];
        BigInteger localDivider = new BigInteger("2");

        BigInteger x = s.pow(2).mod(n);
        key[0] = x.mod(localDivider).byteValue();

        for (int i = 1; i < messagesBits.length; i++) {
            x = x.pow(2).mod(n);
            key[i] = x.mod(localDivider).byteValue();
        }

        return key;
    }

    private byte[] createBitArray() {
        byte[] bytes = text.getBytes();
        byte[] bits = new byte[bytes.length * 8];
        int iterator = 0;

        for (byte singleByte : bytes) {
            int intValue = singleByte;

            for (int i = 0; i < 8; i++) {
                bits[iterator + i] = (intValue & 128) == 0 ? (byte) 0 : (byte) 1;
                intValue <<= 1;
            }

            iterator += 8;
        }

        return bits;
    }

    private byte[] encryptBytes() {
        return xorOperations(messagesBits, keyBits);
    }

    private byte[] decryptMessage (byte[] encryptedBytes) {
        return xorOperations(encryptedBytes, keyBits);
    }

    private byte[] xorOperations(byte[] first, byte[] second) {
        byte[] result = new byte[first.length];
        int iterator = 0;

        for (byte bit : first) {
            result[iterator] = (byte)(first[iterator] ^ second[iterator]);
            iterator++;
        }

        return result;
    }

    private byte[] createByteArrayFromBitsArray(byte[] bitsArray) {

        byte[] result = new byte[bitsArray.length / 8];
        String byteString = "";
        int iterator = 0;

        for (byte bit : bitsArray) {
            if(iterator != 0 && iterator % 8 == 0) {
                result[iterator/8] = (byte)Integer.parseInt(byteString, 2);
                byteString = "";
            }

            byteString += bit;

            iterator++;
        }

        return result;
    }

    public String createTextFromBitsArray(byte[] bytes) {
        return new String(createByteArrayFromBitsArray(bytes));
    }
}
