package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * Implementacija Shell programa.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class MyShell {
    public static void main(String[] args) {

        try(MyEnvironment env = new MyEnvironment()){
            SortedMap<String, ShellCommand> cmds = env.commands();

            env.writeln("Welcome to MyShell v1.0");

            ShellStatus status = ShellStatus.CONTINUE;

            do {
                env.write(env.getPromptSymbol().toString() + " ");
                String readLine = env.readLine();
                while (readLine.endsWith(env.getMorelinesSymbol().toString())) {
                    env.write(env.getMultilineSymbol().toString() + " ");
                    //Makni multiline simbol
                    readLine = readLine.substring(0, readLine.length() - 1);
                    readLine += env.readLine();
                }

                String[] input = readLine.split(" ", 2);
                String commandName = input[0].toLowerCase();
                String arguments = "";
                if (input.length == 2)
                    arguments = input[1];

                ShellCommand command = cmds.get(commandName);

                if (command == null) {
                    env.writeln("Command " + commandName + " is not recognized.");
                    continue;
                }

                status = command.executeCommand(env, arguments);

            } while (status != ShellStatus.TERMINATE);

        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }

    }
}
