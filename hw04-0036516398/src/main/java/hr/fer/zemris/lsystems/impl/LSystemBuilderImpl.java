package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;

import java.awt.*;
import java.util.Optional;

import static java.lang.Math.*;

/**
 * Implementacija <code>LSystemBuilder</code>.
 * Sustav se moze ucitati programski pomocu javnih metoda
 * ili pomocu configuracijskog stringa.
 *
 * @author matejC
 */
public class LSystemBuilderImpl implements LSystemBuilder {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final double DEG_TO_RAD = Math.PI / 180;

    private final Dictionary<Character, String> productions;
    private final Dictionary<Character, Command> commands;
    private double unitLength;
    private Vector2D origin;
    private double angleRadian;
    private String axiom;
    private double unitLengthDegreeScaler;

    /**
     * Defaultni konstruktor koji stvara razred s defaultnim vrijednostima.
     */
    public LSystemBuilderImpl() {
        productions = new Dictionary<>();
        commands = new Dictionary<>();
        unitLength = 0.1;
        origin = new Vector2D(0, 0);
        angleRadian = 0;
        axiom = "";
        unitLengthDegreeScaler = 1;
    }

    @Override
    public LSystemBuilder setUnitLength(double length) {
        unitLength = length;
        return this;
    }

    @Override
    public LSystemBuilder setOrigin(double v, double y) {
        origin = new Vector2D(v, y);
        return this;
    }

    @Override
    public LSystemBuilder setAngle(double angleDegrees) {
        angleRadian = angleDegrees * DEG_TO_RAD;
        return this;
    }

    @Override
    public LSystemBuilder setAxiom(String axiom) {
        this.axiom = axiom;
        return this;
    }

    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
        this.unitLengthDegreeScaler = unitLengthDegreeScaler;
        return this;
    }

    @Override
    public LSystemBuilder registerProduction(char c, String production) {
        productions.put(c, production);
        return this;
    }

    @Override
    public LSystemBuilder registerCommand(char c, String command) {
        String[] strings = command.split("\\s+");
        String s1 = strings[0];
        Optional<String> s2 = Optional.ofNullable(strings.length == 2 ? strings[1] : null);

        commands.put(c
                , switch (s1) {
                    case ("pop") -> new PopCommand();
                    case ("push") -> new PushCommand();
                    case ("draw") -> new DrawCommand(Double.parseDouble(s2.orElseThrow()));
                    case ("skip") -> new SkipCommand(Double.parseDouble(s2.orElseThrow()));
                    case ("scale") -> new ScaleCommand(Double.parseDouble(s2.orElseThrow()));
                    case ("rotate") -> new RotateCommand(Double.parseDouble(s2.orElseThrow()));
                    case ("color") -> new ColorCommand(Color.decode("0x" + s2.orElseThrow()));
                    default -> throw new IllegalArgumentException("Command does not exist.");
                });

        return this;
    }

    @Override
    public LSystemBuilder configureFromText(String[] strings) {
        for (String line : strings) {
            String[] stringArray = line.split("\\s+");

            String action = stringArray[0];

            switch (action) {
                case ("origin") -> setOrigin(Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]));
                case ("angle") -> setAngle(Double.parseDouble(stringArray[1]));
                case ("unitLength") -> setUnitLength(Double.parseDouble(stringArray[1]));
                case ("unitLengthDegreeScaler") -> parseAndSetUnitLenDegScale(line.substring("unitLengthDegreeScaler".length()).strip());
                case ("command") -> parseAndSetCommand(line.substring("command".length()).strip());
                case ("axiom") -> setAxiom(stringArray[1]);
                case ("production") -> registerProduction(stringArray[1].charAt(0), stringArray[2]);
            }
        }
        return this;
    }

    private void parseAndSetCommand(String command) {
        String[] commandParts = command.split("\\s+");
        if (commandParts.length < 2 || commandParts.length > 3)
            throw new IllegalArgumentException("Command input invalid.");

        if (commandParts.length == 2)
            registerCommand(commandParts[0].charAt(0), commandParts[1]);
        else
            registerCommand(commandParts[0].charAt(0), commandParts[1] + " " + commandParts[2]);

    }

    private void parseAndSetUnitLenDegScale(String s) {
        //Provjera je li broj double s jednim argumentom
        if (s.matches("\\d+\\.?\\d*")) {
            setUnitLengthDegreeScaler(Double.parseDouble(s));
            return;
        }

        //Provjera je li double s tri argumenta
        if (s.matches("\\d+\\.?\\d*\\s*/\\s*\\d+\\.?\\d*")) {
            String[] strings = s.split("/");
            setUnitLengthDegreeScaler(Double.parseDouble(strings[0]) / Double.parseDouble(strings[1]));
            return;
        }

        throw new IllegalArgumentException("Length degree scalar input invalid.");

    }

    @Override
    public LSystem build() {
        return new LSystem() {
            @Override
            public String generate(int i) {
                String value = axiom;

                for (int j = 0; j < i; j++) {
                    StringBuilder sb = new StringBuilder(100);
                    for (char ch : value.toCharArray()) {
                        String production = productions.get(ch);
                        if (production != null) {
                            sb.append(production);
                        } else {
                            sb.append(ch);
                        }
                    }
                    value = sb.toString();
                }

                return value;
            }

            @Override
            public void draw(int i, Painter painter) {
                Context context = new Context(new TurtleState(origin.copy()
                        , new Vector2D(cos(angleRadian), sin(angleRadian))
                        , DEFAULT_COLOR, unitLength * pow(unitLengthDegreeScaler, i)));


                String generated = generate(i);

                for (var symbol : generated.toCharArray()) {
                    Command command = commands.get(symbol);
                    if (command != null)
                        command.execute(context, painter);
                }
            }
        };
    }
}
