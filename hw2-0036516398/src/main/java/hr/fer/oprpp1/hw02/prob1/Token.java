package hr.fer.oprpp1.hw02.prob1;

/**
 * Klasa koja predstvalja token leksičke analize
 *
 * @author matej
 */
public class Token {

    TokenType type;
    Object value;

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
