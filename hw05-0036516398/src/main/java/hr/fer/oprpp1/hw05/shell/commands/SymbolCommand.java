package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.util.Arrays;
import java.util.List;

/**
 * Naredba prima simbol zamijenjuje ga u svojem enviromentu ili
 * ispisuje trenutni simbol.
 * Dopusteni tipovi simbola su:
 * <code>PROMPT</code>,<code>MORELINES</code>,<code>MULTILINE</code>
 * Očekuju se jedan ili dva argumenta, prvi je naziv tipa, a drugi znak s kojim se mijenja
 * ako se zeli mijenjati.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 02/12/2020
 */
public class SymbolCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() < 1 || argumentsList.size() > 2) {
            env.writeln("Symbol command requires 1 or 2 arguments.");
            return ShellStatus.CONTINUE;
        }

        if (argumentsList.size() == 2) {
            //Mijenjanje simbola
            String symbolString = argumentsList.get(1).replaceAll("'","");

            if (symbolString.length() != 1) {
                env.writeln("New character has to be one character.");
                return ShellStatus.CONTINUE;
            }

            Character symbol = symbolString.charAt(0);
            switch (argumentsList.get(0).toUpperCase()) {
                case "PROMPT" -> {
                    env.writeln("Symbol for PROMPT changed from '" + env.getPromptSymbol() + "' to '" + symbol + "'");
                    env.setPromptSymbol(symbol);
                }
                case "MORELINES" -> {
                    env.writeln("Symbol for MORELINES changed from '" + env.getMorelinesSymbol() + "' to '" + symbol + "'");
                    env.setMorelinesSymbol(symbol);
                }
                case "MULTILINE" -> {
                    env.writeln("Symbol for MULTILINE changed from '" + env.getMultilineSymbol() + "' to '" + symbol + "'");
                    env.setMultilineSymbol(symbol);
                }
                default -> {
                    env.writeln("Wrong first argument.");
                }

            }
        } else {
            //Ispis simbola
            switch (argumentsList.get(0).toUpperCase()) {
                case "PROMPT" -> {
                    env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
                }
                case "MORELINES" -> {
                    env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'");
                }
                case "MULTILINE" -> {
                    env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'");
                }
                default -> {
                    env.writeln("Wrong first argument.");
                }

            }
        }

        return ShellStatus.CONTINUE;

    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Symbol command changes shell symbols in enviroment or displays current symbol.",
                "Allowed symbols are PROMPT, MORELINES and MULTILINE.",
                "One or two arguments are expected. First is name of symbol to change",
                "and second is the new symbol if it wants to be replaced.");
    }
}
