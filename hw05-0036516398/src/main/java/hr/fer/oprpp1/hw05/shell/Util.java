package hr.fer.oprpp1.hw05.shell;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pomoćni razred za shell
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class Util {
    //Ne stvaraj
    private Util() {
    }

    /**
     * Metoda iz arguemnta izvlaci path.
     *
     * @param path putanja
     * @return <code>Path</code> objekt
     */
    public static Path parsePath(String path) throws InvalidPathException {
        return Paths.get(path.replaceAll("\"", ""));
    }

    public static List<String> getArgumentsList(String arguments) {
        //Ako ima navodnike dozvoli space inace ne
        Matcher m = Pattern.compile("(\"[^\"]+\"|[^\" ]+)").matcher(arguments);

        List<String> argumentsList = new ArrayList<>();
        while (m.find()) {
            argumentsList.add(m.group(1));
        }
        return argumentsList;
    }

    public static String getFormattedDateOfFile(Path path) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );
        BasicFileAttributes attributes = faView.readAttributes();
        FileTime fileTime = attributes.creationTime();
        return sdf.format(new Date(fileTime.toMillis()));
    }
}
