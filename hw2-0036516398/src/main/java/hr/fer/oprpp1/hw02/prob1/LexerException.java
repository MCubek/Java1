package hr.fer.oprpp1.hw02.prob1;

/**
 * Iznimka koja se baca kada dođe do pogreške kod Lexera
 *
 * @author matej
 */
public class LexerException extends RuntimeException {
    public LexerException() {
        super();
    }

    public LexerException(String message) {
        super(message);
    }

    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LexerException(Throwable cause) {
        super(cause);
    }
}
