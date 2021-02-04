package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Naredba ispisuje datoteku zadanu kao argument kao hex izlaz.
 * Naredba prima jedan argument, path na datoteku.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class HexdumpCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() != 1) {
            env.writeln("Hexdump command requires 1 argument.");
            return ShellStatus.CONTINUE;
        }
        Path path = Util.parsePath(argumentsList.get(0));
        if (! Files.isReadable(path)) {
            env.writeln("File is not readable.");
            return ShellStatus.CONTINUE;
        }

        try {
            generateHexLineList(path).forEach(env::writeln);
        } catch (IOException e) {
            env.writeln("Error while generating hexdump.");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Hexdump command writes hexdump for given file.",
                "Command takes one argument, the path of the file.");
    }

    private List<String> generateHexLineList(Path file) throws IOException {
        var br = Files.newBufferedReader(file);
        char[] chars = new char[16];
        List<String> lines = new ArrayList<>();
        int linesRead;
        int counter = 0;
        while ((linesRead = br.read(chars, 0, 16)) > 0) {
            StringBuilder newLine = new StringBuilder(String.format("%08x:", counter));
            StringBuilder stringEnd = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                //Trenutni char
                char currentChar = chars[i];
                //Krivi char
                if (currentChar < 32 || currentChar > 127) {
                    currentChar = '.';
                }
                //prva polovica
                if (i < 8)
                    newLine.append(" ");

                //test je li procitano sve
                if (i < linesRead)
                    newLine.append(String.format("%x", (int) currentChar));
                else
                    newLine.append("  ");

                //Sredina
                if (i == 7)
                    newLine.append("|");

                //Druga polovica
                if (i >= 8)
                    newLine.append(" ");

                //String na kraju
                if (i < linesRead)
                    stringEnd.append(currentChar);

            }
            newLine.append("| ").append(stringEnd);

            lines.add(newLine.toString());
            counter += 16;
        }

        br.close();
        return lines;
    }
}
