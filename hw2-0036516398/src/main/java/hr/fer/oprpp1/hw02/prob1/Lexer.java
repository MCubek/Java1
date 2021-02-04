package hr.fer.oprpp1.hw02.prob1;

import java.util.Objects;

/**
 * Razred leksera koji pretvara kod u tokene
 *
 * @author matej
 */
public class Lexer {
    private char[] data;
    private Token token;
    private int currentIndex;
    private LexerState state;

    /**
     * Konstruktor
     *
     * @param text input string
     */
    public Lexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
        state = LexerState.BASIC;
    }

    /**
     * Metoda koja omogućuje postavlajnje stanja Lexera
     * Stanja su tipa <code>LexerState</code>
     *
     * @param state stanje koje se postavlja
     * @throws NullPointerException ako je poslan null kao stanje
     */
    public void setState(LexerState state) {
        this.state = Objects.requireNonNull(state);
    }

    /**
     * Metoda koja traži i vraća idući token leksičke analize
     *
     * @return idući token
     * @throws LexerException ako nema više tokena ili kod krivo esceapan
     */
    public Token nextToken() {
        if (token != null && token.type == TokenType.EOF) throw new LexerException("There are no more tokens!");
        StringBuilder sequence = new StringBuilder();
        TokenType tokenType = null;
        boolean escaped = false;
        char character;

        //Preskaci vodeće whitespacese
        while (currentIndex < data.length && Character.isWhitespace(character = data[currentIndex])) {
            currentIndex++;
        }

        for (; currentIndex < data.length; currentIndex++) {
            character = data[currentIndex];

            //Samo # je na ulazu
            if (character == '#' && sequence.length() == 0) {
                setState(state == LexerState.BASIC ? LexerState.EXTENDED : LexerState.BASIC);
                currentIndex++;
                sequence.append(character);
                tokenType = TokenType.SYMBOL;
                break;
            }

            //Gotov niz jer je dosao poseban znak
            if (character == '#')
                break;


            if (state == LexerState.BASIC) {

                //Escape pokreni
                if (! escaped && character == '\\') {
                    escaped = true;
                    tokenType = TokenType.WORD;
                    continue;
                }
                //Dupli '\'
                if (escaped && character == '\\') {
                    escaped = false;
                    sequence.append(character);
                    continue;
                }

                //Prvi znak
                if (tokenType == null) {
                    tokenType = getTypeFromChar(character);
                    sequence.append(character);
                    continue;
                }

                //Krivo escapean
                if (escaped && getTypeFromChar(character) != TokenType.NUMBER)
                    throw new LexerException("Invalid escape!");

                //Simboli su odvojeni
                if (tokenType == TokenType.SYMBOL && getTypeFromChar(character) == TokenType.SYMBOL)
                    break;

                //Razliciti tipovi kod ne esceapanih
                if (tokenType != getTypeFromChar(character) && ! escaped)
                    break;

                sequence.append(character);
                escaped = false;

            } else {
                if (Character.isWhitespace(character))
                    break;

                tokenType = TokenType.WORD;
                sequence.append(character);
            }
        }

        if (escaped) throw new LexerException("Invalid escape!");

        if (sequence.length() != 0 && sequence.toString().isBlank())
            return new Token(TokenType.EOF, null);

        Object result;
        if (tokenType == TokenType.NUMBER) {
            try {
                result = Long.valueOf(sequence.toString());
            } catch (NumberFormatException e) {
                throw new LexerException(e.getMessage());
            }

        } else if (tokenType == TokenType.SYMBOL) {
            result = sequence.charAt(0);
        } else if (tokenType == null) {
            tokenType = TokenType.EOF;
            result = null;
        } else {
            result = sequence.toString();
        }
        token = new Token(tokenType, result);
        return token;
    }

    /**
     * Metoda koja vraća zadnji izračunati token
     *
     * @return zadnji token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Privatna metoda koja računa tip danog charactera
     * @param character char čiji se tim računa
     * @return tip danog charactera
     */
    private TokenType getTypeFromChar(char character) {
        if (Character.isDigit(character)) return TokenType.NUMBER;
        if (Character.isLetter(character)) return TokenType.WORD;
        if (Character.isWhitespace(character)) return null;
        return TokenType.SYMBOL;
    }
}
