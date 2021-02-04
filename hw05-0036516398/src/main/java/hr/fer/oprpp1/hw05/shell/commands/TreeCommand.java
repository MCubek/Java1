package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

/**
 * Naredba ispisuje stablo poceci od direktorija predanog kao argument.
 * Naredba prima jedan argument, path direktorija.
 * Svaka ispisana dubina biti ce sva znaka pomaknuta udesno.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class TreeCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> argumentsList = Util.getArgumentsList(arguments);
        if (argumentsList.size() != 1) {
            env.writeln("Tree command requires 1 argument.");
            return ShellStatus.CONTINUE;
        }
        Path path = Util.parsePath(argumentsList.get(0));
        if (! Files.isDirectory(path)) {
            env.writeln("Argument is not a directory.");
            return ShellStatus.CONTINUE;
        }

        try {
            Files.walkFileTree(path, new TreeFileVisitor(env));
        } catch (IOException e) {
            env.writeln("Error while walking path.");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList("Tree command outputs tree of files and directories.",
                "Command takes one argument, the path of the root directory from which",
                "is makes the tree",
                "Every new level will be moved 2 characters to the right.");
    }

    /**
     * Moja implementacija FileVisitora koja ispisuje datoteke i direktorije
     * na enviroment.
     * Svaka datoteka i direktorij se ispisuju s pomakom velicine koliko su nisko
     * u stablu.
     */
    public static class TreeFileVisitor extends SimpleFileVisitor<Path> {
        private int level;
        private final Environment env;

        public TreeFileVisitor(Environment env) {
            super();
            this.env = env;
            this.level = 0;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            env.writeln("  ".repeat(level) + dir.getFileName().toString());
            level++;
            return super.preVisitDirectory(dir, attrs);
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            super.visitFile(file, attrs);
            env.writeln("  ".repeat(level) + file.getFileName().toString());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            level--;
            return super.postVisitDirectory(dir, exc);
        }
    }
}
