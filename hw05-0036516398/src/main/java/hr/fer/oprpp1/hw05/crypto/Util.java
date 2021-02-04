package hr.fer.oprpp1.hw05.crypto;

/**
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 30/11/2020
 */
public class Util {
    //Ne stvaraj
    private Util() {
    }

    /**
     * Metoda iz Stringa heksadekadskih znakova stvara primitivno polje byteova i vraća ga.
     * Potrebno je da string ima paran broj znakova.
     *
     * @param keyText ulazni niz heksadekadskih znakova.
     * @return novo polje byteova.
     * @throws ArithmeticException      Ako je neparan broj znakova.
     * @throws IllegalArgumentException Ako je predan nedozvoljen znak.
     */
    public static byte[] hextobyte(String keyText) {
        if (keyText.length() % 2 == 1)
            throw new ArithmeticException("Cannot have odd number of digits.");
        if (! keyText.matches("[\\dabcdefABCDEF]*"))
            throw new IllegalArgumentException("Invalid character.");

        byte[] array = new byte[keyText.length() / 2];

        for (int i = 0; i < keyText.length(); i += 2) {
            array[i / 2] = (byte) ((Character.digit(keyText.charAt(i), 16) << 4)
                    + Character.digit(keyText.charAt(i + 1), 16));
        }
        return array;
    }

    public static String bytetohex(byte[] bytearray) {
        char[] HEX_SYMBOLS = "0123456789abcdef".toCharArray();
        char[] charsArrays = new char[bytearray.length * 2];

        for (int i = 0; i < bytearray.length; i++) {
            int num = bytearray[i] & 0xFF;
            charsArrays[i * 2] = HEX_SYMBOLS[num >>> 4];
            charsArrays[i * 2 + 1] = HEX_SYMBOLS[num & 0x0F];
        }

        return new String(charsArrays);
    }
}
