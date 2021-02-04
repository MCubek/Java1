package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.ObjectStack;

import java.util.Objects;

/**
 * Klasa koja sprema kornjače na stog
 *
 * @author matejC
 */
public class Context {
    private final ObjectStack<TurtleState> objectStack;

    public Context() {
        this.objectStack = new ObjectStack<>();
    }

    /**
     * Konstrutor koji stvara novi kontekst kornjača i na vrh stoga stavlja
     * prvu, početnu kornjaču
     *
     * @param firstState prva kornjača
     */
    public Context(TurtleState firstState) {
        this();
        objectStack.push(Objects.requireNonNull(firstState));
    }

    /**
     * Trenutna kornjača s vrha stoga
     *
     * @return kornjača s vrha stoga
     */
    public TurtleState getCurrentState() {
        return objectStack.peek();
    }

    /**
     * Stavi kornjaču na stog
     *
     * @param turtleState korjnača koja se stavlja na stog
     * @throws NullPointerException ako je predan <code>null</code>
     */
    public void pushState(TurtleState turtleState) {
        objectStack.push(Objects.requireNonNull(turtleState));
    }

    /**
     * Metoda briše zadnje dodanu kornjaču s stoga
     */
    public void popState() {
        objectStack.pop();
    }
}
