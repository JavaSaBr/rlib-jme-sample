package org.sample.client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Запускатор клиента.
 *
 * @author Ronn
 */
public class Starter extends Application {

    public static void main(String[] args) throws IOException {
        SampleGame.start(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
    }
}
