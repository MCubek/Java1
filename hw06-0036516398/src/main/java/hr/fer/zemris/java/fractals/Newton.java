package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Razred koji ilustrira faktale iz Newton-Raphsonove iteracije.
 * Razred također sadrži statičke metode koje služe generiranju takvih fraktala
 * i one su javno dostupne.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 09/12/2020
 */
public class Newton {

    /**
     * Main metoda koja sekvencijalno crta fraktal s vrijednostima koje
     * korisnik upise u konzolu.
     *
     * @param args argumenti
     */
    public static void main(String[] args) {
        ComplexRootedPolynomial complexRootedPolynomial
                = newtonCommandLine();
        FractalViewer.show(new MySequentialNewtonProducer(complexRootedPolynomial));
    }

    /**
     * Metoda koja sa pomocu konzole korisnika trazi korijene i stvara
     * objekt <code>ComplexRootedPolynomial</code> kojega i vraća.
     *
     * @return objekt <code>ComplexRootedPolynomial</code>
     */
    public static ComplexRootedPolynomial newtonCommandLine() {
        System.out.print("""
                Welcome to Newton-Raphson iteration-based fractal viewer.
                Please enter at least two roots, one root per line. Enter 'done' when done.
                """);

        Scanner scanner = new Scanner(System.in);
        int numRoots = 1;
        List<Complex> roots = new ArrayList<>();

        while (true) {
            System.out.printf("Root %d> ", numRoots);
            String input = scanner.nextLine();

            if (input.equals("done")) {
                if (numRoots < 3) {
                    System.out.println("Please enter at least two roots!");
                } else {
                    break;
                }
            }

            try {
                Complex complex = Complex.parse(input);

                roots.add(complex);
                numRoots++;

            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        scanner.close();
        System.out.println("Image of fractal will appear shortly. Thank you.");

        return new ComplexRootedPolynomial(Complex.ONE, roots.toArray(new Complex[0]));
    }

    /**
     * Metoda preslikava zadane vrijednosti u kompleksni broj s njihovom vrijednosti
     * na trenutno vidljivoj ravnini.
     *
     * @return novi preslikani kompleksni broj.
     */
    public static Complex mapToComplexPlain(int x, int y, double reMin, double imMin, double reMax, double imMax,
                                            int width, int height) {
        double real = x / (width - 1.0) * (reMax - reMin) + reMin;
        double imaginary = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
        return new Complex(real, imaginary);
    }

    /**
     * Metoda računa potreben vrijednosti i sprema ih u data polje kako bi se mogao vizualno prikazati fraktal.
     * Metoda omogućava zadavanje minimalne i maksimalne linije kao potporu moguće paralelnosti izvođenja.
     */
    public static void calculate(double reMin, double reMax, double imMin, double imMax, int width,
                                 int height, int iterations, double rootDistance, double threshold,
                                 int yMin, int yMax, short[] data, AtomicBoolean cancel,
                                 ComplexRootedPolynomial complexRootedPolynomial, ComplexPolynomial complexPolynomial,
                                 ComplexPolynomial derived) {

        int offset = yMin * width;

        for (int y = yMin; y <= yMax && ! cancel.get(); y++) {
            for (int x = 0; x < width; x++) {
                Complex complex = mapToComplexPlain(x, y, reMin, imMin, reMax, imMax, width, height);

                double module;
                int iters = 0;

                Complex old;

                do {
                    Complex numerator = complexPolynomial.apply(complex);
                    Complex denominator = derived.apply(complex);

                    old = new Complex(complex.getReal(), complex.getImaginary());

                    complex = complex.sub(numerator.divide(denominator));

                    iters++;
                    module = old.sub(complex).module();
                } while (module > threshold && iters < iterations);

                int index = complexRootedPolynomial.indexOfClosestRootFor(complex, rootDistance);
                data[offset++] = (short) (index + 1);
            }
        }
    }

    /**
     * Staticka klasa koja sekvencijalno implementira <code>IfractalProducer</code>
     */
    public static class MySequentialNewtonProducer implements IFractalProducer {
        private final ComplexRootedPolynomial complexRootedPolynomial;
        private final ComplexPolynomial complexPolynomial;

        private static final double THRESHOLD = 1E-3;
        private static final int ITERATIONS = 16*16*16;
        private static final double ROOT_DISTANCE = 0.002;

        public MySequentialNewtonProducer(ComplexRootedPolynomial complexRootedPolynomial) {
            this.complexRootedPolynomial = complexRootedPolynomial;
            this.complexPolynomial = complexRootedPolynomial.toComplexPolynomial();
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            short[] data = new short[width * height];

            Newton.calculate(reMin, reMax, imMin, imMax, width, height, ITERATIONS, ROOT_DISTANCE, THRESHOLD,
                    0, height - 1, data, cancel, complexRootedPolynomial, complexPolynomial, complexPolynomial.derive());

            /*int offset = 0;
            ComplexPolynomial derived = complexPolynomial.derive();

            for (int y = 0; y < height; y++) {
                if (cancel.get()) break;
                for (int x = 0; x < width; x++) {
                    Complex complex = mapToComplex(x, y, reMin, imMin, reMax, imMax, width, height);

                    double module;
                    int iters = 0;

                    do {
                        Complex numerator = complexPolynomial.apply(complex);
                        Complex denominator = derived.apply(complex);
                        Complex fraction = numerator.divide(denominator);
                        complex = complex.sub(fraction);

                        iters++;
                        module = complex.module();
                    } while (module > THRESHOLD && iters < ITERATIONS);

                    int index = complexRootedPolynomial.indexOfClosestRootFor(complex, ROOT_DISTANCE);
                    data[offset++] = (short) (index + 1);
                }
            }*/
            observer.acceptResult(data, (short) (complexPolynomial.order() + 1), requestNo);
        }
    }
}
