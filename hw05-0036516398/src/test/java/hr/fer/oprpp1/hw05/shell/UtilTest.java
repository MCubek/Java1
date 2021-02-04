package hr.fer.oprpp1.hw05.shell;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 02/12/2020
 */
class UtilTest {

    @Test
    void parsePath() {
        assertEquals(Path.of("."), Util.parsePath("\".\""));
    }

    @Test
    void getArgumentsList() {
        var expected = Arrays.asList("\"C:/Program Files/Program1/info.txt\"",
                "C:/tmp/informacije.txt", "argument", "/home/john/backupFolder");

        assertLinesMatch(expected, Util.getArgumentsList("\"C:/Program Files/Program1/info.txt\" C:/tmp/informacije.txt  argument /home/john/backupFolder  "));
    }

}