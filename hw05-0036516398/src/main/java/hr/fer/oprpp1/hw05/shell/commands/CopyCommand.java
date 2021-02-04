package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Naredva kopira datoteku.
 * Datoteka koju želimo kopirati je zadana svojim pathom kao prvi argument.
 * Destinacija i ime kopirane datoteke zadajemo u drugome argumentu.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class CopyCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() != 2) {
            env.writeln("Copy command requires 2 arguments.");
            return ShellStatus.CONTINUE;
        }

        Path originalPath = Util.parsePath(argumentsList.get(0));
        if (! Files.isReadable(originalPath)) {
            env.writeln("Provided file can't be accessed.");
            return ShellStatus.CONTINUE;
        }

        try {
            Path newPath = Util.parsePath(argumentsList.get(1));
            Files.copy(originalPath, newPath);
        } catch (IOException | InvalidPathException e) {
            env.writeln("File copy error.");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Copy command copies files",
                "Command takes 2 arguments the first of which is file to be copied,",
                "and the second is file where to copy.");
    }
}
