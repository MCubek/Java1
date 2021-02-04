package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 30/11/2020
 */
class CryptoTest {
    @Test
    void TestCryptoDigestWorks() {
        Path path = Paths.get("src/main/resources/hw05test.bin");

        assertEquals("Digesting completed. Digest of hw05test.bin matches expected digest.",
                Crypto.compareDigestAndGenerateMessage("2e7b3a91235ad72cb7e7f6a721f077faacfeafdea8f3785627a5245bea112598",
                        path));
    }

    @Test
    void TestCryptoDigestError() {
        Path path = Paths.get("src/main/resources/hw05test.bin");

        assertEquals("Digesting completed. Digest of hw05test.bin does not match the expected digest. Digest was: 2e7b3a91235ad72cb7e7f6a721f077faacfeafdea8f3785627a5245bea112598",
                Crypto.compareDigestAndGenerateMessage("d03d4424461e22a458c6c716395f07dd9cea2180a996e78349985eda78e8b800",
                        path));
    }
}