package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Naredba koja stvara direktorij s imenom zadanim kao argumentom.
 * Naredba prima samo jedan argument.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class MakeDirCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() != 1) {
            env.writeln("Mkdir command requires 1 argument.");
            return ShellStatus.CONTINUE;
        }

        String name = argumentsList.get(0);

        try {
            Files.createDirectory(Path.of(".").resolve(name));
        } catch (IOException e) {
            env.writeln("Error creating directory");
            return ShellStatus.CONTINUE;
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Mkdir command makes a new directory with the name from the argument.",
                "Command only takes one command, path of new folder with it's name.");
    }
}
