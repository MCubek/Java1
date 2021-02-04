package hr.fer.oprpp1.hw04;

/**
 * Iznimka kod obrade querija
 *
 * @author matej
 * @project hw04-0036516398
 * @created 04/11/2020
 */
public class QueryException extends RuntimeException {
    public QueryException() {
    }

    public QueryException(String message) {
        super(message);
    }
}
