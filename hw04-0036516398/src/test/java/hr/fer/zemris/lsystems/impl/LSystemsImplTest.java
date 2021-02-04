package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.math.Vector2D;
import org.junit.jupiter.api.Test;
import hr.fer.zemris.lsystems.impl.commands.*;

class LSystemsImplTest {

    @Test
    public void generateTest() {

        LSystemBuilderImpl lSystemBuilder = new LSystemBuilderImpl();
        lSystemBuilder.registerProduction('F', "F+F--F+F").setAxiom("F");

        assertEquals("F", lSystemBuilder.build().generate(0));
        assertEquals("F+F--F+F", lSystemBuilder.build().generate(1));
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", lSystemBuilder.build().generate(2));
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F+F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F+F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", lSystemBuilder.build().generate(3));
    }


    /**
     * Sljedeći testovi nisu moji več su posuđeni
     */
    @Test
    public void turtleCopyTest() {
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        TurtleState state2 = state.copyOf();

        assertEquals(state.getCurrentPosition().getX(), state2.getCurrentPosition().getX());
        assertEquals(state.getCurrentPosition().getY(), state2.getCurrentPosition().getY());
        assertEquals(state.getOrientation().getX(), state2.getOrientation().getX());
        assertEquals(state.getOrientation().getY(), state2.getOrientation().getY());
        assertEquals(state.getColor(), state2.getColor());
        assertEquals(state.getDistance(), state2.getDistance());

        assertEquals(20, state.getCurrentPosition().getX());
        assertEquals(15, state.getCurrentPosition().getY());
        assertEquals(1, state.getOrientation().getX());
        assertEquals(0, state.getOrientation().getY());
        assertEquals(Color.BLACK, state.getColor());
        assertEquals(20, state.getDistance());

        state2.getCurrentPosition().add(state2.getCurrentPosition());
        state2.getOrientation().add(state2.getOrientation());
        state2.setColor(Color.GREEN);
        state2.setDistance(10);

        assertEquals(40, state2.getCurrentPosition().getX());
        assertEquals(30, state2.getCurrentPosition().getY());
        assertEquals(2, state2.getOrientation().getX());
        assertEquals(0, state2.getOrientation().getY());
        assertEquals(Color.GREEN, state2.getColor());
        assertEquals(10, state2.getDistance());

        assertEquals(20, state.getCurrentPosition().getX());
        assertEquals(15, state.getCurrentPosition().getY());
        assertEquals(1, state.getOrientation().getX());
        assertEquals(0, state.getOrientation().getY());
        assertEquals(Color.BLACK, state.getColor());
        assertEquals(20, state.getDistance());
    }

    @Test
    public void contextGetCurrentStateTest() {
        Context ctx = new Context();
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        ctx.pushState(state);
        TurtleState state2 = ctx.getCurrentState();
        TurtleState state3 = ctx.getCurrentState();

        assertEquals(state, state2);
        assertEquals(state, state3);
    }

    @Test
    public void popCommandTest() {
        Context ctx = new Context();
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        ctx.pushState(state);
        PopCommand comm = new PopCommand();
        comm.execute(ctx, null);
        try {
            ctx.getCurrentState();
            fail("Should fail");
        } catch (EmptyStackException ex) {

        }

        TurtleState state2 = state.copyOf();
        ctx.pushState(state);
        ctx.pushState(state2);
        comm.execute(ctx, null);
        assertEquals(state, ctx.getCurrentState());
    }

    @Test
    public void pushCommandTest() {
        Context ctx = new Context();
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        ctx.pushState(state);
        PushCommand comm = new PushCommand();
        comm.execute(ctx, null);

        assertNotEquals(state, ctx.getCurrentState());
        assertEquals(state.getColor(), ctx.getCurrentState().getColor());
        assertEquals(state.getDistance(), ctx.getCurrentState().getDistance());
        assertEquals(state.getCurrentPosition().getX(), ctx.getCurrentState().getCurrentPosition().getX());
        assertEquals(state.getCurrentPosition().getY(), ctx.getCurrentState().getCurrentPosition().getY());
        assertEquals(state.getOrientation().getX(), ctx.getCurrentState().getOrientation().getX());
        assertEquals(state.getOrientation().getY(), ctx.getCurrentState().getOrientation().getY());
    }

    @Test
    public void rotateCommandTest() {
        Context ctx = new Context();
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        ctx.pushState(state);
        RotateCommand comm = new RotateCommand(90);
        comm.execute(ctx, null);

        assertEquals(0, state.getOrientation().getX(), 1E-3);
        assertEquals(1, state.getOrientation().getY(), 1E-3);
    }

    @Test
    public void skipCommandTest() {
        Context ctx = new Context();
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        ctx.pushState(state);
        SkipCommand comm = new SkipCommand(2);
        comm.execute(ctx, null);

        assertEquals(60, state.getCurrentPosition().getX());
        assertEquals(15, state.getCurrentPosition().getY());
    }

    @Test
    public void scaleCommandTest() {
        Context ctx = new Context();
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        ctx.pushState(state);
        ScaleCommand comm = new ScaleCommand(2);
        comm.execute(ctx, null);

        assertEquals(40, state.getDistance());
    }

    @Test
    public void colorCommandTest() {
        Context ctx = new Context();
        TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
        ctx.pushState(state);
        ColorCommand comm = new ColorCommand(Color.CYAN);
        comm.execute(ctx, null);

        assertEquals(Color.CYAN, state.getColor());
    }

}