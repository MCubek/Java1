package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Razred koji prdstavlja kriptografiju datoteke.
 * Razred može računati i uspoređivati SHA-256 sažetak i može
 * kriptirati te dekriptirati AES algoritmom.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 30/11/2020
 */
public class Crypto {
    private static final int ARRAY_SIZE = 4096;

    /**
     * Metoda računa sažetak datoteka na zadanom Pathu
     * i vraća polje byteova s izračunatim sažetkom.
     *
     * @param path Putanja datoteke.
     * @return polje byteova s izračunatim sažetkom.
     * @throws IOException Ukoliko ne postoji datoteka na zadanom pathu.
     */
    private static byte[] calculateDigest(Path path) throws IOException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            //Not able to be envoked by user
            throw new RuntimeException("Algorithm doesn't exist");
        }

        try (InputStream is = new BufferedInputStream(Files.newInputStream(path))) {
            byte[] array = new byte[ARRAY_SIZE];

            while (true) {
                int readBytes = is.read(array);
                if (readBytes < 1) break;

                messageDigest.update(array, 0, readBytes);
            }
        }

        return messageDigest.digest();
    }

    /**
     * Metoda uspoređuje predani sažetak i izračunati, te vraća odgovarajuću poruku
     *
     * @param expectedDigest predani i očekivani sažetak.
     * @param path           Putanja datoteke za koju se računa i ispituje sažetak.
     * @return Poruka o usporedbi.
     */
    public static String compareDigestAndGenerateMessage(String expectedDigest, Path path) {
        String inputDigest = expectedDigest.toLowerCase();
        byte[] digest;
        try {
            digest = calculateDigest(path);
        } catch (IOException e) {
            return "Error: " + e.toString()
                    + "\n" + path.toAbsolutePath();
        }
        String resultDigest = Util.bytetohex(digest);

        if (inputDigest.equals(resultDigest))
            return "Digesting completed. Digest of " + path.getFileName() + " matches expected digest.";
        else
            return "Digesting completed. Digest of " + path.getFileName() + " does not match the expected digest. " +
                    "Digest was: " + resultDigest;
    }

    /**
     * Metoda kriptira ili dekriptira pomocu AES algoritma i rezultat sprema u datoteku.
     * Koristi šifru i inicijalizacijski vektor oboje od 32 hex znamenke.
     * Metoda prima enumeraciju <code>CYPHER.ENCRYPT_MODE/DECRYPT_MODE</code> kako bi odlucila
     * o zežjenoj operaciji.
     *
     * @param source      Izvorna datoteka.
     * @param destination Odredišna datoteka.
     * @param cipherMode  Šifriranje/dešifriranje
     * @param keyText     šifra.
     * @param ivText      inicijalizacijski vektor.
     * @return <code>true</code> ako je uspješno inače <code>false</code>
     */
    public static boolean encryptOrDecrypt(Path source, Path destination, int cipherMode, String keyText, String ivText) {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(source));
             OutputStream os = new BufferedOutputStream(Files.newOutputStream(destination))) {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, new SecretKeySpec(Util.hextobyte(keyText), "AES"),
                    new IvParameterSpec(Util.hextobyte(ivText)));

            byte[] array = new byte[ARRAY_SIZE];

            while (true) {
                int readBytes = is.read(array);
                if (readBytes < 1) break;

                os.write(cipher.update(array, 0, readBytes));
            }
            os.write(cipher.doFinal());

        } catch (Exception e) {
            System.err.println("CriptographyError:" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Main metoda koja izvodi program s uzaznim parametrima operacije u argumentima.
     * Dopušteni argumenti su <code>checksha</code>, <code>encrypt</code> i <code>decrypt</code>
     * Checksha prima jedan argument -> datoteku koju ispituje.
     * Encrypt i decrypt primaju dva argumenta, ulaznu i izlaznu datoteku.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) throw new IllegalArgumentException("Wrong number of arguments.");

        if ("checksha".equals(args[0])) {
            if (args.length != 2) throw new IllegalArgumentException("Wrong number of arguments for checksha.");

            System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");

            String line;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                line = br.readLine();
            } catch (IOException e) {
                System.err.println("Error reading console.");
                return;
            }

            System.out.println(compareDigestAndGenerateMessage(line, Paths.get(args[1])));

        } else if ("encrypt".equals(args[0]) || "decrypt".equals(args[0])) {
            if (args.length != 3) throw new IllegalArgumentException("Wrong number of arguments for encrypt.");

            String keyText;
            String ivText;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
                keyText = br.readLine();
                System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
                ivText = br.readLine();
            } catch (IOException e) {
                System.err.println("Error reading console.");
                return;
            }

            int operation = switch (args[0]) {
                case ("decrypt") -> Cipher.DECRYPT_MODE;
                case ("encrypt") -> Cipher.ENCRYPT_MODE;
                default -> throw new IllegalStateException("Unexpected value: " + args[0]);
            };

            if (keyText.length() != 32 || ivText.length() != 32)
                throw new IllegalArgumentException("Provided text must be 16 bytes (32 hex digits)");

            if (encryptOrDecrypt(Paths.get(args[1]), Paths.get(args[2]), operation, keyText, ivText))
                System.out.println(switch (operation) {
                    case (Cipher.DECRYPT_MODE) -> "Decryption";
                    case (Cipher.ENCRYPT_MODE) -> "Encryption";
                    default -> throw new IllegalStateException("Unexpected value: " + args[0]);
                } + " completed. Generated file " + args[2] + " based on file " + args[1] + ".");
            else
                System.out.println("Cryptography failed.");

        } else {
            throw new IllegalArgumentException("Unknown argument " + args[0]);
        }
    }
}
