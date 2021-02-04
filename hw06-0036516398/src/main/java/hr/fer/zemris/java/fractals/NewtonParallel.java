package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Razred koji ilustrira faktale iz Newton-Raphsonove iteracije.
 * Fraktali se prikazuju korištenjem više dretvi radi poboljšanih
 * performanci.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 09/12/2020
 */
public class NewtonParallel {

    /**
     * Main metoda koja prima argumente, pokrece program komandne linije gdje se ispituju korijeni
     * i iscrtava fraktal.
     * <p>
     * Dopusteni argumenti su <code>--workers=</code> ili <code>-w</code>, te
     * <code>--tracks</code> ili <code>-t</code>.
     * Dopušteno je pozvati bez argumenata te se onda koriste podrazumijevane vrijednosti ovisne o računalu.
     *
     * @param args argumenti
     */
    public static void main(String[] args) {
        int workers = Runtime.getRuntime().availableProcessors();
        int tracks = workers * 4;
        boolean workersSet = false;
        boolean tracksSet = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.matches("--workers=\\d+")) {
                if (workersSet) throw new IllegalArgumentException("Duplicate argument!");
                workersSet = true;
                int number = Integer.parseInt(arg.replace("--workers=", ""));
                if (number > 0)
                    workers = number;
            } else if (arg.matches("--tracks=\\d+")) {
                if (tracksSet) throw new IllegalArgumentException("Duplicate argument!");
                tracksSet = true;
                int number = Integer.parseInt(arg.replace("--tracks=", ""));
                if (number > 0)
                    tracks = number;
            } else if (arg.equals("-w")) {
                if (workersSet) throw new IllegalArgumentException("Duplicate argument!");
                workersSet = true;
                int number = Integer.parseInt(args[++ i]);
                if (number > 0)
                    workers = number;
            } else if (arg.equals("-t")) {
                if (tracksSet) throw new IllegalArgumentException("Duplicate argument!");
                tracksSet = true;
                int number = Integer.parseInt(args[++ i]);
                if (number > 0)
                    tracks = number;
            } else {
                throw new IllegalArgumentException(String.format("Argument %s is not valid!", arg));
            }
        }
        var polynomial = Newton.newtonCommandLine();
        FractalViewer.show(new MyParallelNewtonProducer(polynomial, workers, tracks));
    }

    /**
     * Staticka klasa koja paralelno implementira <code>IfractalProducer</code>
     */
    public static class MyParallelNewtonProducer implements IFractalProducer {
        private final ComplexRootedPolynomial complexRootedPolynomial;
        private final ComplexPolynomial complexPolynomial;
        private final ComplexPolynomial derived;

        private static final double THRESHOLD = 1E-3;
        private static final int ITERATIONS = 16*16+16;
        private static final double ROOT_DISTANCE = 0.002;

        private final int workers;
        private int tracks;

        public MyParallelNewtonProducer(ComplexRootedPolynomial complexRootedPolynomial, int workers, int tracks) {
            this.complexRootedPolynomial = complexRootedPolynomial;
            this.complexPolynomial = complexRootedPolynomial.toComplexPolynomial();
            this.derived = complexPolynomial.derive();
            this.workers = workers;
            this.tracks = tracks;
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

            if (tracks > height) tracks = height;
            int numberOfLinesPerTrack = height / tracks;
            System.out.printf("Threads: %d\nTracks: %d\n", workers,tracks);


            short[] data = new short[width * height];

            final BlockingQueue<CalculationWork> queue = new LinkedBlockingQueue<>();

            Thread[] workerThreads = new Thread[workers];
            for (int i = 0; i < workerThreads.length; i++) {
                workerThreads[i] = new Thread(() -> {
                    while (true) {
                        CalculationWork work;
                        try {
                            work = queue.take();
                            if (work == CalculationWork.NO_JOB) break;
                        } catch (InterruptedException e) {
                            continue;
                        }
                        work.run();
                    }
                });
            }
            for (Thread worker : workerThreads) {
                worker.start();
            }

            for (int i = 0; i < tracks; i++) {
                int yMin = i * numberOfLinesPerTrack;
                int yMax = (i + 1) * numberOfLinesPerTrack - 1;
                if (i == tracks - 1) {
                    yMax = height - 1;
                }

                CalculationWork work = new CalculationWork(reMin, reMax, imMin, imMax, width, height,
                        ITERATIONS, ROOT_DISTANCE, THRESHOLD, yMin, yMax, data, cancel,
                        complexRootedPolynomial, complexPolynomial, derived);

                while (true) {
                    try {
                        queue.put(work);
                        break;
                    } catch (InterruptedException ignore) {
                    }
                }
            }

            for (int i = 0; i < workerThreads.length; i++) {
                while (true) {
                    try {
                        queue.put(CalculationWork.NO_JOB);
                        break;
                    } catch (InterruptedException ignore) {
                    }
                }
            }
            for (Thread workerThread : workerThreads) {
                while (true) {
                    try {
                        workerThread.join();
                        break;
                    } catch (InterruptedException ignore) {
                    }
                }
            }

            observer.acceptResult(data, (short) (complexPolynomial.order() + 1), requestNo);
        }
    }

    /**
     * Staticka klasa koja demonstrira posao racunanja linija ilustracije fraktala.
     * Klasa implementira sučelje <code>Runnable</code>
     */
    private static class CalculationWork implements Runnable {
        public static final CalculationWork NO_JOB = new CalculationWork();
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int iterations;
        double rootDistance;
        double threshold;
        int yMin;
        int yMax;
        short[] data;
        AtomicBoolean cancel;
        ComplexRootedPolynomial complexRootedPolynomial;
        ComplexPolynomial complexPolynomial;
        ComplexPolynomial derived;

        public CalculationWork() {
        }

        public CalculationWork(double reMin, double reMax, double imMin, double imMax,
                               int width, int height, int iterations, double rootDistance, double threshold,
                               int yMin, int yMax, short[] data, AtomicBoolean cancel,
                               ComplexRootedPolynomial complexRootedPolynomial, ComplexPolynomial complexPolynomial,
                               ComplexPolynomial derived) {
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.iterations = iterations;
            this.rootDistance = rootDistance;
            this.threshold = threshold;
            this.yMin = yMin;
            this.yMax = yMax;
            this.data = data;
            this.cancel = cancel;
            this.complexRootedPolynomial = complexRootedPolynomial;
            this.complexPolynomial = complexPolynomial;
            this.derived = derived;
        }

        @Override
        public void run() {
            Newton.calculate(reMin, reMax, imMin, imMax, width, height, iterations, rootDistance,
                    threshold, yMin, yMax, data, cancel,
                    complexRootedPolynomial, complexPolynomial, derived);
        }
    }


}
