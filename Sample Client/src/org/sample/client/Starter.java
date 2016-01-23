package org.sample.client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by ronn on 23.01.16.
 */
public class Starter extends Application {

    public static void main(String[] args) {
        final SampleGame game = new SampleGame();
        game.start();
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

    }
}
