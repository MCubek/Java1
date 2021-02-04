package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Naredba izlaska iz shella.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class ExitCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        return Collections.singletonList("Command exits shell.");
    }
}
