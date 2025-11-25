package it.unibo.mvc;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {
    private static final int MIN = 0;
    private static final int MAX = 100;
    private static final int ATTEMPTS = 10;
    private static final int CONFIG_NUM=3;
    private static final String SEPARATOR = System.lineSeparator();

    private final DrawNumber model;
    private final List<DrawNumberView> views;

    /**
     * @param views
     *            the views to attach
     */
    public DrawNumberApp(final DrawNumberView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        List<Integer> config = new ArrayList<>(CONFIG_NUM);
        config.add(MIN);config.add(MAX);config.add(ATTEMPTS);
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.yml");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    config.set(i,Integer.parseInt(line.split(": ")[1]));
                    i++;
                }
        }
         catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("No Config file was set. Using Default values of MIN: " + MIN+", MAX: "+MAX+", ATTEMPTS: "+ATTEMPTS);

        }
            this.model = new DrawNumberImpl(config.getFirst(),config.get(1) , config.getLast());
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
        new DrawNumberApp(new DrawNumberViewImpl(), new PrintStreamView("src/main/resources/logs.txt"), new DrawNumberViewImpl());
    }

}
