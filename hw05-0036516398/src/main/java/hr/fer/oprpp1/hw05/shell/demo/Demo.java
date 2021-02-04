package hr.fer.oprpp1.hw05.shell.demo;

import hr.fer.oprpp1.hw05.shell.Util;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println(Util.getArgumentsList("\"C:/Program Files/Program1/info.txt\" C:/tmp/informacije.txt users"));
    }
}
