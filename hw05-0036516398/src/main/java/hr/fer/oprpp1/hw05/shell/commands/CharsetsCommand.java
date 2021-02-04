package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Naredba koja ispisuje sve dostupne charsetove.
 * Svaki charset ispistati će se u svoj redak na dani Enviroment.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class CharsetsCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (! arguments.isEmpty()) {
            env.writeln("Charsets command doesn't support arguments.");
            return ShellStatus.CONTINUE;
        }
        Charset.availableCharsets().keySet().forEach(env::writeln);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Charsets command lists all available charsets for your system.",
                "Every available charset is outputted in a new line.");
    }
}
