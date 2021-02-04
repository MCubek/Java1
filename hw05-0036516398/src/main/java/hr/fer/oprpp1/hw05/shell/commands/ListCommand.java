package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Naredba ispisuje sve datoteke i direktorije za zadani path.
 * Path kroz koji se prolazi se zadaje kao jedini argument.
 * Metoda također ispisuje zastavice o dopuštenim operacijama nad pojedinom
 * obiđenom datotekom, velicinu datoteke u bytovima i vrijeme kreacije datoteke.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class ListCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() != 1) {
            env.writeln("List command requires 1 argument.");
            return ShellStatus.CONTINUE;
        }
        Path path = Util.parsePath(argumentsList.get(0));
        if (! Files.isDirectory(path)) {
            env.writeln("Argument is not a directory.");
            return ShellStatus.CONTINUE;
        }


        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            List<Path> files = new ArrayList<>();
            stream.forEach(files::add);

            for (Path file : files) {
                env.writeln(formatFileInfo(file));
            }

        } catch (IOException e) {
            env.writeln("Error while reading files.");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command takes a path and for all items in that path outputs",
                "their name, date of creation, size in bytes and flags.",
                "d indicates directory, r readable, w writable, x executable.",
                "One argument is required, the path of the directory.");
    }

    private static String formatFileInfo(Path path) throws IOException {
        boolean d = Files.isDirectory(path);
        boolean r = Files.isReadable(path);
        boolean w = Files.isWritable(path);
        boolean x = Files.isExecutable(path);
        long size = Files.size(path);
        String date = Util.getFormattedDateOfFile(path);
        String name = path.getFileName().toString();

        return String.format("%c%c%c%c %10d %s %s",
                d ? 'd' : '-', r ? 'r' : '-', w ? 'w' : '-', x ? 'x' : '-',
                size, date, name);
    }
}
