package it.unibo.mvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;

import it.unibo.controller.MyController;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {
    //private static final int MIN = 0;
    //private static final int MAX = 100;
    //private static final int ATTEMPTS = 10;

    private final DrawNumber model;
    private final List<DrawNumberView> views;


    /**
     * @param views
     *            the views to attach
     */
    public DrawNumberApp(final DrawNumberView... views) {
        final MyController control = new MyController();
        final Map<String, Integer> configs = control.readAllLines(new File("src"+System.getProperty("file.separator")+
                                                                            "main"+System.getProperty("file.separator")+
                                                                            "resources"+System.getProperty("file.separator")+
                                                                            "config.yml"));
        
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }

        this.model = new DrawNumberImpl(configs.get("minimum"), configs.get("maximum"), configs.get("attempts"));
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view: views) {
                view.result(result);
            }
        } catch (IllegalArgumentException e) {
            for (final DrawNumberView view: views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0);
    }

    /**
     * @param args
     *            ignored
     * @throws FileNotFoundException 
     */
    public static void main(final String... args) throws FileNotFoundException {
        new DrawNumberApp(new DrawNumberViewImpl());
        new DrawNumberApp(new DrawNumberViewImpl());
    }

}
