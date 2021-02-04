package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.io.*;
import java.util.Collections;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Implementacija sučelja <code>Enviroment</code> koje koristi konzolu,
 * <code>System.in</code> & <code>System.out</code>, kako bi komunicirao
 * s korisnikom.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class MyEnvironment implements Environment,AutoCloseable {

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private char promptSymbol;
    private char multilineSymbol;
    private char morelinesSymbol;

    private final SortedMap<String, ShellCommand> shellCommands;

    public MyEnvironment() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        this.promptSymbol = '>';
        this.multilineSymbol = '|';
        this.morelinesSymbol = '\\';
        shellCommands = createShellCommandsMap();
    }

    private static SortedMap<String, ShellCommand> createShellCommandsMap() {
        SortedMap<String, ShellCommand> map = new ConcurrentSkipListMap<>();

        map.put("charsets", new CharsetsCommand());
        map.put("cat", new ConcatCommand());
        map.put("ls", new ListCommand());
        map.put("tree", new TreeCommand());
        map.put("copy", new CopyCommand());
        map.put("mkdir", new MakeDirCommand());
        map.put("hexdump", new HexdumpCommand());
        map.put("exit", new ExitCommand());
        map.put("help", new HelpCommand());
        map.put("symbol", new SymbolCommand());

        return map;
    }

    @Override
    public String readLine() throws ShellIOException {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new ShellIOException("Error while reading line.");
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        try {
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new ShellIOException("Error while writing line");
        }
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        this.write(text + System.lineSeparator());
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(shellCommands);
    }

    @Override
    public Character getMultilineSymbol() {
        return multilineSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        this.multilineSymbol = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        this.promptSymbol = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return morelinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.morelinesSymbol = symbol;
    }

    @Override
    public void close() throws Exception {
        bufferedReader.close();
        bufferedWriter.close();
    }
}
