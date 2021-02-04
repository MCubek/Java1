package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * Sučelje koje opisuje način kako se može upravljati s konzolom.
 * Komande će imati pristup implementaciji ovoga sučelje i njime će komunicirati
 * s korisnikom i konzolom.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 01/12/2020
 */
public interface Environment {
    /**
     * Metoda čita liniju.
     *
     * @return Pročitana linija.
     * @throws ShellIOException Ukoliko se dogodi greška kod čitanja.
     */
    String readLine() throws ShellIOException;

    /**
     * Metoda zapisuje liniju.
     *
     * @param text text koji se zapiše u liniju.
     * @throws ShellIOException Ukoliko se dogodi pogrška pri pisanju.
     */
    void write(String text) throws ShellIOException;

    /**
     * Metoda zapisuje u novu liniju.
     *
     * @param text text koji se zapiše u novu liniju.
     * @throws ShellIOException Ukoliko se dogodi pogrška pri pisanju.
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Metoda vraća nepromijenjivu mapu svih naredaba i njihovih imena.
     * Komande se ne mogu micati iz dobivene mape.
     *
     * @return Mapa dostupnih komandi.
     */
    SortedMap<String, ShellCommand> commands();

    Character getMultilineSymbol();

    void setMultilineSymbol(Character symbol);

    Character getPromptSymbol();

    void setPromptSymbol(Character symbol);

    Character getMorelinesSymbol();

    void setMorelinesSymbol(Character symbol);
}
