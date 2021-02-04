package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Naredba koja ispisuje kako zadana naredba funkcionira.
 * Prima jedan argument koji je ime naredbe za koju se traže informacije.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class HelpCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() != 1) {
            env.writeln("Help command requires 1 arguments.");
            return ShellStatus.CONTINUE;
        }

        ShellCommand command = env.commands().get(argumentsList.get(0));
        if (command == null) {
            env.writeln("Command does not exist.");
            return ShellStatus.CONTINUE;
        }

        command.getCommandDescription().forEach(env::writeln);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList("Help command shows how a given command works.",
                "Command takes one argument, the name of command in question.");
    }
}
