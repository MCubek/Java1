package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Naredva <code>cat</code> ispisuje zadanu datoteku na konzolu.
 * Naredba prima dva argumenta od kojih je prvi obavezan i sadzi putanju
 * do datoteke, te drugi opcionalan koji sadrzi charset koji se treba koristiit.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class ConcatCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() < 1 || argumentsList.size() > 2) {
            env.writeln("Concat command requires 1 or 2 arguments.");
            return ShellStatus.CONTINUE;
        }

        Path filePath = Util.parsePath(argumentsList.get(0));
        if (! Files.isReadable(filePath)) {
            env.writeln("Provided file can't be accessed.");
            return ShellStatus.CONTINUE;
        }

        Charset charset = Charset.defaultCharset();
        if (argumentsList.size() == 2) {
            try {
                charset = Charset.forName(argumentsList.get(1));
            } catch (IllegalArgumentException e) {
                env.writeln("Provided charset can't be accessed.");
                return ShellStatus.CONTINUE;
            }
        }

        List<String> fileLines;
        try {
            fileLines = Files.readAllLines(filePath, charset);
        } catch (IOException e) {
            env.writeln("Error while reading file.");
            return ShellStatus.CONTINUE;
        }

        fileLines.forEach(env::writeln);
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Cat command writes file who's path was given as an argument to the console.",
                "Command takes 2 arguments the first of which is required and is the path of the file in question,",
                "and the second is optional and is the charset which will be used when reading the file.");
    }
}
