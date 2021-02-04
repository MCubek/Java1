package hr.fer.oprpp1.hw05.shell;

import java.util.List;

/**
 * Sučelje za komande Shell programa.
 * Potrebno je da ga svaka komanda implementira.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public interface ShellCommand {
    /**
     * Metoda koja na zadanom okruženju izvršuje komandu
     * s zadanim ulaznim argumentima u parametru.
     * String argumenti sadrži sve što je korisnik upisao nakon
     * imena naredbe sve su linije, ukoliko ih je više bilo, konkatinirane u jedan string.
     * Ukoliko je bilo više argumenata potrebno ih je parsirati.
     * Nakon što se naredba izvrši metoda vraća <code>ShellStatus</code> s
     * informacijom o daljem izvešenju shell programa.
     *
     * @param env       okruženje na kojem se izvodi naredba
     * @param arguments argumenti naredbe konkatinirani.
     * @return <code>ShellStatus</code> enumeriaciju sa
     * informacijom o daljnjem izvršenju programa.
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Metoda vraća ime naredbe.
     *
     * @return ime naredbe.
     */
    String getCommandName();

    /**
     * Metoda generira i vraća opis funkcionlanosti komande kao
     * Listu stringova gdje sveki element predstavlja jedan redak.
     *
     * @return opis komande.
     */
    List<String> getCommandDescription();
}
